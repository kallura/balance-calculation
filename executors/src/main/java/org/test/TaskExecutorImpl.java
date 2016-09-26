package org.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.task.models.Balance;
import org.test.exceptions.NotFoundTaskException;
import org.test.exceptions.OverloadException;
import org.test.exceptions.TimeoutTaskException;
import org.test.tasks.Task;

import javax.annotation.PreDestroy;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;

public class TaskExecutorImpl implements TaskExecutor {

    @Autowired
    private TaskMonitorExecutor taskMonitorExecutor;

    private final ExecutorService poolExecutors;
    private final Map<String, Integer> timeouts = new LinkedHashMap<>();
    private final Map<String, Future<Balance>> tasks = new LinkedHashMap<>();
    ;
    private final Integer defaultTimeout;
    private final Integer executorMapSize;

    public TaskExecutorImpl(Integer defaultTimeout, Integer executorMapSize, Integer threadPoolSize) {
        this.defaultTimeout = defaultTimeout;
        this.executorMapSize = executorMapSize;
        poolExecutors = Executors.newFixedThreadPool(threadPoolSize);
    }

    @Override
    public String execute(Task task) throws OverloadException {
        if (tasks.size() > executorMapSize) {
            throw new OverloadException("The server is currently unable!");
        }
        String taskId = UUID.randomUUID().toString();
        Future<Balance> future = poolExecutors.submit(task);
        taskMonitorExecutor.monitor(future, taskId);
        timeouts.put(taskId, task.getTaskConditions().getTimeout());
        tasks.put(taskId, future);
        return taskId;
    }

    @Override
    public Balance get(String taskId) throws NotFoundTaskException, TimeoutTaskException {
        Future<Balance> resultFuture = tasks.get(taskId);
        if (resultFuture == null) {
            throw new NotFoundTaskException("Task with id: " + taskId + " wasn't found!");
        }
        Integer timeout = timeouts.get(taskId);
        try {
            return resultFuture.get(timeout == null ? defaultTimeout : timeout, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e.getMessage());
        } catch (TimeoutException e) {
            throw new TimeoutTaskException(e.getMessage());
        }
    }

    @Override
    public void cancel(String taskId) throws NotFoundTaskException {
        Future<Balance> resultFuture = tasks.get(taskId);
        if (resultFuture == null) {
            throw new NotFoundTaskException("Task with id: " + taskId + " wasn't found!");
        }
        resultFuture.cancel(true);
        taskMonitorExecutor.cancel(taskId);
        tasks.remove(taskId);
        timeouts.remove(taskId);
    }

    @PreDestroy
    public void cleanUp() {
        poolExecutors.shutdown();
    }
}
