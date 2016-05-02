package ru.spbau.mit.pool;

import org.junit.Before;
import org.junit.Test;

import java.util.function.Supplier;


/**
 * Created by airvan21 on 02.05.16.
 */
public class ThreadPoolImplTest {
    private static final int NUMBER_OF_THREADS = 2;
    private static final Supplier<String> waitSupplier = () -> {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            return "Woke up!";
        }
    };
    private ThreadPool pool;


    @Before
    public void setUp() {
        pool = new ThreadPoolImpl(NUMBER_OF_THREADS);
    }

    @Test
    public void testSubmitTask() throws LightExecutionException, InterruptedException {
        Supplier<String> supplier = () -> "Hello, world".substring(0, 5);

        LightFuture<String> future1 = pool.submitTask(waitSupplier);
        LightFuture<String> future2 = pool.submitTask(waitSupplier);
        LightFuture<String> future3 = pool.submitTask(waitSupplier);

        int count = 3;

        boolean[] already = {false, false, false};

        while (count > 0) {
            if (future1.isReady() && !already[0]) {
                System.out.println(future1.get());
                already[0] = true;
                count--;
            }

            if (future2.isReady() && !already[1]) {
                System.out.println(future2.get());
                already[1] = true;
                count--;
            }

            if (future3.isReady() && !already[2]) {
                System.out.println(future3.get());
                already[2] = true;
                count--;
            }
        }

    }

    @Test
    public void testShutdown() {

    }
}