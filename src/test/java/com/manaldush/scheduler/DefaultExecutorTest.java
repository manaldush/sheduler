package com.manaldush.scheduler;

import com.manaldush.scheduler.exceptions.TaskProcessingException;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

public class DefaultExecutorTest {

    @Test
    public void test() throws InterruptedException {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        Task task = new Task(true) {
            @Override
            public void process() throws TaskProcessingException {
                countDownLatch.countDown();
            }
        };
        DefaultExecutor executor = new DefaultExecutor();
        executor.start();
        executor.start();
        executor.addTask(task);
        // When task would be processed semaphore will be released
        countDownLatch.await();
        executor.stop();
        executor.stop();
    }

}