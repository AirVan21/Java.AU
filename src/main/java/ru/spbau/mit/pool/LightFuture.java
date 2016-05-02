package ru.spbau.mit.pool;

import java.util.function.Function;

/**
 * Created by airvan21 on 02.05.16.
 */
public interface LightFuture<A> {

    boolean isReady();

    A get() throws LightExecutionException, InterruptedException;

    <R> LightFuture<R> thenApply(Function<? super A, R> transformation);

    void run() throws LightExecutionException;
}
