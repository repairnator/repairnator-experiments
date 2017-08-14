/*
 * Licensed to Metamarkets Group Inc. (Metamarkets) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. Metamarkets licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package io.druid.server.lookup.namespace;

import io.druid.common.utils.JodaUtils;
import io.druid.java.util.common.Pair;
import io.druid.java.util.common.StringUtils;
import io.druid.java.util.common.logger.Logger;
import io.druid.query.lookup.namespace.CacheGenerator;
import io.druid.query.lookup.namespace.JdbcExtractionNamespace;
import io.druid.server.lookup.namespace.cache.CacheScheduler;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.HandleCallback;
import org.skife.jdbi.v2.tweak.ResultSetMapper;
import org.skife.jdbi.v2.util.TimestampMapper;

import com.google.common.base.Strings;

import javax.annotation.Nullable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 *
 */
public final class JdbcCacheGenerator implements CacheGenerator<JdbcExtractionNamespace>
{
  private static final Logger LOG = new Logger(JdbcCacheGenerator.class);
  private final ConcurrentMap<CacheScheduler.EntryImpl<JdbcExtractionNamespace>, DBI> dbiCache =
      new ConcurrentHashMap<>();

  @Override
  @Nullable
  public CacheScheduler.VersionedCache generateCache(
      final JdbcExtractionNamespace namespace,
      final CacheScheduler.EntryImpl<JdbcExtractionNamespace> entryId,
      final String lastVersion,
      final CacheScheduler scheduler
  )
  {
    final long lastCheck = lastVersion == null ? JodaUtils.MIN_INSTANT : Long.parseLong(lastVersion);
    final Long lastDBUpdate = lastUpdates(entryId, namespace);
    if (lastDBUpdate != null && lastDBUpdate <= lastCheck) {
      return null;
    }
    final long dbQueryStart = System.currentTimeMillis();
    final DBI dbi = ensureDBI(entryId, namespace);
    final String table = namespace.getTable();
    final String filter = namespace.getFilter();
    final String valueColumn = namespace.getValueColumn();
    final String keyColumn = namespace.getKeyColumn();

    LOG.debug("Updating %s", entryId);
    final List<Pair<String, String>> pairs = dbi.withHandle(
        new HandleCallback<List<Pair<String, String>>>()
        {
          @Override
          public List<Pair<String, String>> withHandle(Handle handle) throws Exception
          {
            return handle
                .createQuery(
                    buildLookupQuery(table, filter, keyColumn, valueColumn)
                ).map(
                    new ResultSetMapper<Pair<String, String>>()
                    {
                      @Override
                      public Pair<String, String> map(
                          final int index,
                          final ResultSet r,
                          final StatementContext ctx
                      ) throws SQLException
                      {
                        return new Pair<>(r.getString(keyColumn), r.getString(valueColumn));
                      }
                    }
                ).list();
          }
        }
    );
    final String newVersion;
    if (lastDBUpdate != null) {
      newVersion = lastDBUpdate.toString();
    } else {
      newVersion = StringUtils.format("%d", dbQueryStart);
    }
    final CacheScheduler.VersionedCache versionedCache = scheduler.createVersionedCache(entryId, newVersion);
    try {
      final Map<String, String> cache = versionedCache.getCache();
      for (Pair<String, String> pair : pairs) {
        cache.put(pair.lhs, pair.rhs);
      }
      LOG.info("Finished loading %d values for %s", cache.size(), entryId);
      return versionedCache;
    }
    catch (Throwable t) {
      try {
        versionedCache.close();
      }
      catch (Exception e) {
        t.addSuppressed(e);
      }
      throw t;
    }
  }

  private String buildLookupQuery(String table, String filter, String keyColumn, String valueColumn)
  {
    if (Strings.isNullOrEmpty(filter)) {
      return StringUtils.format(
          "SELECT %s, %s FROM %s",
          keyColumn,
          valueColumn,
          table
      );
    }

    return StringUtils.format(
        "SELECT %s, %s FROM %s WHERE %s",
        keyColumn,
        valueColumn,
        table,
        filter
    );
  }

  private DBI ensureDBI(CacheScheduler.EntryImpl<JdbcExtractionNamespace> id, JdbcExtractionNamespace namespace)
  {
    final CacheScheduler.EntryImpl<JdbcExtractionNamespace> key = id;
    DBI dbi = null;
    if (dbiCache.containsKey(key)) {
      dbi = dbiCache.get(key);
    }
    if (dbi == null) {
      final DBI newDbi = new DBI(
          namespace.getConnectorConfig().getConnectURI(),
          namespace.getConnectorConfig().getUser(),
          namespace.getConnectorConfig().getPassword()
      );
      dbiCache.putIfAbsent(key, newDbi);
      dbi = dbiCache.get(key);
    }
    return dbi;
  }

  private Long lastUpdates(CacheScheduler.EntryImpl<JdbcExtractionNamespace> id, JdbcExtractionNamespace namespace)
  {
    final DBI dbi = ensureDBI(id, namespace);
    final String table = namespace.getTable();
    final String tsColumn = namespace.getTsColumn();
    if (tsColumn == null) {
      return null;
    }
    final Timestamp update = dbi.withHandle(
        new HandleCallback<Timestamp>()
        {

          @Override
          public Timestamp withHandle(Handle handle) throws Exception
          {
            final String query = StringUtils.format(
                "SELECT MAX(%s) FROM %s",
                tsColumn, table
            );
            return handle
                .createQuery(query)
                .map(TimestampMapper.FIRST)
                .first();
          }
        }
    );
    return update.getTime();

  }
}
