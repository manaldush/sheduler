package com.manaldush.scheduler;

import com.manaldush.scheduler.exceptions.TaskProcessingException;

/**
 * Class describe tasks for scheduling.
 *
 * @version 1.00
 * @author manaldush
 */
public abstract class Task {

    private static final boolean CANCELED_TASK = true;
    private final boolean mandatory;
    private volatile boolean canceled = false;

    /**
     * Create task object.
     *
     * @param _isMandatory - is task mandatory for execution even if scheduler is stopping
     */
    protected Task(final boolean _isMandatory) {
        mandatory = _isMandatory;
    }

    /**
     * Method will be executed when task is processing.
     *
     * @throws TaskProcessingException - any error during processing task.
     */
    public abstract void process() throws TaskProcessingException;

    /**
     * Cancel the task, but if the task is processing now the cancel operation do not stop current execution.
     */
    public void cancel() {
        canceled = CANCELED_TASK;
    }

    /**
     * Return flag that task was canceled or not.
     * @return true/false
     */
    protected boolean isCanceled() {
        return canceled;
    }

    /**
     *  Check if task is mandatory for execution even if scheduler is stopping.
     * @return true/false
     */
    protected boolean isMandatory() {
        return mandatory;
    }
}
