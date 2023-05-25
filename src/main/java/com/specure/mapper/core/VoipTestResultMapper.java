package com.specure.mapper.core;

import com.specure.common.model.jpa.qos.VoipTestResult;
import com.specure.request.core.measurement.qos.request.VoipTestResultRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VoipTestResultMapper {
    VoipTestResult voipTestResultRequestToVoipTestResult(VoipTestResultRequest voipTestResultRequest);
}
