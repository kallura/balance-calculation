package org.test;


import org.test.exceptions.NotFoundTaskException;

import java.util.concurrent.Future;

public interface TaskMonitorExecutor {

    void monitor(Future future, String taskId);

    void cancel(String taskId) throws NotFoundTaskException;
}
