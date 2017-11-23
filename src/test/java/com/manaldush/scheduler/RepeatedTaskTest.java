package com.manaldush.scheduler;

import com.manaldush.scheduler.exceptions.TaskProcessingException;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class RepeatedTaskTest {
    @Test
    public void process() throws Exception {
        Task task = Mockito.mock(Task.class);
        IScheduler scheduler = Mockito.mock(IScheduler.class);
        RepeatedTask repeatedTask = new RepeatedTask(task, scheduler, 100);

        //1. Task is not canceled
        repeatedTask.process();
        verify(task, times(1)).process();
        verify(scheduler, times(1)).schedule(repeatedTask, 100);
        reset(task);
        reset(scheduler);

        //2. Task is canceled
        when(task.isCanceled()).thenReturn(true);
        repeatedTask.process();
        verify(task, times(1)).process();
        verify(scheduler, times(0)).schedule(repeatedTask, 100);
        reset(task);
        reset(scheduler);

        // Check cancel method
        task = new Task(false) {
            @Override
            public void process() throws TaskProcessingException {

            }
        };
        repeatedTask = new RepeatedTask(task, scheduler, 100);
        Assert.assertFalse(repeatedTask.isCanceled());
        repeatedTask.cancel();
        Assert.assertTrue(repeatedTask.isCanceled());
    }

}