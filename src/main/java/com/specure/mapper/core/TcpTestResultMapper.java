package com.specure.mapper.core;

import com.specure.common.model.jpa.qos.TcpTestResult;
import com.specure.request.core.measurement.qos.request.TcpTestResultRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TcpTestResultMapper {
    TcpTestResult tcpTestResultToTcpTestResultRequest(TcpTestResultRequest tcpTestResultRequest);
}
