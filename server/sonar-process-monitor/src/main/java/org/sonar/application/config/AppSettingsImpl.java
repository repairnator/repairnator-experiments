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
package org.sonar.application.config;

import java.util.Optional;
import org.sonar.process.Props;

public class AppSettingsImpl implements AppSettings {

  private Props props;

  AppSettingsImpl(Props props) {
    this.props = props;
  }

  @Override
  public Props getProps() {
    return props;
  }

  @Override
  public Optional<String> getValue(String key) {
    return Optional.ofNullable(props.value(key));
  }

  @Override
  public void reload(Props copy) {
    this.props = copy;
  }
}
