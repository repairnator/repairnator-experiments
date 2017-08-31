package com.cedarsoftware.util;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.fail;

/**
 * @author John DeRegnaucourt (john@cedarsoftware.com)
 *         <br>
 *         Copyright (c) Cedar Software LLC
 *         <br><br>
 *         Licensed under the Apache License, Version 2.0 (the "License");
 *         you may not use this file except in compliance with the License.
 *         You may obtain a copy of the License at
 *         <br><br>
 *         http://www.apache.org/licenses/LICENSE-2.0
 *         <br><br>
 *         Unless required by applicable law or agreed to in writing, software
 *         distributed under the License is distributed on an "AS IS" BASIS,
 *         WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *         See the License for the specific language governing permissions and
 *         limitations under the License.
 */
public class TestFastByteArrayBuffer
{
    @Test
    public void testSimple() throws IOException
    {
        FastByteArrayOutputStream fbaos = new FastByteArrayOutputStream();
        byte[] originalBuffer = fbaos.buffer;
        String hello = "Hello, world.";
        fbaos.write(hello.getBytes());

        byte[] content = fbaos.getBuffer();

        String content2 = new String(content, 0, fbaos.size);
        assert content2.equals(hello);
        assert content == fbaos.buffer; // same address as internal buffer
        assert content == originalBuffer;
        assert content.length == 1024;    // started at 1024
    }

    @Test
    public void testSimple2() throws IOException
    {
        FastByteArrayOutputStream fbaos = new FastByteArrayOutputStream(1, 2);
        byte[] originalBuffer = fbaos.buffer;
        String hello = "Hello, world.";
        fbaos.write(hello.getBytes());

        byte[] content = fbaos.getBuffer();

        String content2 = new String(content, 0, fbaos.size);
        assert content2.equals(hello);
        assert content == fbaos.buffer; // same address as internal buffer
        assert content != originalBuffer;
        assert content.length == 13;    // started at 1, +2 until finished.
    }

    @Test
    public void testDouble() throws IOException
    {
        FastByteArrayOutputStream fbaos = new FastByteArrayOutputStream(10);
        byte[] originalBuffer = fbaos.buffer;
        String hello = "Hello, world.";
        fbaos.write(hello.getBytes());

        byte[] content = fbaos.getBuffer();

        String content2 = new String(content, 0, fbaos.size);
        assert content2.equals(hello);
        assert content == fbaos.buffer; // same address as internal buffer
        assert content != originalBuffer;
        assert content.length == 20;    // started at 1, +2 until finished.
    }

    @Test
    public void testEdgeCase()
    {
        try
        {
            FastByteArrayOutputStream fbaos = new FastByteArrayOutputStream(0);
            fail();
        }
        catch (Exception e)
        {
            assert e instanceof IllegalArgumentException;
        }
    }
}
