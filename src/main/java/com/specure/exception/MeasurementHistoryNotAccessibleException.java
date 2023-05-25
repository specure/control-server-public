package com.specure.exception;

import com.specure.constant.ErrorMessage;

public class MeasurementHistoryNotAccessibleException extends RuntimeException {
    public MeasurementHistoryNotAccessibleException(){
        super(ErrorMessage.MEASUREMENT_HISTORY_IS_NOT_AVAILABLE); // this message was simplified by product owner request
    }
}
