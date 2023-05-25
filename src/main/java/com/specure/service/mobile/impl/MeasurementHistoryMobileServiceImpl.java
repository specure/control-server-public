package com.specure.service.mobile.impl;

import com.specure.common.model.elastic.BasicQosTest;
import com.specure.common.model.elastic.BasicTest;
import com.specure.mapper.mobile.BasicTestMobileMapper;
import com.specure.multitenant.MultiTenantManager;
import com.specure.request.mobile.MeasurementHistoryMobileRequest;
import com.specure.response.mobile.BasicTestHistoryMobileResponse;
import com.specure.response.mobile.BasicTestHistoryMobileResponseContainer;
import com.specure.response.mobile.MeasurementHistoryLoopUuidResponse;
import com.specure.response.mobile.MeasurementHistoryMobileResponse;
import com.specure.service.BasicQosTestService;
import com.specure.service.mobile.MeasurementHistoryMobileService;
import com.specure.service.sah.BasicTestHistoryCacheService;
import com.specure.service.sah.BasicTestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MeasurementHistoryMobileServiceImpl implements MeasurementHistoryMobileService {

    private final BasicTestService basicTestService;
    private final BasicQosTestService basicQosTestService;
    private final BasicTestMobileMapper basicTestMobileMapper;
    private final MultiTenantManager multiTenantManager;
    private final BasicTestHistoryCacheService basicTestHistoryCacheService;

    @Override
    public MeasurementHistoryMobileResponse getMeasurementHistoryMobileResponse(Pageable pageable, MeasurementHistoryMobileRequest measurementHistoryMobileRequest) {
        log.debug("MeasurementHistoryMobileServiceImpl:getMeasurementHistoryMobileResponse started with tenant = {}, pageable = {}, measurementHistoryMobileRequest = {}",
                multiTenantManager.getCurrentTenant(), pageable, measurementHistoryMobileRequest);
        Page<BasicTest> basicTestResponsePage = basicTestService.getFilteredBasicTestsByMeasurementHistoryMobileRequest(pageable, measurementHistoryMobileRequest);

        List<String> openTestUuids = basicTestResponsePage.getContent().stream()
                .map(BasicTest::getOpenTestUuid)
                .collect(Collectors.toList());

        Map<String, BasicQosTest> basicQosTestResponses = basicQosTestService.getBasicQosTestsByBasicTestUuid(openTestUuids).stream()
                .collect(Collectors.toMap(BasicQosTest::getOpenTestUuid, Function.identity()));

        Page<BasicTestHistoryMobileResponse> basicTestHistoryMobileResponsePage = basicTestResponsePage
                .map(basicTestResponse -> basicTestMobileMapper.basicTestResponseToBasicTestHistoryMobileResponse(basicTestResponse, basicQosTestResponses.get(basicTestResponse.getOpenTestUuid())));

        MeasurementHistoryMobileResponse measurementHistoryMobileResponse = MeasurementHistoryMobileResponse.builder()
                .history(basicTestHistoryMobileResponsePage)
                .error(Collections.emptyList())
                .build();
        log.debug("MeasurementHistoryMobileServiceImpl:getMeasurementHistoryMobileResponse finished with tenant = {}, response = {}", multiTenantManager.getCurrentTenant(), measurementHistoryMobileResponse);
        return measurementHistoryMobileResponse;
    }

    @Override
    public BasicTestHistoryMobileResponse getMeasurementHistoryMobileResponseByUuid(String uuid) {
        log.debug("MeasurementHistoryMobileServiceImpl:getMeasurementHistoryMobileResponseByUuid started with tenant = {}, uuid = {}", multiTenantManager.getCurrentTenant(), uuid);
        return basicTestHistoryCacheService.getBasicTestHistoryMobileResponseFromCacheByOpenTestUuid(uuid)
                .orElseGet(() -> getBasicTestHistoryMobileResponseFromElastic(uuid));
    }

    @Override
    public MeasurementHistoryLoopUuidResponse getMeasurementHistoryMobileResponseLoopUuidAggregation(Pageable pageable, MeasurementHistoryMobileRequest measurementHistoryMobileRequest) {
        Page<List<BasicTest>> basicTestResponsePage = basicTestService.getFilteredLoopUuidAggregatedBasicTestsByMeasurementHistoryMobileRequest(pageable, measurementHistoryMobileRequest);

        List<String> openTestUuids = basicTestResponsePage.getContent().stream()
                .flatMap(Collection::stream)
                .map(BasicTest::getOpenTestUuid)
                .collect(Collectors.toList());

        Map<String, BasicQosTest> basicQosTestResponses = basicQosTestService.getBasicQosTestsByBasicTestUuid(openTestUuids).stream()
                .collect(Collectors.toMap(BasicQosTest::getOpenTestUuid, Function.identity()));

        Page<BasicTestHistoryMobileResponseContainer> basicTestHistoryMobileResponsePage = basicTestResponsePage
                .map(x -> getListBasicTestHistoryMobileResponseFunction(x, basicQosTestResponses));

        return MeasurementHistoryLoopUuidResponse.builder()
                .measurements(basicTestHistoryMobileResponsePage)
                .error(Collections.emptyList())
                .build();
    }

    private BasicTestHistoryMobileResponseContainer getListBasicTestHistoryMobileResponseFunction(List<BasicTest> basicTests, Map<String, BasicQosTest> basicQosTestResponses) {
        List<BasicTestHistoryMobileResponse> basicTestHistoryMobileResponses = basicTests.stream()
                .map(basicTest -> basicTestMobileMapper.basicTestResponseToBasicTestHistoryMobileResponse(basicTest, basicQosTestResponses.get(basicTest.getOpenTestUuid())))
                .collect(Collectors.toList());
        return BasicTestHistoryMobileResponseContainer.builder()
                .tests(basicTestHistoryMobileResponses)
                .build();
    }

    private BasicTestHistoryMobileResponse getBasicTestHistoryMobileResponseFromElastic(String uuid) {
        BasicTest basicTest = basicTestService.getBasicTestByUUID(uuid);
        BasicQosTest basicQosTest = basicQosTestService.getBasicQosTestByOpenTestUuid(uuid);

        return basicTestMobileMapper.basicTestResponseToBasicTestHistoryMobileResponse(basicTest, basicQosTest);
    }
}
