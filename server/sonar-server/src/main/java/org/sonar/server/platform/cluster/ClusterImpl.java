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
package org.sonar.server.platform.cluster;

import org.sonar.api.config.Settings;
import org.sonar.api.utils.log.Loggers;

public class ClusterImpl implements Cluster {

  private final boolean enabled;
  private final boolean startupLeader;

  public ClusterImpl(Settings settings) {
    this.enabled = settings.getBoolean(ClusterProperties.ENABLED);
    if (this.enabled) {
      this.startupLeader = settings.getBoolean(ClusterProperties.STARTUP_LEADER);
      Loggers.get(ClusterImpl.class).info("Cluster enabled (startup {})", startupLeader ? "leader" : "follower");
    } else {
      this.startupLeader = true;
    }
  }

  @Override
  public boolean isEnabled() {
    return enabled;
  }

  @Override
  public boolean isStartupLeader() {
    return startupLeader;
  }
}
