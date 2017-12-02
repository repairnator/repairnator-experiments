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
package com.facebook.presto.orc.metadata.statistics;

import com.google.common.primitives.Longs;
import org.apache.hive.common.util.BloomFilter;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class HiveBloomFilter
        extends BloomFilter
{
    // constructor that allows deserialization of a long list into the actual hive bloom filter
    public HiveBloomFilter(List<Long> bits, int numBits, int numHashFunctions)
    {
        this.bitSet = new BitSet(Longs.toArray(bits));
        this.numBits = numBits;
        this.numHashFunctions = numHashFunctions;
    }

    public HiveBloomFilter(BloomFilter bloomFilter)
    {
        this.bitSet = new BitSet(bloomFilter.getBitSet().clone());
        this.numBits = bloomFilter.getBitSize();
        this.numHashFunctions = bloomFilter.getNumHashFunctions();
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HiveBloomFilter that = (HiveBloomFilter) o;
        return Objects.equals(numBits, that.numBits) &&
                Objects.equals(numHashFunctions, that.numHashFunctions) &&
                Arrays.equals(bitSet.getData(), that.bitSet.getData());
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(numBits, numHashFunctions, bitSet.getData());
    }
}
