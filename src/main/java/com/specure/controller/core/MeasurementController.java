package com.specure.controller.core;

import com.specure.constant.MeasurementServerConstants;
import com.specure.constant.URIConstants;
import com.specure.request.core.MeasurementQosParametersRequest;
import com.specure.request.core.MeasurementRegistrationForAdminRequest;
import com.specure.request.core.MeasurementRegistrationForWebClientRequest;
import com.specure.response.core.MeasurementHistoryResponse;
import com.specure.response.core.MeasurementRegistrationResponse;
import com.specure.response.core.measurement.qos.response.MeasurementQosParametersResponse;
import com.specure.service.core.MeasurementQosService;
import com.specure.service.core.MeasurementServerService;
import com.specure.service.core.MeasurementService;
import com.specure.service.core.SettingsService;
import com.specure.common.constant.AdminSetting;
import com.specure.service.sah.BasicTestService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@Slf4j
public class MeasurementController {
    private final MeasurementService measurementService;
    @Qualifier("basicMeasurementQosService")
    private final MeasurementQosService basicMeasurementQosService;
    @Qualifier("basicMeasurementServerService")
    private final MeasurementServerService basicMeasurementServerService;
    private final BasicTestService basicTestService;
    private final SettingsService settingsService;


    @ApiOperation("Return test servers and settings for web client measurements.")
    @PostMapping(URIConstants.TEST_REQUEST_FOR_WEB_CLIENT)
    public MeasurementRegistrationResponse registerMeasurementForWebClient(@Validated @RequestBody MeasurementRegistrationForWebClientRequest measurementRegistrationForWebClientRequest, @RequestHeader Map<String, String> headers) {
        var onlyMeasurementServerData = basicMeasurementServerService.getMeasurementServerForWebClient(measurementRegistrationForWebClientRequest);
        var response = measurementService.registerMeasurement(onlyMeasurementServerData, headers);
        response.setTestNumThreads(getTestNumThreadsWebFromSetting());
        return response;
    }

    @ApiOperation("Return test servers chosen by admin and settings for admin client measurements.")
    @PostMapping(URIConstants.TEST_REQUEST_FOR_ADMIN)
    public MeasurementRegistrationResponse registerMeasurementForAdmin(@Validated @RequestBody MeasurementRegistrationForAdminRequest measurementRegistrationForAdminRequest, @RequestHeader Map<String, String> headers) {
        var dataWithServerChosenByAdminForMeasurement = basicMeasurementServerService.getMeasurementServerById(measurementRegistrationForAdminRequest);
        var response = measurementService.registerMeasurement(dataWithServerChosenByAdminForMeasurement, headers);
        response.setTestNumThreads(getTestNumThreadsWebFromSetting());
        return response;
    }


    @ApiOperation("Provide parameters for QoS measurements.")
    @PostMapping(URIConstants.MEASUREMENT_QOS_REQUEST)
    public MeasurementQosParametersResponse provideMeasurementQoSParameters(@Validated @RequestBody MeasurementQosParametersRequest measurementQosParametersRequest) {
        return basicMeasurementQosService.getQosParameters(measurementQosParametersRequest);
    }

    @ApiOperation("Return comprehensive measurement result from index.")
    @GetMapping(URIConstants.MEASUREMENT_RESULT_BY_UUID)
    public MeasurementHistoryResponse getMeasurementResult(@PathVariable String uuid) throws InterruptedException {
        return basicTestService.getMeasurementDetailByUuidFromElasticSearch(uuid);
    }


    @ApiOperation("Return comprehensive measurement result from DB.")
    @GetMapping(URIConstants.MEASUREMENT_RESULT_BY_UUID_FROM_DB)
    public MeasurementHistoryResponse getMeasurementResultFromDB(@PathVariable String uuid) {
        final MeasurementHistoryResponse measurementHistoryResponse = measurementService.getMeasurementDetailByUuid(uuid);
        measurementHistoryResponse.setMeasurementServerName(basicMeasurementServerService.getMeasurementServerById(measurementHistoryResponse.getMeasurementServerId()).getName());
        return measurementHistoryResponse;


    }

    private Integer getTestNumThreadsWebFromSetting() {
        return Optional.ofNullable(settingsService.getSettingsMap())
                .map(x -> x.get(AdminSetting.MEASUREMENT_NUM_THREADS_WEB_KEY))
                .map(Integer::valueOf)
                .orElse(MeasurementServerConstants.TEST_NUM_THREADS_FOR_WEB);
    }
}
