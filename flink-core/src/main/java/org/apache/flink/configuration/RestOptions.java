/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.flink.configuration;

import org.apache.flink.annotation.Internal;

import static org.apache.flink.configuration.ConfigOptions.key;

/**
 * Configuration parameters for REST communication.
 */
@Internal
public class RestOptions {

	/**
	 * The address that the server binds itself to / the client connects to.
	 */
	public static final ConfigOption<String> REST_ADDRESS =
		key("rest.address")
			.defaultValue("localhost");

	/**
	 * The port that the server listens on / the client connects to.
	 */
	public static final ConfigOption<Integer> REST_PORT =
		key("rest.port")
			.defaultValue(9067);

	/**
	 * The time in ms to wait for the leader address, e.g., Dispatcher or WebMonitorEndpoint
	 */
	public static final ConfigOption<Integer> AWAIT_LEADER_TIMEOUT =
		key("rest.await-leader-timeout")
			.defaultValue(30_000);

	/**
	 * The number of attempts to retry a retryable operation.
	 * @see #RETRY_DELAY
	 */
	public static final ConfigOption<Integer> RETRY_MAX_ATTEMPTS =
		key("rest.retry.max-attempts")
			.defaultValue(20);

	/**
	 * The time in ms to wait between retries.
	 * @see #RETRY_MAX_ATTEMPTS
	 */
	public static final ConfigOption<Integer> RETRY_DELAY =
		key("rest.retry.delay")
			.defaultValue(20);

	/**
	 * The maximum time in ms to establish a TCP connection.
	 * @see #RETRY_MAX_ATTEMPTS
	 */
	public static final ConfigOption<Integer> CONNECTION_TIMEOUT =
		key("rest.connection-timeout")
			.defaultValue(15_000);
}
