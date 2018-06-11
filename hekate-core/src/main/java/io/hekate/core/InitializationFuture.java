/*
 * Copyright 2018 The Hekate Project
 *
 * The Hekate Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package io.hekate.core;

import io.hekate.util.HekateFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Asynchronous result of {@link Hekate#initializeAsync()} operation.
 *
 * @see Hekate#initializeAsync()
 */
public class InitializationFuture extends HekateFuture<Hekate, InitializationFuture> {
    @Override
    public Hekate get() throws InterruptedException, HekateFutureException {
        try {
            return super.get();
        } catch (ExecutionException e) {
            throw new HekateFutureException(e.getCause().getMessage(), e.getCause());
        }
    }

    @Override
    public Hekate get(long timeout, TimeUnit unit) throws InterruptedException, TimeoutException, HekateFutureException {
        try {
            return super.get(timeout, unit);
        } catch (ExecutionException e) {
            throw new HekateFutureException(e.getCause().getMessage(), e.getCause());
        }
    }

    @Override
    public Hekate getUninterruptedly() throws HekateFutureException {
        try {
            return super.getUninterruptedly();
        } catch (ExecutionException e) {
            throw new HekateFutureException(e.getCause().getMessage(), e.getCause());
        }
    }

    @Override
    protected InitializationFuture newInstance() {
        return new InitializationFuture();
    }
}
