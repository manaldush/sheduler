package com.manaldush.scheduler.exceptions;

/**
 * Exception describe error during processing of task.
 *
 * @version 1.00
 * @author manaldush
 */
public final class TaskProcessingException extends Exception {
    /**
     * Create task processing exception.
     * @param _msg - error message
     */
    public TaskProcessingException(final String _msg) {
        super(_msg);
    }

    /**
     * Create task processing exception.
     * @param _cause - exception cause
     */
    public TaskProcessingException(final Throwable _cause) {
        super(_cause);
    }

    /**
     * Create task processing exception.
     * @param _msg - error message
     * @param _cause - exception cause
     */
    public TaskProcessingException(final String _msg, final Throwable _cause) {
        super(_msg, _cause);
    }
}
