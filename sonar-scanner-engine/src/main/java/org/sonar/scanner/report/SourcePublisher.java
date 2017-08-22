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
package org.sonar.scanner.report;

import org.apache.commons.io.ByteOrderMark;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.BOMInputStream;
import org.sonar.api.batch.fs.internal.DefaultInputFile;
import org.sonar.scanner.protocol.output.ScannerReportWriter;
import org.sonar.scanner.scan.filesystem.InputComponentStore;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class SourcePublisher implements ReportPublisherStep {

  private final InputComponentStore componentCache;

  public SourcePublisher(InputComponentStore componentStore) {
    this.componentCache = componentStore;
  }

  @Override
  public void publish(ScannerReportWriter writer) {
    for (final DefaultInputFile inputFile : componentCache.allFilesToPublish()) {
      File iofile = writer.getSourceFile(inputFile.batchId());

      try (FileOutputStream output = new FileOutputStream(iofile);
        BOMInputStream bomIn = new BOMInputStream(new FileInputStream(inputFile.file()),
          ByteOrderMark.UTF_8, ByteOrderMark.UTF_16LE, ByteOrderMark.UTF_16BE, ByteOrderMark.UTF_32LE, ByteOrderMark.UTF_32BE);
        BufferedReader reader = new BufferedReader(new InputStreamReader(bomIn, inputFile.charset()))) {
        writeSource(reader, output, inputFile.lines());
      } catch (IOException e) {
        throw new IllegalStateException("Unable to store file source in the report", e);
      }
    }
  }

  private static void writeSource(BufferedReader reader, FileOutputStream output, int lines) throws IOException {
    int line = 0;
    String lineStr = reader.readLine();
    while (lineStr != null) {
      IOUtils.write(lineStr, output, StandardCharsets.UTF_8);
      line++;
      if (line < lines) {
        IOUtils.write("\n", output, StandardCharsets.UTF_8);
      }
      lineStr = reader.readLine();
    }
  }
}
