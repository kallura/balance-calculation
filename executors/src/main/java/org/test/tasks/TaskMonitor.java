package org.test.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class TaskMonitor implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(TaskMonitor.class);

    private final Integer timeout;
    private final Future future;


    public TaskMonitor(Future future, Integer timeout) {
        this.future = future;
        this.timeout = timeout;
    }

    public void run() {
        try {
            future.get(timeout, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        } catch (ExecutionException e) {
            logger.debug(e.getMessage());
        } catch (TimeoutException e) {
            logger.error(e.getMessage());
            future.cancel(true);
        }
    }
}
