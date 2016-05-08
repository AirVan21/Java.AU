package ru.spbau.mit.pool;

/**
 * Created by airvan21 on 02.05.16.
 */
public class PoolThread extends Thread {
    private final BlockingQueue<LightFuture> taskQueue;
    private volatile boolean isWaiting = true;

    public PoolThread(BlockingQueue<LightFuture> taskQueue) {
        this.taskQueue = taskQueue;
    }

    public synchronized boolean isWaiting() {
        return isWaiting;
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            LightFuture task;
            try {
                synchronized (taskQueue) {
                    while (taskQueue.isEmpty()) {
                        isWaiting = true;
                        taskQueue.wait();
                    }

                    isWaiting = false;
                    task = taskQueue.dequeue();
                }

                task.run();
            } catch (InterruptedException e) {
                break;
            } catch (LightExecutionException e) {
                e.printStackTrace();
            }
        }
    }
}
