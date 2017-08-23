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
package org.sonar.server.computation.task.projectanalysis.qualityprofile;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import javax.annotation.concurrent.Immutable;
import org.sonar.api.rule.RuleKey;

@Immutable
public class ActiveRule {
  private final RuleKey ruleKey;
  private final String severity;
  private final Map<String, String> params;
  private final long createdAt;

  public ActiveRule(RuleKey ruleKey, String severity, Map<String, String> params, long createdAt) {
    this.ruleKey = ruleKey;
    this.severity = severity;
    this.params = ImmutableMap.copyOf(params);
    this.createdAt = createdAt;
  }

  public RuleKey getRuleKey() {
    return ruleKey;
  }

  public String getSeverity() {
    return severity;
  }

  public Map<String, String> getParams() {
    return params;
  }

  public long getCreatedAt() {
    return createdAt;
  }
}
