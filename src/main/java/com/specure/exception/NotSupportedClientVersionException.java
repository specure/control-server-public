package com.specure.exception;

import com.specure.constant.ErrorMessage;

public class NotSupportedClientVersionException extends RuntimeException {

    public NotSupportedClientVersionException(String name) {
        super(String.format(ErrorMessage.UNSUPPORTED_CLIENT_NAME, name));
    }
}
