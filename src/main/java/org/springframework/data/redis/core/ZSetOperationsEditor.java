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
package org.springframework.data.redis.core;

import java.beans.PropertyEditorSupport;

/**
 * PropertyEditor allowing for easy injection of {@link ZSetOperations} from {@link RedisOperations}.
 * 
 * @author Costin Leau
 */
class ZSetOperationsEditor extends PropertyEditorSupport {

	public void setValue(Object value) {
		if (value instanceof RedisOperations) {
			super.setValue(((RedisOperations) value).opsForZSet());
		} else {
			throw new java.lang.IllegalArgumentException("Editor supports only conversion of type " + RedisOperations.class);
		}
	}
}
