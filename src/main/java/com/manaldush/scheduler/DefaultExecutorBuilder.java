package com.manaldush.scheduler;

/**
 * Implementations of interface IExecutorBuilder used for creation IExecutors.
 *
 * @version 1.00
 * @author manaldush
 */
final class DefaultExecutorBuilder implements IExecutorBuilder {
    /**
     * Build IExecutor implementation.
     *
     * @return IExecutor
     */
    @Override
    public IExecutor build() {
        return new DefaultExecutor();
    }
}
