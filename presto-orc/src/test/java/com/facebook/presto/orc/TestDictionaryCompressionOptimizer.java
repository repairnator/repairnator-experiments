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
package com.facebook.presto.orc;

import com.facebook.presto.orc.DictionaryCompressionOptimizer.DictionaryColumn;
import com.google.common.collect.ImmutableSet;
import io.airlift.units.DataSize;
import io.airlift.units.DataSize.Unit;
import org.testng.annotations.Test;

import java.util.List;
import java.util.OptionalInt;
import java.util.Set;
import java.util.stream.Collectors;

import static com.facebook.presto.orc.DictionaryCompressionOptimizer.DICTIONARY_MEMORY_MAX_RANGE;
import static com.facebook.presto.orc.DictionaryCompressionOptimizer.estimateIndexBytesPerValue;
import static com.google.common.base.Preconditions.checkArgument;
import static io.airlift.testing.Assertions.assertGreaterThanOrEqual;
import static io.airlift.testing.Assertions.assertLessThan;
import static java.lang.Math.min;
import static java.lang.Math.toIntExact;
import static java.util.Objects.requireNonNull;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class TestDictionaryCompressionOptimizer
{
    @Test
    public void testNoDictionariesBytesLimit()
            throws Exception
    {
        int stripeMaxBytes = megabytes(100);
        int bytesPerRow = 1024;
        int expectedMaxRowCount = stripeMaxBytes / bytesPerRow;
        DataSimulator simulator = new DataSimulator(stripeMaxBytes, 10_000, expectedMaxRowCount * 2, megabytes(16), bytesPerRow);

        for (int loop = 0; loop < 3; loop++) {
            assertFalse(simulator.isDictionaryMemoryFull());
            assertEquals(simulator.getRowCount(), 0);
            assertEquals(simulator.getBufferedBytes(), 0);

            simulator.advanceToNextStateChange();

            // since there are no dictionary columns, the simulator should advance until the strip is full
            assertFalse(simulator.isDictionaryMemoryFull());
            assertGreaterThanOrEqual(simulator.getBufferedBytes(), stripeMaxBytes);
            assertGreaterThanOrEqual(simulator.getRowCount(), expectedMaxRowCount);

            simulator.finalOptimize();

            assertFalse(simulator.isDictionaryMemoryFull());
            assertGreaterThanOrEqual(simulator.getBufferedBytes(), stripeMaxBytes);
            assertGreaterThanOrEqual(simulator.getRowCount(), expectedMaxRowCount);

            simulator.reset();
        }
    }

    @Test
    public void testSingleDictionaryColumnRowLimit()
            throws Exception
    {
        int bytesPerEntry = 1024;
        TestDictionaryColumn column = dictionaryColumn(bytesPerEntry, 1024);

        // construct a simulator that will hit the row limit first
        int stripeMaxBytes = megabytes(1000);
        int expectedMaxRowCount = 1_000_000;
        DataSimulator simulator = new DataSimulator(stripeMaxBytes, 10_000, expectedMaxRowCount, megabytes(16), 0, column);

        for (int loop = 0; loop < 3; loop++) {
            assertFalse(simulator.isDictionaryMemoryFull());
            assertFalse(column.isDirect());
            assertEquals(simulator.getRowCount(), 0);
            assertEquals(simulator.getBufferedBytes(), 0);

            simulator.advanceToNextStateChange();

            // since there dictionary columns is only 1 MB, the simulator should advance until the strip is full
            assertFalse(simulator.isDictionaryMemoryFull());
            assertFalse(column.isDirect());
            assertLessThan(simulator.getBufferedBytes(), stripeMaxBytes);
            assertGreaterThanOrEqual(simulator.getRowCount(), expectedMaxRowCount);

            simulator.finalOptimize();

            assertFalse(simulator.isDictionaryMemoryFull());
            assertFalse(column.isDirect());
            assertLessThan(simulator.getBufferedBytes(), stripeMaxBytes);
            assertGreaterThanOrEqual(simulator.getRowCount(), expectedMaxRowCount);

            simulator.reset();
        }
    }

    @Test
    public void testSingleDictionaryColumnByteLimit()
            throws Exception
    {
        int bytesPerEntry = 1024;
        int dictionaryEntries = 1024;
        TestDictionaryColumn column = dictionaryColumn(bytesPerEntry, dictionaryEntries);

        // construct a simulator that will hit the row limit first
        int stripeMaxBytes = megabytes(100);
        int bytesPerRow = estimateIndexBytesPerValue(dictionaryEntries);
        int expectedMaxRowCount = stripeMaxBytes / bytesPerRow;
        DataSimulator simulator = new DataSimulator(stripeMaxBytes, 10_000, expectedMaxRowCount * 10, megabytes(16), 0, column);

        for (int loop = 0; loop < 3; loop++) {
            assertFalse(simulator.isDictionaryMemoryFull());
            assertFalse(column.isDirect());
            assertEquals(simulator.getRowCount(), 0);
            assertEquals(simulator.getBufferedBytes(), 0);

            simulator.advanceToNextStateChange();

            // since there dictionary columns is only 1 MB, the simulator should advance until the strip is full
            assertFalse(simulator.isDictionaryMemoryFull());
            assertFalse(column.isDirect());
            assertGreaterThanOrEqual(simulator.getBufferedBytes(), stripeMaxBytes);
            assertLessThan(simulator.getRowCount(), expectedMaxRowCount);

            simulator.finalOptimize();

            assertFalse(simulator.isDictionaryMemoryFull());
            assertFalse(column.isDirect());
            assertGreaterThanOrEqual(simulator.getBufferedBytes(), stripeMaxBytes);
            assertLessThan(simulator.getRowCount(), expectedMaxRowCount);

            simulator.reset();
        }
    }

    @Test
    public void testSingleDictionaryColumnMemoryLimit()
            throws Exception
    {
        int bytesPerEntry = 1024;
        int dictionaryMaxMemoryBytes = megabytes(32);
        double uniquePercentage = 0.5;
        TestDictionaryColumn column = directColumn(bytesPerEntry, uniquePercentage);

        // construct a simulator that will hit the dictionary (low) memory limit by estimating the number of rows at the memory limit, and then setting large limits around this value
        int stripeMaxBytes = megabytes(100);
        int dictionaryMaxMemoryBytesLow = dictionaryMaxMemoryBytes - (int) DICTIONARY_MEMORY_MAX_RANGE.toBytes();
        int expectedMaxRowCount = (int) (dictionaryMaxMemoryBytesLow / bytesPerEntry / uniquePercentage);
        DataSimulator simulator = new DataSimulator(stripeMaxBytes, expectedMaxRowCount / 2, expectedMaxRowCount * 2, dictionaryMaxMemoryBytes, 0, column);

        for (int loop = 0; loop < 3; loop++) {
            assertFalse(simulator.isDictionaryMemoryFull());
            assertFalse(column.isDirect());
            assertEquals(simulator.getRowCount(), 0);
            assertEquals(simulator.getBufferedBytes(), 0);

            simulator.advanceToNextStateChange();

            // the simulator should advance until memory is full
            assertTrue(simulator.isDictionaryMemoryFull());
            assertFalse(column.isDirect());
            assertLessThan(simulator.getBufferedBytes(), stripeMaxBytes);
            assertGreaterThanOrEqual(simulator.getRowCount(), expectedMaxRowCount);

            simulator.finalOptimize();

            assertTrue(simulator.isDictionaryMemoryFull());
            assertFalse(column.isDirect());
            assertLessThan(simulator.getBufferedBytes(), stripeMaxBytes);
            assertGreaterThanOrEqual(simulator.getRowCount(), expectedMaxRowCount);

            simulator.reset();
        }
    }

    @Test
    public void testSingleDictionaryColumnMemoryLimitHighlyCompressed()
            throws Exception
    {
        int bytesPerEntry = 1024 * 1024;
        int dictionaryMaxMemoryBytes = megabytes(8) + 100;
        double uniquePercentage = 0.2;
        TestDictionaryColumn column = dictionaryColumn(bytesPerEntry, 16, uniquePercentage);

        // construct a simulator that will hit the dictionary memory before hitting the minimum row limit
        int stripeMaxBytes = megabytes(100);
        int expectedMaxRowCount = (int) (dictionaryMaxMemoryBytes / bytesPerEntry / uniquePercentage);
        DataSimulator simulator = new DataSimulator(stripeMaxBytes, Integer.MAX_VALUE, Integer.MAX_VALUE, dictionaryMaxMemoryBytes, 0, column);

        for (int loop = 0; loop < 3; loop++) {
            assertFalse(simulator.isDictionaryMemoryFull());
            assertFalse(column.isDirect());
            assertEquals(simulator.getRowCount(), 0);
            assertEquals(simulator.getBufferedBytes(), 0);

            simulator.advanceToNextStateChange();

            // the simulator should advance until memory is full
            assertTrue(simulator.isDictionaryMemoryFull());
            assertFalse(column.isDirect());
            assertLessThan(simulator.getBufferedBytes(), stripeMaxBytes);
            assertGreaterThanOrEqual(simulator.getRowCount(), expectedMaxRowCount);

            simulator.finalOptimize();

            assertTrue(simulator.isDictionaryMemoryFull());
            assertFalse(column.isDirect());
            assertLessThan(simulator.getBufferedBytes(), stripeMaxBytes);
            assertGreaterThanOrEqual(simulator.getRowCount(), expectedMaxRowCount);

            simulator.reset();
        }
    }

    @Test
    public void testSingleDirectBytesLimit()
            throws Exception
    {
        int bytesPerEntry = 1024;
        int dictionaryMaxMemoryBytes = megabytes(16);
        TestDictionaryColumn column = directColumn(bytesPerEntry, 1.0);

        // construct a simulator that will flip the column to direct and then hit the bytes limit
        int stripeMaxBytes = megabytes(100);
        int expectedRowCountAtFlip = (int) ((dictionaryMaxMemoryBytes - DICTIONARY_MEMORY_MAX_RANGE.toBytes()) / bytesPerEntry);
        int expectedMaxRowCountAtFull = stripeMaxBytes / bytesPerEntry;
        DataSimulator simulator = new DataSimulator(stripeMaxBytes, 1, expectedMaxRowCountAtFull, dictionaryMaxMemoryBytes, 0, column);

        for (int loop = 0; loop < 3; loop++) {
            assertFalse(simulator.isDictionaryMemoryFull());
            assertFalse(column.isDirect());
            assertEquals(simulator.getRowCount(), 0);
            assertEquals(simulator.getBufferedBytes(), 0);

            simulator.advanceToNextStateChange();

            // the simulator should advance until the dictionary column is flipped
            assertFalse(simulator.isDictionaryMemoryFull());
            assertTrue(column.isDirect());
            assertLessThan(simulator.getBufferedBytes(), stripeMaxBytes);
            assertGreaterThanOrEqual(simulator.getRowCount(), expectedRowCountAtFlip);

            simulator.advanceToNextStateChange();

            // the simulator should advance until the stripe is full
            assertFalse(simulator.isDictionaryMemoryFull());
            assertTrue(column.isDirect());
            assertGreaterThanOrEqual(simulator.getBufferedBytes(), stripeMaxBytes);
            assertGreaterThanOrEqual(simulator.getRowCount(), expectedMaxRowCountAtFull);

            simulator.finalOptimize();

            assertFalse(simulator.isDictionaryMemoryFull());
            assertTrue(column.isDirect());
            assertGreaterThanOrEqual(simulator.getBufferedBytes(), stripeMaxBytes);
            assertGreaterThanOrEqual(simulator.getRowCount(), expectedMaxRowCountAtFull);

            simulator.reset();
        }
    }

    @Test
    public void testDictionaryAndDirectBytesLimit()
            throws Exception
    {
        int bytesPerEntry = 1024;
        int dictionaryMaxMemoryBytes = megabytes(8);
        double directUniquePercentage = 0.75;
        TestDictionaryColumn directColumn = directColumn(bytesPerEntry, directUniquePercentage);
        TestDictionaryColumn dictionaryColumn = dictionaryColumn(bytesPerEntry, 1024);

        // construct a simulator that will flip the column to direct and then hit the bytes limit
        int stripeMaxBytes = megabytes(100);
        int expectedRowCountAtFlip = (int) (dictionaryMaxMemoryBytes / (bytesPerEntry * 2 * directUniquePercentage));
        int expectedMaxRowCountAtFull = stripeMaxBytes / (bytesPerEntry * 2);
        DataSimulator simulator = new DataSimulator(stripeMaxBytes, expectedMaxRowCountAtFull, expectedMaxRowCountAtFull * 2, dictionaryMaxMemoryBytes, 0, directColumn, dictionaryColumn);

        for (int loop = 0; loop < 3; loop++) {
            assertFalse(simulator.isDictionaryMemoryFull());
            assertFalse(directColumn.isDirect());
            assertFalse(dictionaryColumn.isDirect());
            assertEquals(simulator.getRowCount(), 0);
            assertEquals(simulator.getBufferedBytes(), 0);

            simulator.advanceToNextStateChange();

            // the simulator should advance until the dictionary column is flipped
            assertFalse(simulator.isDictionaryMemoryFull());
            assertTrue(directColumn.isDirect());
            assertFalse(dictionaryColumn.isDirect());
            assertLessThan(simulator.getBufferedBytes(), stripeMaxBytes);
            assertGreaterThanOrEqual(simulator.getRowCount(), expectedRowCountAtFlip);

            simulator.advanceToNextStateChange();

            // the simulator should advance until the stripe is full
            assertFalse(simulator.isDictionaryMemoryFull());
            assertTrue(directColumn.isDirect());
            assertFalse(dictionaryColumn.isDirect());
            assertGreaterThanOrEqual(simulator.getBufferedBytes(), stripeMaxBytes);
            assertGreaterThanOrEqual(simulator.getRowCount(), expectedMaxRowCountAtFull);

            simulator.finalOptimize();

            assertFalse(simulator.isDictionaryMemoryFull());
            assertTrue(directColumn.isDirect());
            assertFalse(dictionaryColumn.isDirect());
            assertGreaterThanOrEqual(simulator.getBufferedBytes(), stripeMaxBytes);
            assertGreaterThanOrEqual(simulator.getRowCount(), expectedMaxRowCountAtFull);

            simulator.reset();
        }
    }

    @Test
    public void testWideDictionaryAndNarrowDirectBytesLimit()
            throws Exception
    {
        int dictionaryMaxMemoryBytes = megabytes(32);
        int directBytesPerEntry = 100;
        double directUniquePercentage = 0.75;
        TestDictionaryColumn directColumn = directColumn(directBytesPerEntry, directUniquePercentage);

        int dictionaryBytesPerEntry = 1024 * 1024;
        int dictionaryEntries = 10;
        TestDictionaryColumn dictionaryColumn = dictionaryColumn(dictionaryBytesPerEntry, dictionaryEntries);

        // construct a simulator that will flip the column to direct and then hit the bytes limit
        int stripeMaxBytes = megabytes(2000);
        int dictionaryMaxMemoryBytesLow = (int) (dictionaryMaxMemoryBytes - DICTIONARY_MEMORY_MAX_RANGE.toBytes());
        int expectedRowCountAtFlip = (int) ((dictionaryMaxMemoryBytesLow - (dictionaryEntries * dictionaryBytesPerEntry)) / (directBytesPerEntry * directUniquePercentage));
        int maxRowCount = 10_000_000;
        DataSimulator simulator = new DataSimulator(stripeMaxBytes, 1, maxRowCount, dictionaryMaxMemoryBytes, 0, directColumn, dictionaryColumn);

        for (int loop = 0; loop < 3; loop++) {
            assertFalse(simulator.isDictionaryMemoryFull());
            assertFalse(directColumn.isDirect());
            assertFalse(dictionaryColumn.isDirect());
            assertEquals(simulator.getRowCount(), 0);
            assertEquals(simulator.getBufferedBytes(), 0);

            simulator.advanceToNextStateChange();

            // the simulator should advance until the dictionary column is flipped
            assertFalse(simulator.isDictionaryMemoryFull());
            assertTrue(directColumn.isDirect());
            assertFalse(dictionaryColumn.isDirect());
            assertLessThan(simulator.getBufferedBytes(), stripeMaxBytes);
            assertGreaterThanOrEqual(simulator.getRowCount(), expectedRowCountAtFlip);

            simulator.advanceToNextStateChange();

            // the simulator should advance until the stripe is full
            assertFalse(simulator.isDictionaryMemoryFull());
            assertTrue(directColumn.isDirect());
            assertFalse(dictionaryColumn.isDirect());
            assertLessThan(simulator.getBufferedBytes(), stripeMaxBytes);
            assertGreaterThanOrEqual(simulator.getRowCount(), maxRowCount);

            simulator.finalOptimize();

            assertFalse(simulator.isDictionaryMemoryFull());
            assertTrue(directColumn.isDirect());
            assertFalse(dictionaryColumn.isDirect());
            assertLessThan(simulator.getBufferedBytes(), stripeMaxBytes);
            assertGreaterThanOrEqual(simulator.getRowCount(), maxRowCount);

            simulator.reset();
        }
    }

    private static int megabytes(int size)
    {
        return toIntExact(new DataSize(size, Unit.MEGABYTE).toBytes());
    }

    private static class DataSimulator
    {
        private final int stripeMaxBytes;
        private final int stripeMaxRowCount;

        private final int otherColumnsBytesPerRow;
        private final Set<TestDictionaryColumn> dictionaryColumns;

        private final DictionaryCompressionOptimizer optimizer;

        private int rowCount;

        public DataSimulator(
                int stripeMaxBytes,
                int stripeMinRowCount,
                int stripeMaxRowCount,
                int dictionaryMemoryMaxBytes,
                int otherColumnsBytesPerRow,
                TestDictionaryColumn... dictionaryColumns)
        {
            this.stripeMaxBytes = stripeMaxBytes;
            this.stripeMaxRowCount = stripeMaxRowCount;
            this.otherColumnsBytesPerRow = otherColumnsBytesPerRow;
            this.dictionaryColumns = ImmutableSet.copyOf(dictionaryColumns);

            this.optimizer = new DictionaryCompressionOptimizer(this.dictionaryColumns, stripeMaxBytes, stripeMinRowCount, stripeMaxRowCount, dictionaryMemoryMaxBytes);
        }

        public void advanceToNextStateChange()
        {
            List<Boolean> directColumnFlags = getDirectColumnFlags();
            while (!optimizer.isFull() && getBufferedBytes() < stripeMaxBytes && getRowCount() < stripeMaxRowCount && directColumnFlags.equals(getDirectColumnFlags())) {
                rowCount += 1024;
                for (TestDictionaryColumn dictionaryColumn : dictionaryColumns) {
                    dictionaryColumn.advanceTo(rowCount);
                }
                optimizer.optimize(getBufferedBytes(), getRowCount());
            }
        }

        public boolean isDictionaryMemoryFull()
        {
            return optimizer.isFull();
        }

        public void finalOptimize()
        {
            optimizer.finalOptimize();
        }

        private List<Boolean> getDirectColumnFlags()
        {
            return dictionaryColumns.stream()
                    .map(TestDictionaryColumn::isDirect)
                    .collect(Collectors.toList());
        }

        public void reset()
        {
            rowCount = 0;
            optimizer.reset();
            for (TestDictionaryColumn dictionaryColumn : dictionaryColumns) {
                dictionaryColumn.reset();
            }
        }

        public int getBufferedBytes()
        {
            return (rowCount * otherColumnsBytesPerRow) +
                    dictionaryColumns.stream()
                            .mapToInt(TestDictionaryColumn::getBufferedBytes)
                            .sum();
        }

        public int getRowCount()
        {
            return rowCount;
        }
    }

    private static TestDictionaryColumn directColumn(int bytesPerEntry, double uniquePercentage)
    {
        return new TestDictionaryColumn(bytesPerEntry, uniquePercentage, OptionalInt.empty(), 1, 0);
    }

    private static TestDictionaryColumn dictionaryColumn(int bytesPerEntry, int maxDictionaryEntries)
    {
        return dictionaryColumn(bytesPerEntry, maxDictionaryEntries, 0.5);
    }

    private static TestDictionaryColumn dictionaryColumn(int bytesPerEntry, int maxDictionaryEntries, double uniquePercentage)
    {
        return new TestDictionaryColumn(bytesPerEntry, uniquePercentage, OptionalInt.of(maxDictionaryEntries), 1, 0);
    }

    private static class TestDictionaryColumn
            implements DictionaryColumn
    {
        private final int bytesPerEntry;

        private final double uniquePercentage;
        private final OptionalInt maxDictionaryEntries;

        private final double valuesPerRow;
        private final double nullRate;

        private int rowCount;
        private boolean direct;

        private TestDictionaryColumn(int bytesPerEntry, double uniquePercentage, OptionalInt maxDictionaryEntries, double valuesPerRow, double nullRate)
        {
            checkArgument(bytesPerEntry >= 0, "bytesPerEntry is negative");
            this.bytesPerEntry = bytesPerEntry;

            checkArgument(uniquePercentage >= 0 && uniquePercentage <= 1, "bytesPerEntry must be between 0 and 1");
            this.uniquePercentage = uniquePercentage;

            this.maxDictionaryEntries = requireNonNull(maxDictionaryEntries, "maxDictionaryEntries is null");
            maxDictionaryEntries.ifPresent(value -> checkArgument(value >= 0, "maxDictionaryEntries is negative"));

            checkArgument(valuesPerRow >= 0, "valuesPerRow is negative");
            this.valuesPerRow = valuesPerRow;

            checkArgument(nullRate >= 0 && nullRate <= 1, "nullRate must be between 0 and 1");
            this.nullRate = nullRate;
        }

        public void reset()
        {
            rowCount = 0;
            direct = false;
        }

        public void advanceTo(int rowCount)
        {
            assertTrue(rowCount >= this.rowCount);
            this.rowCount = rowCount;
        }

        public int getBufferedBytes()
        {
            if (direct) {
                return (int) (rowCount * valuesPerRow * bytesPerEntry);
            }
            int dictionaryEntries = getDictionaryEntries();
            int bytesPerValue = estimateIndexBytesPerValue(dictionaryEntries);
            return (dictionaryEntries * bytesPerEntry) + (getNonNullValueCount() * bytesPerValue);
        }

        @Override
        public int getValueCount()
        {
            return (int) (rowCount * valuesPerRow);
        }

        @Override
        public int getNonNullValueCount()
        {
            return (int) (getValueCount() * (1 - nullRate));
        }

        @Override
        public long getRawBytes()
        {
            return (long) bytesPerEntry * getNonNullValueCount();
        }

        @Override
        public int getDictionaryEntries()
        {
            int dictionaryEntries = (int) (getNonNullValueCount() * uniquePercentage);
            return min(dictionaryEntries, maxDictionaryEntries.orElse(dictionaryEntries));
        }

        @Override
        public int getDictionaryBytes()
        {
            return getDictionaryEntries() * bytesPerEntry;
        }

        @Override
        public void convertToDirect()
        {
            assertFalse(direct);
            direct = true;
        }

        public boolean isDirect()
        {
            return direct;
        }
    }
}
