package com.specure.exception;

import com.specure.constant.ErrorMessage;

public class ClientNotFoundException extends RuntimeException {

    public ClientNotFoundException(String uuid) {
        super(String.format(ErrorMessage.CLIENT_NOT_FOUND, uuid));
    }
}
