package com.manaldush.scheduler;

import com.manaldush.scheduler.exceptions.TaskScheduleException;

/**
 * Interface describe scheduler methods.
 *
 * @version 1.00
 * @author manaldush
 */
public interface IScheduler {
    /**
     * Start scheduler module.
     */
    void start();

    /**
     * Stop scheduler module.
     */
    void stop();

    /**
     * Schedule the task.
     * The task will be executed after _timeout expired.
     *
     * @param _task - task for execution
     * @param _timeout - execution period in sec
     * @throws TaskScheduleException - any error during scheduling of the task.
     */
    void schedule(Task _task, int _timeout) throws TaskScheduleException;

    /**
     * Schedule the task.
     * The task will be repeated after every _timeout expired.
     *
     * @param _task - task for execution
     * @param _timeout - execution period in sec
     * @throws TaskScheduleException - any error during scheduling of the task.
     */
    void scheduleAndRepeat(Task _task, int _timeout) throws TaskScheduleException;
}
