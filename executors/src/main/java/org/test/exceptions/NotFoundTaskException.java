package org.test.exceptions;


public class NotFoundTaskException extends Exception {

    private String message;

    public NotFoundTaskException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
