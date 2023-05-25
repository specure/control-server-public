package com.specure.common.exception;


import com.specure.common.constant.ErrorMessage;

public class MeasurementNotFoundByUuidException extends RuntimeException {
    public MeasurementNotFoundByUuidException(String uuid) {
        super(ErrorMessage.MEASUREMENT_NOT_FOUND_BY_UUID); // this message was simplified by product owner request
    }
}
