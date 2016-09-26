package org.test;

import org.test.exceptions.NotFoundTaskException;
import org.test.tasks.TaskMonitor;

import javax.annotation.PreDestroy;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class TaskMonitorExecutorImpl implements TaskMonitorExecutor {

    private final ExecutorService poolExecutors;
    private final Map<String, Future> tasks = new LinkedHashMap<>();
    private final Integer cancelTimeout;

    public TaskMonitorExecutorImpl(Integer cancelTimeout, Integer threadPoolSize) {
        this.cancelTimeout = cancelTimeout;
        this.poolExecutors = Executors.newFixedThreadPool(threadPoolSize);
    }

    @Override
    public void monitor(Future future, String taskId) {
        TaskMonitor taskMonitor = new TaskMonitor(future, cancelTimeout);
        Future monitorFuture = poolExecutors.submit(taskMonitor);
        tasks.put(taskId, monitorFuture);
    }

    @Override
    public void cancel(String taskId) throws NotFoundTaskException {
        Future resultFuture = tasks.get(taskId);
        if (resultFuture == null) {
            throw new NotFoundTaskException("Task with id: " + taskId + " wasn't found!");
        }
        resultFuture.cancel(true);
        tasks.remove(taskId);
    }

    @PreDestroy
    public void cleanUp() {
        poolExecutors.shutdown();
    }
}
