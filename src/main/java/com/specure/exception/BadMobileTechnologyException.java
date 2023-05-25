package com.specure.exception;

import static com.specure.constant.ErrorMessage.WRONG_MOBILE_TECHNOLOGY_PARAMETER;

public class BadMobileTechnologyException extends RuntimeException {

    public BadMobileTechnologyException(String mobileTechnology) {
        super(String.format(WRONG_MOBILE_TECHNOLOGY_PARAMETER, mobileTechnology));
    }

}
