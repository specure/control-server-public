package com.specure.mapper.core;

import com.specure.common.model.jpa.qos.UdpTestResult;
import com.specure.request.core.measurement.qos.request.UdpTestResultRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UdpTestResultMapper {
    UdpTestResult udpTestResultRequestToUdpTestResult(UdpTestResultRequest udpTestResultRequest);
}
