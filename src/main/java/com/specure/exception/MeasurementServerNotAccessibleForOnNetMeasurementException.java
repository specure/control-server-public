package com.specure.exception;

import com.specure.constant.ErrorMessage;

public class MeasurementServerNotAccessibleForOnNetMeasurementException extends RuntimeException {
    public MeasurementServerNotAccessibleForOnNetMeasurementException() {
        super(ErrorMessage.MEASUREMENT_SERVER_NOT_ACCESSIBLE_FOR_ON_NET);
    }
}
