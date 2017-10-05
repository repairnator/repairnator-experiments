/*
 * Copyright 2011-2013 the original author or authors.
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
package org.springframework.data.redis.serializer;

import org.springframework.lang.Nullable;

/**
 * Basic interface serialization and deserialization of Objects to byte arrays (binary data). It is recommended that
 * implementations are designed to handle null objects/empty arrays on serialization and deserialization side. Note that
 * Redis does not accept null keys or values but can return null replies (for non existing keys).
 * 
 * @author Mark Pollack
 * @author Costin Leau
 * @author Christoph Strobl
 */
public interface RedisSerializer<T> {

	/**
	 * Serialize the given object to binary data.
	 * 
	 * @param t object to serialize. Can be {@literal null}.
	 * @return the equivalent binary data. Can be {@literal null}.
	 */
	@Nullable
	byte[] serialize(@Nullable T t) throws SerializationException;

	/**
	 * Deserialize an object from the given binary data.
	 * 
	 * @param bytes object binary representation. Can be {@literal null}.
	 * @return the equivalent object instance. Can be {@literal null}.
	 */
	@Nullable
	T deserialize(@Nullable byte[] bytes) throws SerializationException;
}
