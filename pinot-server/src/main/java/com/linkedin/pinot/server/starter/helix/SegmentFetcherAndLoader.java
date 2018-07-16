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
package com.linkedin.pinot.server.starter.helix;

import com.google.common.base.Preconditions;
import com.linkedin.pinot.common.Utils;
import com.linkedin.pinot.common.config.TableNameBuilder;
import com.linkedin.pinot.common.data.DataManager;
import com.linkedin.pinot.common.metadata.ZKMetadataProvider;
import com.linkedin.pinot.common.metadata.segment.OfflineSegmentZKMetadata;
import com.linkedin.pinot.common.segment.SegmentMetadata;
import com.linkedin.pinot.common.segment.fetcher.SegmentFetcherFactory;
import com.linkedin.pinot.common.utils.CommonConstants;
import com.linkedin.pinot.common.utils.TarGzCompressionUtils;
import com.linkedin.pinot.core.segment.index.SegmentMetadataImpl;
import com.linkedin.pinot.core.segment.index.loader.LoaderUtils;
import com.linkedin.pinot.core.segment.index.loader.V3RemoveIndexException;
import java.io.File;
import java.util.concurrent.locks.Lock;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.io.FileUtils;
import org.apache.helix.ZNRecord;
import org.apache.helix.store.zk.ZkHelixPropertyStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SegmentFetcherAndLoader {
  private static final Logger LOGGER = LoggerFactory.getLogger(SegmentFetcherAndLoader.class);

  private final ZkHelixPropertyStore<ZNRecord> _propertyStore;
  private final DataManager _dataManager;

  public SegmentFetcherAndLoader(DataManager dataManager, ZkHelixPropertyStore<ZNRecord> propertyStore,
      Configuration pinotHelixProperties) throws Exception {
    _propertyStore = propertyStore;
    _dataManager = dataManager;

    Configuration segmentFetcherFactoryConfig =
        pinotHelixProperties.subset(CommonConstants.Server.PREFIX_OF_CONFIG_OF_SEGMENT_FETCHER_FACTORY);

    SegmentFetcherFactory.getInstance().init(segmentFetcherFactoryConfig);
  }

  public void addOrReplaceOfflineSegment(String tableNameWithType, String segmentName) {
    OfflineSegmentZKMetadata newSegmentZKMetadata =
        ZKMetadataProvider.getOfflineSegmentZKMetadata(_propertyStore, tableNameWithType, segmentName);
    Preconditions.checkNotNull(newSegmentZKMetadata);

    LOGGER.info("Adding or replacing segment {} for table {}, metadata {}", segmentName, tableNameWithType,
        newSegmentZKMetadata);

    // This method might modify the file on disk. Use segment lock to prevent race condition
    Lock segmentLock = SegmentLocks.getSegmentLock(tableNameWithType, segmentName);
    try {
      segmentLock.lock();

      // We lock the segment in order to get its metadata, and then release the lock, so it is possible
      // that the segment is dropped after we get its metadata.
      SegmentMetadata localSegmentMetadata = _dataManager.getSegmentMetadata(tableNameWithType, segmentName);

      if (localSegmentMetadata == null) {
        LOGGER.info("Segment {} of table {} is not loaded in memory, checking disk", segmentName, tableNameWithType);
        File indexDir = new File(getSegmentLocalDirectory(tableNameWithType, segmentName));
        // Restart during segment reload might leave segment in inconsistent state (index directory might not exist but
        // segment backup directory existed), need to first try to recover from reload failure before checking the
        // existence of the index directory and loading segment metadata from it
        LoaderUtils.reloadFailureRecovery(indexDir);
        if (indexDir.exists()) {
          LOGGER.info("Segment {} of table {} found on disk, attempting to load it", segmentName, tableNameWithType);
          try {
            localSegmentMetadata = new SegmentMetadataImpl(indexDir);
            LOGGER.info("Found segment {} of table {} with crc {} on disk", segmentName, tableNameWithType,
                localSegmentMetadata.getCrc());
          } catch (Exception e) {
            // The localSegmentDir should help us get the table name,
            LOGGER.error("Failed to load segment metadata from {}. Deleting it.", indexDir, e);
            FileUtils.deleteQuietly(indexDir);
            localSegmentMetadata = null;
          }
          try {
            if (!isNewSegmentMetadata(newSegmentZKMetadata, localSegmentMetadata)) {
              LOGGER.info("Segment metadata same as before, loading {} of table {} (crc {}) from disk", segmentName,
                  tableNameWithType, localSegmentMetadata.getCrc());
              _dataManager.addOfflineSegment(tableNameWithType, segmentName, indexDir);
              // TODO Update zk metadata with CRC for this instance
              return;
            }
          } catch (V3RemoveIndexException e) {
            LOGGER.info(
                "Unable to remove local index from V3 format segment: {}, table: {}, try to reload it from controller.",
                segmentName, tableNameWithType, e);
            FileUtils.deleteQuietly(indexDir);
            localSegmentMetadata = null;
          } catch (Exception e) {
            LOGGER.error("Failed to load {} of table {} from local, will try to reload it from controller!",
                segmentName, tableNameWithType, e);
            FileUtils.deleteQuietly(indexDir);
            localSegmentMetadata = null;
          }
        }
      }
      // There is a very unlikely race condition that we may have gotten the metadata of a
      // segment that was not dropped when we checked, but was dropped after the check above.
      // That is possible only if we get two helix transitions (to drop, and then to add back) the
      // segment at the same, or very close to each other.If the race condition triggers, and the
      // two segments are same in metadata, then we may end up NOT adding back the segment
      // that is in the process of being dropped.

      // If we get here, then either it is the case that we have the segment loaded in memory (and therefore present
      // in disk) or, we need to load from the server. In the former case, we still need to check if the metadata
      // that we have is different from that in zookeeper.
      if (isNewSegmentMetadata(newSegmentZKMetadata, localSegmentMetadata)) {
        if (localSegmentMetadata == null) {
          LOGGER.info("Loading new segment {} of table {} from controller", segmentName, tableNameWithType);
        } else {
          LOGGER.info("Trying to refresh segment {} of table {} with new data.", segmentName, tableNameWithType);
        }
        String uri = newSegmentZKMetadata.getDownloadUrl();
        // Retry will be done here.
        String localSegmentDir = downloadSegmentToLocal(uri, tableNameWithType, segmentName);
        SegmentMetadata segmentMetadata = new SegmentMetadataImpl(new File(localSegmentDir));
        _dataManager.addOfflineSegment(tableNameWithType, segmentName, new File(localSegmentDir));
        LOGGER.info("Downloaded segment {} of table {} crc {} from controller", segmentName, tableNameWithType,
            segmentMetadata.getCrc());
      } else {
        LOGGER.info("Got already loaded segment {} of table {} crc {} again, will do nothing.", segmentName,
            tableNameWithType, localSegmentMetadata.getCrc());
      }
    } catch (final Exception e) {
      LOGGER.error("Cannot load segment : " + segmentName + " for table " + tableNameWithType, e);
      Utils.rethrowException(e);
      throw new AssertionError("Should not reach this");
    } finally {
      segmentLock.unlock();
    }
  }

  private boolean isNewSegmentMetadata(@Nonnull OfflineSegmentZKMetadata newSegmentZKMetadata,
      @Nullable SegmentMetadata existedSegmentMetadata) {
    String offlineTableName = TableNameBuilder.OFFLINE.tableNameWithType(newSegmentZKMetadata.getTableName());
    String segmentName = newSegmentZKMetadata.getSegmentName();

    if (existedSegmentMetadata == null) {
      LOGGER.info("Existed segment metadata is null for segment: {} in table: {}", segmentName, offlineTableName);
      return true;
    }

    long newCrc = newSegmentZKMetadata.getCrc();
    long existedCrc = Long.valueOf(existedSegmentMetadata.getCrc());
    LOGGER.info("New segment CRC: {}, existed segment CRC: {} for segment: {} in table: {}", newCrc, existedCrc,
        segmentName, offlineTableName);
    return newCrc != existedCrc;
  }

  @Nonnull
  private String downloadSegmentToLocal(@Nonnull String uri, @Nonnull String tableName, @Nonnull String segmentName)
      throws Exception {
    File tempDir = new File(new File(_dataManager.getSegmentFileDirectory(), tableName),
        "tmp_" + segmentName + "_" + System.nanoTime());
    FileUtils.forceMkdir(tempDir);
    File tempTarFile = new File(tempDir, segmentName + ".tar.gz");
    File tempSegmentDir = new File(tempDir, segmentName);
    try {
      SegmentFetcherFactory.getInstance().getSegmentFetcherBasedOnURI(uri).fetchSegmentToLocal(uri, tempTarFile);
      LOGGER.info("Downloaded tarred segment: {} for table: {} from: {} to: {}, file length: {}", segmentName,
          tableName, uri, tempTarFile, tempTarFile.length());

      // If an exception is thrown when untarring, it means the tar file is broken OR not found after the retry.
      // Thus, there's no need to retry again.
      TarGzCompressionUtils.unTar(tempTarFile, tempSegmentDir);

      File[] files = tempSegmentDir.listFiles();
      Preconditions.checkState(files != null && files.length == 1);
      File tempIndexDir = files[0];

      File indexDir = new File(new File(_dataManager.getSegmentDataDirectory(), tableName), segmentName);
      if (indexDir.exists()) {
        LOGGER.info("Deleting existing index directory for segment: {} for table: {}", segmentName, tableName);
        FileUtils.deleteDirectory(indexDir);
      }
      FileUtils.moveDirectory(tempIndexDir, indexDir);
      LOGGER.info("Successfully downloaded segment: {} for table: {} to: {}", segmentName, tableName, indexDir);
      return indexDir.getAbsolutePath();
    } finally {
      FileUtils.deleteQuietly(tempDir);
    }
  }

  public String getSegmentLocalDirectory(String tableName, String segmentId) {
    return _dataManager.getSegmentDataDirectory() + "/" + tableName + "/" + segmentId;
  }
}
