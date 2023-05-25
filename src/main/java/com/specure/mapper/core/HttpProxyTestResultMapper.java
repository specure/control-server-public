package com.specure.mapper.core;

import com.specure.common.model.jpa.qos.HttpProxyTestResult;
import com.specure.request.core.measurement.qos.request.HttpProxyTestResultRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface HttpProxyTestResultMapper {
    HttpProxyTestResult httpProxyTestResultRequestToHttpProxyTestResult(HttpProxyTestResultRequest httpProxyTestResultRequest);
}
