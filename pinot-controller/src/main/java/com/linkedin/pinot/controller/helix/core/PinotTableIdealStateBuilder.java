/**
 * Copyright (C) 2014-2018 LinkedIn Corp. (pinot-core@linkedin.com)
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
package com.linkedin.pinot.controller.helix.core;

import com.linkedin.pinot.common.config.RealtimeTagConfig;
import com.linkedin.pinot.common.config.TableConfig;
import com.linkedin.pinot.common.exception.InvalidConfigException;
import com.linkedin.pinot.common.metadata.ZKMetadataProvider;
import com.linkedin.pinot.common.metadata.instance.InstanceZKMetadata;
import com.linkedin.pinot.common.utils.CommonConstants;
import com.linkedin.pinot.common.utils.CommonConstants.Helix;
import com.linkedin.pinot.common.utils.StringUtil;
import com.linkedin.pinot.common.utils.retry.RetryPolicies;
import com.linkedin.pinot.controller.helix.core.realtime.PinotLLCRealtimeSegmentManager;
import com.linkedin.pinot.core.realtime.impl.kafka.SimpleConsumerWrapper;
import com.linkedin.pinot.core.realtime.stream.PinotStreamConsumer;
import com.linkedin.pinot.core.realtime.stream.PinotStreamConsumerFactory;
import com.linkedin.pinot.core.realtime.stream.StreamMetadata;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.helix.HelixAdmin;
import org.apache.helix.ZNRecord;
import org.apache.helix.model.IdealState;
import org.apache.helix.model.builder.CustomModeISBuilder;
import org.apache.helix.store.zk.ZkHelixPropertyStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Pinot data server layer IdealState builder.
 *
 *
 */
public class PinotTableIdealStateBuilder {
  private static final Logger LOGGER = LoggerFactory.getLogger(PinotTableIdealStateBuilder.class);
  public static final String ONLINE = "ONLINE";
  public static final String OFFLINE = "OFFLINE";

  /**
   *
   * Building an empty idealState for a given table.
   * Used when creating a new table.
   *
   * @param tableName resource name
   * @param numCopies is the number of replicas
   * @return
   */
  public static IdealState buildEmptyIdealStateFor(String tableName, int numCopies) {
    final CustomModeISBuilder customModeIdealStateBuilder = new CustomModeISBuilder(tableName);
    final int replicas = numCopies;
    customModeIdealStateBuilder
        .setStateModel(PinotHelixSegmentOnlineOfflineStateModelGenerator.PINOT_SEGMENT_ONLINE_OFFLINE_STATE_MODEL)
        .setNumPartitions(0).setNumReplica(replicas).setMaxPartitionsPerNode(1);
    final IdealState idealState = customModeIdealStateBuilder.build();
    idealState.setInstanceGroupTag(tableName);
    return idealState;
  }

  /**
   *
   * Building an empty idealState for a given table.
   * Used when creating a new table.
   *
   * @param helixAdmin
   * @param helixClusterName
   * @return
   */
  public static IdealState buildEmptyIdealStateForBrokerResource(HelixAdmin helixAdmin, String helixClusterName) {
    final CustomModeISBuilder customModeIdealStateBuilder =
        new CustomModeISBuilder(CommonConstants.Helix.BROKER_RESOURCE_INSTANCE);
    customModeIdealStateBuilder
        .setStateModel(
            PinotHelixBrokerResourceOnlineOfflineStateModelGenerator.PINOT_BROKER_RESOURCE_ONLINE_OFFLINE_STATE_MODEL)
        .setMaxPartitionsPerNode(Integer.MAX_VALUE).setNumReplica(Integer.MAX_VALUE)
        .setNumPartitions(Integer.MAX_VALUE);
    final IdealState idealState = customModeIdealStateBuilder.build();
    return idealState;
  }

  public static IdealState addNewRealtimeSegmentToIdealState(String segmentId, IdealState state, String instanceName) {
    state.setPartitionState(segmentId, instanceName, ONLINE);
    state.setNumPartitions(state.getNumPartitions() + 1);
    return state;
  }

  public static IdealState buildInitialHighLevelRealtimeIdealStateFor(String realtimeTableName,
      TableConfig realtimeTableConfig, HelixAdmin helixAdmin, String helixClusterName,
      ZkHelixPropertyStore<ZNRecord> zkHelixPropertyStore) {
    RealtimeTagConfig realtimeTagConfig = new RealtimeTagConfig(realtimeTableConfig);
    final List<String> realtimeInstances = helixAdmin.getInstancesInClusterWithTag(helixClusterName,
        realtimeTagConfig.getConsumingServerTag());
    IdealState idealState = buildEmptyKafkaConsumerRealtimeIdealStateFor(realtimeTableName, 1);
    if (realtimeInstances.size() % Integer.parseInt(realtimeTableConfig.getValidationConfig().getReplication()) != 0) {
      throw new RuntimeException(
          "Number of instance in current tenant should be an integer multiples of the number of replications");
    }
    setupInstanceConfigForKafkaHighLevelConsumer(realtimeTableName, realtimeInstances.size(),
        Integer.parseInt(realtimeTableConfig.getValidationConfig().getReplication()),
        realtimeTableConfig.getIndexingConfig().getStreamConfigs(), zkHelixPropertyStore, realtimeInstances);
    return idealState;
  }

  public static void buildLowLevelRealtimeIdealStateFor(String realtimeTableName, TableConfig realtimeTableConfig,
      IdealState idealState) {

    // Validate replicasPerPartition here.
    final String replicasPerPartitionStr = realtimeTableConfig.getValidationConfig().getReplicasPerPartition();
    if (replicasPerPartitionStr == null || replicasPerPartitionStr.isEmpty()) {
      throw new RuntimeException("Null or empty value for replicasPerPartition, expected a number");
    }
    final int nReplicas;
    try {
      nReplicas = Integer.valueOf(replicasPerPartitionStr);
    } catch (NumberFormatException e) {
      throw new PinotHelixResourceManager.InvalidTableConfigException(
          "Invalid value for replicasPerPartition, expected a number: " + replicasPerPartitionStr, e);
    }
    if (idealState == null) {
      idealState = buildEmptyKafkaConsumerRealtimeIdealStateFor(realtimeTableName, nReplicas);
    }
    final PinotLLCRealtimeSegmentManager segmentManager = PinotLLCRealtimeSegmentManager.getInstance();
    try {
      segmentManager.setupNewTable(realtimeTableConfig, idealState);
    } catch (InvalidConfigException e) {
      throw new IllegalStateException("Caught exception when creating table " + realtimeTableName, e);
    }
  }

  public static int getPartitionCount(StreamMetadata kafkaMetadata) {
    KafkaPartitionsCountFetcher fetcher = new KafkaPartitionsCountFetcher(kafkaMetadata);
    try {
      RetryPolicies.noDelayRetryPolicy(3).attempt(fetcher);
      return fetcher.getPartitionCount();
    } catch (Exception e) {
      Exception fetcherException = fetcher.getException();
      LOGGER.error("Could not get partition count for {}", kafkaMetadata.getKafkaTopicName(), fetcherException);
      throw new RuntimeException(fetcherException);
    }
  }

  public static IdealState buildEmptyKafkaConsumerRealtimeIdealStateFor(String realtimeTableName, int replicaCount) {
    final CustomModeISBuilder customModeIdealStateBuilder = new CustomModeISBuilder(realtimeTableName);
    customModeIdealStateBuilder
        .setStateModel(PinotHelixSegmentOnlineOfflineStateModelGenerator.PINOT_SEGMENT_ONLINE_OFFLINE_STATE_MODEL)
        .setNumPartitions(0).setNumReplica(replicaCount).setMaxPartitionsPerNode(1);
    final IdealState idealState = customModeIdealStateBuilder.build();
    idealState.setInstanceGroupTag(realtimeTableName);

    return idealState;
  }

  private static void setupInstanceConfigForKafkaHighLevelConsumer(String realtimeTableName, int numDataInstances,
      int numDataReplicas, Map<String, String> streamProviderConfig,
      ZkHelixPropertyStore<ZNRecord> zkHelixPropertyStore, List<String> instanceList) {
    int numInstancesPerReplica = numDataInstances / numDataReplicas;
    int partitionId = 0;
    int replicaId = 0;

    String groupId = getGroupIdFromRealtimeDataTable(realtimeTableName, streamProviderConfig);
    for (int i = 0; i < numInstancesPerReplica * numDataReplicas; ++i) {
      String instance = instanceList.get(i);
      InstanceZKMetadata instanceZKMetadata = ZKMetadataProvider.getInstanceZKMetadata(zkHelixPropertyStore, instance);
      if (instanceZKMetadata == null) {
        instanceZKMetadata = new InstanceZKMetadata();
        String[] instanceConfigs = instance.split("_");
        assert (instanceConfigs.length == 3);
        instanceZKMetadata.setInstanceType(instanceConfigs[0]);
        instanceZKMetadata.setInstanceName(instanceConfigs[1]);
        instanceZKMetadata.setInstancePort(Integer.parseInt(instanceConfigs[2]));
      }
      instanceZKMetadata.setGroupId(realtimeTableName, groupId + "_" + replicaId);
      instanceZKMetadata.setPartition(realtimeTableName, Integer.toString(partitionId));
      partitionId = (partitionId + 1) % numInstancesPerReplica;
      if (partitionId == 0) {
        replicaId++;
      }
      ZKMetadataProvider.setInstanceZKMetadata(zkHelixPropertyStore, instanceZKMetadata);
    }
  }

  private static String getGroupIdFromRealtimeDataTable(String realtimeTableName,
      Map<String, String> streamProviderConfig) {
    String keyOfGroupId =
        StringUtil
            .join(".", Helix.DataSource.STREAM_PREFIX, Helix.DataSource.Realtime.Kafka.HighLevelConsumer.GROUP_ID);
    String groupId = StringUtil.join("_", realtimeTableName, System.currentTimeMillis() + "");
    if (streamProviderConfig.containsKey(keyOfGroupId) && !streamProviderConfig.get(keyOfGroupId).isEmpty()) {
      groupId = streamProviderConfig.get(keyOfGroupId);
    }
    return groupId;
  }

  private static class KafkaPartitionsCountFetcher implements Callable<Boolean> {
    private int _partitionCount = -1;
    private final StreamMetadata _streamMetadata;
    private Exception _exception;

    private KafkaPartitionsCountFetcher(StreamMetadata streamMetadata) {
      _streamMetadata = streamMetadata;
    }

    private int getPartitionCount() {
      return _partitionCount;
    }

    private Exception getException() {
      return _exception;
    }

    @Override
    public Boolean call() throws Exception {
      final String bootstrapHosts = _streamMetadata.getBootstrapHosts();
      final String kafkaTopicName = _streamMetadata.getKafkaTopicName();
      if (bootstrapHosts == null || bootstrapHosts.isEmpty()) {
        LOGGER.warn("Could not get partition count for topic {}. Invalid config for bootstrap hosts:'{}'",
            kafkaTopicName, bootstrapHosts);
        throw new RuntimeException("Invalid value for " + Helix.DataSource.Realtime.Kafka.KAFKA_BROKER_LIST + ":'"
            + bootstrapHosts + "'");
      }
      PinotStreamConsumerFactory pinotStreamConsumerFactory = PinotStreamConsumerFactory.create(_streamMetadata);
      PinotStreamConsumer consumerWrapper = pinotStreamConsumerFactory.buildMetadataFetcher(
          PinotTableIdealStateBuilder.class.getSimpleName() + "-" + kafkaTopicName, _streamMetadata);
      try {
        _partitionCount = consumerWrapper.getPartitionCount(kafkaTopicName, /*maxWaitTimeMs=*/5000L);
        if (_exception != null) {
          // We had at least one failure, but succeeded now. Log an info
          LOGGER.info("Successfully retrieved partition count as {} for topic {}", _partitionCount, kafkaTopicName);
        }
        return Boolean.TRUE;
      } catch (SimpleConsumerWrapper.TransientConsumerException e) {
        LOGGER.warn("Could not get Kafka partition count for topic {}:{}", kafkaTopicName, e);
        _exception = e;
        return Boolean.FALSE;
      } catch (Exception e) {
        LOGGER.warn("Could not get Kafka partition count for topic {}:{}", kafkaTopicName, e);
        _exception = e;
        throw e;
      } finally {
        IOUtils.closeQuietly(consumerWrapper);
      }
    }
  }
}
