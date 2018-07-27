/*
 * MIT License
 *
 * Copyright 2018 Sabre GLBL Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package com.sabre.oss.yare.common.mapper;

import org.junit.jupiter.api.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;


class MapperUtilsTest {
    private final Mapper<String, Integer> mapper = Integer::valueOf;

    @Test
    void shouldMapNullCollectionToNull() {
        List<Integer> result = MapperUtils.mapCollection(mapper, null);

        assertThat(result).isNull();
    }

    @Test
    void shouldMapListOfElement() {
        List<String> input = asList("1", "2");

        List<Integer> result = MapperUtils.mapCollection(mapper, input);

        assertThat(result).containsExactly(1, 2);
    }

    @Test
    void shouldReturnNullWhenNullSafeAndInputIsNull() {
        Mapper<String, Integer> nullSafeMapper = MapperUtils.nullSafe(mapper);

        assertThat(nullSafeMapper.map(null)).isEqualTo(null);
    }

    @Test
    void shouldReturnMappedInputWhenNullSafeAndInputIsNotNull() {
        Mapper<String, Integer> nullSafeMapper = MapperUtils.nullSafe(mapper);

        assertThat(nullSafeMapper.map("1")).isEqualTo(1);
    }
}
