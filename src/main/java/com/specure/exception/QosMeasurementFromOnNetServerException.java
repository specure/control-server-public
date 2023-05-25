package com.specure.exception;

import com.specure.constant.ErrorMessage;

public class QosMeasurementFromOnNetServerException extends RuntimeException {
    public QosMeasurementFromOnNetServerException(String uuid, long serverId){
        super(String.format(ErrorMessage.QOS_MEASUREMENT_FROM_ON_NET_SERVER, uuid, serverId));
    }
}
