package com.alibaba.dubbo.common.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.FutureTask;

/**
 * A {@link FutureTask} that also implements the {@link ListenableFuture}
 * interface.  Unlike {@code FutureTask}, {@code ListenableFutureTask} does not
 * provide an overrideable {@link FutureTask#done() done()} method.  For similar
 * functionality, call {@link #addListener}.
 * <p>
 * <p>
 *
 * @author Sven Mawson
 * @since 1.0
 */
public class ListenableFutureTask<V> extends FutureTask<V>
        implements ListenableFuture<V> {
    // TODO(cpovirk): explore ways of making ListenableFutureTask final. There are
    // some valid reasons such as BoundedQueueExecutorService to allow extends but it
    // would be nice to make it final to avoid unintended usage.

    // The execution list to hold our listeners.
    private final ExecutionList executionList = new ExecutionList();

    /**
     * Creates a {@code ListenableFutureTask} that will upon running, execute the
     * given {@code Callable}.
     *
     * @param callable the callable task
     * @since 10.0
     */
    public static <V> ListenableFutureTask<V> create(Callable<V> callable) {
        return new ListenableFutureTask<V>(callable);
    }

    /**
     * Creates a {@code ListenableFutureTask} that will upon running, execute the
     * given {@code Runnable}, and arrange that {@code get} will return the
     * given result on successful completion.
     *
     * @param runnable the runnable task
     * @param result   the result to return on successful completion. If you don't
     *                 need a particular result, consider using constructions of the form:
     *                 {@code ListenableFuture<?> f = ListenableFutureTask.create(runnable,
     *                 null)}
     * @since 10.0
     */
    public static <V> ListenableFutureTask<V> create(
            Runnable runnable, V result) {
        return new ListenableFutureTask<V>(runnable, result);
    }

    ListenableFutureTask(Callable<V> callable) {
        super(callable);
    }

    ListenableFutureTask(Runnable runnable, V result) {
        super(runnable, result);
    }

    @Override
    public void addListener(Runnable listener, Executor exec) {
        executionList.add(listener, exec);
    }

    @Override
    public void addListener(Runnable listener) {
        executionList.add(listener, null);
    }

    /**
     * Internal implementation detail used to invoke the listeners.
     */
    @Override
    protected void done() {
        executionList.execute();
    }
}