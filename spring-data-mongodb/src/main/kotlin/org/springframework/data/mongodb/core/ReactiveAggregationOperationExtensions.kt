/*
 * Copyright 2017 the original author or authors.
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
package org.springframework.data.mongodb.core

import kotlin.reflect.KClass

/**
 * Extension for [ExecutableAggregationOperation.aggregateAndReturn] providing a [KClass] based variant.
 *
 * @author Mark Paluch
 * @since 2.0
 */
fun <T : Any> ReactiveAggregationOperation.aggregateAndReturn(entityClass: KClass<T>): ReactiveAggregationOperation.ReactiveAggregation<T> =
        aggregateAndReturn(entityClass.java)

/**
 * Extension for [ExecutableAggregationOperation.aggregateAndReturn] leveraging reified type parameters.
 *
 * @author Mark Paluch
 * @since 2.0
 */
inline fun <reified T : Any> ReactiveAggregationOperation.aggregateAndReturn(): ReactiveAggregationOperation.ReactiveAggregation<T> =
        aggregateAndReturn(T::class.java)