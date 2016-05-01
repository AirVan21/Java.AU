package ru.spbau.mit.pool;

import java.util.function.Function;

/**
 * Created by airvan21 on 01.05.16.
 */
public class LightFutureImpl<A> implements LightFuture<A> {

    @Override
    public boolean isReady() {
        return false;
    }

    @Override
    public void get() throws LightExecutionException {

    }

    @Override
    public <R> LightFuture<R> thenApply(Function<? super A, R> transformation) {
        return null;
    }

    @Override
    public void run() {

    }
}
