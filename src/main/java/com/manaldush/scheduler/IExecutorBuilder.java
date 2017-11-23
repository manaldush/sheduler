package com.manaldush.scheduler;

/**
 * Implementations of this interface used for creation IExecutors.
 *
 * @version 1.00
 * @author manaldush
 */
public interface IExecutorBuilder {
    /**
     * Build IExecutor implementation.
     * @return IExecutor
     */
    IExecutor build();
}
