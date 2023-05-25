package com.specure.service.sah;


import com.specure.request.sah.SignalRequest;
import com.specure.response.sah.SignalResponse;

import javax.servlet.http.HttpServletRequest;

public interface SignalService {

    SignalResponse registerSignal(SignalRequest signalRequest, HttpServletRequest httpServletRequest);
}
