package com.specure.exception;

public class WrongTenantException extends RuntimeException {

    public WrongTenantException(String message) {
        super(message);
    }
}
