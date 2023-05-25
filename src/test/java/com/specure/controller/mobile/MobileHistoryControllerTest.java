package com.specure.controller.mobile;

import com.specure.advice.SahBackendAdvice;
import com.specure.constant.URIConstants;
import com.specure.common.model.dto.TestResultCounter;
import com.specure.request.mobile.MeasurementHistoryMobileRequest;
import com.specure.response.mobile.BasicTestHistoryMobileResponse;
import com.specure.response.mobile.BasicTestHistoryMobileResponseContainer;
import com.specure.response.mobile.MeasurementHistoryLoopUuidResponse;
import com.specure.response.mobile.MeasurementHistoryMobileResponse;
import com.specure.sah.TestConstants;
import com.specure.sah.TestUtils;
import com.specure.service.mobile.MeasurementHistoryMobileService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
public class MobileHistoryControllerTest {
    private MockMvc mockMvc;

    @MockBean
    private MeasurementHistoryMobileService measurementHistoryMobileService;

    @Before
    public void setUp() {
        MobileHistoryController mobileHistoryController = new MobileHistoryController(measurementHistoryMobileService);
        mockMvc = MockMvcBuilders.standaloneSetup(mobileHistoryController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setControllerAdvice(new SahBackendAdvice())
                .build();
    }

    @Test
    public void processResult_whenCommonRequest_expectMeasurementHistoryMobileResponse() throws Exception {
        var request = getMeasurementHistoryMobileRequest();
        var response = getMeasurementHistoryMobileResponse();
        when(measurementHistoryMobileService.getMeasurementHistoryMobileResponse(any(), eq(request))).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post(URIConstants.MOBILE + URIConstants.HISTORY)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.asJsonString(request)))
                .andExpect(jsonPath("$.history.content[0].test_uuid").value(TestConstants.DEFAULT_UUID))
                .andExpect(jsonPath("$.history.content[0].speed_upload").value(TestConstants.DEFAULT_UPLOAD_SPEED))
                .andExpect(jsonPath("$.history.content[0].speed_download").value(TestConstants.DEFAULT_DOWNLOAD_SPEED))
                .andExpect(jsonPath("$.history.content[0].ping").value(TestConstants.DEFAULT_PING))
                .andExpect(jsonPath("$.history.content[0].voip_result_jitter_millis").value(TestConstants.DEFAULT_JITTER))
                .andExpect(jsonPath("$.history.content[0].voip_result_packet_loss_percents").value(TestConstants.DEFAULT_PACKET_LOSS))
                .andExpect(jsonPath("$.history.content[0].network_type").value(TestConstants.DEFAULT_NETWORK_TYPE_LTE.getCategory()))
                .andExpect(jsonPath("$.history.content[0].qos").value(TestConstants.DEFAULT_OVERALL_QOS_PERCENTAGE))
                .andExpect(jsonPath("$.history.content[0].qosTestResultCounters[0].successCount").value(TestConstants.DEFAULT_SUCCESS_COUNT))
                .andExpect(jsonPath("$.history.content[0].qosTestResultCounters[0].totalCount").value(TestConstants.DEFAULT_TOTAL_COUNT))
                .andExpect(jsonPath("$.history.content[0].qosTestResultCounters[0].testType").value(TestConstants.DEFAULT_TEST_TYPE.getValue()))
                .andExpect(jsonPath("$.history.content[0].measurement_date").value(TestConstants.DEFAULT_MEASUREMENT_DATE))
                .andExpect(jsonPath("$.history.content[0].device").value(TestConstants.DEFAULT_DEVICE))
                .andExpect(jsonPath("$.history.content[0].appVersion").value(TestConstants.DEFAULT_APP_VERSION))
                .andExpect(jsonPath("$.history.content[0].platform").value(TestConstants.DEFAULT_TEST_PLATFORM.name()))
                .andExpect(status().isOk());

        verify(measurementHistoryMobileService).getMeasurementHistoryMobileResponse(any(), eq(request));
    }

    @Test
    public void processResult_whenCommonRequest_expectBasicTestHistoryMobileResponse() throws Exception {
        when(measurementHistoryMobileService.getMeasurementHistoryMobileResponseByUuid(TestConstants.DEFAULT_UUID)).thenReturn(getBasicTestHistoryMobileByUuidResponse());

        mockMvc.perform(MockMvcRequestBuilders.get(URIConstants.MOBILE + URIConstants.HISTORY_BY_UUID, TestConstants.DEFAULT_UUID))
                .andDo(print())
                .andExpect(jsonPath("$.test_uuid").value(TestConstants.DEFAULT_UUID))
                .andExpect(jsonPath("$.speed_upload").value(TestConstants.DEFAULT_UPLOAD_SPEED))
                .andExpect(jsonPath("$.speed_download").value(TestConstants.DEFAULT_DOWNLOAD_SPEED))
                .andExpect(jsonPath("$.ping").value(TestConstants.DEFAULT_PING))
                .andExpect(jsonPath("$.voip_result_jitter_millis").value(TestConstants.DEFAULT_JITTER))
                .andExpect(jsonPath("$.voip_result_packet_loss_percents").value(TestConstants.DEFAULT_PACKET_LOSS))
                .andExpect(jsonPath("$.network_type").value(TestConstants.DEFAULT_NETWORK_TYPE_LTE.getCategory()))
                .andExpect(jsonPath("$.qos").value(TestConstants.DEFAULT_OVERALL_QOS_PERCENTAGE))
                .andExpect(jsonPath("$.qosTestResultCounters[0].successCount").value(TestConstants.DEFAULT_SUCCESS_COUNT))
                .andExpect(jsonPath("$.qosTestResultCounters[0].totalCount").value(TestConstants.DEFAULT_TOTAL_COUNT))
                .andExpect(jsonPath("$.qosTestResultCounters[0].testType").value(TestConstants.DEFAULT_TEST_TYPE.getValue()))
                .andExpect(jsonPath("$.measurement_date").value(TestConstants.DEFAULT_MEASUREMENT_DATE))
                .andExpect(jsonPath("$.device").value(TestConstants.DEFAULT_DEVICE))
                .andExpect(jsonPath("$.device").value(TestConstants.DEFAULT_DEVICE))
                .andExpect(jsonPath("$.appVersion").value(TestConstants.DEFAULT_APP_VERSION))
                .andExpect(jsonPath("$.platform").value(TestConstants.DEFAULT_TEST_PLATFORM.name()))
                .andExpect(status().isOk());
    }

    @Test
    public void getMobileHistoryLoopUuidAggregation_correctInvocation_expecetedMeasurementHistoryLoopUuidResponse() throws Exception {
        var request = getMeasurementHistoryMobileRequest();
        var response = getMeasurementHistoryLoopUuidResponse();
        when(measurementHistoryMobileService.getMeasurementHistoryMobileResponseLoopUuidAggregation(any(), eq(request))).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post(URIConstants.V2 + URIConstants.MOBILE + URIConstants.HISTORY)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.asJsonString(request)))
                .andDo(print())
                .andExpect(jsonPath("$.measurements.content[0].tests[0].test_uuid").value(TestConstants.DEFAULT_UUID))
                .andExpect(jsonPath("$.measurements.content[0].tests[0].speed_upload").value(TestConstants.DEFAULT_UPLOAD_SPEED))
                .andExpect(jsonPath("$.measurements.content[0].tests[0].speed_download").value(TestConstants.DEFAULT_DOWNLOAD_SPEED))
                .andExpect(jsonPath("$.measurements.content[0].tests[0].ping").value(TestConstants.DEFAULT_PING))
                .andExpect(jsonPath("$.measurements.content[0].tests[0].voip_result_jitter_millis").value(TestConstants.DEFAULT_JITTER))
                .andExpect(jsonPath("$.measurements.content[0].tests[0].voip_result_packet_loss_percents").value(TestConstants.DEFAULT_PACKET_LOSS))
                .andExpect(jsonPath("$.measurements.content[0].tests[0].network_type").value(TestConstants.DEFAULT_NETWORK_TYPE_LTE.getCategory()))
                .andExpect(jsonPath("$.measurements.content[0].tests[0].qos").value(TestConstants.DEFAULT_OVERALL_QOS_PERCENTAGE))
                .andExpect(jsonPath("$.measurements.content[0].tests[0].qosTestResultCounters[0].successCount").value(TestConstants.DEFAULT_SUCCESS_COUNT))
                .andExpect(jsonPath("$.measurements.content[0].tests[0].qosTestResultCounters[0].totalCount").value(TestConstants.DEFAULT_TOTAL_COUNT))
                .andExpect(jsonPath("$.measurements.content[0].tests[0].qosTestResultCounters[0].testType").value(TestConstants.DEFAULT_TEST_TYPE.getValue()))
                .andExpect(jsonPath("$.measurements.content[0].tests[0].measurement_date").value(TestConstants.DEFAULT_MEASUREMENT_DATE))
                .andExpect(jsonPath("$.measurements.content[0].tests[0].device").value(TestConstants.DEFAULT_DEVICE))
                .andExpect(jsonPath("$.measurements.content[0].tests[0].appVersion").value(TestConstants.DEFAULT_APP_VERSION))
                .andExpect(jsonPath("$.measurements.content[0].tests[0].platform").value(TestConstants.DEFAULT_TEST_PLATFORM.name()))
                .andExpect(status().isOk());

        verify(measurementHistoryMobileService).getMeasurementHistoryMobileResponseLoopUuidAggregation(any(Pageable.class), eq(request));
    }

    private MeasurementHistoryLoopUuidResponse getMeasurementHistoryLoopUuidResponse() {
        BasicTestHistoryMobileResponseContainer basicTestHistoryMobileResponseContainer = getBasicTestHistoryMobileResponseContainer();
        return MeasurementHistoryLoopUuidResponse.builder()
                .measurements(new PageImpl<>(List.of(basicTestHistoryMobileResponseContainer)))
                .error(Collections.emptyList())
                .build();
    }
    private MeasurementHistoryMobileResponse getMeasurementHistoryMobileResponse() {
        BasicTestHistoryMobileResponse basicTestHistoryMobileResponse = getBasicTestHistoryMobileResponse();
        return MeasurementHistoryMobileResponse.builder()
                .history(new PageImpl<>(List.of(basicTestHistoryMobileResponse)))
                .error(Collections.emptyList())
                .build();
    }

    private BasicTestHistoryMobileResponseContainer getBasicTestHistoryMobileResponseContainer() {
        return BasicTestHistoryMobileResponseContainer.builder()
                .tests(List.of(getBasicTestHistoryMobileResponse()))
                .build();
    }

    private BasicTestHistoryMobileResponse getBasicTestHistoryMobileResponse() {
        return BasicTestHistoryMobileResponse.builder()
                .device(TestConstants.DEFAULT_DEVICE)
                .openTestUuid(TestConstants.DEFAULT_UUID)
                .measurementDate(TestConstants.DEFAULT_MEASUREMENT_DATE)
                .networkType(TestConstants.DEFAULT_NETWORK_TYPE_LTE_CATEGORY)
                .downloadSpeed(TestConstants.DEFAULT_DOWNLOAD_SPEED)
                .jitter(TestConstants.DEFAULT_JITTER)
                .packetLoss(TestConstants.DEFAULT_PACKET_LOSS)
                .ping(TestConstants.DEFAULT_PING)
                .qos(TestConstants.DEFAULT_OVERALL_QOS_PERCENTAGE)
                .qosTestResultCounters(getTestResultCounterDtoList())
                .uploadSpeed(TestConstants.DEFAULT_UPLOAD_SPEED)
                .appVersion(TestConstants.DEFAULT_APP_VERSION)
                .platform(TestConstants.DEFAULT_TEST_PLATFORM.name())
                .build();
    }

    private BasicTestHistoryMobileResponse getBasicTestHistoryMobileByUuidResponse() {
        return BasicTestHistoryMobileResponse.builder()
                .device(TestConstants.DEFAULT_DEVICE)
                .openTestUuid(TestConstants.DEFAULT_UUID)
                .measurementDate(TestConstants.DEFAULT_MEASUREMENT_DATE)
                .networkType(TestConstants.DEFAULT_NETWORK_TYPE_LTE_CATEGORY)
                .downloadSpeed(TestConstants.DEFAULT_DOWNLOAD_SPEED)
                .jitter(TestConstants.DEFAULT_JITTER)
                .packetLoss(TestConstants.DEFAULT_PACKET_LOSS)
                .ping(TestConstants.DEFAULT_PING)
                .qos(TestConstants.DEFAULT_OVERALL_QOS_PERCENTAGE)
                .qosTestResultCounters(getTestResultCounterDtoList())
                .uploadSpeed(TestConstants.DEFAULT_UPLOAD_SPEED)
                .appVersion(TestConstants.DEFAULT_APP_VERSION)
                .platform(TestConstants.DEFAULT_TEST_PLATFORM.name())
                .build();
    }

    private MeasurementHistoryMobileRequest getMeasurementHistoryMobileRequest() {
        return MeasurementHistoryMobileRequest.builder()
                .clientUuid(TestConstants.DEFAULT_CLIENT_UUID_STRING)
                .devices(List.of(TestConstants.DEFAULT_DEVICE))
                .networkTypes(List.of(TestConstants.DEFAULT_NETWORK_TYPE_LTE_CATEGORY))
                .build();
    }

    private List<TestResultCounter> getTestResultCounterDtoList() {
        return List.of(
                TestResultCounter.builder()
                        .testType(TestConstants.DEFAULT_TEST_TYPE)
                        .successCount(TestConstants.DEFAULT_SUCCESS_COUNT)
                        .totalCount(TestConstants.DEFAULT_TOTAL_COUNT)
                        .build()
        );
    }
}
