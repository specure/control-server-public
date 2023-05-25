package com.specure.exception;

import com.specure.constant.ErrorMessage;

public class MeasurementServerNotFoundByProviderOnOffNetException extends RuntimeException {

    public MeasurementServerNotFoundByProviderOnOffNetException(String providerName, Boolean isOnNet) {
        super(String.format(ErrorMessage.MEASUREMENT_SERVER_ON_OFF_NET_NOT_FOUND, isOnNet, providerName));
    }
}
