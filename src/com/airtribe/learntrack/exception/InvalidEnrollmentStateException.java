package com.airtribe.learntrack.exception;

public class InvalidEnrollmentStateException extends Exception {
    private static final long serialVersionUID = 1L;

    public InvalidEnrollmentStateException(String message) {
        super(message);
    }

    public InvalidEnrollmentStateException(String message, Throwable cause) {
        super(message, cause);
    }
}
