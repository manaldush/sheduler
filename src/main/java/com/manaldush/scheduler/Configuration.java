package com.manaldush.scheduler;


import com.google.common.base.Preconditions;

/**
 * Configuration of ImplScheduler object.
 *
 * @version 1.00
 * @author manaldush
 */
public final class Configuration implements Cloneable {
    private volatile IExecutorBuilder executorBuilder = new DefaultExecutorBuilder();
    private volatile String moduleName = "SCHEDULER";
    private volatile int threadsNumber = 1;

    /**
     * Set IExecutorBuilder implementation, used for creation executors threads.
     *
     * @param _executorBuilder - implementation of interface IExecutorBuilder
     * @throws NullPointerException - if IExecutorBuilder is null object
     * @return configuration object
     */
    public Configuration setExecutorBuilder(final IExecutorBuilder _executorBuilder) {
        Preconditions.checkNotNull(_executorBuilder, "Executor builder is null object");
        executorBuilder = _executorBuilder;
        return this;
    }

    /**
     * Set module scheduler name. Default value is "SCHEDULER";
     *
     * @param _moduleName - name of module
     * @throws IllegalArgumentException - if _moduleName is empty
     * @throws NullPointerException - if _moduleName is null
     * @return configuration object
     */
    public Configuration setModuleName(final String _moduleName) {
        Preconditions.checkNotNull(_moduleName, "Module name can not be null object");
        Preconditions.checkArgument(!_moduleName.isEmpty(), "Module name can not be empty string");
        moduleName = _moduleName;
        return this;
    }

    /**
     * Set number of executors threads, default value = 1.
     *
     * @param _threadsNumber - number
     * @return configuration object
     */
    public Configuration setThreadsNumber(final int _threadsNumber) {
        Preconditions.checkArgument(_threadsNumber > 0);
        threadsNumber = _threadsNumber;
        return this;
    }

    @Override
    public Object clone() {
        return new Configuration().setExecutorBuilder(executorBuilder).setModuleName(moduleName).setThreadsNumber(
                threadsNumber);
    }

    IExecutorBuilder getExecutorBuilder() {
        return executorBuilder;
    }

    String getModuleName() {
        return moduleName;
    }

    int getThreadsNumber() {
        return threadsNumber;
    }
}
