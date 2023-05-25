package com.specure.service.mobile.impl;

import com.specure.common.model.elastic.BasicQosTest;
import com.specure.common.model.elastic.BasicTest;
import com.specure.mapper.mobile.BasicTestMobileMapper;
import com.specure.multitenant.MultiTenantManager;
import com.specure.request.mobile.MeasurementHistoryMobileRequest;
import com.specure.response.mobile.BasicTestHistoryMobileResponse;
import com.specure.response.mobile.BasicTestHistoryMobileResponseContainer;
import com.specure.response.mobile.MeasurementHistoryLoopUuidResponse;
import com.specure.response.sah.RadioSignalResponse;
import com.specure.sah.TestConstants;
import com.specure.service.BasicQosTestService;
import com.specure.service.mobile.MeasurementHistoryMobileService;
import com.specure.service.sah.BasicTestHistoryCacheService;
import com.specure.service.sah.BasicTestService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class MeasurementHistoryMobileServiceImplTest {
    private MeasurementHistoryMobileService measurementHistoryMobileService;

    @MockBean
    private BasicTestService basicTestService;
    @MockBean
    private BasicQosTestService basicQosTestService;
    @MockBean
    private BasicTestMobileMapper basicTestMobileMapper;
    @MockBean
    private MultiTenantManager multiTenantManager;
    @MockBean
    private BasicTestHistoryCacheService basicTestHistoryCacheService;
    @Mock
    private Pageable pageable;
    @Mock
    private MeasurementHistoryMobileRequest measurementHistoryMobileRequest;
    @Mock
    private BasicQosTest basicQosTest;
    @Mock
    private BasicTestHistoryMobileResponse basicTestHistoryMobileResponse;
    @Mock
    private BasicTest basicTest;
    @Mock
    private RadioSignalResponse radioSignalResponse;

    @Before
    public void setUp() {
        measurementHistoryMobileService = new MeasurementHistoryMobileServiceImpl(basicTestService, basicQosTestService,
                basicTestMobileMapper, multiTenantManager, basicTestHistoryCacheService);
    }

    @Test
    public void getMeasurementHistoryMobileResponse_whenCommonData_expectMeasurementHistoryMobileResponse() {
        when(measurementHistoryMobileRequest.getClientUuid()).thenReturn(TestConstants.DEFAULT_CLIENT_UUID_STRING);
        when(basicTest.getOpenTestUuid()).thenReturn(TestConstants.DEFAULT_UUID);
        when(basicQosTest.getOverallQos()).thenReturn(TestConstants.DEFAULT_OVERALL_QOS);
        when(basicQosTest.getOpenTestUuid()).thenReturn(TestConstants.DEFAULT_UUID);
        when(basicTestService.getFilteredBasicTestsByMeasurementHistoryMobileRequest(pageable, measurementHistoryMobileRequest)).thenReturn(new PageImpl<>(List.of(basicTest)));
        when(basicQosTestService.getBasicQosTestsByBasicTestUuid(List.of(TestConstants.DEFAULT_UUID))).thenReturn(List.of(basicQosTest));
        when(basicTestMobileMapper.basicTestResponseToBasicTestHistoryMobileResponse(basicTest, basicQosTest)).thenReturn(basicTestHistoryMobileResponse);

        var response = measurementHistoryMobileService.getMeasurementHistoryMobileResponse(pageable, measurementHistoryMobileRequest);

        assertEquals(List.of(basicTestHistoryMobileResponse), response.getHistory().getContent());
    }

    @Test
    public void getMeasurementHistoryMobileResponseByUuid_whenCacheNotFound_expectBasicTestHistoryMobileResponseFromElastic() {
        when(basicTestService.getBasicTestByUUID(TestConstants.DEFAULT_UUID)).thenReturn(basicTest);
        when(basicQosTestService.getBasicQosTestByOpenTestUuid(TestConstants.DEFAULT_UUID)).thenReturn(basicQosTest);

        when(basicTestMobileMapper.basicTestResponseToBasicTestHistoryMobileResponse(basicTest, basicQosTest)).thenReturn(basicTestHistoryMobileResponse);

        var response = measurementHistoryMobileService.getMeasurementHistoryMobileResponseByUuid(TestConstants.DEFAULT_UUID);

        assertEquals(basicTestHistoryMobileResponse, response);
    }

    @Test
    public void getMeasurementHistoryMobileResponseByUuid_whenExist_expectBasicTestHistoryMobileResponseFromCache() {
        when(basicTestHistoryCacheService.getBasicTestHistoryMobileResponseFromCacheByOpenTestUuid(TestConstants.DEFAULT_OPEN_TEST_UUID_STRING))
                .thenReturn(Optional.of(basicTestHistoryMobileResponse));

        var response = measurementHistoryMobileService.getMeasurementHistoryMobileResponseByUuid(TestConstants.DEFAULT_OPEN_TEST_UUID_STRING);

        assertEquals(basicTestHistoryMobileResponse, response);
        verifyNoInteractions(basicQosTestService);
        verifyNoInteractions(basicTestService);
    }

    @Test
    public void getMeasurementHistoryMobileResponseLoopUuidAggregation_whenCorrectInvocation_expected() {
        var basicTestHistoryMobileResponseContainer = BasicTestHistoryMobileResponseContainer.builder()
                .tests(List.of(basicTestHistoryMobileResponse))
                .build();
        var expectedMeasurementHistoryLoopUuidResponse = MeasurementHistoryLoopUuidResponse.builder()
                .measurements(new PageImpl<>(List.of(basicTestHistoryMobileResponseContainer)))
                .error(Collections.emptyList())
                .build();
        when(basicTestService.getFilteredLoopUuidAggregatedBasicTestsByMeasurementHistoryMobileRequest(pageable, measurementHistoryMobileRequest)).thenReturn(new PageImpl<>(List.of(List.of(basicTest))));
        when(basicTestMobileMapper.basicTestResponseToBasicTestHistoryMobileResponse(basicTest, basicQosTest)).thenReturn(basicTestHistoryMobileResponse);
        when(basicQosTestService.getBasicQosTestsByBasicTestUuid(List.of(TestConstants.DEFAULT_UUID))).thenReturn(List.of(basicQosTest));
        when(basicTest.getOpenTestUuid()).thenReturn(TestConstants.DEFAULT_UUID);
        when(basicQosTest.getOpenTestUuid()).thenReturn(TestConstants.DEFAULT_UUID);

        var result = measurementHistoryMobileService.getMeasurementHistoryMobileResponseLoopUuidAggregation(pageable, measurementHistoryMobileRequest);

        assertEquals(expectedMeasurementHistoryLoopUuidResponse, result);
    }
}
