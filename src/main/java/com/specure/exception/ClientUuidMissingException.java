package com.specure.exception;

import com.specure.constant.ErrorMessage;

public class ClientUuidMissingException extends RuntimeException {
    public ClientUuidMissingException() {
        super(ErrorMessage.CLIENT_UUID_REQUIRED);
    }
}
