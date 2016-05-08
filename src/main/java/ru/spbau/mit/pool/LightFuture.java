package ru.spbau.mit.pool;

import java.util.function.Function;

/**
 * Created by airvan21 on 02.05.16.
 */
public interface LightFuture<A> {

    A get() throws LightExecutionException, InterruptedException;

    <R> LightFuture<R> thenApply(Function<? super A, R> transformation) throws InterruptedException;

    void run() throws LightExecutionException;

    void markRejected();

    void rejectThenApplyFutures();

    boolean isReady();

    boolean hasException();
}
