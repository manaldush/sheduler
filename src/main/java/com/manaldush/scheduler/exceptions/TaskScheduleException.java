package com.manaldush.scheduler.exceptions;

/**
 * Exception describe error during scheduling of the task.
 *
 * @version 1.00
 * @author manaldush
 */
public final class TaskScheduleException extends Exception {
    /**
     * Create schedule operation exception.
     * @param _msg - error message
     */
    public TaskScheduleException(final String _msg) {
        super(_msg);
    }

    /**
     * Create schedule operation exception.
     * @param _cause - exception cause
     */
    public TaskScheduleException(final Throwable _cause) {
        super(_cause);
    }

    /**
     * Create schedule operation exception.
     * @param _msg - error message
     * @param _cause - exception cause
     */
    public TaskScheduleException(final String _msg, final Throwable _cause) {
        super(_msg, _cause);
    }
}
