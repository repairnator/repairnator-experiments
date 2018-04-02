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

package io.hekate.messaging.unicast;

import io.hekate.messaging.MessagingChannel;

/**
 * Callback for {@link MessagingChannel#send(Object, SendCallback) send(...)} operation.
 *
 * @see MessagingChannel#send(Object, SendCallback)
 */
@FunctionalInterface
public interface SendCallback {
    /**
     * Called when the request operation gets completed either successfully or with an error.
     *
     * @param err Error ({@code null} if operation completed successfully).
     */
    void onComplete(Throwable err);
}
