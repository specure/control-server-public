package com.specure.exception;

import com.specure.constant.ErrorMessage;

public class QoSMeasurementServerNotFoundByUuidException extends RuntimeException {

    public QoSMeasurementServerNotFoundByUuidException(String uuid) {
        super(String.format(ErrorMessage.QOS_MEASUREMENT_SERVER_FOR_UUID_NOT_FOUND, uuid));
    }
}
