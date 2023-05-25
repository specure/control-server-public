package com.specure.mapper.core;

import com.specure.common.model.jpa.qos.NonTransparentProxyTestResult;
import com.specure.request.core.measurement.qos.request.NonTransparentProxyTestResultRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NonTransparentProxyTestResultMapper {
    NonTransparentProxyTestResult nonTransparentProxyTestResultRequestToNonTransparentProxyTestResult(NonTransparentProxyTestResultRequest nonTransparentProxyTestResultRequest);
}
