package com.specure.mapper.core;

import com.specure.common.model.jpa.qos.DnsTestResult;
import com.specure.request.core.measurement.qos.request.DnsTestResultRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DnsTestResultMapper {
    DnsTestResult dnsTestResultRequestToDnsTestResult(DnsTestResultRequest dnsTestResultRequest);
}
