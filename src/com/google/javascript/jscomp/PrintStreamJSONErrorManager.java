/*
 * Copyright 2017 The Closure Compiler Authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.javascript.jscomp;

import com.google.common.annotations.GwtIncompatible;
import com.google.debugging.sourcemap.proto.Mapping.OriginalMapping;
import com.google.gson.stream.JsonWriter;
import com.google.javascript.jscomp.LightweightMessageFormatter.LineNumberingFormatter;
import com.google.javascript.jscomp.SourceExcerptProvider.SourceExcerpt;
import com.google.javascript.jscomp.parsing.parser.util.format.SimpleFormat;
import com.google.javascript.rhino.TokenUtil;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * An error manager that prints error and warning data to the print stream as an array of JSON
 * objects provided in addition to the functionality of the {@link BasicErrorManager}.
 */
public class PrintStreamJSONErrorManager extends BasicErrorManager {
  private final PrintStream stream;
  private final SourceExcerptProvider sourceExcerptProvider;
  private static final LineNumberingFormatter excerptFormatter = new LineNumberingFormatter();

  /**
   * Creates an error manager.
   *
   * @param stream the stream on which the errors and warnings should be printed. This class does
   *     not close the stream
   * @param sourceExcerptProvider used to retrieve the source context which generated the error
   */
  public PrintStreamJSONErrorManager(
      PrintStream stream, SourceExcerptProvider sourceExcerptProvider) {
    this.stream = stream;
    this.sourceExcerptProvider = sourceExcerptProvider;
  }

  @Override
  @GwtIncompatible
  public void generateReport() {
    ByteArrayOutputStream bufferedStream = new ByteArrayOutputStream();
    List<ErrorWithLevel> list = new ArrayList<>();
    try (JsonWriter jsonWriter = new JsonWriter(new OutputStreamWriter(bufferedStream, "UTF-8"))) {
      jsonWriter.beginArray();
      for (ErrorWithLevel message = messages.poll(); message != null; message = messages.poll()) {
        String sourceName = message.error.sourceName;
        int lineNumber = message.error.getLineNumber();
        int charno = message.error.getCharno();

        jsonWriter.beginObject();
        jsonWriter.name("level").value(message.level == CheckLevel.ERROR ? "error" : "warning");
        jsonWriter.name("description").value(message.error.description);
        jsonWriter.name("source").value(sourceName);
        jsonWriter.name("line").value(lineNumber);
        jsonWriter.name("column").value(charno);

        // extract source excerpt
        String sourceExcerpt =
            SourceExcerpt.LINE.get(sourceExcerptProvider, sourceName, lineNumber, excerptFormatter);
        if (sourceExcerpt != null) {
          StringBuilder b = new StringBuilder(sourceExcerpt);
          b.append("\n");

          // padding equal to the excerpt and arrow at the end
          // charno == sourceExcerpt.length() means something is missing
          // at the end of the line
          if (0 <= charno && charno <= sourceExcerpt.length()) {
            for (int i = 0; i < charno; i++) {
              char c = sourceExcerpt.charAt(i);
              if (TokenUtil.isWhitespace(c)) {
                b.append(c);
              } else {
                b.append(' ');
              }
            }
            if (message.error.node == null) {
              b.append("^");
            } else {
              int length =
                  Math.max(
                      1, Math.min(message.error.node.getLength(), sourceExcerpt.length() - charno));
              for (int i = 0; i < length; i++) {
                b.append("^");
              }
            }
          }

          jsonWriter.name("context").value(b.toString());
        }

        OriginalMapping mapping =
            sourceExcerptProvider.getSourceMapping(
                sourceName, message.error.lineNumber, message.error.getCharno());

        if (mapping != null) {
          jsonWriter.name("originalLocation").beginObject();
          jsonWriter.name("source").value(mapping.getOriginalFile());
          jsonWriter.name("line").value(mapping.getLineNumber());
          jsonWriter.name("column").value(mapping.getColumnPosition());
          jsonWriter.endObject();
        }

        jsonWriter.endObject();
        list.add(message);
      }

      StringBuilder summaryBuilder = new StringBuilder();
      if (getTypedPercent() > 0.0) {
        summaryBuilder.append(
            SimpleFormat.format(
                "%d error(s), %d warning(s), %.1f%% typed",
                getErrorCount(), getWarningCount(), getTypedPercent()));
      } else {
        summaryBuilder.append(
            SimpleFormat.format("%d error(s), %d warning(s)", getErrorCount(), getWarningCount()));
      }
      jsonWriter.beginObject();
      jsonWriter.name("level").value("info");
      jsonWriter.name("description").value(summaryBuilder.toString());
      jsonWriter.endObject();

      jsonWriter.endArray();
      jsonWriter.flush();
      jsonWriter.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    stream.append(bufferedStream.toString());

    // Restore the messages since some tests assert the values after generating the report.
    messages.addAll(list);
  }

  // This class overrides generateReport(), so nothing will call println().
  @Override
  public void println(CheckLevel level, JSError error) {
    throw new UnsupportedOperationException(
        "should not be called for PrintStreamJSONErrorManager");
  }

  // This class overrides generateReport(), so nothing will call printSummary().
  @Override
  public void printSummary() {
    throw new UnsupportedOperationException(
        "should not be called for PrintStreamJSONErrorManager");
  }
}
