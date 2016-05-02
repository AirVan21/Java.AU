package ru.spbau.mit.pool;

import java.util.function.Supplier;

/**
 * Created by airvan21 on 01.05.16.
 */
public interface ThreadPool {
    void shutdown();
    <R> LightFuture<R> submitTask(Supplier<R> supplier);
}
