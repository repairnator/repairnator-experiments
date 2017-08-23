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
package org.sonar.server.qualityprofile;

import java.util.List;
import java.util.Map;

public interface DefinedQProfileRepository {
  /**
   * Initializes the Repository.
   *
   * This method is intended to be called from a startup task
   * (see {@link org.sonar.server.platform.platformlevel.PlatformLevelStartup}).
   *
   * @throws IllegalStateException if called more then once
   */
  void initialize();

  /**
   * @return an immutable map containing immutable lists.
   *
   * @throws IllegalStateException if {@link #initialize()} has not been called
   */
  Map<String, List<DefinedQProfile>> getQProfilesByLanguage();
}
