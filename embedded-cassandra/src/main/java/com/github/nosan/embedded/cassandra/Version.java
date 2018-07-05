/*
 * Copyright 2012-2018 the original author or authors.
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

package com.github.nosan.embedded.cassandra;

/**
 * List of Cassandra's versions.
 *
 * @author Dmytro Nosan
 */
public enum Version {

	/**
	 * The version 3.11.2.
	 */
	LATEST("3.11.2");

	private final String value;

	Version(String value) {
		this.value = value;
	}

	/**
	 * Retrieves Cassandra's version.
	 * @return Version to use.
	 */
	public String getValue() {
		return this.value;
	}

}
