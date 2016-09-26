package org.task.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.test.exceptions.NotFoundTaskException;
import org.test.exceptions.OverloadException;
import org.test.exceptions.TimeoutTaskException;

import java.util.concurrent.CancellationException;

@ControllerAdvice
public class ExceptionTaskHandler {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionTaskHandler.class);

    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Task not found")
    @ExceptionHandler(NotFoundTaskException.class)
    public void taskNotFoundException(NotFoundTaskException e) {
        logger.info(e.getMessage(), e);
    }

    @ResponseStatus(value = HttpStatus.REQUEST_TIMEOUT, reason = "Read timeout")
    @ExceptionHandler(TimeoutTaskException.class)
    public void taskTimeoutException(TimeoutTaskException e) {
        logger.warn(e.getMessage(), e);
    }

    @ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "Task was canceled")
    @ExceptionHandler(CancellationException.class)
    public void cancelException(CancellationException e) {
        logger.warn(e.getMessage(), e);
    }

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Server error")
    @ExceptionHandler(RuntimeException.class)
    public void serverException(RuntimeException e) {
        logger.error(e.getMessage(), e);
    }

    @ResponseStatus(value = HttpStatus.SERVICE_UNAVAILABLE, reason = "The server is currently unable!")
    @ExceptionHandler(OverloadException.class)
    public void serverOverloadException(OverloadException e) {
        logger.error(e.getMessage(), e);
    }
}
