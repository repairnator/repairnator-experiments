/*
 * Copyright 2018 the original author or authors.
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
package org.springframework.data.redis.connection.lettuce;

import io.lettuce.core.AbstractRedisClient;

/**
 * Extension to {@link LettuceConnectionProvider} for providers exposing {@link io.lettuce.core.RedisClient}.
 *
 * @author Mark Paluch
 * @since 2.0.5
 */
interface RedisClientProvider {

	/**
	 * Returns the underlying Redis Client.
	 *
	 * @return the {@link AbstractRedisClient}. Never {@literal null}.
	 */
	AbstractRedisClient getRedisClient();
}
