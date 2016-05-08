package ru.spbau.mit.pool;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.function.Function;
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
            Thread.currentThread().interrupt();
        } finally {
            return TWO_SECONDS;
        }
    };
    private static final Supplier<Integer> THROWS_EXCEPTION = () -> 1 / 0;
    private static final Function<Integer, Integer> INC = number -> number + 1;

    @Before
    public void setUp() {
        pool = new ThreadPoolImpl(NUMBER_OF_THREADS);
    }

    @Test
    public void testSubmitOneTask() throws LightExecutionException, InterruptedException {
        assertEquals(NUMBER_OF_THREADS, getAmountOfWaitingThreads());

        final LightFuture<Integer> future = pool.submitTask(WAIT_TWO_SECONDS);

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

        final LightFuture<Integer> future1 = pool.submitTask(WAIT_TWO_SECONDS);
        final LightFuture<Integer> future2 = pool.submitTask(WAIT_TWO_SECONDS);
        final LightFuture<Integer> future3 = pool.submitTask(WAIT_TWO_SECONDS);
        final LightFuture<Integer> future4 = pool.submitTask(WAIT_ONE_SECOND);

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

    @Test(expected = LightExecutionException.class)
    public void testSimpleException() throws InterruptedException, LightExecutionException {
        final LightFuture<Integer> future = pool.submitTask(THROWS_EXCEPTION);
        Thread.sleep(100);
        future.get();
    }

    @Test(expected = LightExecutionException.class)
    public void testExceptionDoNotBreakPool() throws InterruptedException, LightExecutionException {
        final LightFuture<Integer> future1 = pool.submitTask(THROWS_EXCEPTION);
        final LightFuture<Integer> future2 = pool.submitTask(THROWS_EXCEPTION);
        final LightFuture<Integer> future3 = pool.submitTask(THROWS_EXCEPTION);
        final LightFuture<Integer> future4 = pool.submitTask(WAIT_ONE_SECOND);

        Thread.sleep(500);

        assertEquals(NUMBER_OF_THREADS - 1, getAmountOfWaitingThreads());

        assertEquals(ONE_SECOND, (int) future4.get());
        assertTrue(future4.isReady());
        assertFalse(future4.hasException());

        assertTrue(future1.isReady());
        assertTrue(future1.hasException());

        assertTrue(future2.isReady());
        assertTrue(future2.hasException());

        assertTrue(future3.isReady());
        assertTrue(future3.hasException());

        future3.get();
    }

    @Test
    public void testSimpleThenApply() throws InterruptedException, LightExecutionException {
        final LightFuture<Integer> future1 = pool.submitTask(WAIT_ONE_SECOND);
        final LightFuture<Integer> future2 = future1.thenApply(INC);

        assertEquals(ONE_SECOND + 1, (int) future2.get());
        assertTrue(future2.isReady());
        assertFalse(future2.hasException());
    }

    @Test
    public void testThenApplyDoNotStorePool() throws InterruptedException, LightExecutionException {
        final LightFuture<Integer> future1 = pool.submitTask(WAIT_TWO_SECONDS);
        final LightFuture<Integer> future2 = future1.thenApply(INC);
        final LightFuture<Integer> future3 = future1.thenApply(INC);
        final LightFuture<Integer> future4 = pool.submitTask(WAIT_ONE_SECOND);

        Thread.sleep(100);

        // 1st and 4th should be active => only one thread is waiting
        assertEquals(1, getAmountOfWaitingThreads());

        assertEquals(TWO_SECONDS, (int) future1.get());
        assertEquals(TWO_SECONDS + 1, (int) future2.get());
        assertEquals(TWO_SECONDS + 1, (int) future3.get());
        assertEquals(ONE_SECOND, (int) future4.get());
    }

    @Test
    public void thenApplyCascade() throws InterruptedException, LightExecutionException {
        final LightFuture<Integer> future1 = pool.submitTask(WAIT_TWO_SECONDS);
        final LightFuture<Integer> future2 = future1.thenApply(INC);
        final LightFuture<Integer> future3 = future2.thenApply(INC);

        Thread.sleep(100);

        // 1st should be active => 2 threads are waiting
        assertEquals(2, getAmountOfWaitingThreads());

        assertEquals(TWO_SECONDS + 2, (int) future3.get());
        assertEquals(TWO_SECONDS + 1, (int) future2.get());
        assertEquals(TWO_SECONDS, (int) future1.get());

        assertTrue(future1.isReady());
        assertTrue(future2.isReady());
        assertTrue(future3.isReady());
    }

    @Test(expected = LightExecutionException.class)
    public void testApplyCascadeException() throws InterruptedException, LightExecutionException {
        final LightFuture<Integer> future1 = pool.submitTask(THROWS_EXCEPTION);
        final LightFuture<Integer> future2 = future1.thenApply(INC);
        final LightFuture<Integer> future3 = future2.thenApply(INC);

        Thread.sleep(300);

        assertTrue(future1.hasException());
        assertTrue(future2.hasException());
        assertTrue(future3.hasException());

        assertTrue(future1.isReady());
        assertTrue(future2.isReady());
        assertTrue(future3.isReady());

        future3.get();
    }

    @Test
    public void testShutdown() throws InterruptedException, LightExecutionException {
        final LightFuture<Integer> future1 = pool.submitTask(WAIT_ONE_SECOND);
        final LightFuture<Integer> future2 = pool.submitTask(WAIT_TWO_SECONDS);
        final LightFuture<Integer> future3 = future2.thenApply(INC);

        // Succeeded before interrupt
        assertEquals(ONE_SECOND, (int) future1.get());

        // 2nd should be active => 2 threads are waiting
        assertEquals(2, getAmountOfWaitingThreads());

        pool.shutdown();
        assertTrue(future1.isReady());
        assertFalse(future2.isReady());
        assertFalse(future3.isReady());
    }

    private int getAmountOfWaitingThreads() {
        return (int) pool
                .getThreads()
                .stream()
                .filter(thread -> thread.isWaiting())
                .count();
    }
}