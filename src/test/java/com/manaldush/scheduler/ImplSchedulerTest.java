package com.manaldush.scheduler;

import com.manaldush.scheduler.exceptions.TaskProcessingException;
import com.manaldush.scheduler.exceptions.TaskScheduleException;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

import static org.mockito.Mockito.mock;

public class ImplSchedulerTest {
    @Test
    public void test() throws TaskScheduleException, InterruptedException {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        Task task = mock(Task.class);
        final IExecutor executor = new IExecutor() {
            @Override
            public void start() throws InterruptedException {

            }

            @Override
            public void stop() throws InterruptedException {

            }

            @Override
            public boolean addTask(Task _task) throws InterruptedException {
                countDownLatch.countDown();
                return true;
            }
        };
        IExecutorBuilder builder = new IExecutorBuilder() {
            @Override
            public IExecutor build() {
                return executor;
            }
        };
        Configuration configuration = new Configuration().setExecutorBuilder(builder);
        ImplScheduler scheduler = ImplScheduler.build(configuration);
        scheduler.start();
        scheduler.schedule(task, 1);
        // when task will be added in executor thread countDownLatch rise
        countDownLatch.await();
        scheduler.stop();
    }

    @Test(timeout = 5000)
    public void test_2() throws TaskScheduleException, InterruptedException {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        Task task = new Task(false) {
            @Override
            public void process() throws TaskProcessingException {
                countDownLatch.countDown();
            }
        };
        Configuration configuration = new Configuration();
        ImplScheduler scheduler = ImplScheduler.build(configuration);
        scheduler.start();
        scheduler.schedule(task, 2);
        // when task will be added in executor thread countDownLatch rise
        countDownLatch.await();
        scheduler.stop();
    }
}