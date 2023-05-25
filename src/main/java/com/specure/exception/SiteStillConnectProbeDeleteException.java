package com.specure.exception;

import com.specure.constant.ErrorMessage;

public class SiteStillConnectProbeDeleteException extends RuntimeException {

    public SiteStillConnectProbeDeleteException(String id) {
        super(String.format(ErrorMessage.PROBE_STILL_CONNECT_TO_SITE, id));
    }
}
