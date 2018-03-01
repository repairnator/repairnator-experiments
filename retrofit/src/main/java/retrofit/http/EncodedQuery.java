/*
 * Copyright (C) 2013 Square, Inc.
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
package retrofit.http;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Encoded query parameter appended to the URL.
 * <p>
 * Values are converted to strings using {@link String#valueOf(Object)}. Values are not URL
 * encoded. {@code null} values will not include the query parameter in the URL. See
 * {@link Query @Query} for URL-encoding equivalent.
 *
 * @see Query
 * @see QueryMap
 * @see EncodedQueryMap
 */
@Documented
@Target(PARAMETER)
@Retention(RUNTIME)
public @interface EncodedQuery {
  String value();
}
