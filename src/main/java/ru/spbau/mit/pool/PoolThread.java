package ru.spbau.mit.pool;

/**
 * Created by airvan21 on 02.05.16.
 */
public class PoolThread extends Thread {
    private BlockingQueue<LightFuture> taskQueue;
    private volatile boolean isWaiting = true;

    public PoolThread(BlockingQueue<LightFuture> taskQueue) {
        this.taskQueue = taskQueue;
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            LightFuture task;
            try {
                synchronized (taskQueue) {
                    while (taskQueue.isEmpty()) {
                        wait();
                    }

                    task = taskQueue.dequeue();
                }

                task.run();
            } catch (InterruptedException e) {
                break;
            } catch (EmptyBlockingQueueException e) {
                e.getMessage();
                e.printStackTrace();
            } catch (LightExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized boolean isWaiting() {
        return isWaiting;
    }
}
