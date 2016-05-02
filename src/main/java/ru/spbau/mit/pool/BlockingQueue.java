package ru.spbau.mit.pool;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by airvan21 on 01.05.16.
 */
public class BlockingQueue<T> {
    private final List<T> queue = new LinkedList<>();

    public BlockingQueue() {}

    public synchronized boolean isEmpty() {
        return queue.isEmpty();
    }

    public synchronized void enqueue(T task) {
        queue.add(task);
        notify();
    }

    public synchronized T dequeue() throws EmptyBlockingQueueException {
        if (queue.size() == 0) {
            throw new EmptyBlockingQueueException();
        }

        return queue.remove(0);
    }
}
