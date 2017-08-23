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
package com.facebook.presto.operator;

import com.facebook.presto.SequencePageBuilder;
import com.facebook.presto.block.BlockAssertions;
import com.facebook.presto.connector.ConnectorId;
import com.facebook.presto.metadata.Metadata;
import com.facebook.presto.metadata.MetadataManager;
import com.facebook.presto.metadata.Signature;
import com.facebook.presto.metadata.Split;
import com.facebook.presto.operator.index.PageRecordSet;
import com.facebook.presto.operator.project.CursorProcessor;
import com.facebook.presto.operator.project.PageProcessor;
import com.facebook.presto.operator.project.TestPageProcessor.LazyPagePageProjection;
import com.facebook.presto.operator.project.TestPageProcessor.SelectAllFilter;
import com.facebook.presto.operator.scalar.AbstractTestFunctions;
import com.facebook.presto.spi.ConnectorPageSource;
import com.facebook.presto.spi.FixedPageSource;
import com.facebook.presto.spi.Page;
import com.facebook.presto.spi.RecordPageSource;
import com.facebook.presto.spi.block.Block;
import com.facebook.presto.spi.block.LazyBlock;
import com.facebook.presto.sql.gen.ExpressionCompiler;
import com.facebook.presto.sql.gen.PageFunctionCompiler;
import com.facebook.presto.sql.planner.plan.PlanNodeId;
import com.facebook.presto.sql.relational.RowExpression;
import com.facebook.presto.testing.MaterializedResult;
import com.facebook.presto.testing.TestingSplit;
import com.facebook.presto.testing.TestingTransactionHandle;
import com.google.common.collect.ImmutableList;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Supplier;

import static com.facebook.presto.SessionTestUtils.TEST_SESSION;
import static com.facebook.presto.block.BlockAssertions.toValues;
import static com.facebook.presto.metadata.MetadataManager.createTestMetadataManager;
import static com.facebook.presto.operator.OperatorAssertion.toMaterializedResult;
import static com.facebook.presto.spi.type.BigintType.BIGINT;
import static com.facebook.presto.spi.type.VarcharType.VARCHAR;
import static com.facebook.presto.sql.relational.Expressions.call;
import static com.facebook.presto.sql.relational.Expressions.field;
import static com.facebook.presto.testing.TestingTaskContext.createTaskContext;
import static com.facebook.presto.testing.assertions.Assert.assertEquals;
import static io.airlift.concurrent.Threads.daemonThreadsNamed;
import static java.util.concurrent.Executors.newCachedThreadPool;
import static java.util.concurrent.Executors.newScheduledThreadPool;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

public class TestScanFilterAndProjectOperator
        extends AbstractTestFunctions
{
    private final MetadataManager metadata = createTestMetadataManager();
    private final ExpressionCompiler expressionCompiler = new ExpressionCompiler(metadata, new PageFunctionCompiler(metadata, 0));
    private ExecutorService executor;
    private ScheduledExecutorService scheduledExecutor;

    public TestScanFilterAndProjectOperator()
    {
        executor = newCachedThreadPool(daemonThreadsNamed("test-executor-%s"));
        scheduledExecutor = newScheduledThreadPool(2, daemonThreadsNamed("test-scheduledExecutor-%s"));
    }

    @Test
    public void testPageSource()
            throws Exception
    {
        final Page input = SequencePageBuilder.createSequencePage(ImmutableList.of(VARCHAR), 10_000, 0);
        DriverContext driverContext = newDriverContext();

        List<RowExpression> projections = ImmutableList.of(field(0, VARCHAR));
        Supplier<CursorProcessor> cursorProcessor = expressionCompiler.compileCursorProcessor(Optional.empty(), projections, "key");
        Supplier<PageProcessor> pageProcessor = expressionCompiler.compilePageProcessor(Optional.empty(), projections);

        ScanFilterAndProjectOperator.ScanFilterAndProjectOperatorFactory factory = new ScanFilterAndProjectOperator.ScanFilterAndProjectOperatorFactory(
                0,
                new PlanNodeId("test"),
                new PlanNodeId("0"),
                (session, split, columns) -> new FixedPageSource(ImmutableList.of(input)),
                cursorProcessor,
                pageProcessor,
                ImmutableList.of(),
                ImmutableList.of(VARCHAR));

        SourceOperator operator = factory.createOperator(driverContext);
        operator.addSplit(new Split(new ConnectorId("test"), TestingTransactionHandle.create(), TestingSplit.createLocalSplit()));
        operator.noMoreSplits();

        MaterializedResult expected = toMaterializedResult(driverContext.getSession(), ImmutableList.of(VARCHAR), ImmutableList.of(input));
        MaterializedResult actual = toMaterializedResult(driverContext.getSession(), ImmutableList.of(VARCHAR), toPages(operator));

        assertEquals(actual.getRowCount(), expected.getRowCount());
        assertEquals(actual, expected);
    }

    @Test
    public void testPageSourceLazyLoad()
            throws Exception
    {
        Block inputBlock = BlockAssertions.createLongSequenceBlock(0, 100);
        // If column 1 is loaded, test will fail
        Page input = new Page(100, inputBlock, new LazyBlock(100, lazyBlock -> {
            throw new AssertionError("Lazy block should not be loaded");
        }));
        DriverContext driverContext = newDriverContext();

        List<RowExpression> projections = ImmutableList.of(field(0, VARCHAR));
        Supplier<CursorProcessor> cursorProcessor = expressionCompiler.compileCursorProcessor(Optional.empty(), projections, "key");
        PageProcessor pageProcessor = new PageProcessor(Optional.of(new SelectAllFilter()), ImmutableList.of(new LazyPagePageProjection()));

        ScanFilterAndProjectOperator.ScanFilterAndProjectOperatorFactory factory = new ScanFilterAndProjectOperator.ScanFilterAndProjectOperatorFactory(
                0,
                new PlanNodeId("test"),
                new PlanNodeId("0"),
                (session, split, columns) -> new SinglePagePageSource(input),
                cursorProcessor,
                () -> pageProcessor,
                ImmutableList.of(),
                ImmutableList.of(BIGINT));

        SourceOperator operator = factory.createOperator(driverContext);
        operator.addSplit(new Split(new ConnectorId("test"), TestingTransactionHandle.create(), TestingSplit.createLocalSplit()));
        operator.noMoreSplits();

        MaterializedResult expected = toMaterializedResult(driverContext.getSession(), ImmutableList.of(BIGINT), ImmutableList.of(new Page(inputBlock)));
        MaterializedResult actual = toMaterializedResult(driverContext.getSession(), ImmutableList.of(BIGINT), toPages(operator));

        assertEquals(actual.getRowCount(), expected.getRowCount());
        assertEquals(actual, expected);
    }

    @Test
    public void testRecordCursorSource()
            throws Exception
    {
        final Page input = SequencePageBuilder.createSequencePage(ImmutableList.of(VARCHAR), 10_000, 0);
        DriverContext driverContext = newDriverContext();

        List<RowExpression> projections = ImmutableList.of(field(0, VARCHAR));
        Supplier<CursorProcessor> cursorProcessor = expressionCompiler.compileCursorProcessor(Optional.empty(), projections, "key");
        Supplier<PageProcessor> pageProcessor = expressionCompiler.compilePageProcessor(Optional.empty(), projections);

        ScanFilterAndProjectOperator.ScanFilterAndProjectOperatorFactory factory = new ScanFilterAndProjectOperator.ScanFilterAndProjectOperatorFactory(
                0,
                new PlanNodeId("test"),
                new PlanNodeId("0"),
                (session, split, columns) -> new RecordPageSource(new PageRecordSet(ImmutableList.of(VARCHAR), input)),
                cursorProcessor,
                pageProcessor,
                ImmutableList.of(),
                ImmutableList.of(VARCHAR));

        SourceOperator operator = factory.createOperator(driverContext);
        operator.addSplit(new Split(new ConnectorId("test"), TestingTransactionHandle.create(), TestingSplit.createLocalSplit()));
        operator.noMoreSplits();

        MaterializedResult expected = toMaterializedResult(driverContext.getSession(), ImmutableList.of(VARCHAR), ImmutableList.of(input));
        MaterializedResult actual = toMaterializedResult(driverContext.getSession(), ImmutableList.of(VARCHAR), toPages(operator));

        assertEquals(actual.getRowCount(), expected.getRowCount());
        assertEquals(actual, expected);
    }

    @Test
    public void testRecordCursorYield()
            throws Exception
    {
        // create a generic long function that yields for projection on every row
        // verify we will yield #row times totally

        // create a table with 15 rows
        int length = 15;
        Page input = SequencePageBuilder.createSequencePage(ImmutableList.of(BIGINT), length, 0);
        DriverContext driverContext = newDriverContext();

        // set up generic long function with a callback to force yield
        Metadata metadata = functionAssertions.getMetadata();
        metadata.getFunctionRegistry().addFunctions(ImmutableList.of(new GenericLongFunction("record_cursor", value -> {
            driverContext.getYieldSignal().forceYieldForTesting();
            return value;
        })));
        ExpressionCompiler expressionCompiler = new ExpressionCompiler(metadata, new PageFunctionCompiler(metadata, 0));

        List<RowExpression> projections = ImmutableList.of(call(
                Signature.internalScalarFunction("generic_long_record_cursor", BIGINT.getTypeSignature(), ImmutableList.of(BIGINT.getTypeSignature())),
                BIGINT,
                field(0, BIGINT)));
        Supplier<CursorProcessor> cursorProcessor = expressionCompiler.compileCursorProcessor(Optional.empty(), projections, "key");
        Supplier<PageProcessor> pageProcessor = expressionCompiler.compilePageProcessor(Optional.empty(), projections);

        ScanFilterAndProjectOperator.ScanFilterAndProjectOperatorFactory factory = new ScanFilterAndProjectOperator.ScanFilterAndProjectOperatorFactory(
                0,
                new PlanNodeId("test"),
                new PlanNodeId("0"),
                (session, split, columns) -> new RecordPageSource(new PageRecordSet(ImmutableList.of(BIGINT), input)),
                cursorProcessor,
                pageProcessor,
                ImmutableList.of(),
                ImmutableList.of(BIGINT));

        SourceOperator operator = factory.createOperator(driverContext);
        operator.addSplit(new Split(new ConnectorId("test"), TestingTransactionHandle.create(), TestingSplit.createLocalSplit()));
        operator.noMoreSplits();

        // start driver; get null value due to yield for the first 15 times
        for (int i = 0; i < length; i++) {
            driverContext.getYieldSignal().setWithDelay(5 * SECONDS.toNanos(1), driverContext.getYieldExecutor());
            assertNull(operator.getOutput());
            driverContext.getYieldSignal().reset();
        }

        // the 16th yield is not going to prevent the operator from producing a page
        driverContext.getYieldSignal().setWithDelay(5 * SECONDS.toNanos(1), driverContext.getYieldExecutor());
        Page output = operator.getOutput();
        driverContext.getYieldSignal().reset();
        assertNotNull(output);
        assertEquals(toValues(BIGINT, output.getBlock(0)), toValues(BIGINT, input.getBlock(0)));
    }

    private static List<Page> toPages(Operator operator)
    {
        ImmutableList.Builder<Page> outputPages = ImmutableList.builder();

        // read output until input is needed or operator is finished
        int nullPages = 0;
        while (!operator.isFinished()) {
            Page outputPage = operator.getOutput();
            if (outputPage == null) {
                // break infinite loop due to null pages
                assertTrue(nullPages < 1_000_000, "Too many null pages; infinite loop?");
                nullPages++;
            }
            else {
                outputPages.add(outputPage);
                nullPages = 0;
            }
        }

        return outputPages.build();
    }

    private DriverContext newDriverContext()
    {
        return createTaskContext(executor, scheduledExecutor, TEST_SESSION)
                .addPipelineContext(0, true, true)
                .addDriverContext();
    }

    public class SinglePagePageSource
            implements ConnectorPageSource
    {
        private Page page;

        public SinglePagePageSource(Page page)
        {
            this.page = page;
        }

        @Override
        public void close()
        {
            page = null;
        }

        @Override
        public long getTotalBytes()
        {
            return 0;
        }

        @Override
        public long getCompletedBytes()
        {
            return 0;
        }

        @Override
        public long getReadTimeNanos()
        {
            return 0;
        }

        @Override
        public long getSystemMemoryUsage()
        {
            return 0;
        }

        @Override
        public boolean isFinished()
        {
            return page == null;
        }

        @Override
        public Page getNextPage()
        {
            Page page = this.page;
            this.page = null;
            return page;
        }
    }
}
