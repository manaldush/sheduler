package com.manaldush.scheduler;

import com.manaldush.scheduler.exceptions.TaskProcessingException;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Implementation of executor.
 *
 * @version 1.00
 * @author manaldush
 */
final class DefaultExecutor implements IExecutor, Runnable {

    private static final int TASK_EXPECTED_TIMEOUT = 100;
    /**Available states of executor.*/
    private enum STATES { STARTED, STOPPED };
    private volatile STATES state = STATES.STOPPED;
    private final Thread thread = new Thread(this);
    private final LinkedBlockingQueue<Task> tasks = new LinkedBlockingQueue<>();
    private final CountDownLatch thrStarted = new CountDownLatch(1);

    /**
     * Start executor thread.
     */
    @Override
    public synchronized void start() throws InterruptedException {
        if (state == STATES.STARTED) {
            return;
        }
        state = STATES.STARTED;
        thread.start();
    }

    /**
     * Stop executor thread.
     */
    @Override
    public synchronized void stop() throws InterruptedException {
        if (state == STATES.STOPPED) {
            return;
        }
        state = STATES.STOPPED;
        thrStarted.await();
        thread.join();
    }

    /**
     * Add task for processing.
     *
     * @param _task - task for execution
     */
    @Override
    public boolean addTask(final Task _task) throws InterruptedException {
        return tasks.offer(_task);
    }

    @Override
    public void run() {
        thrStarted.countDown();
        try {
            while (state == STATES.STARTED) {
                Task task = tasks.poll(TASK_EXPECTED_TIMEOUT, TimeUnit.MILLISECONDS);
                if (task == null) {
                    continue;
                }
                processTask(task);
            }
            // process queue for the end
            while (true) {
                Task task = tasks.poll();
                if (task == null) {
                    // All tasks was processed
                    break;
                }
                if (task.isMandatory()) {
                    processTask(task);
                }
            }
        } catch (InterruptedException e) {
            return;
        }
    }

    private void processTask(Task _task) {
        try {
            _task.process();
        } catch (TaskProcessingException e) {
            e.printStackTrace();
        }
    }
}
