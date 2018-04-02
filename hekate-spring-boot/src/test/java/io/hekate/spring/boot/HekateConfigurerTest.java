/*
 * Copyright 2018 The Hekate Project
 *
 * The Hekate Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package io.hekate.spring.boot;

import io.hekate.cluster.event.ClusterEvent;
import io.hekate.cluster.event.ClusterEventType;
import io.hekate.cluster.event.ClusterJoinEvent;
import io.hekate.cluster.event.ClusterLeaveEvent;
import io.hekate.codec.CodecFactory;
import io.hekate.codec.CodecService;
import io.hekate.core.Hekate;
import io.hekate.core.service.Service;
import io.hekate.core.service.ServiceFactory;
import io.hekate.network.NetworkService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class HekateConfigurerTest extends HekateAutoConfigurerTestBase {
    @EnableAutoConfiguration
    public static class DefaultTestConfig extends HekateTestConfigBase {
        @Component
        public static class TestClusterListener {
            private final List<ClusterEvent> events = Collections.synchronizedList(new ArrayList<>());

            @EventListener
            public void onJoin(ClusterJoinEvent evt) {
                events.add(evt);
            }

            @EventListener
            public void onLeave(ClusterLeaveEvent evt) {
                events.add(evt);
            }
        }

        @Component
        public static class TestLifecycleListener implements Hekate.LifecycleListener {
            private final List<Hekate.State> states = Collections.synchronizedList(new ArrayList<>());

            @Override
            public void onStateChanged(Hekate changed) {
                states.add(changed.state());
            }
        }

        @Autowired
        private Hekate node;

        @Autowired
        private CodecService codecService;

        @Autowired
        private NetworkService networkService;
    }

    @EnableAutoConfiguration
    public static class DisableTestConfig {
        @Autowired(required = false)
        private Hekate node;
    }

    @Test
    public void testDefault() {
        registerAndRefresh(DefaultTestConfig.class);

        Hekate node = getNode();

        assertNotNull(node);
        assertSame(Hekate.State.UP, node.state());

        DefaultTestConfig app = get(DefaultTestConfig.class);

        assertNotNull(app.node);
        assertNotNull(app.codecService);
        assertNotNull(app.networkService);

        DefaultTestConfig.TestClusterListener clusterListener = get(DefaultTestConfig.TestClusterListener.class);
        DefaultTestConfig.TestLifecycleListener lifecycleListener = get(DefaultTestConfig.TestLifecycleListener.class);

        getContext().close();

        assertEquals(2, clusterListener.events.size());
        assertSame(ClusterEventType.JOIN, clusterListener.events.get(0).type());
        assertSame(ClusterEventType.LEAVE, clusterListener.events.get(1).type());

        assertEquals(lifecycleListener.states.toString(), 7, lifecycleListener.states.size());
        assertSame(Hekate.State.INITIALIZING, lifecycleListener.states.get(0));
        assertSame(Hekate.State.INITIALIZED, lifecycleListener.states.get(1));
        assertSame(Hekate.State.JOINING, lifecycleListener.states.get(2));
        assertSame(Hekate.State.UP, lifecycleListener.states.get(3));
        assertSame(Hekate.State.LEAVING, lifecycleListener.states.get(4));
        assertSame(Hekate.State.TERMINATING, lifecycleListener.states.get(5));
        assertSame(Hekate.State.DOWN, lifecycleListener.states.get(6));
    }

    @Test
    public void testDisabled() {
        registerAndRefresh(DisableTestConfig.class);

        assertTrue(getContext().getBeansOfType(Hekate.class).isEmpty());
        assertTrue(getContext().getBeansOfType(ServiceFactory.class).isEmpty());
        assertTrue(getContext().getBeansOfType(Service.class).isEmpty());
        assertTrue(getContext().getBeansOfType(CodecFactory.class).isEmpty());

        assertNull(get(DisableTestConfig.class).node);
    }
}
