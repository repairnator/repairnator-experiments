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
 * Extension for [ReactiveFindOperation.query] providing a [KClass] based variant.
 *
 * @author Mark Paluch
 * @since 2.0
 */
fun <T : Any> ReactiveFindOperation.query(entityClass: KClass<T>): ReactiveFindOperation.ReactiveFind<T> =
		query(entityClass.java)

/**
 * Extension for [ReactiveFindOperation.query] leveraging reified type parameters.
 *
 * @author Mark Paluch
 * @since 2.0
 */
inline fun <reified T : Any> ReactiveFindOperation.query(): ReactiveFindOperation.ReactiveFind<T> =
		query(T::class.java)

/**
 * Extension for [ReactiveFindOperation.FindWithProjection.as] providing a [KClass] based variant.
 *
 * @author Mark Paluch
 * @since 2.0
 */
fun <T : Any> ReactiveFindOperation.FindWithProjection<T>.asType(resultType: KClass<T>): ReactiveFindOperation.FindWithQuery<T> =
		`as`(resultType.java)

/**
 * Extension for [ReactiveFindOperation.FindWithProjection.as] leveraging reified type parameters.
 *
 * @author Mark Paluch
 * @since 2.0
 */
inline fun <reified T : Any> ReactiveFindOperation.FindWithProjection<T>.asType(): ReactiveFindOperation.FindWithQuery<T> =
		`as`(T::class.java)

/**
 * Extension for [ExecutableFindOperation.DistinctWithProjection. as] providing a [KClass] based variant.
 *
 * @author Christoph Strobl
 * @since 2.1
 */
fun <T : Any> ReactiveFindOperation.DistinctWithProjection.asType(resultType: KClass<T>): ReactiveFindOperation.TerminatingDistinct<T> =
		`as`(resultType.java);
