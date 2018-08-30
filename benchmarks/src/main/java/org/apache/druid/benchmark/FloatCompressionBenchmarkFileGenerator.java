/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.druid.benchmark;

import com.google.common.collect.ImmutableList;
import org.apache.druid.benchmark.datagen.BenchmarkColumnSchema;
import org.apache.druid.benchmark.datagen.BenchmarkColumnValueGenerator;
import org.apache.druid.java.util.common.logger.Logger;
import org.apache.druid.segment.column.ValueType;
import org.apache.druid.segment.data.ColumnarFloatsSerializer;
import org.apache.druid.segment.data.CompressionFactory;
import org.apache.druid.segment.data.CompressionStrategy;
import org.apache.druid.segment.writeout.OffHeapMemorySegmentWriteOutMedium;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FloatCompressionBenchmarkFileGenerator
{
  private static final Logger log = new Logger(FloatCompressionBenchmarkFileGenerator.class);
  public static final int ROW_NUM = 5000000;
  public static final List<CompressionStrategy> compressions =
      ImmutableList.of(
          CompressionStrategy.LZ4,
          CompressionStrategy.NONE
      );

  private static String dirPath = "floatCompress/";

  public static void main(String[] args) throws IOException
  {
    if (args.length >= 1) {
      dirPath = args[0];
    }

    BenchmarkColumnSchema enumeratedSchema = BenchmarkColumnSchema.makeEnumerated("", ValueType.FLOAT, true, 1, 0d,
                                                                                  ImmutableList.of(
                                                                                      0f,
                                                                                      1.1f,
                                                                                      2.2f,
                                                                                      3.3f,
                                                                                      4.4f
                                                                                  ),
                                                                                  ImmutableList.of(
                                                                                      0.95,
                                                                                      0.001,
                                                                                      0.0189,
                                                                                      0.03,
                                                                                      0.0001
                                                                                  )
    );
    BenchmarkColumnSchema zipfLowSchema = BenchmarkColumnSchema.makeZipf(
        "",
        ValueType.FLOAT,
        true,
        1,
        0d,
        -1,
        1000,
        1d
    );
    BenchmarkColumnSchema zipfHighSchema = BenchmarkColumnSchema.makeZipf(
        "",
        ValueType.FLOAT,
        true,
        1,
        0d,
        -1,
        1000,
        3d
    );
    BenchmarkColumnSchema sequentialSchema = BenchmarkColumnSchema.makeSequential(
        "",
        ValueType.FLOAT,
        true,
        1,
        0d,
        1470187671,
        2000000000
    );
    BenchmarkColumnSchema uniformSchema = BenchmarkColumnSchema.makeContinuousUniform(
        "",
        ValueType.FLOAT,
        true,
        1,
        0d,
        0,
        1000
    );

    Map<String, BenchmarkColumnValueGenerator> generators = new HashMap<>();
    generators.put("enumerate", new BenchmarkColumnValueGenerator(enumeratedSchema, 1));
    generators.put("zipfLow", new BenchmarkColumnValueGenerator(zipfLowSchema, 1));
    generators.put("zipfHigh", new BenchmarkColumnValueGenerator(zipfHighSchema, 1));
    generators.put("sequential", new BenchmarkColumnValueGenerator(sequentialSchema, 1));
    generators.put("uniform", new BenchmarkColumnValueGenerator(uniformSchema, 1));

    File dir = new File(dirPath);
    dir.mkdir();

    // create data files using BenchmarkColunValueGenerator
    for (Map.Entry<String, BenchmarkColumnValueGenerator> entry : generators.entrySet()) {
      final File dataFile = new File(dir, entry.getKey());
      dataFile.delete();
      try (Writer writer = Files.newBufferedWriter(dataFile.toPath(), StandardCharsets.UTF_8)) {
        for (int i = 0; i < ROW_NUM; i++) {
          writer.write((Float) entry.getValue().generateRowValue() + "\n");
        }
      }
    }

    // create compressed files using all combinations of CompressionStrategy and FloatEncoding provided
    for (Map.Entry<String, BenchmarkColumnValueGenerator> entry : generators.entrySet()) {
      for (CompressionStrategy compression : compressions) {
        String name = entry.getKey() + "-" + compression.toString();
        log.info("%s: ", name);
        File compFile = new File(dir, name);
        compFile.delete();
        File dataFile = new File(dir, entry.getKey());

        ColumnarFloatsSerializer writer = CompressionFactory.getFloatSerializer(
            new OffHeapMemorySegmentWriteOutMedium(),
            "float",
            ByteOrder.nativeOrder(),
            compression
        );
        try (
            BufferedReader br = Files.newBufferedReader(dataFile.toPath(), StandardCharsets.UTF_8);
            FileChannel output =
                FileChannel.open(compFile.toPath(), StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE)
        ) {
          writer.open();
          String line;
          while ((line = br.readLine()) != null) {
            writer.add(Float.parseFloat(line));
          }
          writer.writeTo(output, null);
        }
        log.info("%d", compFile.length() / 1024);
      }
    }
  }
}
