package ru.spbau.mit.pool;

import java.util.List;
import java.util.function.Supplier;

/**
 * Created by airvan21 on 01.05.16.
 */
public interface ThreadPool {
    <R> LightFuture<R> submitTask(Supplier<R> supplier);

    void shutdown();

    List<PoolThread> getThreads();

    BlockingQueue<LightFuture> getTaskQueue();
}