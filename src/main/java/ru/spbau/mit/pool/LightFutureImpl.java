package ru.spbau.mit.pool;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by airvan21 on 01.05.16.
 */
public class LightFutureImpl<A> implements LightFuture<A> {
    private final Supplier<A> supplier;
    private final BlockingQueue<LightFuture> thenApplyQueue = new BlockingQueue<>();
    private volatile A data;
    private volatile boolean isReady     = false;
    private volatile boolean isException = false;

    public LightFutureImpl(Supplier<A> supplier) {
        this.supplier = supplier;
    }

    @Override
    public boolean isReady() {
        return false;
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
    public <R> LightFuture<R> thenApply(Function<? super A, R> transformation) {
        LightFuture<R> future = new LightFutureImpl<>(() -> transformation.apply(data));
        synchronized (thenApplyQueue) {
            if (!isReady) {
                thenApplyQueue.enqueue(future);
            }
        }
        return null;
    }

    public void run() throws LightExecutionException {
        try {
            data = supplier.get();
            isReady = true;
        } catch (Exception e) {
            isException = true;
        } finally {
            synchronized (this) {
                notifyAll();
            }
        }
    }
}
