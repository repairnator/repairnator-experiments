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
package org.sonar.server.platform.db;

import org.junit.Test;
import org.sonar.api.config.Settings;
import org.sonar.api.config.MapSettings;
import org.sonar.api.database.DatabaseProperties;
import org.sonar.api.utils.System2;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class EmbeddedDatabaseFactoryTest {

  private Settings settings = new MapSettings();
  private System2 system2 = mock(System2.class);

  @Test
  public void should_start_and_stop_tcp_h2_database() {
    settings.setProperty(DatabaseProperties.PROP_URL, "jdbc:h2:tcp:localhost");

    EmbeddedDatabase embeddedDatabase = mock(EmbeddedDatabase.class);

    EmbeddedDatabaseFactory databaseFactory = new EmbeddedDatabaseFactory(settings, system2) {
      @Override
      EmbeddedDatabase createEmbeddedDatabase() {
        return embeddedDatabase;
      }
    };
    databaseFactory.start();
    databaseFactory.stop();

    verify(embeddedDatabase).start();
    verify(embeddedDatabase).stop();
  }

  @Test
  public void should_not_start_mem_h2_database() {
    settings.setProperty(DatabaseProperties.PROP_URL, "jdbc:h2:mem");

    EmbeddedDatabase embeddedDatabase = mock(EmbeddedDatabase.class);

    EmbeddedDatabaseFactory databaseFactory = new EmbeddedDatabaseFactory(settings, system2) {
      @Override
      EmbeddedDatabase createEmbeddedDatabase() {
        return embeddedDatabase;
      }
    };
    databaseFactory.start();

    verify(embeddedDatabase, never()).start();
  }
}
