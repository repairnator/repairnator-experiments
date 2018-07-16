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
package com.linkedin.pinot.core.util;

/**
 * Util class to encapsulate all math required to compute storage space.
 */
public class SizeUtil {
  public static int BIT_UNPACK_BATCH_SIZE = 32;

  public static int computeBytesRequired(int numValues, int numBits, int entriesPerBatch) {
    int bitsRequiredPerBatch = entriesPerBatch * numBits;
    //Align to batch boundary to avoid if checks while reading
    int totalBitsRounded = (int) (Math.ceil((numValues * numBits * 1.0) / bitsRequiredPerBatch)
        * bitsRequiredPerBatch);
    return totalBitsRounded / 8;
  }
}
