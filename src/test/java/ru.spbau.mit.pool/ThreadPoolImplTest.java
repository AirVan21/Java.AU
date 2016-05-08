package ru.spbau.mit.pool;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.function.Supplier;


/**
 * Created by airvan21 on 02.05.16.
 */
public class ThreadPoolImplTest {
    private ThreadPool pool;
    private static final int NUMBER_OF_THREADS  = 3;
    private static final int ONE_SECOND         = 1000;
    private static final int TWO_SECONDS        = 2000;
    private static final Supplier<Integer> WAIT_ONE_SECOND = () -> {
        try {
            Thread.sleep(ONE_SECOND);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            return ONE_SECOND;
        }
    };
    private static final Supplier<Integer> WAIT_TWO_SECONDS = () -> {
        try {
            Thread.sleep(TWO_SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            return TWO_SECONDS;
        }
    };
    private static final Supplier<Integer> THROWS_EXCEPTION = () -> 1 / 0;

    @Before
    public void setUp() {
        pool = new ThreadPoolImpl(NUMBER_OF_THREADS);
    }

    @Test
    public void testSubmitOneTask() throws LightExecutionException, InterruptedException {
        assertEquals(NUMBER_OF_THREADS, getAmountOfWaitingThreads());

        LightFuture<Integer> future = pool.submitTask(WAIT_TWO_SECONDS);

        Thread.sleep(100);

        assertEquals(NUMBER_OF_THREADS - 1, getAmountOfWaitingThreads());

        assertEquals(TWO_SECONDS, (int) future.get());
        assertTrue(future.isReady());
        assertFalse(future.hasException());

        // Empty queue - all threads are waiting
        assertEquals(NUMBER_OF_THREADS, getAmountOfWaitingThreads());
    }

    @Test
    public void testSubmitSomeTasks() throws LightExecutionException, InterruptedException {
        assertEquals(NUMBER_OF_THREADS, getAmountOfWaitingThreads());

        LightFuture<Integer> future1 = pool.submitTask(WAIT_TWO_SECONDS);
        LightFuture<Integer> future2 = pool.submitTask(WAIT_TWO_SECONDS);
        LightFuture<Integer> future3 = pool.submitTask(WAIT_TWO_SECONDS);
        LightFuture<Integer> future4 = pool.submitTask(WAIT_ONE_SECOND);

        Thread.sleep(100);

        // every thread is a poll is busy
        assertEquals(0, getAmountOfWaitingThreads());


        assertEquals(TWO_SECONDS, (int) future1.get());
        assertTrue(future1.isReady());
        assertFalse(future1.hasException());

        assertEquals(TWO_SECONDS, (int) future2.get());
        assertTrue(future2.isReady());
        assertFalse(future2.hasException());

        assertEquals(TWO_SECONDS, (int) future3.get());
        assertTrue(future3.isReady());
        assertFalse(future3.hasException());

        // 4-th thread in a queue | 2 threads are waiting
        assertEquals(NUMBER_OF_THREADS - 1, getAmountOfWaitingThreads());

        assertEquals(ONE_SECOND, (int) future4.get());
        assertTrue(future4.isReady());
        assertFalse(future4.hasException());

        // Empty queue - all threads are waiting
        assertEquals(NUMBER_OF_THREADS, getAmountOfWaitingThreads());
    }

    @Test
    public void testShutdown() {

    }

    private int getAmountOfWaitingThreads() {
        return (int) pool
                .getThreads()
                .stream()
                .filter(thread -> thread.isWaiting())
                .count();
    }
}