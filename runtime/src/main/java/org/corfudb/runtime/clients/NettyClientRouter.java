package org.corfudb.runtime.clients;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Gauge;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.ssl.SslContext;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.Nonnull;
import javax.net.ssl.SSLException;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.corfudb.protocols.wireprotocol.CorfuMsg;
import org.corfudb.protocols.wireprotocol.CorfuMsgType;
import org.corfudb.protocols.wireprotocol.NettyCorfuMessageDecoder;
import org.corfudb.protocols.wireprotocol.NettyCorfuMessageEncoder;
import org.corfudb.runtime.CorfuRuntime;
import org.corfudb.runtime.CorfuRuntime.CorfuRuntimeParameters;
import org.corfudb.runtime.exceptions.NetworkException;
import org.corfudb.runtime.exceptions.unrecoverable.UnrecoverableCorfuError;
import org.corfudb.runtime.exceptions.unrecoverable.UnrecoverableCorfuInterruptedError;
import org.corfudb.runtime.exceptions.WrongEpochException;
import org.corfudb.security.sasl.SaslUtils;
import org.corfudb.security.sasl.plaintext.PlainTextSaslNettyClient;
import org.corfudb.security.tls.SslContextConstructor;
import org.corfudb.util.CFUtils;
import org.corfudb.util.MetricsUtils;
import org.corfudb.util.NodeLocator;


/**
 * A client router which multiplexes operations over the Netty transport.
 *
 * <p>Created by mwei on 12/8/15.
 */
@Slf4j
@ChannelHandler.Sharable
public class NettyClientRouter extends SimpleChannelInboundHandler<CorfuMsg>
        implements IClientRouter {

    /**
     * Metrics: meter (counter), histogram.
     */
    private Gauge<Integer> gaugeConnected;
    private Timer timerConnect;
    private Timer timerSyncOp;
    private Counter counterConnectFailed;
    private Counter counterSendDisconnected;
    private Counter counterSendTimeout;
    private Counter counterAsyncOpSent;

    /**
     * A random instance.
     */
    public static final Random random = new Random();
    /**
     * The epoch this router is in.
     */
    @Getter
    public long epoch;

    /**
     * We should never set epoch backwards
     *
     * @param epoch
     */
    public void setEpoch(long epoch){
        if (epoch < this.epoch) {
            log.warn("setEpoch: Rejected attempt to set the "
                    + "router {} to epoch {} smaller than current epoch {}",
                    node, epoch, this.epoch);
            return;
        }
        this.epoch = epoch;
    }

    /**
     * New connection timeout (milliseconds).
     */
    @Getter
    @Setter
    public long timeoutConnect;

    /**
     * Sync call response timeout (milliseconds).
     */
    @Getter
    @Setter
    public long timeoutResponse;

    /**
     * Retry interval after timeout (milliseconds).
     */
    @Getter
    @Setter
    public long timeoutRetry;

    /**
     * The current request ID.
     */
    @Getter
    @Deprecated // TODO: Add replacement method that conforms to style
    @SuppressWarnings("checkstyle:abbreviation") // Due to deprecation
    public AtomicLong requestID;
    /**
     * The handlers registered to this router.
     */
    public Map<CorfuMsgType, IClient> handlerMap;
    /**
     * The clients registered to this router.
     */
    public List<IClient> clientList;
    /**
     * The outstanding requests on this router.
     */
    public Map<Long, CompletableFuture> outstandingRequests;
    /**
     * The currently registered channel context.
     */
    public ChannelHandlerContext context;
    /**
     * The currently registered channel.
     */
    public Channel channel = null;
    /**
     * The worker group for this router.
     */
    public EventLoopGroup workerGroup;
    /**
     * The event executor group for this router.
     */
    public EventExecutorGroup ee;
    /**
     * Whether or not this router is shutdown.
     */
    public volatile boolean shutdown;

    @Getter
    final NodeLocator node;

    private final CorfuRuntimeParameters parameters;

    @Deprecated
    @Override
    public Integer getPort() {
        return node.getPort();
    }

    @Deprecated
    public String getHost() {
        return node.getHost();
    }

    /**
     * Flag, if we are connected.
     */
    @Getter
    volatile Boolean connected;

    private SslContext sslContext;
    /**
     * Creates a new NettyClientRouter connected to the specified endpoint.
     *
     * @param endpoint Endpoint to connect to.
     */
    @Deprecated
    public NettyClientRouter(String endpoint) {
        this(endpoint.split(":")[0], Integer.parseInt(endpoint.split(":")[1]));
    }

    /**
     * Creates a new NettyClientRouter connected to the specified host and port.
     *
     * @param host Host to connect to.
     * @param port Port to connect to.
     */
    @Deprecated
    public NettyClientRouter(String host, Integer port) {
        this(NodeLocator.builder().host(host).port(port).build(),
             CorfuRuntimeParameters.builder().build());
    }

    /**
     * Creates a new NettyClientRouter connected to the specified host and port.
     *
     * @param host Host to connect to.
     * @param port Port to connect to.
     */
    @Deprecated
    public NettyClientRouter(String host, Integer port, Boolean tls,
        String keyStore, String ksPasswordFile, String trustStore,
        String tsPasswordFile, Boolean saslPlainText, String usernameFile,
        String passwordFile) {
        this(NodeLocator.builder().host(host).port(port).build(),
            CorfuRuntimeParameters.builder()
                .tlsEnabled(tls)
                .keyStore(keyStore)
                .ksPasswordFile(ksPasswordFile)
                .trustStore(trustStore)
                .tsPasswordFile(tsPasswordFile)
                .saslPlainTextEnabled(saslPlainText)
                .usernameFile(usernameFile)
                .passwordFile(passwordFile)
                .build());
    }

    /**
     * Creates a new NettyClientRouter connected to the specified host and port with the
     * specified tls and sasl options.
     *
     * @param node           The node to connect to.
     * @param parameters     A {@link CorfuRuntimeParameters} with the desired configuration.
     */
    public NettyClientRouter(@Nonnull NodeLocator node,
                             @Nonnull CorfuRuntimeParameters parameters) {
        this.node = node;
        this.parameters = parameters;

        connected = false;
        timeoutConnect = parameters.getConnectionTimeout().toMillis();
        timeoutResponse = parameters.getRequestTimeout().toMillis();
        timeoutRetry = parameters.getConnectionRetryRate().toMillis();

        handlerMap = new ConcurrentHashMap<>();
        clientList = new ArrayList<>();
        requestID = new AtomicLong();
        outstandingRequests = new ConcurrentHashMap<>();
        shutdown = true;

        MetricRegistry metrics = CorfuRuntime.getDefaultMetrics();

        String pfx = CorfuRuntime.getMpCR() + node + ".";
        synchronized (metrics) {
            if (!metrics.getNames().contains(pfx + "connected")) {
                gaugeConnected = metrics.register(pfx + "connected", () -> connected ? 1 : 0);
            }
        }
        timerConnect = metrics.timer(pfx + "connect");
        timerSyncOp = metrics.timer(pfx + "sync-op");
        counterConnectFailed = metrics.counter(pfx + "connect-failed");
        counterSendDisconnected = metrics.counter(pfx + "send-disconnected");
        counterSendTimeout = metrics.counter(pfx + "send-timeout");
        counterAsyncOpSent = metrics.counter(pfx + "async-op-sent");

        if (parameters.isTlsEnabled()) {
            try {
                sslContext = SslContextConstructor.constructSslContext(false,
                        parameters.getKeyStore(),
                        parameters.getKsPasswordFile(),
                        parameters.getTrustStore(),
                        parameters.getTsPasswordFile());
            } catch (SSLException e) {
                throw new UnrecoverableCorfuError(e);
            }
        }

        addClient(new BaseClient());
        start();
    }

    /**
     * Add a new client to the router.
     *
     * @param client The client to add to the router.
     * @return This NettyClientRouter, to support chaining and the builder pattern.
     */
    public IClientRouter addClient(IClient client) {
        // Set the client's router to this instance.
        client.setRouter(this);

        // Iterate through all types of CorfuMsgType, registering the handler
        client.getHandledTypes().stream()
                .forEach(x -> {
                    handlerMap.put(x, client);
                    log.trace("Registered {} to handle messages of type {}", client, x);
                });

        // Register this type
        clientList.add(client);
        return this;
    }

    /**
     * Gets a client that matches a particular type.
     *
     * @param clientType The class of the client to match.
     * @param <T>        The type of the client to match.
     * @return The first client that matches that type.
     * @throws NoSuchElementException If there are no clients matching that type.
     */
    @SuppressWarnings("unchecked")
    public <T extends IClient> T getClient(Class<T> clientType) {
        return (T) clientList.stream()
                .filter(clientType::isInstance)
                .findFirst().get();
    }

    public void start() {
        start(-1);
    }

    /**
     * Initiates the connection and starts the netty client router.
     *
     * @param c Server startup code.
     */
    public void start(long c) {
        shutdown = false;
        if (workerGroup == null
                || workerGroup.isShutdown()
                || !channel.isOpen()) {
            workerGroup = new NioEventLoopGroup(Runtime.getRuntime()
                    .availableProcessors() * 2, new ThreadFactory() {
                        final AtomicInteger threadNum = new AtomicInteger(0);

                        @Override
                        public Thread newThread(Runnable r) {
                            Thread t = new Thread(r);
                            t.setName("worker-" + threadNum.getAndIncrement());
                            t.setDaemon(true);
                            return t;
                        }
                    });

            ee = new DefaultEventExecutorGroup(Runtime.getRuntime()
                    .availableProcessors() * 2, new ThreadFactory() {

                        final AtomicInteger threadNum = new AtomicInteger(0);

                        @Override
                        public Thread newThread(Runnable r) {
                            Thread t = new Thread(r);
                            t.setName(this.getClass().getName() + "event-"
                                    + threadNum.getAndIncrement());
                            t.setDaemon(true);
                            return t;
                        }
                    });

            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.option(ChannelOption.SO_REUSEADDR, true);
            b.option(ChannelOption.TCP_NODELAY, true);
            NettyClientRouter router = this;
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    if (parameters.isTlsEnabled()) {
                        ch.pipeline().addLast("ssl", sslContext.newHandler(ch.alloc()));
                    }
                    ch.pipeline().addLast(new LengthFieldPrepender(4));
                    ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,
                            0, 4, 0,
                            4));
                    if (parameters.isSaslPlainTextEnabled()) {
                        PlainTextSaslNettyClient saslNettyClient =
                                SaslUtils.enableSaslPlainText(parameters.getUsernameFile(),
                                        parameters.getPasswordFile());
                        ch.pipeline().addLast("sasl/plain-text", saslNettyClient);
                    }
                    ch.pipeline().addLast(ee, new NettyCorfuMessageDecoder());
                    ch.pipeline().addLast(ee, new NettyCorfuMessageEncoder());
                    ch.pipeline().addLast(ee, router);
                }
            });

            try {
                connectChannel(b, c);
            } catch (Exception e) {

                try {
                    // shutdown EventLoopGroup
                    workerGroup.shutdownGracefully().sync();
                } catch (InterruptedException ie) {
                    throw new UnrecoverableCorfuInterruptedError(
                        "Interrupted while shutting down", ie);
                }
                throw new NetworkException(e.getClass().getSimpleName()
                        + " connecting to endpoint failed", node, e);
            }
        }
    }

    synchronized void connectChannel(Bootstrap b, long c) {
        boolean isEnabled = MetricsUtils.isMetricsCollectionEnabled();
        try (Timer.Context context = MetricsUtils.getConditionalContext(isEnabled, timerConnect)) {
            ChannelFuture cf = b.connect(node.getHost(), node.getPort());
            cf.syncUninterruptibly();
            if (!cf.awaitUninterruptibly(timeoutConnect)) {
                cf.channel().close();   // close port
//                MetricsUtils.incConditionalCounter(isEnabled, counterConnectFailed, 1);
                throw new NetworkException(c + " Timeout connecting to endpoint", node);
            }
            channel = cf.channel();
        }
        channel.closeFuture().addListener((r) -> {
            connected = false;
            outstandingRequests.forEach((reqId, reqCompletableFuture) -> {
//                MetricsUtils.incConditionalCounter(isEnabled, counterSendDisconnected, 1);
                reqCompletableFuture.completeExceptionally(new NetworkException("Disconnected",
                    node));
                outstandingRequests.remove(reqId);
            });
            if (!shutdown) {
                log.trace("Disconnected, reconnecting...");
                while (!shutdown) {
                    try {
                        connectChannel(b, c);
                        return;
                    } catch (Exception ex) {
//                        MetricsUtils.incConditionalCounter(isEnabled,
//                                counterConnectFailed, 1);
                        log.warn("Exception while reconnecting, retry in {} ms", timeoutRetry);
                        Thread.sleep(timeoutRetry);
                    }
                }
            }
        });
        connected = true;
    }

    /**
     * Stops routing requests.
     */
    @Override
    public void stop() {
        stop(false);
    }

    @Override
    public void stop(boolean shutdown) {
        // A very hasty check of Netty state-of-the-art is that shutting down
        // the worker threads is tricksy or impossible.
        this.shutdown = shutdown;
        connected = false;

        if (shutdown) {
            try {
                ChannelFuture cf = channel.close();
                cf.syncUninterruptibly();
                cf.awaitUninterruptibly(1000);
            } catch (Exception e) {
                log.error("Error in closing channel");
            }
            try {
                ee.shutdownGracefully().sync();
                workerGroup.shutdownGracefully().sync();
            } catch (InterruptedException e) {
                throw new UnrecoverableCorfuInterruptedError("Interrupted while stopping", e);
            }
        } else {
            ChannelFuture cf = channel.disconnect();
            cf.syncUninterruptibly();
            boolean b1 = cf.awaitUninterruptibly(1000);
        }
    }

    /**
     * Send a message and get a completable future to be fulfilled by the reply.
     *
     * @param ctx     The channel handler context to send the message under.
     * @param message The message to send.
     * @param <T>     The type of completable to return.
     * @return A completable future which will be fulfilled by the reply,
     *     or a timeout in the case there is no response.
     */
    public <T> CompletableFuture<T> sendMessageAndGetCompletable(ChannelHandlerContext ctx,
                                                                 CorfuMsg message) {
        boolean isEnabled = MetricsUtils.isMetricsCollectionEnabled();
        if (!connected) {
            log.trace("Disconnected endpoint {}", node);
            MetricsUtils.incConditionalCounter(isEnabled, counterSendDisconnected, 1);
            throw new NetworkException("Disconnected endpoint", node);
        } else {
            final Timer.Context context = MetricsUtils
                    .getConditionalContext(isEnabled, timerSyncOp);
            // Get the next request ID.
            final long thisRequest = requestID.getAndIncrement();
            // Set the message fields.
            message.setClientID(parameters.getClientId());
            message.setRequestID(thisRequest);
            message.setEpoch(epoch);

            // Generate a future and put it in the completion table.
            final CompletableFuture<T> cf = new CompletableFuture<>();
            outstandingRequests.put(thisRequest, cf);
            // Write the message out to the channel.
            if (ctx == null) {
                channel.writeAndFlush(message, channel.voidPromise());
            } else {
                ctx.writeAndFlush(message, ctx.voidPromise());
            }
            log.trace("Sent message: {}", message);
            final CompletableFuture<T> cfElapsed = cf.thenApply(x -> {
                MetricsUtils.stopConditionalContext(context);
                return x;
            });
            // Generate a timeout future, which will complete exceptionally
            // if the main future is not completed.
            final CompletableFuture<T> cfTimeout =
                    CFUtils.within(cfElapsed, Duration.ofMillis(timeoutResponse));
            cfTimeout.exceptionally(e -> {
//                MetricsUtils.incConditionalCounter(isEnabled, counterSendTimeout, 1);
                outstandingRequests.remove(thisRequest);
                log.debug("Remove request {} due to timeout!", thisRequest);
                return null;
            });
            return cfTimeout;
        }
    }

    /**
     * Send a one way message, without adding a completable future.
     *
     * @param ctx     The context to send the message under.
     * @param message The message to send.
     */
    public void sendMessage(ChannelHandlerContext ctx, CorfuMsg message) {
        ChannelHandlerContext outContext = context;
        if (ctx == null) {
            if (context == null) {
                // if the router's context is not set, return a failure
                log.warn("Attempting to send on a channel that is not ready.");
                return;
            }
            outContext = context;
        }
        // Get the next request ID.
        final long thisRequest = requestID.getAndIncrement();
        // Set the base fields for this message.
        message.setClientID(parameters.getClientId());
        message.setRequestID(thisRequest);
        message.setEpoch(epoch);
        // Write this message out on the channel.
        outContext.writeAndFlush(message, outContext.voidPromise());
//        MetricsUtils.incConditionalCounter(MetricsUtils
//                .isMetricsCollectionEnabled(), counterAsyncOpSent, 1);
        log.trace("Sent one-way message: {}", message);
    }


    /**
     * Send a netty message through this router, setting the fields in the outgoing message.
     *
     * @param ctx    Channel handler context to use.
     * @param inMsg  Incoming message to respond to.
     * @param outMsg Outgoing message.
     */
    public void sendResponseToServer(ChannelHandlerContext ctx, CorfuMsg inMsg, CorfuMsg outMsg) {
        outMsg.copyBaseFields(inMsg);
        outMsg.setEpoch(epoch);
        ctx.writeAndFlush(outMsg, ctx.voidPromise());
        log.trace("Sent response: {}", outMsg);
    }

    /**
     * Complete a given outstanding request with a completion value.
     *
     * @param requestID  The request to complete.
     * @param completion The value to complete the request with
     * @param <T>        The type of the completion.
     */
    @Deprecated // TODO: Add replacement method that conforms to style
    @SuppressWarnings("checkstyle:abbreviation") // Due to deprecation
    public <T> void completeRequest(long requestID, T completion) {
        CompletableFuture<T> cf;
        if ((cf = (CompletableFuture<T>) outstandingRequests.get(requestID)) != null) {
            cf.complete(completion);
            outstandingRequests.remove(requestID);
        } else {
            log.warn("Attempted to complete request {}, but request not outstanding!", requestID);
        }
    }

    /**
     * Exceptionally complete a request with a given cause.
     *
     * @param requestID The request to complete.
     * @param cause     The cause to give for the exceptional completion.
     */
    public void completeExceptionally(long requestID, Throwable cause) {
        CompletableFuture cf;
        if ((cf = outstandingRequests.get(requestID)) != null) {
            cf.completeExceptionally(cause);
            outstandingRequests.remove(requestID);
        } else {
            log.warn("Attempted to exceptionally complete request {}, but request not outstanding!",
                    requestID);
        }
    }

    /**
     * Validate the epoch of a CorfuMsg, and send a WRONG_EPOCH response if
     * the server is in the wrong epoch. Ignored if the message type is reset (which
     * is valid in any epoch).
     *
     * @param msg The incoming message to validate.
     * @param ctx The context of the channel handler.
     * @return True, if the epoch is correct, but false otherwise.
     */
    @Deprecated // TODO: Add replacement method that conforms to style
    @SuppressWarnings("checkstyle:abbreviation") // Due to deprecation
    private boolean validateEpochAndClientID(CorfuMsg msg, ChannelHandlerContext ctx) {
        // Check if the message is intended for us. If not, drop the message.
        if (!msg.getClientID().equals(parameters.getClientId())) {
            log.warn("Incoming message intended for client {}, our id is {}, dropping!",
                    msg.getClientID(), parameters.getClientId());
            return false;
        }
        // Check if the message is in the right epoch.
        if (!msg.getMsgType().ignoreEpoch && msg.getEpoch() != epoch) {
            log.trace("Incoming message with wrong epoch, got {}, expected {}, message was: {}",
                    msg.getEpoch(), epoch, msg);
            /* If this message was pending a completion, complete it with an error. */
            completeExceptionally(msg.getRequestID(), new WrongEpochException(msg.getEpoch()));
            return false;
        }
        return true;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CorfuMsg m) throws Exception {
        try {
            // We get the handler for this message from the map
            IClient handler = handlerMap.get(m.getMsgType());
            if (handler == null) {
                // The message was unregistered, we are dropping it.
                log.warn("Received unregistered message {}, dropping", m);
            } else {
                if (validateEpochAndClientID(m, ctx)) {
                    // Route the message to the handler.
                    if (log.isTraceEnabled()) {
                        log.trace("Message routed to {}: {}",
                                handler.getClass().getSimpleName(), m);
                    }
                    handler.handleMessage(m, ctx);
                }
            }
        } catch (Exception e) {
            log.error("Exception during read!", e);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("Exception during channel handling.", cause);
        ctx.close();
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        context = ctx;
        log.debug("Registered new channel {}", ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        super.channelUnregistered(ctx);
        context = null;
        log.debug("Unregistered channel {}", ctx);
    }
}
