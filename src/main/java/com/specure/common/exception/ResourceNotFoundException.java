package com.specure.common.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message, Object... args) {
        super(String.format(message, args));
    }
}
