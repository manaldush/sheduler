package com.manaldush;

import com.manaldush.scheduler.Configuration;
import com.manaldush.scheduler.IScheduler;
import com.manaldush.scheduler.ImplScheduler;
import com.manaldush.scheduler.Task;
import com.manaldush.scheduler.exceptions.TaskProcessingException;
import com.manaldush.scheduler.exceptions.TaskScheduleException;

import java.util.concurrent.CountDownLatch;

public class Main {

    public static void main(String[] args) throws TaskScheduleException, InterruptedException {
        Configuration conf = new Configuration();
        IScheduler scheduler = ImplScheduler.build(conf);
        CountDownLatch singleSchedule = new CountDownLatch(1);
        CountDownLatch repeatedSchedule = new CountDownLatch(3);
        CountDownLatch mandatorySchedule = new CountDownLatch(2);

        scheduler.start();
        scheduler.schedule(new Task(false) {
            @Override
            public void process() throws TaskProcessingException {
                System.out.print("Single Schedule task\n");
                singleSchedule.countDown();
            }
        }, 10);
        singleSchedule.await();
        scheduler.scheduleAndRepeat(new Task(false) {
            @Override
            public void process() throws TaskProcessingException {
                System.out.print("Repeated schedule task\n");
                repeatedSchedule.countDown();
            }
        }, 5);
        repeatedSchedule.await();
        scheduler.schedule(new Task(true) {
            @Override
            public void process() throws TaskProcessingException {
                System.out.print("Mandatory Schedule task #1\n");
                mandatorySchedule.countDown();
            }
        }, 1000);
        scheduler.schedule(new Task(true) {
            @Override
            public void process() throws TaskProcessingException {
                System.out.print("Mandatory Schedule task #2\n");
                mandatorySchedule.countDown();
            }
        }, 1000);
        scheduler.stop();
        mandatorySchedule.await();
    }
}
