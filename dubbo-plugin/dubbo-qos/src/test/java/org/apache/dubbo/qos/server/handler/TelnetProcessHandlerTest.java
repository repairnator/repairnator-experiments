/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.dubbo.qos.server.handler;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TelnetProcessHandlerTest {
    @Test
    public void testPrompt() throws Exception {
        ChannelHandlerContext context = mock(ChannelHandlerContext.class);
        TelnetProcessHandler handler = new TelnetProcessHandler();
        handler.channelRead0(context, "");
        verify(context).writeAndFlush(QosProcessHandler.prompt);
    }

    @Test
    public void testBye() throws Exception {
        ChannelHandlerContext context = mock(ChannelHandlerContext.class);
        TelnetProcessHandler handler = new TelnetProcessHandler();
        ChannelFuture future = mock(ChannelFuture.class);
        when(context.writeAndFlush("BYE!\n")).thenReturn(future);
        handler.channelRead0(context, "quit");
        verify(future).addListener(ChannelFutureListener.CLOSE);
    }

    @Test
    public void testUnknownCommand() throws Exception {
        ChannelHandlerContext context = mock(ChannelHandlerContext.class);
        TelnetProcessHandler handler = new TelnetProcessHandler();
        handler.channelRead0(context, "unknown");
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(context, Mockito.atLeastOnce()).writeAndFlush(captor.capture());
        assertThat(captor.getAllValues(), contains("unknown :no such command", "\r\ndubbo>"));
    }

    @Test
    public void testGreeting() throws Exception {
        ChannelHandlerContext context = mock(ChannelHandlerContext.class);
        TelnetProcessHandler handler = new TelnetProcessHandler();
        handler.channelRead0(context, "greeting");
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(context).writeAndFlush(captor.capture());
        assertThat(captor.getValue(), containsString("greeting"));
        assertThat(captor.getValue(), containsString("dubbo>"));
    }
}
