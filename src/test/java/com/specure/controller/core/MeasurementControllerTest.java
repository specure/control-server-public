package com.specure.controller.core;

import com.specure.common.constant.AdminSetting;
import com.specure.common.enums.Platform;
import com.specure.common.model.jpa.MeasurementServer;
import com.specure.advice.ControllerErrorAdvice;
import com.specure.constant.MeasurementServerConstants;
import com.specure.constant.URIConstants;
import com.specure.core.TestUtils;
import com.specure.model.dto.internal.DataForMeasurementRegistration;
import com.specure.request.core.MeasurementQosRequest;
import com.specure.request.core.MeasurementRegistrationForAdminRequest;
import com.specure.request.core.MeasurementRegistrationForWebClientRequest;
import com.specure.response.core.MeasurementHistoryResponse;
import com.specure.response.core.MeasurementRegistrationResponse;
import com.specure.sah.TestConstants;
import com.specure.service.core.MeasurementQosService;
import com.specure.service.core.MeasurementServerService;
import com.specure.service.core.MeasurementService;
import com.specure.service.core.SettingsService;
import com.specure.service.sah.BasicTestService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.Map;

import static com.specure.core.TestConstants.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
public class MeasurementControllerTest {

    @MockBean
    private MeasurementService measurementService;

    @MockBean
    private MeasurementServerService measurementServerService;

    @MockBean
    private MeasurementQosService measurementQosService;

    @MockBean
    private BasicTestService basicTestService;
    @MockBean
    private SettingsService settingsService;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        MeasurementController measurementResultController = new MeasurementController(measurementService, measurementQosService, measurementServerService, basicTestService, settingsService);
        mockMvc = MockMvcBuilders.standaloneSetup(measurementResultController)
                .setControllerAdvice(new ControllerErrorAdvice())
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((viewName, locale) -> new MappingJackson2JsonView())
                .build();
    }


    @Test
    public void registerMeasurementForWebClient_WhenCalledAndSettingsIsEmpty_ExpectCorrectResponse() throws Exception {
        final var requestFromWebClient = MeasurementRegistrationForWebClientRequest.builder().client(DEFAULT_CLIENT).build();
        final var measurementServer = MeasurementServer.builder().build();
        final DataForMeasurementRegistration dataForMeasurementRegistration = DataForMeasurementRegistration.builder()
                .measurementServer(measurementServer)
                .build();

        when(measurementServerService.getMeasurementServerForWebClient(requestFromWebClient))
                .thenReturn(dataForMeasurementRegistration);
        when(measurementService.registerMeasurement(any(), any()))
                .thenReturn(getDefaultResponse());

        mockMvc.perform(MockMvcRequestBuilders
                        .post(URIConstants.TEST_REQUEST_FOR_WEB_CLIENT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.convertObjectToJsonBytes(requestFromWebClient))
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.test_id").isNotEmpty())
                .andExpect(jsonPath("$.test_numthreads").value(MeasurementServerConstants.TEST_NUM_THREADS_FOR_WEB));
    }

    @Test
    public void registerMeasurementForWebClient_WhenCalledAndSettingsNotEmpty_ExpectCorrectResponse() throws Exception {
        final var requestFromWebClient = MeasurementRegistrationForWebClientRequest.builder().client(DEFAULT_CLIENT).build();
        final var measurementServer = MeasurementServer.builder().build();
        final DataForMeasurementRegistration dataForMeasurementRegistration = DataForMeasurementRegistration.builder()
                .measurementServer(measurementServer)
                .build();

        when(settingsService.getSettingsMap())
                .thenReturn(Map.of(AdminSetting.MEASUREMENT_NUM_THREADS_WEB_KEY, String.valueOf(DEFAULT_NUM_TEST_THREADS)));
        when(measurementServerService.getMeasurementServerForWebClient(requestFromWebClient))
                .thenReturn(dataForMeasurementRegistration);
        when(measurementService.registerMeasurement(any(), any()))
                .thenReturn(getDefaultResponse());

        mockMvc.perform(MockMvcRequestBuilders
                        .post(URIConstants.TEST_REQUEST_FOR_WEB_CLIENT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.convertObjectToJsonBytes(requestFromWebClient))
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.test_id").isNotEmpty())
                .andExpect(jsonPath("$.test_numthreads").value(DEFAULT_NUM_TEST_THREADS));
    }

    @Test
    public void measurementQoSRequest_WhenCalled_ExpectCorrectResponse() throws Exception {
        MeasurementQosRequest measurementQosRequest = MeasurementQosRequest.builder()
                .testToken(DEFAULT_TOKEN)
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post(URIConstants.MEASUREMENT_QOS_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.convertObjectToJsonBytes(measurementQosRequest))
        ).andExpect(status().isOk());
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
    public void registerMeasurementForAdmin_WhenSettingsIsEmpty_ExpectCorrectResponse() throws Exception {

        final MeasurementRegistrationForAdminRequest measurementRegistrationForAdminRequest = MeasurementRegistrationForAdminRequest.builder()
                .measurementServerId(DEFAULT_MEASUREMENT_SERVER_ID)
                .client(DEFAULT_CLIENT)
                .uuid(DEFAULT_UUID)
                .build();

        final DataForMeasurementRegistration dataForMeasurementRegistration = DataForMeasurementRegistration.builder()
                .measurementServer(MeasurementServer.builder().id(DEFAULT_MEASUREMENT_SERVER_ID).build())
                .deviceOrProbeId(DEFAULT_PROBE_ID)
                .port(DEFAULT_PORT)
                .build();

        final MeasurementRegistrationResponse measurementRegistrationResponse = getDefaultResponse();

        when(measurementServerService.getMeasurementServerById(measurementRegistrationForAdminRequest))
                .thenReturn(dataForMeasurementRegistration);
        when(measurementService.registerMeasurement(eq(dataForMeasurementRegistration), any()))
                .thenReturn(measurementRegistrationResponse);

        mockMvc.perform(MockMvcRequestBuilders
                        .post(URIConstants.TEST_REQUEST_FOR_ADMIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.convertObjectToJsonBytes(measurementRegistrationForAdminRequest))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.test_numthreads").value(MeasurementServerConstants.TEST_NUM_THREADS_FOR_WEB));
    }

    @Test
    public void registerMeasurementForAdmin_WhenCalled_ExpectCorrectResponse() throws Exception {

        final MeasurementRegistrationForAdminRequest measurementRegistrationForAdminRequest = MeasurementRegistrationForAdminRequest.builder()
                .measurementServerId(DEFAULT_MEASUREMENT_SERVER_ID)
                .client(DEFAULT_CLIENT)
                .uuid(DEFAULT_UUID)
                .telephonyPermissionGranted(true)
                .uuidPermissionGranted(false)
                .locationPermissionGranted(true)
                .appVersion(com.specure.sah.TestConstants.DEFAULT_APP_VERSION)
                .platform(Platform.BROWSER)
                .build();

        final DataForMeasurementRegistration dataForMeasurementRegistration = DataForMeasurementRegistration.builder()
                .measurementServer(MeasurementServer.builder().id(DEFAULT_MEASUREMENT_SERVER_ID).build())
                .deviceOrProbeId(DEFAULT_PROBE_ID)
                .port(DEFAULT_PORT)
                .telephonyPermissionGranted(true)
                .uuidPermissionGranted(false)
                .locationPermissionGranted(true)
                .build();

        final MeasurementRegistrationResponse measurementRegistrationResponse = getDefaultResponse();

        when(settingsService.getSettingsMap())
                .thenReturn(Map.of(AdminSetting.MEASUREMENT_NUM_THREADS_WEB_KEY, String.valueOf(DEFAULT_NUM_TEST_THREADS)));
        when(measurementServerService.getMeasurementServerById(measurementRegistrationForAdminRequest))
                .thenReturn(dataForMeasurementRegistration);
        when(measurementService.registerMeasurement(eq(dataForMeasurementRegistration), any()))
                .thenReturn(measurementRegistrationResponse);

        mockMvc.perform(MockMvcRequestBuilders
                        .post(URIConstants.TEST_REQUEST_FOR_ADMIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.convertObjectToJsonBytes(measurementRegistrationForAdminRequest))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.test_numthreads").value(DEFAULT_NUM_TEST_THREADS))
                .andExpect(jsonPath("$.app_version").value(TestConstants.DEFAULT_APP_VERSION));
    }

    @Test
    public void getMeasurementResult_WhenCalled_expectCorrectResponse() throws Exception {

        final MeasurementHistoryResponse measurementHistoryResponse = MeasurementHistoryResponse.builder()
                .measurementServerId(DEFAULT_MEASUREMENT_SERVER_ID)
                .appVersion(TestConstants.DEFAULT_APP_VERSION)
                .platform(TestConstants.DEFAULT_TEST_PLATFORM.name())
                .build();
        when(basicTestService.getMeasurementDetailByUuidFromElasticSearch(DEFAULT_UUID))
                .thenReturn(measurementHistoryResponse);

        mockMvc
                .perform(MockMvcRequestBuilders.get(URIConstants.MEASUREMENT_RESULT_BY_UUID, DEFAULT_UUID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.app_version").value(TestConstants.DEFAULT_APP_VERSION))
                .andExpect(jsonPath("$.platform").value(TestConstants.DEFAULT_TEST_PLATFORM.name()));
    }

    private MeasurementRegistrationResponse getDefaultResponse() {
        return MeasurementRegistrationResponse
                .builder()
                .testUuid(DEFAULT_UUID)
                .resultUrl(DEFAULT_TEST_RESULT_URL)
                .resultQosUrl(DEFAULT_QOS_TEST_RESULT_URL)
                .testDuration(DEFAULT_TEST_DURATION)
                .testServerName(DEFAULT_MEASUREMENT_SERVER_NAME)
                .testWait(DEFAULT_TEST_WEIGHT)
                .testServerAddress(DEFAULT_MEASUREMENT_SERVER_ADDRESS)
                .testNumThreads(DEFAULT_NUM_TEST_THREADS)
                .testServerPort(DEFAULT_MEASUREMENT_SERVER_PORT)
                .testServerEncryption(DEFAULT_IS_MEASUREMENT_SERVER_ENCRYPTED)
                .testToken(DEFAULT_MEASUREMENT_SERVER_TOKEN)
                .testNumPings(DEFAULT_TEST_NUM_PINGS)
                .testId(DEFAULT_TEST_ID)
                .appVersion(com.specure.sah.TestConstants.DEFAULT_APP_VERSION)
                .build();
    }
}

