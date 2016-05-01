package ru.spbau.mit.pool;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * Created by airvan21 on 01.05.16.
 */
public class ThreadPoolImpl implements ThreadPool {
    private final Queue<LightFutureImpl> taskQueue = new ArrayDeque<LightFutureImpl>();
    private final List<Thread> pool;

    public ThreadPoolImpl(int amoutOfThreads) {
        pool = new ArrayList<>(amoutOfThreads);
    }

    public void shutdown() {

    }
}
