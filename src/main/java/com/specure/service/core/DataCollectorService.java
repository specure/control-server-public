package com.specure.service.core;

import com.specure.response.core.DataCollectorResponse;
import com.specure.response.core.IpResponse;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface DataCollectorService {
    DataCollectorResponse extrudeData(HttpServletRequest request, Map<String, String> headers);

    IpResponse getIpVersion(HttpServletRequest request, Map<String, String> headers);

}
