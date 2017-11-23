package com.manaldush.scheduler;

import com.google.common.base.Preconditions;
import com.manaldush.scheduler.exceptions.TaskProcessingException;
import com.manaldush.scheduler.exceptions.TaskScheduleException;

/**
 * Implementation of Task class. Task after execution will be scheduled again.
 * All repeated tasks are not mandatory.
 *
 * @version 1.00
 * @author manaldush
 */
final class RepeatedTask extends Task {

    private static final boolean MANDATORY = false;
    private final Task task;
    private final IScheduler scheduler;
    private final int timeout;

    RepeatedTask(final Task _task, final IScheduler _scheduler, final int _timeout) {
        super(MANDATORY);
        Preconditions.checkArgument(!_task.isMandatory(), String.format("Repeated task [%s] can not be mandatory",
                _task.toString()));
        task = _task;
        scheduler = _scheduler;
        timeout = _timeout;
    }

    @Override
    public void cancel() {
        task.cancel();
    }

    @Override
    protected boolean isCanceled() {
        return task.isCanceled();
    }

    /**
     * Method will be executed when task is processing.
     *
     * @throws TaskProcessingException - any error during processing task.
     */
    @Override
    public void process() throws TaskProcessingException {
        try {
            task.process();
        } finally {
            try {
                if (task.isCanceled()) {
                    return;
                }
                scheduler.schedule(this, timeout);
            } catch (TaskScheduleException e) {
                throw new TaskProcessingException(e);
            }
        }
    }
}
