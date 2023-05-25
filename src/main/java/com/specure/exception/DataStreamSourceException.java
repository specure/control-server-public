package com.specure.exception;

import com.specure.constant.ErrorMessage;

public class DataStreamSourceException extends RuntimeException {

    public DataStreamSourceException(String badDataStreamLabel) {
        super(String.format(ErrorMessage.DATA_STREAM_SOURCE, badDataStreamLabel));
    }
}
