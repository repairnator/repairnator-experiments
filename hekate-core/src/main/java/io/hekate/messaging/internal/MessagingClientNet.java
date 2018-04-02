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

package io.hekate.messaging.internal;

import io.hekate.cluster.ClusterNode;
import io.hekate.messaging.unicast.SendCallback;
import io.hekate.network.NetworkClient;
import io.hekate.network.NetworkConnector;
import io.hekate.network.NetworkFuture;
import io.hekate.util.format.ToString;
import io.hekate.util.format.ToStringIgnore;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class MessagingClientNet<T> implements MessagingClient<T> {
    private static final Logger log = LoggerFactory.getLogger(MessagingClientNet.class);

    private static final boolean DEBUG = log.isDebugEnabled();

    private static final int STATE_DISCONNECTED = 1;

    private static final int STATE_CONNECTED = 2;

    private static final int STATE_IDLE = 3;

    private static final int STATE_CLOSED = 4;

    private final String channelName;

    private final ClusterNode node;

    private final MessagingConnectionNetOut<T> conn;

    private final boolean trackIdle;

    @ToStringIgnore
    private final Object mux = new Object();

    private volatile int state = STATE_DISCONNECTED;

    public MessagingClientNet(String channelName, ClusterNode remoteNode, NetworkConnector<MessagingProtocol> net,
        MessagingGatewayContext<T> ctx, boolean trackIdle) {
        assert channelName != null : "Channel name is null.";
        assert remoteNode != null : "Remote node is null.";
        assert net != null : "Network connector is null.";
        assert ctx != null : "Messaging context is null.";

        if (DEBUG) {
            log.debug("Creating new connection [channel={}, node={}]", channelName, remoteNode);
        }

        this.channelName = channelName;
        this.node = remoteNode;
        this.trackIdle = trackIdle;

        NetworkClient<MessagingProtocol> netClient = net.newClient();

        DefaultMessagingEndpoint<T> endpoint = new DefaultMessagingEndpoint<>(remoteNode.id(), ctx.channel());

        this.conn = new MessagingConnectionNetOut<>(remoteNode.address(), netClient, ctx, endpoint, mux, () -> {
            // On internal disconnect:
            synchronized (mux) {
                if (state != STATE_CLOSED) {
                    state = STATE_DISCONNECTED;
                }
            }
        });
    }

    @Override
    public ClusterNode node() {
        return node;
    }

    @Override
    public void send(MessageRoute<T> route, SendCallback callback, boolean retransmit) {
        ensureConnected();

        conn.sendNotification(route, callback, retransmit);
    }

    @Override
    public void stream(MessageRoute<T> route, InternalRequestCallback<T> callback, boolean retransmit) {
        ensureConnected();

        conn.stream(route, callback, retransmit);
    }

    @Override
    public void request(MessageRoute<T> route, InternalRequestCallback<T> callback, boolean retransmit) {
        ensureConnected();

        conn.request(route, callback, retransmit);
    }

    @Override
    public List<NetworkFuture<MessagingProtocol>> close() {
        if (DEBUG) {
            log.debug("Closing connection [channel={}, node={}]", channelName, node);
        }

        synchronized (mux) {
            // Mark as closed.
            state = STATE_CLOSED;

            return Collections.singletonList(conn.disconnect());
        }
    }

    @Override
    public boolean isConnected() {
        synchronized (mux) {
            return state == STATE_CONNECTED || state == STATE_IDLE;
        }
    }

    @Override
    public void disconnectIfIdle() {
        if (trackIdle) {
            if (state == STATE_CONNECTED) {
                if (!conn.hasPendingRequests()) {
                    synchronized (mux) {
                        // Double check with lock.
                        if (state == STATE_CONNECTED && !conn.hasPendingRequests()) {
                            state = STATE_IDLE;
                        }
                    }
                }
            } else if (state == STATE_IDLE) {
                synchronized (mux) {
                    // Double check with lock.
                    if (state == STATE_IDLE) {
                        if (conn.hasPendingRequests()) {
                            state = STATE_CONNECTED;
                        } else {
                            if (DEBUG) {
                                log.debug("Disconnecting idle connection [chanel={}, node={}]", channelName, node);
                            }

                            state = STATE_DISCONNECTED;

                            conn.disconnect();
                        }
                    }
                }
            }
        }
    }

    @Override
    public void touch() {
        if (trackIdle && state == STATE_IDLE) {
            synchronized (mux) {
                if (state == STATE_IDLE) {
                    state = STATE_CONNECTED;
                }
            }
        }
    }

    private void ensureConnected() {
        if (state != STATE_CONNECTED) {
            synchronized (mux) {
                // Double check with lock.
                if (state == STATE_DISCONNECTED) {
                    if (DEBUG) {
                        log.debug("Initializing connection [chanel={}, node={}]", channelName, node);
                    }

                    // Important to connect before updating the 'state' flag.
                    // Otherwise the double check logic will be broken and concurrent threads
                    // will try to access the client while it is not connected yet.
                    conn.connect();

                    state = STATE_CONNECTED;
                } else if (state == STATE_IDLE) {
                    state = STATE_CONNECTED;
                }
            }
        }
    }

    @Override
    public String toString() {
        return ToString.format(this);
    }
}
