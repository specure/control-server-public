package com.specure.exception;

import com.specure.constant.ErrorMessage;

public class ProviderNotFoundByIdException extends RuntimeException {
    public ProviderNotFoundByIdException(Long id) {
        super(String.format(ErrorMessage.PROVIDER_NOT_FOUND_BY_ID, id));
    }
}
