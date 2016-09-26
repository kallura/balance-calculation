package org.test.exceptions;


public class OverloadException extends Exception {

    private String message;

    public OverloadException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
