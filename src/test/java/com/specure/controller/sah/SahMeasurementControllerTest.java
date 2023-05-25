package com.specure.controller.sah;

import com.specure.common.config.MeasurementServerConfig;
import com.specure.common.enums.Platform;
import com.specure.common.model.jpa.MeasurementServer;
import com.specure.common.utils.HeaderExtrudeUtil;
import com.specure.advice.SahBackendAdvice;
import com.specure.constant.URIConstants;
import com.specure.model.dto.internal.DataForMeasurementRegistration;
import com.specure.request.core.MeasurementQosRequest;
import com.specure.request.core.MeasurementRegistrationForProbeRequest;
import com.specure.request.core.MeasurementRequest;
import com.specure.response.core.MeasurementRegistrationResponse;
import com.specure.sah.TestConstants;
import com.specure.sah.TestUtils;
import com.specure.service.core.MeasurementServerService;
import com.specure.service.core.MeasurementService;
import com.specure.service.core.impl.MeasurementQosServiceImpl;
import com.specure.service.sah.BasicTestService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.Map;

import static com.specure.constant.ErrorMessage.*;
import static com.specure.constant.URIConstants.MEASUREMENT_RESULT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
public class SahMeasurementControllerTest {
    @MockBean
    private MeasurementService basicMeasurementService;
    @MockBean
    private MeasurementServerService sahMeasurementServerService;
    @MockBean
    private BasicTestService basicTestService;
    @MockBean
    private MeasurementServerConfig measurementServerConfig;
    @MockBean
    private MeasurementQosServiceImpl sahMeasurementQosService;
    @Captor
    private ArgumentCaptor<DataForMeasurementRegistration> dataForMeasurementRegistrationArgumentCaptor;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        SahMeasurementController measurementResultController = new SahMeasurementController(basicMeasurementService, basicTestService, measurementServerConfig, sahMeasurementServerService, sahMeasurementQosService);
        mockMvc = MockMvcBuilders.standaloneSetup(measurementResultController)
                .setControllerAdvice(new SahBackendAdvice())
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((viewName, locale) -> new MappingJackson2JsonView())
                .build();
    }

    @Test
    public void saveMeasurementQoS_WhenNoVoipSection_ExpectBadRequest() {
        String jsonRequest = "{" +
                "    \"test_token\": \"afbf5a66-2754-4923-ba33-ed4ff88521f9_1589289590_qQJyNQslKzqXxFKFdLozQJCLfrM=\",\n" +
                "    \"client_uuid\": \"540607f2-2a43-4019-af89-fa6d42b9a14f\",\n" +
                "    \"time\": 1589289648171,\n" +
                "    \"client_version\": \"0.3\",\n" +
                "    \"qos_result\": []" +
                "}";
        try {
            mockMvc.perform(MockMvcRequestBuilders.post(URIConstants.MEASUREMENT_RESULT_QOS)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonRequest)
            );

        } catch (Exception e) {
            System.out.println("e.getMessage() = " + e.getMessage());
            assert (e.getMessage()).equals("The measurement result has wrong format: QoS measurement result has to consist VOIP section.");
        }
    }

    @Test
    public void measurementRequest_WhenNoPort_ExpectBadRequest() throws Exception {
        final var measurementServerRequestWithoutPort = MeasurementRegistrationForProbeRequest
                .builder()
                .client(TestConstants.DEFAULT_CLIENT)
                .isOnNet(true)
                .uuid(TestConstants.DEFAULT_UUID)
                .build();

        mockMvc.perform(MockMvcRequestBuilders
                        .post(URIConstants.TEST_REQUEST)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.convertObjectToJsonBytes(measurementServerRequestWithoutPort))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(PORT_NAME_REQUIRED));

        verify(sahMeasurementServerService, never()).getDataFromProbeMeasurementRegistrationRequest(any());
        verify(basicMeasurementService, never()).registerMeasurement(any(), any());
    }

    @Test
    public void measurementRequest_WhenNoUuid_ExpectBadRequest() throws Exception {
        final var measurementServerRequestWithoutPort = MeasurementRegistrationForProbeRequest.builder()
                .isOnNet(true)
                .client(TestConstants.DEFAULT_CLIENT)
                .port(TestConstants.DEFAULT_PORT)
                .build();

        mockMvc.perform(MockMvcRequestBuilders
                        .post(URIConstants.TEST_REQUEST)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(TestUtils.convertObjectToJsonBytes(measurementServerRequestWithoutPort))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(CLIENT_UUID_REQUIRED));

        verify(sahMeasurementServerService, never()).getDataFromProbeMeasurementRegistrationRequest(any());
        verify(basicMeasurementService, never()).registerMeasurement(any(), any());
    }


    @Test
    public void measurementRequest_WhenCalled_ExpectCorrectResponse() throws Exception {
        final var measurementServerRequest = getDefaultMeasurementServerRequest();
        final var measurementServer = MeasurementServer.builder().build();
        final DataForMeasurementRegistration dataForMeasurementRegistration = DataForMeasurementRegistration.builder()
                .measurementServer(measurementServer)
                .deviceOrProbeId(TestConstants.DEFAULT_PROBE_ID)
                .port(TestConstants.DEFAULT_PORT)
                .appVersion(TestConstants.DEFAULT_APP_VERSION)
                .platform(Platform.BROWSER)
                .build();

        when(sahMeasurementServerService.getDataFromProbeMeasurementRegistrationRequest(measurementServerRequest))
                .thenReturn(dataForMeasurementRegistration);
        when(basicMeasurementService.registerMeasurement(dataForMeasurementRegistrationArgumentCaptor.capture(), any()))
                .thenReturn(getDefaultResponse());

        mockMvc.perform(MockMvcRequestBuilders
                        .post(URIConstants.TEST_REQUEST)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.convertObjectToJsonBytes(measurementServerRequest))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.test_id").isNotEmpty())
                .andExpect(jsonPath("$.platform").value(Platform.BROWSER.name()));

        assertEquals(TestConstants.DEFAULT_APP_VERSION, dataForMeasurementRegistrationArgumentCaptor.getValue().getAppVersion());
    }


    @Test
    public void saveTestResult_WhenCalled_ExpectCorrectResponse() throws Exception {
        var measurementRequest = MeasurementRequest.builder()
                .testToken(TestConstants.DEFAULT_TOKEN)
                .time(TestConstants.DEFAULT_TIME)
                .networkType(TestConstants.DEFAULT_NETWORK_TYPE.toString())
                .testNsecUpload(com.specure.core.TestConstants.DEFAULT_N_SEC_UPLOAD)
                .testNsecDownload(com.specure.core.TestConstants.DEFAULT_N_SEC_DOWNLOAD)
                .testBytesUpload(com.specure.core.TestConstants.DEFAULT_BYTES_UPLOAD)
                .testBytesDownload(com.specure.core.TestConstants.DEFAULT_BYTES_DOWNLOAD)
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post(MEASUREMENT_RESULT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.convertObjectToJsonBytes(measurementRequest))
                .header(HeaderExtrudeUtil.HEADER_NGINX_X_REAL_IP, TestConstants.DEFAULT_IP_V4)
        ).andExpect(status().isOk());

        Map<String, String> expectedHeaders = Map.of(HeaderExtrudeUtil.HEADER_NGINX_X_REAL_IP, TestConstants.DEFAULT_IP_V4,
                "Content-Type", "application/json",
                "Content-Length", "778");
        verify(basicMeasurementService).partialUpdateMeasurementFromProbeResult(measurementRequest, expectedHeaders);
    }

    @Test
    public void saveTestResult_WhenCalledWithoutTestToken_ExpectBadRequest() throws Exception {
        var measurementRequest = MeasurementRequest.builder()
                .time(TestConstants.DEFAULT_TIME)
                .networkType(TestConstants.DEFAULT_NETWORK_TYPE.toString())
                .testNsecUpload(com.specure.core.TestConstants.DEFAULT_N_SEC_UPLOAD)
                .testNsecDownload(com.specure.core.TestConstants.DEFAULT_N_SEC_DOWNLOAD)
                .testBytesUpload(com.specure.core.TestConstants.DEFAULT_BYTES_UPLOAD)
                .testBytesDownload(com.specure.core.TestConstants.DEFAULT_BYTES_DOWNLOAD)
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post(MEASUREMENT_RESULT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.convertObjectToJsonBytes(measurementRequest))
                )
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.message").value(TEST_TOKEN_REQUIRED));
        verify(basicMeasurementService, never()).partialUpdateMeasurementFromProbeResult(any(), any());
    }


    @Test
    public void saveMeasurementQoS_WhenCalled_ExpectCorrectResponse() throws Exception {
        MeasurementQosRequest measurementQosRequest = MeasurementQosRequest.builder()
                .testToken(TestConstants.DEFAULT_TOKEN)
                .build();
        mockMvc.perform(MockMvcRequestBuilders.post(URIConstants.MEASUREMENT_RESULT_QOS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.convertObjectToJsonBytes(measurementQosRequest))
        ).andExpect(status().isOk());

        verify(sahMeasurementQosService).saveMeasurementQos(measurementQosRequest);
    }

    @Test
    public void saveMeasurementQoS_WhenCalledWithoutTestToken_ExpectBadRequest() throws Exception {
        MeasurementQosRequest measurementQosRequestWithoutTestToken = MeasurementQosRequest.builder().build();

        mockMvc.perform(MockMvcRequestBuilders.post(URIConstants.MEASUREMENT_RESULT_QOS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.convertObjectToJsonBytes(measurementQosRequestWithoutTestToken))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(TEST_TOKEN_REQUIRED));

        verify(sahMeasurementQosService, never()).saveMeasurementQos(any());
    }

    MeasurementRegistrationResponse getDefaultResponse() {
        return MeasurementRegistrationResponse
                .builder()
                .testUuid(TestConstants.DEFAULT_UUID)
                .resultUrl(TestConstants.DEFAULT_TEST_RESULT_URL)
                .resultQosUrl(TestConstants.DEFAULT_QOS_TEST_RESULT_URL)
                .testDuration(TestConstants.DEFAULT_TEST_DURATION)
                .testServerName(TestConstants.DEFAULT_MEASUREMENT_SERVER_NAME)
                .testWait(TestConstants.DEFAULT_TEST_WEIGHT)
                .testServerAddress(TestConstants.DEFAULT_MEASUREMENT_SERVER_ADDRESS)
                .testNumThreads(TestConstants.DEFAULT_NUM_TEST_THREADS)
                .testServerPort(TestConstants.DEFAULT_MEASUREMENT_SERVER_PORT)
                .testServerEncryption(TestConstants.DEFAULT_IS_MEASUREMENT_SERVER_ENCRYPTED)
                .testToken(TestConstants.DEFAULT_MEASUREMENT_SERVER_TOKEN)
                .testNumPings(TestConstants.DEFAULT_TEST_NUM_PINGS)
                .testId(TestConstants.DEFAULT_TEST_ID)
                .platform(Platform.BROWSER)
                .build();
    }

    MeasurementRegistrationForProbeRequest getDefaultMeasurementServerRequest() {
        return MeasurementRegistrationForProbeRequest
                .builder()
                .client(TestConstants.DEFAULT_CLIENT)
                .port(TestConstants.DEFAULT_PROBE_PORT_INT)
                .isOnNet(true)
                .uuid(TestConstants.DEFAULT_UUID)
                .platform(Platform.BROWSER)
                .build();
    }
}

