/*
 * Copyright 1999-2011 Alibaba Group.
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
package com.alibaba.dubbo.remoting.handler;

import com.alibaba.dubbo.common.utils.ConcurrentHashSet;
import com.alibaba.dubbo.remoting.Channel;
import com.alibaba.dubbo.remoting.ChannelHandler;
import com.alibaba.dubbo.remoting.RemotingException;

import java.util.Collections;
import java.util.Set;

/**
 * @author chao.liuc
 */
public class MockedChannelHandler implements ChannelHandler {
    //    ConcurrentMap<String, Channel> channels = new ConcurrentHashMap<String, Channel>();
    ConcurrentHashSet<Channel> channels = new ConcurrentHashSet<Channel>();

    public void connected(Channel channel) throws RemotingException {
        channels.add(channel);
    }

    public void disconnected(Channel channel) throws RemotingException {
        channels.remove(channel);
    }

    public void sent(Channel channel, Object message) throws RemotingException {
        channel.send(message);
    }

    public void received(Channel channel, Object message) throws RemotingException {
        //echo 
        channel.send(message);
    }

    public void caught(Channel channel, Throwable exception) throws RemotingException {
        throw new RemotingException(channel, exception);

    }

    public Set<Channel> getChannels() {
        return Collections.unmodifiableSet(channels);
    }
}