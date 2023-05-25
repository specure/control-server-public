package com.specure.controller.sah;

import com.specure.common.config.MeasurementServerConfig;
import com.specure.constant.URIConstants;
import com.specure.common.model.jpa.Measurement;
import com.specure.request.core.MeasurementQosRequest;
import com.specure.request.core.MeasurementRegistrationForProbeRequest;
import com.specure.request.core.MeasurementRequest;
import com.specure.response.core.MeasurementRegistrationResponse;
import com.specure.response.core.MeasurementResultRMBTClientResponse;
import com.specure.service.core.MeasurementServerService;
import com.specure.service.core.MeasurementService;
import com.specure.service.core.impl.MeasurementQosServiceImpl;
import com.specure.service.sah.BasicTestService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
public class SahMeasurementController {

    private final MeasurementService basicMeasurementService;
    @Qualifier("sahBasicTestService")
    private final BasicTestService sahBasicTestService;
    private final MeasurementServerConfig measurementServerConfig;
    private final MeasurementServerService sahMeasurementServerService;
    @Qualifier("sahMeasurementQosService")
    private final MeasurementQosServiceImpl sahMeasurementQosService;

    @ApiOperation("Return test servers and settings for measurements.")
    @PostMapping(value = URIConstants.TEST_REQUEST)
    public MeasurementRegistrationResponse registrationMeasurement(@Validated @RequestBody MeasurementRegistrationForProbeRequest measurementServerMeasurePrepareRequest, @RequestHeader Map<String, String> headers) {
        var serverProbeAndPortData = sahMeasurementServerService.getDataFromProbeMeasurementRegistrationRequest(measurementServerMeasurePrepareRequest);
        return basicMeasurementService.registerMeasurement(serverProbeAndPortData, headers);
    }

    @ApiOperation("Save measurements results.")
    @PostMapping(value = {URIConstants.MEASUREMENT_RESULT, URIConstants.MOBILE + URIConstants.MEASUREMENT_RESULT})
    public MeasurementResultRMBTClientResponse saveMeasurementResult(@Validated @RequestBody MeasurementRequest measurementRequest, @RequestHeader Map<String, String> headers) {
        final String historyUrl = measurementServerConfig.getHost() + URIConstants.EN_HISTORY;

        final Measurement measurement = basicMeasurementService.partialUpdateMeasurementFromProbeResult(measurementRequest, headers);

        sahBasicTestService.saveMeasurementToElastic(measurement);

        return MeasurementResultRMBTClientResponse.builder()
                .rmbtResultUrl(historyUrl)
                .error(Collections.emptyList())
                .build();
    }

    @ApiOperation("Save QoS measurements results.")
    @PostMapping(value = {URIConstants.MEASUREMENT_RESULT_QOS, URIConstants.MOBILE + URIConstants.MEASUREMENT_RESULT_QOS})
    public MeasurementResultRMBTClientResponse saveMeasurementQOSResult(@Validated @RequestBody MeasurementQosRequest measurementQosRequest) {
        final String historyUrl = measurementServerConfig.getHost() + URIConstants.EN_HISTORY;
        sahMeasurementQosService.saveMeasurementQos(measurementQosRequest);

        return MeasurementResultRMBTClientResponse.builder()
                .rmbtResultUrl(historyUrl)
                .error(Collections.emptyList())
                .build();
    }
}
