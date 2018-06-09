/*
 * Copyright 2016-present Open Networking Foundation
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
package io.atomix.core.election.impl;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import io.atomix.core.election.AsyncLeaderElector;
import io.atomix.core.election.LeaderElector;
import io.atomix.core.election.Leadership;
import io.atomix.core.election.LeadershipEvent;
import io.atomix.core.election.LeadershipEventListener;
import io.atomix.primitive.AbstractAsyncPrimitiveProxy;
import io.atomix.primitive.PrimitiveRegistry;
import io.atomix.primitive.proxy.ProxyClient;
import io.atomix.primitive.proxy.Proxy;

import java.time.Duration;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * Distributed resource providing the {@link AsyncLeaderElector} primitive.
 */
public class LeaderElectorProxy
    extends AbstractAsyncPrimitiveProxy<AsyncLeaderElector<byte[]>, LeaderElectorService>
    implements AsyncLeaderElector<byte[]>, LeaderElectorClient {

  private final Set<LeadershipEventListener<byte[]>> leadershipChangeListeners = Sets.newCopyOnWriteArraySet();
  private final Map<String, Set<LeadershipEventListener<byte[]>>> topicListeners = Maps.newConcurrentMap();

  public LeaderElectorProxy(ProxyClient proxy, PrimitiveRegistry registry) {
    super(LeaderElectorService.class, proxy, registry);
  }

  @Override
  public void onLeadershipChange(String topic, Leadership<byte[]> oldLeadership, Leadership<byte[]> newLeadership) {
    LeadershipEvent<byte[]> event = new LeadershipEvent<byte[]>(LeadershipEvent.Type.CHANGE, topic, oldLeadership, newLeadership);
    leadershipChangeListeners.forEach(l -> l.onEvent(event));
    Set<LeadershipEventListener<byte[]>> listenerSet = topicListeners.get(topic);
    if (listenerSet != null) {
      listenerSet.forEach(l -> l.onEvent(event));
    }
  }

  @Override
  public CompletableFuture<Leadership<byte[]>> run(String topic, byte[] id) {
    return applyBy(topic, service -> service.run(topic, id));
  }

  @Override
  public CompletableFuture<Void> withdraw(String topic, byte[] id) {
    return acceptBy(topic, service -> service.withdraw(topic, id));
  }

  @Override
  public CompletableFuture<Boolean> anoint(String topic, byte[] id) {
    return applyBy(topic, service -> service.anoint(topic, id));
  }

  @Override
  public CompletableFuture<Boolean> promote(String topic, byte[] id) {
    return applyBy(topic, service -> service.promote(topic, id));
  }

  @Override
  public CompletableFuture<Void> evict(byte[] id) {
    return acceptAll(service -> service.evict(id));
  }

  @Override
  public CompletableFuture<Leadership<byte[]>> getLeadership(String topic) {
    return applyBy(topic, service -> service.getLeadership(topic));
  }

  @Override
  public CompletableFuture<Map<String, Leadership<byte[]>>> getLeaderships() {
    return applyAll(service -> service.getLeaderships())
        .thenApply(leaderships -> {
          ImmutableMap.Builder<String, Leadership<byte[]>> builder = ImmutableMap.builder();
          leaderships.forEach(builder::putAll);
          return builder.build();
        });
  }

  @Override
  public synchronized CompletableFuture<Void> addListener(LeadershipEventListener<byte[]> listener) {
    leadershipChangeListeners.add(listener);
    return acceptAll(service -> service.listen());
  }

  @Override
  public synchronized CompletableFuture<Void> removeListener(LeadershipEventListener<byte[]> listener) {
    if (leadershipChangeListeners.remove(listener)) {
      return acceptAll(service -> service.unlisten());
    }
    return CompletableFuture.completedFuture(null);
  }

  @Override
  public synchronized CompletableFuture<Void> addListener(String topic, LeadershipEventListener<byte[]> listener) {
    boolean empty = topicListeners.isEmpty();
    topicListeners.compute(topic, (t, s) -> {
      if (s == null) {
        s = Sets.newCopyOnWriteArraySet();
      }
      s.add(listener);
      return s;
    });

    if (empty) {
      return acceptBy(topic, service -> service.listen());
    }
    return CompletableFuture.completedFuture(null);
  }

  @Override
  public synchronized CompletableFuture<Void> removeListener(String topic, LeadershipEventListener<byte[]> listener) {
    topicListeners.computeIfPresent(topic, (t, s) -> {
      s.remove(listener);
      return s.size() == 0 ? null : s;
    });
    if (topicListeners.isEmpty()) {
      return acceptBy(topic, service -> service.unlisten());
    }
    return CompletableFuture.completedFuture(null);
  }

  private boolean isListening() {
    return !topicListeners.isEmpty();
  }

  @Override
  public CompletableFuture<AsyncLeaderElector<byte[]>> connect() {
    return super.connect()
        .thenRun(() -> {
          addStateChangeListeners((partition, state) -> {
            if (state == Proxy.State.CONNECTED && isListening()) {
              acceptOn(partition, service -> service.listen());
            }
          });
        })
        .thenApply(v -> this);
  }

  @Override
  public LeaderElector<byte[]> sync(Duration operationTimeout) {
    return new BlockingLeaderElector<>(this, operationTimeout.toMillis());
  }
}