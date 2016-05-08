package ru.spbau.mit.pool;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by airvan21 on 01.05.16.
 */
public class LightFutureImpl<A> implements LightFuture<A> {
    private final Supplier<A> supplier;
    private final BlockingQueue<LightFuture> taskQueue;
    private final BlockingQueue<LightFuture> thenApplyQueue = new BlockingQueue<>();
    private volatile A data;
    private volatile boolean isReady     = false;
    private volatile boolean isException = false;

    public LightFutureImpl(Supplier<A> supplier, BlockingQueue<LightFuture> taskQueue) {
        this.supplier = supplier;
        this.taskQueue = taskQueue;
    }

    @Override
    public boolean isReady() {
        return isReady;
    }

    @Override
    public boolean hasException() {
        return isException;
    }

    @Override
    public synchronized A get() throws LightExecutionException, InterruptedException {
        while (!isReady) {
            wait();
        }

        if (isException) {
            throw new LightExecutionException();
        }

        return data;
    }

    @Override
    public <R> LightFuture<R> thenApply(Function<? super A, R> transformation) throws InterruptedException {
        LightFuture<R> future = new LightFutureImpl<>(() -> transformation.apply(data), taskQueue);

        while (!isReady) {
            thenApplyQueue.enqueue(future);

            return future;
        }

        if (isException) {
            future.markRejected();

            return future;
        }

        while (true) {
            synchronized (taskQueue) {
                taskQueue.enqueue(future);
                taskQueue.notify();
                break;
            }
        }

        return future;
    }

    @Override
    public synchronized void run() throws LightExecutionException {
        try {
            data = supplier.get();
            synchronized (thenApplyQueue) {
                synchronized (taskQueue) {
                    while (!thenApplyQueue.isEmpty()) {
                        taskQueue.enqueue(thenApplyQueue.dequeue());
                    }

                    taskQueue.notifyAll();
                }
            }
        } catch (Exception e) {
            isException = true;
            rejectThenApplyFutures();
        }

        isReady = true;
        notify();
    }

    @Override
    public void rejectThenApplyFutures() {
        synchronized (thenApplyQueue) {
            while (!thenApplyQueue.isEmpty()) {
                thenApplyQueue.dequeue().markRejected();
            }
        }
    }

    @Override
    public synchronized void markRejected() {
        isException = true;
        isReady     = true;
    }
}
