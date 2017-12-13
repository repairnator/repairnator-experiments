/**
 * File Created at 2011-11-25
 * $Id$
 * <p>
 * Copyright 2008 Alibaba.com Croporation Limited.
 * All rights reserved.
 * <p>
 * This software is the confidential and proprietary information of
 * Alibaba Company. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Alibaba.com.
 */
package com.alibaba.dubbo.rpc.protocol.thrift;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Protocol;
import com.alibaba.dubbo.rpc.gen.dubbo.$__DemoStub;
import com.alibaba.dubbo.rpc.gen.dubbo.Demo;
import com.alibaba.dubbo.rpc.protocol.thrift.ext.MultiServiceProcessor;

import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportFactory;
import org.junit.After;
import org.junit.Before;

/**
 * @author gang.lvg 2011-11-25 13:05
 */
public abstract class AbstractTest {

    static final int PORT = 30660;

    protected TServer server;

    protected Protocol protocol;

    protected Invoker<?> invoker;

    protected void init() throws Exception {
        TServerTransport serverTransport = new TServerSocket(PORT);

        TBinaryProtocol.Factory bFactory = new TBinaryProtocol.Factory();

        server = new TThreadPoolServer(
                new TThreadPoolServer.Args(serverTransport)
                        .inputProtocolFactory(bFactory)
                        .outputProtocolFactory(bFactory)
                        .inputTransportFactory(getTransportFactory())
                        .outputTransportFactory(getTransportFactory())
                        .processor(getProcessor()));

        Thread startTread = new Thread() {

            @Override
            public void run() {
                server.serve();
            }

        };

        startTread.setName("thrift-server");

        startTread.start();

        while (!server.isServing()) {
            Thread.sleep(100);
        }

        protocol = ExtensionLoader.getExtensionLoader(Protocol.class)
                .getExtension(ThriftProtocol.NAME);

        invoker = protocol.refer(getInterface(), getUrl());

    }

    protected void destroy() throws Exception {

        if (server != null) {
            server.stop();
            server = null;
        }

        if (protocol != null) {
            protocol.destroy();
            protocol = null;
        }

        if (invoker != null) {
            invoker.destroy();
            invoker = null;
        }

    }

    protected TTransportFactory getTransportFactory() {
        return new FramedTransportFactory();
    }

    protected $__DemoStub.Iface getServiceImpl() {
        return new DubboDemoImpl();
    }

    protected TProcessor getProcessor() {
        MultiServiceProcessor result = new MultiServiceProcessor();
        result.addProcessor(
                com.alibaba.dubbo.rpc.gen.dubbo.Demo.class,
                new $__DemoStub.Processor(getServiceImpl()));
        return result;
    }

    protected Class<?> getInterface() {
        return Demo.class;
    }

    protected URL getUrl() {
        return URL.valueOf(
                "thrift://127.0.0.1:" + PORT + "/" + getInterface().getName());
    }

    @After
    public void tearDown() throws Exception {
        destroy();
    }

    @Before
    public void setUp() throws Exception {
        init();
    }

}
