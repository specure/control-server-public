package com.specure.exception;

import com.specure.constant.ErrorMessage;

public class NationalOperatorNotFoundException extends RuntimeException {

    public NationalOperatorNotFoundException(Long id) {
        super(String.format(ErrorMessage.NATIONAL_OPERATOR_NOT_FOUND_BY_ID, id));
    }
}
