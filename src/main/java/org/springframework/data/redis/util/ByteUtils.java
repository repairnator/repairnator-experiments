/*
 * Copyright 2015-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.data.redis.util;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

/**
 * Some handy methods for dealing with {@code byte} arrays.
 *
 * @author Christoph Strobl
 * @author Mark Paluch
 * @since 1.7
 */
public final class ByteUtils {

	private ByteUtils() {}

	/**
	 * Concatenate the given {@code byte} arrays into one, with overlapping array elements included twice.
	 * <p />
	 * The order of elements in the original arrays is preserved.
	 *
	 * @param array1 the first array.
	 * @param array2 the second array.
	 * @return the new array.
	 */
	public static byte[] concat(byte[] array1, byte[] array2) {

		byte[] result = Arrays.copyOf(array1, array1.length + array2.length);
		System.arraycopy(array2, 0, result, array1.length, array2.length);

		return result;
	}

	/**
	 * Concatenate the given {@code byte} arrays into one, with overlapping array elements included twice. Returns a new,
	 * empty array if {@code arrays} was empty and returns the first array if {@code arrays} contains only a single array.
	 * <p />
	 * The order of elements in the original arrays is preserved.
	 *
	 * @param arrays the arrays.
	 * @return the new array.
	 */
	public static byte[] concatAll(byte[]... arrays) {

		if (arrays.length == 0) {
			return new byte[] {};
		}
		if (arrays.length == 1) {
			return arrays[0];
		}

		byte[] cur = concat(arrays[0], arrays[1]);
		for (int i = 2; i < arrays.length; i++) {
			cur = concat(cur, arrays[i]);
		}
		return cur;
	}

	/**
	 * Split {@code source} into partitioned arrays using delimiter {@code c}.
	 *
	 * @param source the source array.
	 * @param c delimiter.
	 * @return the partitioned arrays.
	 */
	public static byte[][] split(byte[] source, int c) {

		if (ObjectUtils.isEmpty(source)) {
			return new byte[][] {};
		}

		List<byte[]> bytes = new ArrayList<>();
		int offset = 0;
		for (int i = 0; i <= source.length; i++) {

			if (i == source.length) {

				bytes.add(Arrays.copyOfRange(source, offset, i));
				break;
			}

			if (source[i] == c) {
				bytes.add(Arrays.copyOfRange(source, offset, i));
				offset = i + 1;
			}
		}
		return bytes.toArray(new byte[bytes.size()][]);
	}

	/**
	 * Merge multiple {@code byte} arrays into one array
	 *
	 * @param firstArray must not be {@literal null}
	 * @param additionalArrays must not be {@literal null}
	 * @return
	 */
	public static byte[][] mergeArrays(byte[] firstArray, byte[]... additionalArrays) {

		Assert.notNull(firstArray, "first array must not be null");
		Assert.notNull(additionalArrays, "additional arrays must not be null");

		byte[][] result = new byte[additionalArrays.length + 1][];
		result[0] = firstArray;
		System.arraycopy(additionalArrays, 0, result, 1, additionalArrays.length);

		return result;
	}

	/**
	 * Extract a byte array from {@link ByteBuffer} without consuming it.
	 *
	 * @param byteBuffer must not be {@literal null}.
	 * @return
	 * @since 2.0
	 */
	public static byte[] getBytes(ByteBuffer byteBuffer) {

		Assert.notNull(byteBuffer, "ByteBuffer must not be null!");

		ByteBuffer duplicate = byteBuffer.duplicate();
		byte[] bytes = new byte[duplicate.remaining()];
		duplicate.get(bytes);
		return bytes;
	}
}
