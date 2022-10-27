package com.schmix.branch.exceptions;

public class BadUpstreamResponseException extends RuntimeException {
    public BadUpstreamResponseException() {
        super();
    }

    public BadUpstreamResponseException(String message) {
        super(message);
    }

    public BadUpstreamResponseException(String message, Throwable cause) {
        super(message, cause);
    }

    public BadUpstreamResponseException(Throwable cause) {
        super(cause);
    }
}
