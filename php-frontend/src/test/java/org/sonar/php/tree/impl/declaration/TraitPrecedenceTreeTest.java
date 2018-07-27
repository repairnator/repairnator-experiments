/*
 * SonarQube PHP Plugin
 * Copyright (C) 2010-2018 SonarSource SA
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
package org.sonar.php.tree.impl.declaration;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.sonar.php.PHPTreeModelTest;
import org.sonar.php.parser.PHPLexicalGrammar;
import org.sonar.plugins.php.api.tree.Tree.Kind;
import org.sonar.plugins.php.api.tree.statement.TraitPrecedenceTree;

public class TraitPrecedenceTreeTest extends PHPTreeModelTest {

  @Test
  public void test() throws Exception {
    TraitPrecedenceTree tree = parse("NS1\\Class1::method1 insteadof Class2, Class3;", PHPLexicalGrammar.TRAIT_PRECEDENCE);
    assertThat(tree.is(Kind.TRAIT_PRECEDENCE)).isTrue();
    assertThat(tree.methodReference().method().text()).isEqualTo("method1");
    assertThat(tree.insteadOfToken().text()).isEqualTo("insteadof");
    assertThat(tree.traits()).hasSize(2);
    assertThat(tree.eosToken().text()).isEqualTo(";");
  }

}
