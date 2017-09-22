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
package com.facebook.presto.orc.writer;

import com.facebook.presto.orc.checkpoint.BooleanStreamCheckpoint;
import com.facebook.presto.orc.checkpoint.DecimalStreamCheckpoint;
import com.facebook.presto.orc.checkpoint.LongStreamCheckpoint;
import com.facebook.presto.orc.metadata.ColumnEncoding;
import com.facebook.presto.orc.metadata.CompressionKind;
import com.facebook.presto.orc.metadata.MetadataWriter;
import com.facebook.presto.orc.metadata.RowGroupIndex;
import com.facebook.presto.orc.metadata.Stream;
import com.facebook.presto.orc.metadata.Stream.StreamKind;
import com.facebook.presto.orc.metadata.statistics.ColumnStatistics;
import com.facebook.presto.orc.metadata.statistics.LongDecimalStatisticsBuilder;
import com.facebook.presto.orc.metadata.statistics.ShortDecimalStatisticsBuilder;
import com.facebook.presto.orc.stream.DecimalOutputStream;
import com.facebook.presto.orc.stream.LongOutputStream;
import com.facebook.presto.orc.stream.LongOutputStreamV2;
import com.facebook.presto.orc.stream.PresentOutputStream;
import com.facebook.presto.spi.block.Block;
import com.facebook.presto.spi.type.DecimalType;
import com.facebook.presto.spi.type.Decimals;
import com.facebook.presto.spi.type.Type;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import io.airlift.slice.Slice;
import io.airlift.slice.SliceOutput;
import org.openjdk.jol.info.ClassLayout;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.facebook.presto.orc.metadata.ColumnEncoding.ColumnEncodingKind.DIRECT_V2;
import static com.facebook.presto.orc.metadata.CompressionKind.NONE;
import static com.facebook.presto.orc.metadata.Stream.StreamKind.SECONDARY;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
import static java.util.Objects.requireNonNull;

public class DecimalColumnWriter
        implements ColumnWriter
{
    private static final int INSTANCE_SIZE = ClassLayout.parseClass(DecimalColumnWriter.class).instanceSize();
    private final int column;
    private final DecimalType type;
    private final ColumnEncoding columnEncoding;
    private final boolean compressed;
    private final DecimalOutputStream dataStream;
    private final LongOutputStream scaleStream;
    private final PresentOutputStream presentStream;

    private final List<ColumnStatistics> rowGroupColumnStatistics = new ArrayList<>();

    private ShortDecimalStatisticsBuilder shortDecimalStatisticsBuilder;
    private LongDecimalStatisticsBuilder longDecimalStatisticsBuilder;

    private boolean closed;

    public DecimalColumnWriter(int column, Type type, CompressionKind compression, int bufferSize, boolean isDwrf)
    {
        checkArgument(column >= 0, "column is negative");
        checkArgument(!isDwrf, "DWRF does not support %s type", type);
        this.column = column;
        this.type = (DecimalType) requireNonNull(type, "type is null");
        this.compressed = requireNonNull(compression, "compression is null") != NONE;
        this.columnEncoding = new ColumnEncoding(DIRECT_V2, 0);
        this.dataStream = new DecimalOutputStream(compression, bufferSize);
        this.scaleStream = new LongOutputStreamV2(compression, bufferSize, true, SECONDARY);
        this.presentStream = new PresentOutputStream(compression, bufferSize);
        if (this.type.isShort()) {
            shortDecimalStatisticsBuilder = new ShortDecimalStatisticsBuilder(this.type.getScale());
        }
        else {
            longDecimalStatisticsBuilder = new LongDecimalStatisticsBuilder();
        }
    }

    @Override
    public Map<Integer, ColumnEncoding> getColumnEncodings()
    {
        return ImmutableMap.of(column, columnEncoding);
    }

    @Override
    public void beginRowGroup()
    {
        presentStream.recordCheckpoint();
        dataStream.recordCheckpoint();
        scaleStream.recordCheckpoint();
    }

    @Override
    public void writeBlock(Block block)
    {
        checkState(!closed);
        checkArgument(block.getPositionCount() > 0, "Block is empty");

        // record nulls
        for (int position = 0; position < block.getPositionCount(); position++) {
            presentStream.writeBoolean(!block.isNull(position));
        }

        // record values
        if (type.isShort()) {
            for (int position = 0; position < block.getPositionCount(); position++) {
                if (!block.isNull(position)) {
                    long value = type.getLong(block, position);
                    dataStream.writeUnscaledValue(value);
                    shortDecimalStatisticsBuilder.addValue(value);
                }
            }
        }
        else {
            for (int position = 0; position < block.getPositionCount(); position++) {
                if (!block.isNull(position)) {
                    Slice value = type.getSlice(block, position);
                    dataStream.writeUnscaledValue(value);

                    longDecimalStatisticsBuilder.addValue(new BigDecimal(Decimals.decodeUnscaledValue(value), type.getScale()));
                }
            }
        }
        for (int position = 0; position < block.getPositionCount(); position++) {
            if (!block.isNull(position)) {
                scaleStream.writeLong(type.getScale());
            }
        }
    }

    @Override
    public Map<Integer, ColumnStatistics> finishRowGroup()
    {
        checkState(!closed);
        ColumnStatistics statistics;
        if (type.isShort()) {
            statistics = shortDecimalStatisticsBuilder.buildColumnStatistics();
            shortDecimalStatisticsBuilder = new ShortDecimalStatisticsBuilder(type.getScale());
        }
        else {
            statistics = longDecimalStatisticsBuilder.buildColumnStatistics();
            longDecimalStatisticsBuilder = new LongDecimalStatisticsBuilder();
        }
        rowGroupColumnStatistics.add(statistics);

        return ImmutableMap.of(column, statistics);
    }

    @Override
    public void close()
    {
        closed = true;
        dataStream.close();
        scaleStream.close();
        presentStream.close();
    }

    @Override
    public Map<Integer, ColumnStatistics> getColumnStripeStatistics()
    {
        checkState(closed);
        return ImmutableMap.of(column, ColumnStatistics.mergeColumnStatistics(rowGroupColumnStatistics));
    }

    @Override
    public List<Stream> writeIndexStreams(SliceOutput outputStream, MetadataWriter metadataWriter)
            throws IOException
    {
        checkState(closed);

        ImmutableList.Builder<RowGroupIndex> rowGroupIndexes = ImmutableList.builder();

        List<DecimalStreamCheckpoint> dataCheckpoints = dataStream.getCheckpoints();
        List<LongStreamCheckpoint> scaleCheckpoints = scaleStream.getCheckpoints();
        Optional<List<BooleanStreamCheckpoint>> presentCheckpoints = presentStream.getCheckpoints();
        for (int i = 0; i < rowGroupColumnStatistics.size(); i++) {
            int groupId = i;
            ColumnStatistics columnStatistics = rowGroupColumnStatistics.get(groupId);
            DecimalStreamCheckpoint dataCheckpoint = dataCheckpoints.get(groupId);
            LongStreamCheckpoint scaleCheckpoint = scaleCheckpoints.get(groupId);
            Optional<BooleanStreamCheckpoint> presentCheckpoint = presentCheckpoints.map(checkpoints -> checkpoints.get(groupId));
            List<Integer> positions = createDecimalColumnPositionList(compressed, dataCheckpoint, scaleCheckpoint, presentCheckpoint);
            rowGroupIndexes.add(new RowGroupIndex(positions, columnStatistics));
        }

        int length = metadataWriter.writeRowIndexes(outputStream, rowGroupIndexes.build());
        return ImmutableList.of(new Stream(column, StreamKind.ROW_INDEX, length, false));
    }

    private static List<Integer> createDecimalColumnPositionList(
            boolean compressed,
            DecimalStreamCheckpoint dataCheckpoint,
            LongStreamCheckpoint scaleCheckpoint,
            Optional<BooleanStreamCheckpoint> presentCheckpoint)
    {
        ImmutableList.Builder<Integer> positionList = ImmutableList.builder();
        presentCheckpoint.ifPresent(booleanStreamCheckpoint -> positionList.addAll(booleanStreamCheckpoint.toPositionList(compressed)));
        positionList.addAll(dataCheckpoint.toPositionList(compressed));
        positionList.addAll(scaleCheckpoint.toPositionList(compressed));
        return positionList.build();
    }

    @Override
    public List<Stream> writeDataStreams(SliceOutput outputStream)
            throws IOException
    {
        checkState(closed);

        ImmutableList.Builder<Stream> dataStreams = ImmutableList.builder();
        presentStream.writeDataStreams(column, outputStream).ifPresent(dataStreams::add);
        dataStream.writeDataStreams(column, outputStream).ifPresent(dataStreams::add);
        scaleStream.writeDataStreams(column, outputStream).ifPresent(dataStreams::add);
        return dataStreams.build();
    }

    @Override
    public long getBufferedBytes()
    {
        return dataStream.getBufferedBytes() + scaleStream.getBufferedBytes() + presentStream.getBufferedBytes();
    }

    @Override
    public long getRetainedBytes()
    {
        // NOTE: we do not include stats because they should be small and it would be annoying to calculate the size
        return INSTANCE_SIZE + dataStream.getRetainedBytes() + scaleStream.getRetainedBytes() + presentStream.getRetainedBytes();
    }

    @Override
    public void reset()
    {
        closed = false;
        dataStream.reset();
        scaleStream.reset();
        presentStream.reset();
        rowGroupColumnStatistics.clear();
        shortDecimalStatisticsBuilder = new ShortDecimalStatisticsBuilder(this.type.getScale());
        longDecimalStatisticsBuilder = new LongDecimalStatisticsBuilder();
    }
}
