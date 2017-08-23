/*
 * SonarQube
 * Copyright (C) 2009-2017 SonarSource SA
 * mailto:info AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.server.platform.db.migration.version.v60;

import java.sql.SQLException;
import java.sql.Types;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.sonar.db.CoreDbTester;

import static java.lang.String.valueOf;

public class MakeUuidColumnsNotNullOnProjectsTest {

  private static final String PROJECTS_TABLE = "projects";

  @Rule
  public CoreDbTester db = CoreDbTester.createForSchema(MakeUuidColumnsNotNullOnProjectsTest.class,
    "in_progress_projects.sql");
  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  private MakeUuidColumnsNotNullOnProjects underTest = new MakeUuidColumnsNotNullOnProjects(db.database());

  @Test
  public void migration_sets_uuid_columns_not_nullable_on_empty_table() throws SQLException {
    underTest.execute();

    verifyColumnDefinitions();
    verifyIndex();
  }

  @Test
  public void migration_sets_uuid_columns_not_nullable_on_populated_table() throws SQLException {
    insertComponent(1, true, true);
    insertComponent(2, true, true);

    underTest.execute();

    verifyColumnDefinitions();
    verifyIndex();
  }

  @Test
  public void migration_fails_if_some_row_has_a_null_uuid() throws SQLException {
    insertComponent(1, false, true);

    expectedException.expect(IllegalStateException.class);
    expectedException.expectMessage("Fail to execute");

    underTest.execute();
  }

  @Test
  public void migration_fails_if_some_row_has_a_null_rootuuid() throws SQLException {
    insertComponent(1, true, false);

    expectedException.expect(IllegalStateException.class);
    expectedException.expectMessage("Fail to execute");

    underTest.execute();
  }

  private void verifyColumnDefinitions() {
    db.assertColumnDefinition(PROJECTS_TABLE, "uuid", Types.VARCHAR, 50, false);
    db.assertColumnDefinition(PROJECTS_TABLE, "root_uuid", Types.VARCHAR, 50, false);
  }

  private void verifyIndex() {
    db.assertIndex(PROJECTS_TABLE, "projects_root_uuid", "root_uuid");
  }

  private String insertComponent(long id, boolean hasUuid, boolean hasRootUuid) {
    String uuid = "uuid_" + id;
    db.executeInsert(
      "projects",
      "ID", valueOf(id),
      "UUID", hasUuid ? "uuid_" + id : null,
      "ROOT_UUID", hasRootUuid ? "root_uuuid_" + id : null);
    return uuid;
  }

}
