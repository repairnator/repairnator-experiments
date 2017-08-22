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
package org.sonar.api.batch.debt.internal;

import java.util.Collections;
import java.util.List;
import javax.annotation.CheckForNull;
import org.sonar.api.batch.debt.DebtCharacteristic;
import org.sonar.api.batch.debt.DebtModel;

public class DefaultDebtModel implements DebtModel {

  @Override
  public List<DebtCharacteristic> characteristics() {
    return Collections.emptyList();
  }

  @Override
  public List<DebtCharacteristic> subCharacteristics(String characteristicKey) {
    return Collections.emptyList();
  }

  @Override
  public List<DebtCharacteristic> allCharacteristics() {
    return Collections.emptyList();
  }

  @Override
  @CheckForNull
  public DebtCharacteristic characteristicByKey(final String key) {
    return null;
  }

  @CheckForNull
  public DebtCharacteristic characteristicById(int id) {
    return null;
  }
}
