package org.test.exceptions;


public class TimeoutTaskException extends Exception {

    private String message;

    public TimeoutTaskException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
