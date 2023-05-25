package com.specure.mapper.core;

import com.specure.common.model.jpa.qos.WebsiteTestResult;
import com.specure.request.core.measurement.qos.request.WebsiteTestResultRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WebsiteTestResultMapper {
    WebsiteTestResult websiteTestResultRequestToWebsiteTestResult(WebsiteTestResultRequest websiteTestResultRequest);
}
