package com.specure.controller.mobile;

import com.specure.advice.SahBackendAdvice;
import com.specure.constant.URIConstants;
import com.specure.request.core.*;
import com.specure.request.mobile.MobileQosMeasurementsRequest;
import com.specure.sah.TestConstants;
import com.specure.sah.TestUtils;
import com.specure.service.mobile.MobileQosMeasurementService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
public class MobileQosMeasurementControllerTest {
    private MockMvc mockMvc;

    @InjectMocks
    private MobileQosMeasurementController mobileQosMeasurementController;

    @Mock
    private MobileQosMeasurementService mobileQosMeasurementService;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(mobileQosMeasurementController)
                .setControllerAdvice(new SahBackendAdvice())
                .build();
    }

    @Test
    public void provideMeasurementQoSParameters_whenCommonData_expectMobileMeasurementQosResponse() throws Exception {
        var request = MeasurementQosParametersRequest.builder().uuid(TestConstants.DEFAULT_UUID).build();

        mockMvc.perform(MockMvcRequestBuilders.post(URIConstants.MOBILE + URIConstants.MEASUREMENT_QOS_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJsonString(request)))
                .andExpect(status().isOk());

        verify(mobileQosMeasurementService).getQosParameters(eq(request), any());
    }

    @Test
    public void getQosTestResults_whenCommonData_expectQosMeasurementsResponse() throws Exception {
        var request = MobileQosMeasurementsRequest.builder()
                .testUuid(UUID.fromString(TestConstants.DEFAULT_UUID))
                .language(TestConstants.DEFAULT_LANGUAGE)
                .capabilities(getCapabilitiesRequest())
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post(URIConstants.MOBILE + URIConstants.MEASUREMENT_QOS_RESULT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJsonString(request)))
                .andExpect(status().isOk());

        verify(mobileQosMeasurementService).getQosResult(UUID.fromString(TestConstants.DEFAULT_UUID), TestConstants.DEFAULT_LANGUAGE, getCapabilitiesRequest());
    }

    @Test
    public void saveQosMeasurementResult_whenCommonData_expectErrorContainerResponse() throws Exception {
        var request = MeasurementQosRequest.builder().testToken(TestConstants.DEFAULT_TOKEN).build();

        mockMvc.perform(MockMvcRequestBuilders.post(URIConstants.MOBILE + URIConstants.RESULT_QOS_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJsonString(request)))
                .andExpect(status().isOk());

        verify(mobileQosMeasurementService).saveQosMeasurementResult(request);
    }

    @Test
    public void evaluateQosByOpenTestUUID_whenCommonData_expectQosMeasurementsResponse() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(URIConstants.MOBILE + URIConstants.QOS_BY_OPEN_TEST_UUID, TestConstants.DEFAULT_UUID))
                .andExpect(status().isOk());

        verify(mobileQosMeasurementService).evaluateQosByOpenTestUUID(UUID.fromString(TestConstants.DEFAULT_UUID), null);
    }

    private CapabilitiesRequest getCapabilitiesRequest() {
        return CapabilitiesRequest.builder()
                .classification(ClassificationRequest.builder().count(TestConstants.DEFAULT_COUNT).build())
                .qos(QosRequest.builder().supportsInfo(TestConstants.DEFAULT_FLAG_TRUE).build())
                .rmbtHttp(TestConstants.DEFAULT_FLAG_TRUE)
                .build();
    }
}
