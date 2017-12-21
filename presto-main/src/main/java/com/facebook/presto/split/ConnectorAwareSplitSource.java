/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.facebook.presto.split;

import com.facebook.presto.connector.ConnectorId;
import com.facebook.presto.execution.Lifespan;
import com.facebook.presto.metadata.Split;
import com.facebook.presto.spi.ConnectorSplit;
import com.facebook.presto.spi.ConnectorSplitSource;
import com.facebook.presto.spi.ConnectorSplitSource.ConnectorSplitBatch;
import com.facebook.presto.spi.connector.ConnectorPartitionHandle;
import com.facebook.presto.spi.connector.ConnectorSplitManager.SplitSchedulingStrategy;
import com.facebook.presto.spi.connector.ConnectorTransactionHandle;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.List;

import static com.facebook.presto.spi.connector.ConnectorSplitManager.SplitSchedulingStrategy.UNGROUPED_SCHEDULING;
import static com.google.common.base.Preconditions.checkState;
import static io.airlift.concurrent.MoreFutures.toListenableFuture;
import static java.util.Objects.requireNonNull;

public class ConnectorAwareSplitSource
        implements SplitSource
{
    private final ConnectorId connectorId;
    private final ConnectorTransactionHandle transactionHandle;
    private final ConnectorSplitSource source;

    private final SplitSchedulingStrategy splitSchedulingStrategy;

    public ConnectorAwareSplitSource(
            ConnectorId connectorId,
            ConnectorTransactionHandle transactionHandle,
            ConnectorSplitSource source,
            SplitSchedulingStrategy splitSchedulingStrategy)
    {
        this.connectorId = requireNonNull(connectorId, "connectorId is null");
        this.transactionHandle = requireNonNull(transactionHandle, "transactionHandle is null");
        this.source = requireNonNull(source, "source is null");
        this.splitSchedulingStrategy = requireNonNull(splitSchedulingStrategy, "splitSchedulingStrategy is null");
    }

    @Override
    public ConnectorId getConnectorId()
    {
        return connectorId;
    }

    @Override
    public ConnectorTransactionHandle getTransactionHandle()
    {
        return transactionHandle;
    }

    @Override
    public ListenableFuture<List<Split>> getNextBatch(int maxSize)
    {
        checkState(splitSchedulingStrategy == UNGROUPED_SCHEDULING);
        ListenableFuture<List<ConnectorSplit>> nextBatch = toListenableFuture(source.getNextBatch(maxSize));
        return Futures.transform(nextBatch, splits -> Lists.transform(splits, split -> new Split(connectorId, transactionHandle, split)));
    }

    @Override
    public ListenableFuture<SplitBatch> getNextBatch(ConnectorPartitionHandle partitionHandle, Lifespan lifespan, int maxSize)
    {
        ListenableFuture<ConnectorSplitBatch> nextBatch = toListenableFuture(source.getNextBatch(partitionHandle, maxSize));
        return Futures.transform(nextBatch, splitBatch -> {
            ImmutableList.Builder<Split> result = ImmutableList.builder();
            for (ConnectorSplit connectorSplit : splitBatch.getSplits()) {
                result.add(new Split(connectorId, transactionHandle, connectorSplit, lifespan));
            }
            return new SplitBatch(result.build(), splitBatch.isNoMoreSplits());
        });
    }

    @Override
    public void close()
    {
        source.close();
    }

    @Override
    public boolean isFinished()
    {
        return source.isFinished();
    }

    @Override
    public String toString()
    {
        return connectorId + ":" + source;
    }
}
