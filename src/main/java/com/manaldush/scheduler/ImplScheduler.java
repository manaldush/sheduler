package com.manaldush.scheduler;

import com.google.common.base.Preconditions;
import com.manaldush.scheduler.exceptions.TaskScheduleException;

import java.util.SortedMap;
import java.util.TreeMap;
import java.util.List;
import java.util.LinkedList;
import java.util.Map;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Implementation of IScheduler interface.
 *
 * @version 1.00
 * @author manaldush
 */
public final class ImplScheduler implements IScheduler {

    private volatile SchedulerStates state = SchedulerStates.STOPPED;
    private final TreeMap<Long, List<Task>> tasks = new TreeMap<>();
    private final Configuration configuration;
    private final Thread thread = new Thread(new SchedulerController());
    private final CountDownLatch thrStarted = new CountDownLatch(1);

    private ImplScheduler(final Configuration _conf) {
        configuration = (Configuration) _conf.clone();
    }

    /**
     * Build ImplScheduler object.
     * @param _conf - configuration
     * @throws NullPointerException - _conf is null object
     * @return ImplScheduler object
     */
    public static ImplScheduler build(final Configuration _conf) {
        Preconditions.checkNotNull(_conf);
        return new ImplScheduler(_conf);
    }

    /**
     * Start scheduler module.
     */
    @Override
    public void start() {
        if (state == SchedulerStates.STARTED) {
            return;
        }
        state = SchedulerStates.STARTED;
        thread.start();
    }

    /**
     * Stop scheduler module.
     */
    @Override
    public void stop() {
        if (state == SchedulerStates.STOPPED) {
            return;
        }
        state = SchedulerStates.STOPPED;
        try {
            thrStarted.await();
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Schedule the task.
     * The task will be executed after _timeout expired.
     *
     * @param _task    - task for execution
     * @param _timeout - execution period in sec
     * @throws TaskScheduleException - any error during scheduling of the task.
     * @throws IllegalArgumentException - _timeout <= 0
     * @throws NullPointerException - _task is null object
     */
    @Override
    public void schedule(final Task _task, final int _timeout) throws TaskScheduleException {
        checkScheduledArguments(_task, _timeout);
        if (_task.isCanceled()) {
            return;
        }
        addTask(_task, calculateExecTime(_timeout));
    }

    /**
     * Schedule the task.
     * The task will be repeated after every _timeout expired.
     * These tasks are not mandatory even if flag mandatory is installed.
     *
     * @param _task    - task for execution
     * @param _timeout - execution period in sec
     * @throws TaskScheduleException - any error during scheduling of the task.
     * @throws IllegalArgumentException - task is mandatory, _timeout <= 0
     * @throws NullPointerException - _task is null object
     */
    @Override
    public void scheduleAndRepeat(final Task _task, final int _timeout) throws TaskScheduleException {
        checkScheduledArguments(_task, _timeout);
        addTask(new RepeatedTask(_task, this, _timeout), calculateExecTime(_timeout));
    }

    private Long calculateExecTime(final int _delta) {
        long lCurTime = System.currentTimeMillis();
        return convertToSec(lCurTime) + _delta;
    }

    private Long convertToSec(final Long _time) {
        return TimeUnit.MILLISECONDS.toSeconds(_time);
    }

    private synchronized SortedMap<Long, List<Task>> getTasks(final Long _time) {
        SortedMap tasksSnapshot = tasks.headMap(convertToSec(_time), true);
        if (tasksSnapshot == null || tasksSnapshot.isEmpty()) {
            return null;
        }
        // copy task in specific sortedMap and clear tasks
        SortedMap t = new TreeMap<Long, List<Task>>(tasksSnapshot);
        tasksSnapshot.clear();
        return t;
    }

    private synchronized SortedMap<Long, List<Task>> getTasks() {
        return tasks;
    }

    private synchronized void addTask(final Task _task, final Long _execTime) {
        if (state == SchedulerStates.STOPPED) {
            return;
        }
        List<Task> taskList = tasks.get(_execTime);
        if (taskList == null) {
            taskList = new LinkedList<>();
            tasks.put(_execTime, taskList);
        }
        taskList.add(_task);
    }

    private static void checkScheduledArguments(final Task _task, final int _timeout) {
        Preconditions.checkNotNull(_task, "Task must not be null object.");
        Preconditions.checkArgument(_timeout > 0, String.format("Timeout must be > 0, task = [%s]",
                _task.toString()));
    }

    /**
     * Scheduler controller object, monitor tasks and execution time.
     * Every sec check tasks and chose only tasks, that are ready for execution.
     * Then transfer that tasks to executor threads.
     *
     * @version 1.00
     * @author manaldush
     */
    private final class SchedulerController implements Runnable {
        private static final int INIT_INDEX = 0;
        private static final long SEC = 1000;
        private int curIndex = INIT_INDEX;
        @Override
        public void run() {
            thrStarted.countDown();
            ImplScheduler scheduler = ImplScheduler.this;
            List<IExecutor> executors = new LinkedList<>();
            int num = configuration.getThreadsNumber();
            IExecutorBuilder builder = configuration.getExecutorBuilder();
            try {
                for (int i = 1; i <= num; i++) {
                    IExecutor executor = builder.build();
                    executor.start();
                    executors.add(executor);
                }
                while (scheduler.state == SchedulerStates.STARTED) {
                    long start = System.currentTimeMillis();
                    SortedMap<Long, List<Task>> curTasks = scheduler.getTasks(start);
                    addTasks(executors, curTasks, false);
                    sleep(start);
                }
                // execute mandatory tasks
                addTasks(executors, scheduler.getTasks(), true);
                for (int i = 0; i < num; i++) {
                    executors.get(i).stop();
                }
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

        private int getCurIndex() {
            if (curIndex == configuration.getThreadsNumber()) {
                curIndex = INIT_INDEX;
                return curIndex;
            } else {
                int i = curIndex;
                curIndex++;
                return i;
            }
        }

        private void sleep(final long _start) throws InterruptedException {
            long end = System.currentTimeMillis();
            long delta = end - _start;
            if (delta >= SEC) {
                return;
            }
            long secEnd = ImplScheduler.this.convertToSec(end);
            long secStart = ImplScheduler.this.convertToSec(_start);
            if ((secEnd - secStart) >= 1) {
                return;
            }
            sleep((secEnd + 1) * SEC - end);
        }

        private void addTasks(final List<IExecutor> _executors, final SortedMap<Long, List<Task>> _curTasks,
                              final boolean _mandatory) throws InterruptedException {
            if (_curTasks == null || _curTasks.isEmpty()) return;
            for (Map.Entry<Long, List<Task>> entry : _curTasks.entrySet()) {
                List<Task> tasksList = entry.getValue();
                for (Task task : tasksList) {
                    if (task.isCanceled()) {
                        continue;
                    }
                    if (!_mandatory || task.isMandatory()) {
                        boolean b = true;
                        while (b) {
                            if (_executors.get(getCurIndex()).addTask(task)) {
                                b = false;
                            }
                        }
                    }
                }
            }
        }
    }
}
