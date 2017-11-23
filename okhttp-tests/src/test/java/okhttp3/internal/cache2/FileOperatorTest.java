/*
 * Copyright (C) 2016 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package okhttp3.internal.cache2;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Random;
import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.ByteString;
import okio.Okio;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public final class FileOperatorTest {
  @Rule public final TemporaryFolder tempDir = new TemporaryFolder();

  private File file;
  private RandomAccessFile randomAccessFile;

  @Before public void setUp() throws Exception {
    file = tempDir.newFile();
    randomAccessFile = new RandomAccessFile(file, "rw");
  }

  @After public void tearDown() throws Exception {
    randomAccessFile.close();
  }

  @Test public void read() throws Exception {
    write(ByteString.encodeUtf8("Hello, World"));

    FileOperator operator = new FileOperator(randomAccessFile.getChannel());

    Buffer buffer = new Buffer();
    operator.read(0, buffer, 5);
    assertEquals("Hello", buffer.readUtf8());

    operator.read(4, buffer, 5);
    assertEquals("o, Wo", buffer.readUtf8());
  }

  @Test public void write() throws Exception {
    FileOperator operator = new FileOperator(randomAccessFile.getChannel());

    Buffer buffer1 = new Buffer().writeUtf8("Hello, World");
    operator.write(0, buffer1, 5);
    assertEquals(", World", buffer1.readUtf8());

    Buffer buffer2 = new Buffer().writeUtf8("icopter!");
    operator.write(3, buffer2, 7);
    assertEquals("!", buffer2.readUtf8());

    assertEquals(ByteString.encodeUtf8("Helicopter"), snapshot());
  }

  @Test public void readAndWrite() throws Exception {
    FileOperator operator = new FileOperator(randomAccessFile.getChannel());

    write(ByteString.encodeUtf8("woman god creates dinosaurs destroys. "));
    Buffer buffer = new Buffer();
    operator.read(6, buffer, 21);
    operator.read(36, buffer, 1);
    operator.read(5, buffer, 5);
    operator.read(28, buffer, 8);
    operator.read(17, buffer, 10);
    operator.read(36, buffer, 2);
    operator.read(2, buffer, 4);
    operator.write(0, buffer, buffer.size());
    operator.read(0, buffer, 12);
    operator.read(47, buffer, 3);
    operator.read(45, buffer, 2);
    operator.read(47, buffer, 3);
    operator.read(26, buffer, 10);
    operator.read(23, buffer, 3);
    operator.write(47, buffer, buffer.size());
    operator.read(62, buffer, 6);
    operator.read(4, buffer, 19);
    operator.write(80, buffer, buffer.size());

    assertEquals(snapshot(), ByteString.encodeUtf8(""
        + "god creates dinosaurs. "
        + "god destroys dinosaurs. "
        + "god creates man. "
        + "man destroys god. "
        + "man creates dinosaurs. "));
  }

  @Test public void multipleOperatorsShareOneFile() throws Exception {
    FileOperator operatorA = new FileOperator(randomAccessFile.getChannel());
    FileOperator operatorB = new FileOperator(randomAccessFile.getChannel());

    Buffer bufferA = new Buffer();
    Buffer bufferB = new Buffer();

    bufferA.writeUtf8("Dodgson!\n");
    operatorA.write(0, bufferA, 9);

    bufferB.writeUtf8("You shouldn't use my name.\n");
    operatorB.write(9, bufferB, 27);

    bufferA.writeUtf8("Dodgson, we've got Dodgson here!\n");
    operatorA.write(36, bufferA, 33);

    operatorB.read(0, bufferB, 9);
    assertEquals("Dodgson!\n", bufferB.readUtf8());

    operatorA.read(9, bufferA, 27);
    assertEquals("You shouldn't use my name.\n", bufferA.readUtf8());

    operatorB.read(36, bufferB, 33);
    assertEquals("Dodgson, we've got Dodgson here!\n", bufferB.readUtf8());
  }

  @Test public void largeRead() throws Exception {
    ByteString data = randomByteString(1000000);
    write(data);

    FileOperator operator = new FileOperator(randomAccessFile.getChannel());

    Buffer buffer = new Buffer();
    operator.read(0, buffer, data.size());
    assertEquals(data, buffer.readByteString());
  }

  @Test public void largeWrite() throws Exception {
    ByteString data = randomByteString(1000000);

    FileOperator operator = new FileOperator(randomAccessFile.getChannel());

    Buffer buffer = new Buffer().write(data);
    operator.write(0, buffer, data.size());

    assertEquals(data, snapshot());
  }

  @Test public void readBounds() throws Exception {
    FileOperator operator = new FileOperator(randomAccessFile.getChannel());
    Buffer buffer = new Buffer();
    try {
      operator.read(0, buffer, -1L);
      fail();
    } catch (IndexOutOfBoundsException expected) {
    }
  }

  @Test public void writeBounds() throws Exception {
    FileOperator operator = new FileOperator(randomAccessFile.getChannel());
    Buffer buffer = new Buffer().writeUtf8("abc");
    try {
      operator.write(0, buffer, -1L);
      fail();
    } catch (IndexOutOfBoundsException expected) {
    }
    try {
      operator.write(0, buffer, 4L);
      fail();
    } catch (IndexOutOfBoundsException expected) {
    }
  }

  private ByteString randomByteString(int byteCount) {
    byte[] bytes = new byte[byteCount];
    new Random(0).nextBytes(bytes);
    return ByteString.of(bytes);
  }

  private ByteString snapshot() throws IOException {
    randomAccessFile.getChannel().force(false);
    BufferedSource source = Okio.buffer(Okio.source(file));
    return source.readByteString();
  }

  private void write(ByteString data) throws IOException {
    BufferedSink sink = Okio.buffer(Okio.sink(file));
    sink.write(data);
    sink.close();
  }
}
