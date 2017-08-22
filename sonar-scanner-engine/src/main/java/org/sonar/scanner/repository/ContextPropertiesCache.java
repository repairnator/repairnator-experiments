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
package org.sonar.scanner.repository;

import java.util.HashMap;
import java.util.Map;
import org.sonar.api.batch.ScannerSide;

import static com.google.common.base.Preconditions.checkArgument;

@ScannerSide
public class ContextPropertiesCache {

  private final Map<String, String> props = new HashMap<>();

  /**
   * Value is overridden if the key was already stored.
   * @throws IllegalArgumentException if key is null
   * @throws IllegalArgumentException if value is null
   * @since 6.1
   */
  public ContextPropertiesCache put(String key, String value) {
    checkArgument(key != null, "Key of context property must not be null");
    checkArgument(value != null, "Value of context property must not be null");
    props.put(key, value);
    return this;
  }

  public Map<String, String> getAll() {
    return props;
  }
}
