package ru.spbau.mit.pool;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.IntStream;

/**
 * Created by airvan21 on 01.05.16.
 */
public class ThreadPoolImpl implements ThreadPool {
    private final BlockingQueue<LightFuture> taskQueue = new BlockingQueue<>();
    private final List<PoolThread> threads             = new ArrayList<>();

    public ThreadPoolImpl(int numberOfThreads) {
        IntStream
                .range(0, numberOfThreads)
                .forEach(index -> threads.add(new PoolThread(taskQueue)));

        threads.forEach(PoolThread::start);
    }

    public <R> LightFuture<R> submitTask(Supplier<R> supplier) {
        LightFuture<R> future = new LightFutureImpl<>(supplier, taskQueue);
        taskQueue.enqueue(future);

        return future;
    }

    public void shutdown() {
        synchronized (taskQueue) {
            while (!taskQueue.isEmpty()) {
                taskQueue.dequeue();
            }
        }

        threads.forEach(PoolThread::interrupt);
    }

    public List<PoolThread> getThreads() {
        return threads;
    }

    public BlockingQueue<LightFuture> getTaskQueue() {
        return taskQueue;
    }
}
