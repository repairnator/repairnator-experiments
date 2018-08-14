/*
 * Copyright 2016-2018 the original author or authors.
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
package example.springdata.cassandra.util;

import org.springframework.data.util.Version;
import org.springframework.util.Assert;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

import lombok.experimental.UtilityClass;

/**
 * Utility to retrieve the Cassandra release version.
 *
 * @author Mark Paluch
 */
@UtilityClass
public class CassandraVersion {

	/**
	 * Retrieve the Cassandra release version.
	 *
	 * @param session must not be {@literal null}.
	 * @return the release {@link Version}.
	 */
	public static Version getReleaseVersion(Session session) {

		Assert.notNull(session, "Session must not be null");

		ResultSet resultSet = session.execute("SELECT release_version FROM system.local;");
		Row row = resultSet.one();

		return Version.parse(row.getString(0));
	}
}
