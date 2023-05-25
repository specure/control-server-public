package com.specure.common.exception;


import com.specure.common.constant.ErrorMessage;

public class MobileModelNotFoundException extends RuntimeException{

    public MobileModelNotFoundException(Long id) {
        super(String.format(ErrorMessage.MOBILE_MODEL_NOT_FOUND_BY_ID,id));
    }

    public MobileModelNotFoundException(String id) {
        super(String.format(ErrorMessage.MOBILE_MODEL_NOT_FOUND_BY_MODEL,id));
    }
}
