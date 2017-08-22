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
package org.sonar.server.permission.index;

/**
 * An {@link NeedAuthorizationIndexer} defines how
 * a {@link org.sonar.server.es.ProjectIndexer} populates
 * the type named {@link AuthorizationTypeSupport#TYPE_AUTHORIZATION}, which
 * is used to verify that a user can access to projects.
 */
public interface NeedAuthorizationIndexer {

  /**
   * Returns the metadata required by {@link PermissionIndexer} to
   * populate "authorization" types.
   */
  AuthorizationScope getAuthorizationScope();

}
