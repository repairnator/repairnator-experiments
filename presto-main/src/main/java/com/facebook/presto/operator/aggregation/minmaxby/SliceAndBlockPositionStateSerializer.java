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
package com.facebook.presto.operator.aggregation.minmaxby;

import com.facebook.presto.spi.block.Block;
import com.facebook.presto.spi.block.BlockBuilder;
import com.facebook.presto.spi.type.Type;

class SliceAndBlockPositionStateSerializer
        extends KeyAndBlockPositionValueStateSerializer<SliceAndBlockPositionValueState>
{
    SliceAndBlockPositionStateSerializer(Type firstType, Type secondType)
    {
        super(firstType, secondType);
    }

    @Override
    void readFirstField(Block block, int position, SliceAndBlockPositionValueState state)
    {
        state.setFirst(firstType.getSlice(block, position));
    }

    @Override
    void writeFirstField(BlockBuilder out, SliceAndBlockPositionValueState state)
    {
        firstType.writeSlice(out, state.getFirst());
    }
}
