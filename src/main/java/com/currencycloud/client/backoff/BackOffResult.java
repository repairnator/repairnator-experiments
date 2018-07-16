/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Chris Campo
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
 */

package com.currencycloud.client.backoff;

import java.util.Objects;
import java.util.Optional;

public class BackOffResult<T> {

    public final Optional<T> data;
    public final BackOffResultStatus status;

    private BackOffResult(final Optional<T> data, final BackOffResultStatus status) {
        this.data = Objects.requireNonNull(data);
        this.status = Objects.requireNonNull(status);
    }

    public BackOffResult(final T data, final BackOffResultStatus status) {
        this(Optional.ofNullable(data), status);
    }

    public BackOffResult(final BackOffResultStatus status) {
        this(Optional.empty(), status);
    }
}
