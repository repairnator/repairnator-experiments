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
package org.sonar.db.qualityprofile;

import java.util.Date;
import javax.annotation.CheckForNull;
import javax.annotation.Nullable;
import org.sonar.core.util.UtcDateUtils;
import org.sonar.db.Dto;
import org.sonar.db.organization.OrganizationDto;

public class QualityProfileDto extends Dto<String> {

  private Integer id;
  /**
   * The organization, that this quality profile belongs to.
   * Must not be null, but can be the default organization's uuid.
   * Refers to {@link OrganizationDto#getUuid()}.
   */
  private String organizationUuid;
  private String kee;
  private String name;
  private String language;
  private String parentKee;
  private String rulesUpdatedAt;
  private Long lastUsed;
  private Long userUpdatedAt;
  private boolean isDefault;

  /**
   * @deprecated use {@link #createFor(String)}
   */
  @Deprecated
  public QualityProfileDto() {

  }

  public String getOrganizationUuid() {
    return organizationUuid;
  }

  public QualityProfileDto setOrganizationUuid(String organizationUuid) {
    this.organizationUuid = organizationUuid;
    return this;
  }

  @Override
  public String getKey() {
    return kee;
  }

  public QualityProfileDto setKey(String s) {
    return setKee(s);
  }

  public String getKee() {
    return kee;
  }

  public QualityProfileDto setKee(String s) {
    this.kee = s;
    return this;
  }

  public Integer getId() {
    return id;
  }

  public QualityProfileDto setId(Integer id) {
    this.id = id;
    return this;
  }

  public String getName() {
    return name;
  }

  public QualityProfileDto setName(String name) {
    this.name = name;
    return this;
  }

  public String getLanguage() {
    return language;
  }

  public QualityProfileDto setLanguage(String language) {
    this.language = language;
    return this;
  }

  @CheckForNull
  public String getParentKee() {
    return parentKee;
  }

  public QualityProfileDto setParentKee(@Nullable String s) {
    this.parentKee = s;
    return this;
  }

  public String getRulesUpdatedAt() {
    return rulesUpdatedAt;
  }

  public QualityProfileDto setRulesUpdatedAt(String s) {
    this.rulesUpdatedAt = s;
    return this;
  }

  public QualityProfileDto setRulesUpdatedAtAsDate(Date d) {
    this.rulesUpdatedAt = UtcDateUtils.formatDateTime(d);
    return this;
  }

  @CheckForNull
  public Long getLastUsed() {
    return lastUsed;
  }

  public QualityProfileDto setLastUsed(@Nullable Long lastUsed) {
    this.lastUsed = lastUsed;
    return this;
  }

  @CheckForNull
  public Long getUserUpdatedAt() {
    return userUpdatedAt;
  }

  public QualityProfileDto setUserUpdatedAt(@Nullable Long userUpdatedAt) {
    this.userUpdatedAt = userUpdatedAt;
    return this;
  }

  public boolean isDefault() {
    return isDefault;
  }

  public QualityProfileDto setDefault(boolean isDefault) {
    this.isDefault = isDefault;
    return this;
  }

  public static QualityProfileDto createFor(String key) {
    return new QualityProfileDto().setKee(key);
  }
}
