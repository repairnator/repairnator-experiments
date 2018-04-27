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
package com.facebook.presto.util;

import com.facebook.presto.memory.context.AggregatedMemoryContext;
import com.facebook.presto.memory.context.LocalMemoryContext;
import com.facebook.presto.operator.DriverYieldSignal;
import com.facebook.presto.operator.PageWithPositionComparator;
import com.facebook.presto.operator.WorkProcessor;
import com.facebook.presto.operator.WorkProcessor.ProcessorState;
import com.facebook.presto.spi.Page;
import com.facebook.presto.spi.PageBuilder;
import com.facebook.presto.spi.block.Block;
import com.facebook.presto.spi.type.Type;

import java.util.Comparator;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.stream.IntStream;

import static com.facebook.presto.operator.WorkProcessor.mergeSorted;
import static com.google.common.collect.ImmutableList.toImmutableList;
import static java.util.Objects.requireNonNull;

public final class MergeSortedPages
{
    private MergeSortedPages() {}

    public static WorkProcessor<Page> mergeSortedPages(
            List<WorkProcessor<Page>> pageProducers,
            PageWithPositionComparator comparator,
            List<Type> outputTypes,
            AggregatedMemoryContext aggregatedMemoryContext,
            DriverYieldSignal yieldSignal)
    {
        return mergeSortedPages(
                pageProducers,
                comparator,
                IntStream.range(0, outputTypes.size()).boxed().collect(toImmutableList()),
                outputTypes,
                (pageBuilder, pageWithPosition) -> pageBuilder.isFull(),
                false,
                aggregatedMemoryContext,
                yieldSignal);
    }

    public static WorkProcessor<Page> mergeSortedPages(
            List<WorkProcessor<Page>> pageProducers,
            PageWithPositionComparator comparator,
            List<Integer> outputChannels,
            List<Type> outputTypes,
            BiPredicate<PageBuilder, PageWithPosition> pageBreakPredicate,
            boolean updateMemoryAfterEveryPosition,
            AggregatedMemoryContext aggregatedMemoryContext,
            DriverYieldSignal yieldSignal)
    {
        List<WorkProcessor<PageWithPosition>> pageWithPositionProducers = pageProducers.stream()
                .map(pageProducer -> pageWithPositions(pageProducer, aggregatedMemoryContext.newLocalMemoryContext()))
                .collect(toImmutableList());

        Comparator<PageWithPosition> pageWithPositionComparator = (firstPageWithPosition, secondPageWithPosition) -> comparator.compareTo(
                firstPageWithPosition.getPage(), firstPageWithPosition.getPosition(),
                secondPageWithPosition.getPage(), secondPageWithPosition.getPosition());

        return buildPage(
                mergeSorted(pageWithPositionProducers, pageWithPositionComparator),
                outputChannels,
                outputTypes,
                pageBreakPredicate,
                updateMemoryAfterEveryPosition,
                aggregatedMemoryContext.newLocalMemoryContext(),
                yieldSignal);
    }

    private static WorkProcessor<Page> buildPage(
            WorkProcessor<PageWithPosition> pageWithPositions,
            List<Integer> outputChannels,
            List<Type> outputTypes,
            BiPredicate<PageBuilder, PageWithPosition> pageBreakPredicate,
            boolean updateMemoryAfterEveryPosition,
            LocalMemoryContext memoryContext,
            DriverYieldSignal yieldSignal)
    {
        PageBuilder pageBuilder = new PageBuilder(outputTypes);
        return pageWithPositions.transform(pageWithPositionOptional -> {
            if (yieldSignal.isSet()) {
                return ProcessorState.yield();
            }

            boolean finished = !pageWithPositionOptional.isPresent();
            if (finished && pageBuilder.isEmpty()) {
                return ProcessorState.finished();
            }

            if (finished || pageBreakPredicate.test(pageBuilder, pageWithPositionOptional.get())) {
                if (!updateMemoryAfterEveryPosition) {
                    // update memory usage just before producing page to cap from top
                    memoryContext.setBytes(pageBuilder.getRetainedSizeInBytes());
                }

                Page page = pageBuilder.build();
                pageBuilder.reset();
                if (!finished) {
                    pageWithPositionOptional.get().appendTo(pageBuilder, outputChannels, outputTypes);
                }

                if (updateMemoryAfterEveryPosition) {
                    memoryContext.setBytes(pageBuilder.getRetainedSizeInBytes());
                }

                return ProcessorState.ofResult(page, !finished);
            }

            pageWithPositionOptional.get().appendTo(pageBuilder, outputChannels, outputTypes);

            if (updateMemoryAfterEveryPosition) {
                memoryContext.setBytes(pageBuilder.getRetainedSizeInBytes());
            }

            return ProcessorState.needsMoreData();
        });
    }

    private static WorkProcessor<PageWithPosition> pageWithPositions(WorkProcessor<Page> pages, LocalMemoryContext memoryContext)
    {
        return pages.flatMap(page -> {
            memoryContext.setBytes(page.getRetainedSizeInBytes());
            return WorkProcessor.create(new WorkProcessor.Process<PageWithPosition>()
            {
                int position;

                @Override
                public ProcessorState<PageWithPosition> process()
                {
                    if (position >= page.getPositionCount()) {
                        memoryContext.setBytes(0);
                        return ProcessorState.finished();
                    }

                    return ProcessorState.ofResult(new PageWithPosition(page, position++));
                }
            });
        });
    }

    public static class PageWithPosition
    {
        private final Page page;
        private final int position;

        private PageWithPosition(Page page, int position)
        {
            this.page = requireNonNull(page, "page is null");
            this.position = position;
        }

        public Page getPage()
        {
            return page;
        }

        public int getPosition()
        {
            return position;
        }

        public void appendTo(PageBuilder pageBuilder, List<Integer> outputChannels, List<Type> outputTypes)
        {
            pageBuilder.declarePosition();
            for (int i = 0; i < outputChannels.size(); i++) {
                Type type = outputTypes.get(i);
                Block block = page.getBlock(outputChannels.get(i));
                type.appendTo(block, position, pageBuilder.getBlockBuilder(i));
            }
        }
    }
}
