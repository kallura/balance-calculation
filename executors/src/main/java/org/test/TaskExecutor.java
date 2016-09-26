package org.test;

import org.task.models.Balance;
import org.test.exceptions.NotFoundTaskException;
import org.test.exceptions.OverloadException;
import org.test.exceptions.TimeoutTaskException;
import org.test.tasks.Task;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public interface TaskExecutor {

    String execute(Task task) throws OverloadException;

    Balance get(String taskId) throws InterruptedException, ExecutionException, TimeoutException, NotFoundTaskException, TimeoutTaskException;

    void cancel(String taskId) throws NotFoundTaskException;
}
