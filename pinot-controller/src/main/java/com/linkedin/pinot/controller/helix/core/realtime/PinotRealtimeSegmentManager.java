/**
 * Copyright (C) 2014-2016 LinkedIn Corp. (pinot-core@linkedin.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.linkedin.pinot.controller.helix.core.realtime;

import com.google.common.base.Function;
import com.linkedin.pinot.common.Utils;
import com.linkedin.pinot.common.config.TableConfig;
import com.linkedin.pinot.common.config.TableNameBuilder;
import com.linkedin.pinot.common.metadata.ZKMetadataProvider;
import com.linkedin.pinot.common.metadata.instance.InstanceZKMetadata;
import com.linkedin.pinot.common.metadata.segment.RealtimeSegmentZKMetadata;
import com.linkedin.pinot.core.realtime.stream.StreamMetadata;
import com.linkedin.pinot.common.metrics.ControllerMeter;
import com.linkedin.pinot.common.metrics.ControllerMetrics;
import com.linkedin.pinot.common.utils.CommonConstants.Helix.TableType;
import com.linkedin.pinot.common.utils.CommonConstants.Segment.Realtime.Status;
import com.linkedin.pinot.common.utils.CommonConstants.Segment.SegmentType;
import com.linkedin.pinot.common.utils.HLCSegmentName;
import com.linkedin.pinot.common.utils.SegmentName;
import com.linkedin.pinot.common.utils.helix.HelixHelper;
import com.linkedin.pinot.common.utils.retry.RetryPolicies;
import com.linkedin.pinot.controller.helix.core.PinotHelixResourceManager;
import com.linkedin.pinot.controller.helix.core.PinotTableIdealStateBuilder;
import com.linkedin.pinot.core.query.utils.Pair;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.apache.helix.ControllerChangeListener;
import org.apache.helix.NotificationContext;
import org.apache.helix.PropertyPathConfig;
import org.apache.helix.PropertyType;
import org.apache.helix.ZNRecord;
import org.apache.helix.manager.zk.ZNRecordSerializer;
import org.apache.helix.manager.zk.ZkClient;
import org.apache.helix.model.IdealState;
import org.apache.helix.store.HelixPropertyListener;
import org.apache.zookeeper.data.Stat;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Realtime segment manager, which assigns realtime segments to server instances so that they can consume from Kafka.
 */
public class PinotRealtimeSegmentManager implements HelixPropertyListener, IZkChildListener, IZkDataListener {
  private static final Logger LOGGER = LoggerFactory.getLogger(PinotRealtimeSegmentManager.class);
  private static final String TABLE_CONFIG = "/CONFIGS/TABLE";
  private static final String SEGMENTS_PATH = "/SEGMENTS";
  private static final String REALTIME_SEGMENT_PROPERTY_STORE_PATH_PATTERN =
      ".*/SEGMENTS/.*_REALTIME|.*/SEGMENTS/.*_REALTIME/.*";
  private static final String REALTIME_TABLE_CONFIG_PROPERTY_STORE_PATH_PATTERN = ".*/TABLE/.*REALTIME";
  private static final String CONTROLLER_LEADER_CHANGE = "CONTROLLER LEADER CHANGE";

  private String _propertyStorePath;
  private String _tableConfigPath;
  private final PinotHelixResourceManager _pinotHelixResourceManager;
  private ZkClient _zkClient;
  private ControllerMetrics _controllerMetrics;

  public PinotRealtimeSegmentManager(PinotHelixResourceManager pinotManager) {
    _pinotHelixResourceManager = pinotManager;
    String clusterName = _pinotHelixResourceManager.getHelixClusterName();
    _propertyStorePath = PropertyPathConfig.getPath(PropertyType.PROPERTYSTORE, clusterName);
    _tableConfigPath = _propertyStorePath + TABLE_CONFIG;
  }

  public void start(ControllerMetrics controllerMetrics) {
    _controllerMetrics = controllerMetrics;

    LOGGER.info("Starting realtime segments manager, adding a listener on the property store table configs path.");
    String zkUrl = _pinotHelixResourceManager.getHelixZkURL();
    _zkClient = new ZkClient(zkUrl, ZkClient.DEFAULT_SESSION_TIMEOUT, ZkClient.DEFAULT_CONNECTION_TIMEOUT);
    _zkClient.setZkSerializer(new ZNRecordSerializer());
    _zkClient.waitUntilConnected();

    // Subscribe to any data/child changes to property
    _zkClient.subscribeChildChanges(_tableConfigPath, this);
    _zkClient.subscribeDataChanges(_tableConfigPath, this);

    // Subscribe to leadership changes
    _pinotHelixResourceManager.getHelixZkManager().addControllerListener(new ControllerChangeListener() {
      @Override
      public void onControllerChange(NotificationContext changeContext) {
        processPropertyStoreChange(CONTROLLER_LEADER_CHANGE);
      }
    });

    // Setup change listeners for already existing tables, if any.
    processPropertyStoreChange(_tableConfigPath);
  }

  public void stop() {
    LOGGER.info("Stopping realtime segments manager, stopping property store.");
    _pinotHelixResourceManager.getPropertyStore().stop();
  }

  private synchronized void assignRealtimeSegmentsToServerInstancesIfNecessary()
      throws JSONException, IOException {
    // Fetch current ideal state snapshot
    Map<String, IdealState> idealStateMap = new HashMap<>();

    for (String realtimeTableName : _pinotHelixResourceManager.getAllRealtimeTables()) {
      TableConfig tableConfig = _pinotHelixResourceManager.getTableConfig(realtimeTableName);

      // Table config might have already been deleted
      if (tableConfig == null) {
        continue;
      }

      StreamMetadata metadata = new StreamMetadata(tableConfig.getIndexingConfig().getStreamConfigs());
      if (metadata.hasHighLevelKafkaConsumerType()) {
        idealStateMap.put(realtimeTableName, _pinotHelixResourceManager.getHelixAdmin()
            .getResourceIdealState(_pinotHelixResourceManager.getHelixClusterName(), realtimeTableName));
      } else {
        LOGGER.debug("Not considering table {} for realtime segment assignment");
      }
    }

    List<Pair<String, String>> listOfSegmentsToAddToInstances = new ArrayList<Pair<String, String>>();

    for (String realtimeTableName : idealStateMap.keySet()) {
      try {
        IdealState state = idealStateMap.get(realtimeTableName);

        // Are there any partitions?
        if (state.getPartitionSet().size() == 0) {
          // No, this is a brand new ideal state, so we will add one new segment to every partition and replica
          List<String> instancesInResource = new ArrayList<>();
          try {
            instancesInResource.addAll(_pinotHelixResourceManager.getServerInstancesForTable(realtimeTableName, TableType.REALTIME));
          } catch (Exception e) {
            LOGGER.error("Caught exception while fetching instances for resource {}", realtimeTableName, e);
            _controllerMetrics.addMeteredGlobalValue(ControllerMeter.CONTROLLER_REALTIME_TABLE_SEGMENT_ASSIGNMENT_ERROR, 1L);
          }

          // Assign a new segment to all server instances
          for (String instanceId : instancesInResource) {
            InstanceZKMetadata instanceZKMetadata = _pinotHelixResourceManager.getInstanceZKMetadata(instanceId);

            if (instanceZKMetadata == null) {
              LOGGER.warn("Instance {} has no associated instance metadata in ZK, ignoring for segment assignment.",
                  instanceId);
              _controllerMetrics.addMeteredGlobalValue(ControllerMeter.CONTROLLER_REALTIME_TABLE_SEGMENT_ASSIGNMENT_ERROR, 1L);
              continue;
            }

            String groupId = instanceZKMetadata.getGroupId(realtimeTableName);
            String partitionId = instanceZKMetadata.getPartition(realtimeTableName);
            if (groupId != null && !groupId.isEmpty() && partitionId != null && !partitionId.isEmpty()) {
              listOfSegmentsToAddToInstances.add(new Pair<String, String>(
                  new HLCSegmentName(groupId, partitionId, String.valueOf(System.currentTimeMillis())).getSegmentName(),
                  instanceId));
            } else {
              LOGGER.warn("Instance {} has invalid groupId ({}) and/or partitionId ({}) for resource {}, ignoring for segment assignment.",
                  instanceId, groupId, partitionId, realtimeTableName);
              _controllerMetrics.addMeteredGlobalValue(ControllerMeter.CONTROLLER_REALTIME_TABLE_SEGMENT_ASSIGNMENT_ERROR, 1L);
            }
          }
        } else {
          // Add all server instances to the list of instances for which to assign a realtime segment
          Set<String> instancesToAssignRealtimeSegment = new HashSet<String>();
          try {
            instancesToAssignRealtimeSegment.addAll(
                _pinotHelixResourceManager.getServerInstancesForTable(realtimeTableName, TableType.REALTIME));
          } catch (Exception e) {
            LOGGER.error("Caught exception while fetching instances for resource {}", realtimeTableName, e);
            _controllerMetrics.addMeteredGlobalValue(ControllerMeter.CONTROLLER_REALTIME_TABLE_SEGMENT_ASSIGNMENT_ERROR, 1L);
          }

          // Remove server instances that are currently processing a segment
          for (String partition : state.getPartitionSet()) {
            // Helix partition is the segment name
            if (SegmentName.isHighLevelConsumerSegmentName(partition)) {
              HLCSegmentName segName = new HLCSegmentName(partition);
              RealtimeSegmentZKMetadata realtimeSegmentZKMetadata = ZKMetadataProvider
                  .getRealtimeSegmentZKMetadata(_pinotHelixResourceManager.getPropertyStore(), segName.getTableName(),
                      partition);
              if (realtimeSegmentZKMetadata == null) {
                // Segment was deleted by retention manager.
                continue;
              }
              if (realtimeSegmentZKMetadata.getStatus() == Status.IN_PROGRESS) {
                instancesToAssignRealtimeSegment.removeAll(state.getInstanceSet(partition));
              }
            }
          }

          // Assign a new segment to the server instances not currently processing this segment
          for (String instanceId : instancesToAssignRealtimeSegment) {
            InstanceZKMetadata instanceZKMetadata = _pinotHelixResourceManager.getInstanceZKMetadata(instanceId);
            String groupId = instanceZKMetadata.getGroupId(realtimeTableName);
            String partitionId = instanceZKMetadata.getPartition(realtimeTableName);
            listOfSegmentsToAddToInstances.add(new Pair<String, String>(
                new HLCSegmentName(groupId, partitionId, String.valueOf(System.currentTimeMillis())).getSegmentName(),
                instanceId));
          }
        }
      } catch (Exception e) {
        LOGGER.warn("Caught exception while processing resource {}, skipping.", realtimeTableName, e);
        _controllerMetrics.addMeteredGlobalValue(ControllerMeter.CONTROLLER_REALTIME_TABLE_SEGMENT_ASSIGNMENT_ERROR, 1L);
      }
    }

    LOGGER.info("Computed list of new segments to add : " + Arrays.toString(listOfSegmentsToAddToInstances.toArray()));

    // Add the new segments to the server instances
    for (final Pair<String, String> segmentIdAndInstanceId : listOfSegmentsToAddToInstances) {
      final String segmentId = segmentIdAndInstanceId.getFirst();
      final String instanceName = segmentIdAndInstanceId.getSecond();

      try {
        final HLCSegmentName segName = new HLCSegmentName(segmentId);
        String resourceName = segName.getTableName();

        // Does the ideal state already contain this segment?
        if (!idealStateMap.get(resourceName).getPartitionSet().contains(segmentId)) {
          // No, add it
          // Create the realtime segment metadata
          RealtimeSegmentZKMetadata realtimeSegmentMetadataToAdd = new RealtimeSegmentZKMetadata();
          realtimeSegmentMetadataToAdd.setTableName(TableNameBuilder.extractRawTableName(resourceName));
          realtimeSegmentMetadataToAdd.setSegmentType(SegmentType.REALTIME);
          realtimeSegmentMetadataToAdd.setStatus(Status.IN_PROGRESS);
          realtimeSegmentMetadataToAdd.setSegmentName(segmentId);

          // Add the new metadata to the property store
          ZKMetadataProvider.setRealtimeSegmentZKMetadata(_pinotHelixResourceManager.getPropertyStore(),
              realtimeSegmentMetadataToAdd);

          // Update the ideal state to add the new realtime segment
          HelixHelper.updateIdealState(_pinotHelixResourceManager.getHelixZkManager(), resourceName, new Function<IdealState, IdealState>() {
            @Override
            public IdealState apply(IdealState idealState) {
              return PinotTableIdealStateBuilder.addNewRealtimeSegmentToIdealState(segmentId, idealState, instanceName);
            }
          }, RetryPolicies.exponentialBackoffRetryPolicy(5, 500L, 2.0f));
        }
      } catch (Exception e) {
        LOGGER.warn("Caught exception while processing segment {} for instance {}, skipping.", segmentId, instanceName, e);
        _controllerMetrics.addMeteredGlobalValue(ControllerMeter.CONTROLLER_REALTIME_TABLE_SEGMENT_ASSIGNMENT_ERROR, 1L);
      }
    }
  }

  private boolean isLeader() {
    return _pinotHelixResourceManager.isLeader();
  }

  @Override
  public synchronized void onDataChange(String path) {
    LOGGER.info("PinotRealtimeSegmentManager.onDataChange: {}", path);
    processPropertyStoreChange(path);
  }

  @Override
  public synchronized void onDataCreate(String path) {
    LOGGER.info("PinotRealtimeSegmentManager.onDataCreate: {}", path);
    processPropertyStoreChange(path);
  }

  @Override
  public synchronized void onDataDelete(String path) {
    LOGGER.info("PinotRealtimeSegmentManager.onDataDelete: {}", path);
    processPropertyStoreChange(path);
  }

  private void processPropertyStoreChange(String path) {
    try {
      LOGGER.info("Processing change notification for path: {}", path);
      refreshWatchers(path);

      if (isLeader()) {
        if (path.matches(REALTIME_SEGMENT_PROPERTY_STORE_PATH_PATTERN) ||
            path.matches(REALTIME_TABLE_CONFIG_PROPERTY_STORE_PATH_PATTERN) ||
            path.equals(CONTROLLER_LEADER_CHANGE)) {
          assignRealtimeSegmentsToServerInstancesIfNecessary();
        }
      } else {
        LOGGER.info("Not the leader of this cluster, ignoring realtime segment property store change.");
      }
    } catch (Exception e) {
      LOGGER.error("Caught exception while processing change for path {}", path, e);
      Utils.rethrowException(e);
    }
  }

  /**
   * Helper method to perform idempotent operation to refresh all watches (related to real-time segments):
   * - Data change listener for all existing real-time tables.
   * - Child creation listener for all existing real-time tables.
   * - Data change listener for all existing real-time segments
   *
   * @param path
   */
  private void refreshWatchers(String path) {
    LOGGER.info("Received change notification for path: {}", path);
    List<Stat> stats = new ArrayList<>();
    List<ZNRecord> tableConfigs = _pinotHelixResourceManager.getPropertyStore().getChildren(TABLE_CONFIG, stats, 0);

    if (tableConfigs == null) {
      return;
    }

    for (ZNRecord tableConfigZnRecord : tableConfigs) {
      try {
        String znRecordId = tableConfigZnRecord.getId();
        if (TableNameBuilder.getTableTypeFromTableName(znRecordId) == TableType.REALTIME) {
          TableConfig tableConfig = TableConfig.fromZnRecord(tableConfigZnRecord);
          StreamMetadata metadata = new StreamMetadata(tableConfig.getIndexingConfig().getStreamConfigs());
          if (metadata.hasHighLevelKafkaConsumerType()) {
            String realtimeTable = tableConfig.getTableName();
            String realtimeSegmentsPathForTable = _propertyStorePath + SEGMENTS_PATH + "/" + realtimeTable;

            LOGGER.info("Setting data/child changes watch for real-time table '{}'", realtimeTable);
            _zkClient.subscribeDataChanges(realtimeSegmentsPathForTable, this);
            _zkClient.subscribeChildChanges(realtimeSegmentsPathForTable, this);

            List<String> childNames =
                _pinotHelixResourceManager.getPropertyStore().getChildNames(SEGMENTS_PATH + "/" + realtimeTable, 0);

            if (childNames != null && !childNames.isEmpty()) {
              for (String segmentName : childNames) {
                if (!SegmentName.isHighLevelConsumerSegmentName(segmentName)) {
                  continue;
                }
                String segmentPath = realtimeSegmentsPathForTable + "/" + segmentName;
                RealtimeSegmentZKMetadata realtimeSegmentZKMetadata =
                    ZKMetadataProvider.getRealtimeSegmentZKMetadata(_pinotHelixResourceManager.getPropertyStore(),
                        tableConfig.getTableName(), segmentName);
                if (realtimeSegmentZKMetadata == null) {
                  // The segment got deleted by retention manager
                  continue;
                }
                if (realtimeSegmentZKMetadata.getStatus() == Status.IN_PROGRESS) {
                  LOGGER.info("Setting data change watch for real-time segment currently being consumed: {}",
                      segmentPath);
                  _zkClient.subscribeDataChanges(segmentPath, this);
                } else {
                  _zkClient.unsubscribeDataChanges(segmentPath, this);
                }
              }
            }
          }
        }
      } catch (Exception e) {
        // we want to continue setting watches for other tables for any kind of exception here so that
        // errors with one table don't impact others
        if (tableConfigZnRecord == null) {
          // Can happen if the table config zn record failed to parse.
          LOGGER.error("Got null ZN record for table config", e);
        } else {
          LOGGER.error("Caught exception while processing ZNRecord id: {}. Skipping node to continue setting watches",
              tableConfigZnRecord.getId(), e);
        }
      }
    }
  }

  @Override
  public void handleChildChange(String parentPath, List<String> currentChildren)
      throws Exception {
    LOGGER.info("PinotRealtimeSegmentManager.handleChildChange: {}", parentPath);
    processPropertyStoreChange(parentPath);

    // If parent path get removed, currentChildren will be null
    if (currentChildren != null) {
      for (String table : currentChildren) {
        if (table.endsWith("_REALTIME")) {
          LOGGER.info("PinotRealtimeSegmentManager.handleChildChange with table: {}", parentPath + "/" + table);
          processPropertyStoreChange(parentPath + "/" + table);
        }
      }
    }
  }

  @Override
  public void handleDataChange(String dataPath, Object data)
      throws Exception {
    LOGGER.info("PinotRealtimeSegmentManager.handleDataChange: {}", dataPath);
    processPropertyStoreChange(dataPath);
  }

  @Override
  public void handleDataDeleted(String dataPath)
      throws Exception {
    LOGGER.info("PinotRealtimeSegmentManager.handleDataDeleted: {}", dataPath);
    processPropertyStoreChange(dataPath);
  }
}
