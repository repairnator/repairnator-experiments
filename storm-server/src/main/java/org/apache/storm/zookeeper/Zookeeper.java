/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.storm.zookeeper;


import org.apache.commons.lang.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.curator.framework.recipes.leader.LeaderLatchListener;
import org.apache.curator.framework.recipes.leader.Participant;
import org.apache.storm.Config;
import org.apache.storm.blobstore.BlobStore;
import org.apache.storm.cluster.IStormClusterState;
import org.apache.storm.daemon.nimbus.TopoCache;
import org.apache.storm.nimbus.ILeaderElector;
import org.apache.storm.nimbus.LeaderListenerCallback;
import org.apache.storm.nimbus.NimbusInfo;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.server.NIOServerCnxnFactory;
import org.apache.zookeeper.server.ZooKeeperServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.BindException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;


public class Zookeeper {
    private static Logger LOG = LoggerFactory.getLogger(Zookeeper.class);

    // A singleton instance allows us to mock delegated static methods in our
    // tests by subclassing.
    private static final Zookeeper INSTANCE = new Zookeeper();
    private static Zookeeper _instance = INSTANCE;

    /**
     * Provide an instance of this class for delegates to use.  To mock out
     * delegated methods, provide an instance of a subclass that overrides the
     * implementation of the delegated method.
     *
     * @param u a Zookeeper instance
     */
    public static void setInstance(Zookeeper u) {
        _instance = u;
    }

    /**
     * Resets the singleton instance to the default. This is helpful to reset
     * the class to its original functionality when mocking is no longer
     * desired.
     */
    public static void resetInstance() {
        _instance = INSTANCE;
    }

    public static List mkInprocessZookeeper(String localdir, Integer port) throws Exception {
        File localfile = new File(localdir);
        ZooKeeperServer zk = new ZooKeeperServer(localfile, localfile, 2000);
        NIOServerCnxnFactory factory = null;
        int report = 2000;
        int limitPort = 65535;
        if (port != null) {
            report = port;
            limitPort = port;
        }
        while (true) {
            try {
                factory = new NIOServerCnxnFactory();
                factory.configure(new InetSocketAddress(report), 0);
                break;
            } catch (BindException e) {
                report++;
                if (report > limitPort) {
                    throw new RuntimeException("No port is available to launch an inprocess zookeeper");
                }
            }
        }
        LOG.info("Starting inprocess zookeeper at port {} and dir {}", report, localdir);
        factory.startup(zk);
        return Arrays.asList((Object) new Long(report), (Object) factory);
    }

    public static void shutdownInprocessZookeeper(NIOServerCnxnFactory handle) {
        handle.shutdown();
    }

    public static NimbusInfo toNimbusInfo(Participant participant) {
        String id = participant.getId();
        if (StringUtils.isBlank(id)) {
            throw new RuntimeException("No nimbus leader participant host found, have you started your nimbus hosts?");
        }
        NimbusInfo nimbusInfo = NimbusInfo.parse(id);
        nimbusInfo.setLeader(participant.isLeader());
        return nimbusInfo;
    }

    // Leader latch listener that will be invoked when we either gain or lose leadership
    public static LeaderLatchListener leaderLatchListenerImpl(final LeaderListenerCallback callback)
        throws UnknownHostException {
        final String hostName = InetAddress.getLocalHost().getCanonicalHostName();
        return new LeaderLatchListener() {

            @Override
            public void isLeader() {
                callback.leaderCallBack();
                LOG.info("{} gained leadership.", hostName);
            }

            @Override
            public void notLeader() {
                LOG.info("{} lost leadership.", hostName);
                //Just to be sure
                callback.notLeaderCallback();
            }
        };
    }

    /**
     * Get master leader elector.
     * @param conf Config.
     * @param zkClient ZkClient, the client must have a default Config.STORM_ZOOKEEPER_ROOT as root path.
     * @param blobStore {@link BlobStore}
     * @param tc {@link TopoCache}
     * @param clusterState {@link IStormClusterState}
     * @param acls ACLs
     * @return Instance of {@link ILeaderElector}
     * @throws UnknownHostException
     */
    public static ILeaderElector zkLeaderElector(Map<String, Object> conf, CuratorFramework zkClient, BlobStore blobStore,
        final TopoCache tc, IStormClusterState clusterState, List<ACL> acls) throws UnknownHostException {
        return _instance.zkLeaderElectorImpl(conf, zkClient, blobStore, tc, clusterState, acls);
    }

    protected ILeaderElector zkLeaderElectorImpl(Map<String, Object> conf, CuratorFramework zk, BlobStore blobStore,
        final TopoCache tc, IStormClusterState clusterState, List<ACL> acls) throws UnknownHostException {
        List<String> servers = (List<String>) conf.get(Config.STORM_ZOOKEEPER_SERVERS);
        String leaderLockPath = "/leader-lock";
        String id = NimbusInfo.fromConf(conf).toHostPortString();
        AtomicReference<LeaderLatch> leaderLatchAtomicReference = new AtomicReference<>(new LeaderLatch(zk, leaderLockPath, id));
        AtomicReference<LeaderLatchListener> leaderLatchListenerAtomicReference =
                new AtomicReference<>(leaderLatchListenerImpl(new LeaderListenerCallback(conf, zk, leaderLatchAtomicReference.get(), blobStore, tc, clusterState, acls)));
        return new LeaderElectorImp(conf, servers, zk, leaderLockPath, id, leaderLatchAtomicReference,
            leaderLatchListenerAtomicReference, blobStore, tc, clusterState, acls);
    }

}
