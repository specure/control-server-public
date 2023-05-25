package com.specure.exception;

import com.specure.constant.ErrorMessage;

public class MeasurementFormatException extends RuntimeException {
    public MeasurementFormatException(String wrongFormatMeasurementDescription) {
        super(String.format(ErrorMessage.MEASUREMENT_RESULT_WRONG_FORMAT, wrongFormatMeasurementDescription));
    }
}
