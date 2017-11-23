package com.manaldush.scheduler;

import com.manaldush.scheduler.exceptions.TaskProcessingException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Assert.*;

public class TaskTest {
    @Test
    public void test() {
        Task task = new Task(true) {
            @Override
            public void process() throws TaskProcessingException {

            }
        };
        Assert.assertTrue(task.isMandatory());
        Assert.assertFalse(task.isCanceled());
        task.cancel();
        Assert.assertTrue(task.isCanceled());
    }
}