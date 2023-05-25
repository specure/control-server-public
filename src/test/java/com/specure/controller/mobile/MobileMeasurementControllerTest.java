package com.specure.controller.mobile;

import com.specure.advice.SahBackendAdvice;
import com.specure.constant.URIConstants;
import com.specure.request.mobile.MeasurementResultMobileRequest;
import com.specure.request.mobile.MobileMeasurementResultDetailRequest;
import com.specure.request.mobile.MobileMeasurementResultRequest;
import com.specure.request.mobile.MobileMeasurementSettingRequest;
import com.specure.response.mobile.MobileMeasurementRegistrationResponse;
import com.specure.sah.TestConstants;
import com.specure.sah.TestUtils;
import com.specure.service.mobile.MobileMeasurementRegistrationService;
import com.specure.service.mobile.MobileMeasurementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
class MobileMeasurementControllerTest {
    private MockMvc mockMvc;

    @InjectMocks
    private MobileMeasurementController mobileMeasurementController;

    @Mock
    private MobileMeasurementRegistrationService mobileMeasurementRegistrationService;
    @Mock
    private MobileMeasurementService mobileMeasurementService;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(mobileMeasurementController)
                .setControllerAdvice(new SahBackendAdvice())
                .build();
    }

    @Test
    void processResult_whenCommonData_expectMeasurementResultMobileResponse() throws Exception {
        var request = MeasurementResultMobileRequest.builder().build();

        mockMvc.perform(MockMvcRequestBuilders.post(URIConstants.MOBILE + URIConstants.RESULT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.asJsonString(request)))
                .andExpect(status().isOk());

        verify(mobileMeasurementService).processMeasurementResult(request, Map.of("Content-Type", "application/json", "Content-Length", "891"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"telephony_network_operator_name", "telephonyNetworkOperatorName", "network_operator_name"})
    void processResult_whenTelephonyNetworkOperatorNameJsonAliasFields_expectMeasurementResultMobileResponse(String alias) throws Exception {
        ArgumentCaptor<MeasurementResultMobileRequest> argumentCaptor = ArgumentCaptor.forClass(MeasurementResultMobileRequest.class);
        String content = "{" +
                "\"" + alias + "\": \"" + TestConstants.DEFAULT_NETWORK_OPERATOR_NAME + "\"" +
                "}";

        mockMvc.perform(MockMvcRequestBuilders.post(URIConstants.MOBILE + URIConstants.RESULT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(content))

                .andDo(print())
                .andExpect(status().isOk());

        verify(mobileMeasurementService).processMeasurementResult(argumentCaptor.capture(), any());
        assertEquals(TestConstants.DEFAULT_NETWORK_OPERATOR_NAME, argumentCaptor.getValue().getNetworkOperatorName());
    }

    @ParameterizedTest
    @ValueSource(strings = {"telephony_network_operator", "network_mcc_mnc"})
    void processResult_whenTelephonyNetworkOperatorJsonAliasFields_expectMeasurementResultMobileResponse(String alias) throws Exception {
        ArgumentCaptor<MeasurementResultMobileRequest> argumentCaptor = ArgumentCaptor.forClass(MeasurementResultMobileRequest.class);
        String content = "{" +
                "\"" + alias + "\": \"" + TestConstants.DEFAULT_NETWORK_MCC_MNC + "\"" +
                "}";

        mockMvc.perform(MockMvcRequestBuilders.post(URIConstants.MOBILE + URIConstants.RESULT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(content))

                .andDo(print())
                .andExpect(status().isOk());

        verify(mobileMeasurementService).processMeasurementResult(argumentCaptor.capture(), any());
        assertEquals(TestConstants.DEFAULT_NETWORK_MCC_MNC, argumentCaptor.getValue().getNetworkMccMnc());
    }

    @ParameterizedTest
    @ValueSource(strings = {"telephony_network_sim_country", "telephonyNetworkSimCountry", "sim_country"})
    void processResult_whenTelephonyNetworkSimCountryJsonAliasFields_expectMeasurementResultMobileResponse(String alias) throws Exception {
        ArgumentCaptor<MeasurementResultMobileRequest> argumentCaptor = ArgumentCaptor.forClass(MeasurementResultMobileRequest.class);
        String content = "{" +
                "\"" + alias + "\": \"" + TestConstants.DEFAULT_SIM_COUNTRY + "\"" +
                "}";

        mockMvc.perform(MockMvcRequestBuilders.post(URIConstants.MOBILE + URIConstants.RESULT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(content))

                .andDo(print())
                .andExpect(status().isOk());

        verify(mobileMeasurementService).processMeasurementResult(argumentCaptor.capture(), any());
        assertEquals(TestConstants.DEFAULT_SIM_COUNTRY, argumentCaptor.getValue().getSimCountry());
    }

    @ParameterizedTest
    @ValueSource(strings = {"telephony_network_sim_operator", "telephonyNetworkSimOperator", "sim_mcc_mnc"})
    void processResult_whenTelephonyNetworkSimOperatorJsonAliasFields_expectMeasurementResultMobileResponse(String alias) throws Exception {
        ArgumentCaptor<MeasurementResultMobileRequest> argumentCaptor = ArgumentCaptor.forClass(MeasurementResultMobileRequest.class);
        String content = "{" +
                "\"" + alias + "\": \"" + TestConstants.DEFAULT_SIM_MCC_MNC + "\"" +
                "}";

        mockMvc.perform(MockMvcRequestBuilders.post(URIConstants.MOBILE + URIConstants.RESULT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(content))

                .andDo(print())
                .andExpect(status().isOk());

        verify(mobileMeasurementService).processMeasurementResult(argumentCaptor.capture(), any());
        assertEquals(TestConstants.DEFAULT_SIM_MCC_MNC, argumentCaptor.getValue().getSimMccMnc());
    }

    @ParameterizedTest
    @ValueSource(strings = {"telephony_network_sim_operator_name", "sim_operator_name"})
    void processResult_whenTelephonyNetworkSimOperatorNameJsonAliasFields_expectMeasurementResultMobileResponse(String alias) throws Exception {
        ArgumentCaptor<MeasurementResultMobileRequest> argumentCaptor = ArgumentCaptor.forClass(MeasurementResultMobileRequest.class);
        String content = "{" +
                "\"" + alias + "\": \"" + TestConstants.DEFAULT_SIM_OPERATOR_NAME + "\"" +
                "}";

        mockMvc.perform(MockMvcRequestBuilders.post(URIConstants.MOBILE + URIConstants.RESULT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(content))

                .andDo(print())
                .andExpect(status().isOk());

        verify(mobileMeasurementService).processMeasurementResult(argumentCaptor.capture(), any());
        assertEquals(TestConstants.DEFAULT_SIM_OPERATOR_NAME, argumentCaptor.getValue().getSimOperatorName());
    }

    @ParameterizedTest
    @ValueSource(strings = {"telephony_network_is_roaming", "network_is_roaming"})
    void processResult_whenTelephonyNetworkIsRoamingJsonAliasFields_expectMeasurementResultMobileResponse(String alias) throws Exception {
        ArgumentCaptor<MeasurementResultMobileRequest> argumentCaptor = ArgumentCaptor.forClass(MeasurementResultMobileRequest.class);
        String content = "{" +
                "\"" + alias + "\": \"" + TestConstants.DEFAULT_NETWORK_IS_ROAMING + "\"" +
                "}";

        mockMvc.perform(MockMvcRequestBuilders.post(URIConstants.MOBILE + URIConstants.RESULT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(content))

                .andDo(print())
                .andExpect(status().isOk());

        verify(mobileMeasurementService).processMeasurementResult(argumentCaptor.capture(), any());
        assertEquals(TestConstants.DEFAULT_NETWORK_IS_ROAMING, argumentCaptor.getValue().isNetworkIsRoaming());
    }

    @ParameterizedTest
    @ValueSource(strings = {"telephony_network_country", "network_country"})
    void processResult_whenTelephonyNetworkCountryJsonAliasFields_expectMeasurementResultMobileResponse(String alias) throws Exception {
        ArgumentCaptor<MeasurementResultMobileRequest> argumentCaptor = ArgumentCaptor.forClass(MeasurementResultMobileRequest.class);
        String content = "{" +
                "\"" + alias + "\": \"" + TestConstants.DEFAULT_NETWORK_COUNTRY + "\"" +
                "}";

        mockMvc.perform(MockMvcRequestBuilders.post(URIConstants.MOBILE + URIConstants.RESULT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(content))

                .andDo(print())
                .andExpect(status().isOk());

        verify(mobileMeasurementService).processMeasurementResult(argumentCaptor.capture(), any());
        assertEquals(TestConstants.DEFAULT_NETWORK_COUNTRY, argumentCaptor.getValue().getNetworkCountry());
    }

    @Test
    void registrationMeasurement_whenCommonData_expectMobileMeasurementRegistrationResponse() throws Exception {
        var request = MobileMeasurementSettingRequest.builder()
                .clientUuid(TestConstants.DEFAULT_UUID)
                .platform(TestConstants.DEFAULT_TEST_PLATFORM)
                .appVersion(TestConstants.DEFAULT_APP_VERSION)
                .build();
        var response = MobileMeasurementRegistrationResponse.builder()
                .appVersion(TestConstants.DEFAULT_APP_VERSION)
                .platform(TestConstants.DEFAULT_TEST_PLATFORM)
                .build();
        when(mobileMeasurementRegistrationService.registerMobileMeasurement(eq(request), any(), any())).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post(URIConstants.MOBILE + URIConstants.TEST_REQUEST)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.asJsonString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.app_version").value(TestConstants.DEFAULT_APP_VERSION))
                .andExpect(jsonPath("$.platform").value(TestConstants.DEFAULT_TEST_PLATFORM.name()));

        verify(mobileMeasurementRegistrationService).registerMobileMeasurement(eq(request), any(), any());
    }

    @Test
    void getTestResultByTestUUID_whenCommonData_expectMobileMeasurementResultContainerResponse() throws Exception {
        var request = MobileMeasurementResultRequest.builder().testUUID(UUID.fromString(TestConstants.DEFAULT_UUID)).build();

        mockMvc.perform(MockMvcRequestBuilders.post(URIConstants.MOBILE + URIConstants.TEST_RESULT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.asJsonString(request)))
                .andExpect(status().isOk());

        verify(mobileMeasurementService).getTestResult(request);
    }

    @Test
    void getTestResultDetailByTestUUID_whenCommonData_expectMobileMeasurementResultDetailResponse() throws Exception {
        var request = MobileMeasurementResultDetailRequest.builder().testUUID(UUID.fromString(TestConstants.DEFAULT_UUID)).build();

        mockMvc.perform(MockMvcRequestBuilders.post(URIConstants.MOBILE + URIConstants.TEST_RESULT_DETAIL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.asJsonString(request)))
                .andExpect(status().isOk());

        verify(mobileMeasurementService).getTestResultDetailByTestUUID(request);
    }
}
