package com.specure.mapper.core;

import com.specure.common.model.jpa.qos.TracerouteTestResult;
import com.specure.request.core.measurement.qos.request.TracerouteTestResultRequest;
import org.mapstruct.Mapper;

import java.sql.Timestamp;
import java.util.Date;

@Mapper(componentModel = "spring")
public interface TracerouteTestResultMapper {

    TracerouteTestResult tracerouteTestResultRequestToWebsiteTestResult(TracerouteTestResultRequest tracerouteTestResultRequest);

    static Date millisToDate(long time) {
        return new Timestamp(time);
    }
}
