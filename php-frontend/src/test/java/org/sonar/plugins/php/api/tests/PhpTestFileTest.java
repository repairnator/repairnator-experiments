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
package org.sonar.plugins.php.api.tests;

import java.io.File;
import java.nio.file.Paths;
import org.junit.Rule;
import org.junit.Test;
import org.junit.matchers.JUnitMatchers;
import org.junit.rules.ExpectedException;

import static org.assertj.core.api.Assertions.assertThat;

public class PhpTestFileTest {

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  @Test
  public void load_file() throws Exception {
    PhpTestFile file = new PhpTestFile(new File("src/test/resources/tests/testfile.php"));
    assertThat(file.relativePath().toString().replace('\\', '/')).isEqualTo("src/test/resources/tests/testfile.php");
    assertThat(file.contents()).isEqualTo("<?php echo \"Hello\";\n");
    assertThat(file.filename()).isEqualTo("testfile.php");
    String expectedPath = Paths.get("src", "test", "resources", "tests", "testfile.php").toString();
    assertThat(file.toString()).isEqualTo(expectedPath);
  }

  @Test
  public void load_invalid_show_filename() throws Exception {
    expectedException.expect(IllegalArgumentException.class);
    expectedException.expectMessage(JUnitMatchers.containsString("invalid.php"));
    new PhpTestFile(new File("invalid.php"));
  }

}
