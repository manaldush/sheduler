package com.manaldush.scheduler;

/**
 * Thread used for execution tasks.
 *
 * @version 1.00
 * @author manaldush
 */
public interface IExecutor {
    /**
     * Start executor thread.
     * @throws InterruptedException - interrupt execption
     */
    void start() throws InterruptedException;

    /**
     * Stop executor thread. Wait until thread is stop working.
     *
     * @throws InterruptedException - interrupt error
     */
    void stop() throws InterruptedException;

    /**
     * Add task for processing.
     *
     * @param _task - task for execution
     * @throws InterruptedException - interrupt error
     * @return result of add operation, failed is queue is overload
     */
    boolean addTask(Task _task) throws InterruptedException;
}
