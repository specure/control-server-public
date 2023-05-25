package com.specure.exception;

import com.specure.constant.ErrorMessage;

public class TestTokenMissingException extends RuntimeException {
    public TestTokenMissingException() {
        super(ErrorMessage.TEST_TOKEN_REQUIRED);
    }
}
