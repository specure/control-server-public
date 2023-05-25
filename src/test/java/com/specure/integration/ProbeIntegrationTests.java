package com.specure.integration;

import com.specure.constant.URIConstants;
import com.specure.common.enums.MeasurementServerType;
import com.specure.common.enums.Platform;
import com.specure.request.core.MeasurementQosParametersRequest;
import com.specure.request.core.MeasurementRegistrationForProbeRequest;
import com.specure.request.core.MeasurementRequest;
import com.specure.response.core.MeasurementRegistrationResponse;
import com.specure.response.core.measurement.qos.response.MeasurementQosParametersResponse;
import com.specure.sah.TestConstants;
import com.specure.sah.TestUtils;
import com.specure.common.utils.HeaderExtrudeUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
@EnabledIf("integrationEnabled")
public class ProbeIntegrationTests extends AbstractIntegrationTest {

    public static final String PROBE_ID = "bbm007";
    @Autowired
    TestRestTemplate testRestTemplate;

    @Test
    void probeScenario() throws IOException, InterruptedException {
        //1 POST /testRequest
        //2 POST /qosTestRequest
        //3 POST /measurementResult
        //4 POST /measurementQosResult
        MeasurementRegistrationResponse measurementRegistrationResponse = postTestRequest(TestConstants.DEFAULT_IP_FOR_PROVIDER, "BATELCO");
        postQosTestRequest();
        postMeasurementResult(measurementRegistrationResponse.getTestToken(), measurementRegistrationResponse.getTestUuid());
    }

    private void postMeasurementResult(String testToken, String openTestUuid) throws IOException, InterruptedException {
        HttpHeaders headers = new HttpHeaders();
        headers.set(X_NETTEST_CLIENT_HEADER, "nt");

        var request = getMeasurementResultRequest(testToken);

        var requestEntity = new HttpEntity<>(request, headers);

        var response = testRestTemplate.postForEntity(URIConstants.MEASUREMENT_RESULT,
                requestEntity,
                String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());


        Map<String, String> basicTestTemplateParams = Map.of("openTestUuid", openTestUuid,
                "clientUuid", PROBE_ID);
        verifyBasicTestSaved(openTestUuid, "integration/probe/measurementResult/basicTest.json", basicTestTemplateParams);
    }

    private MeasurementRequest getMeasurementResultRequest(String testToken) throws IOException {

        Map<String, String> parameters = Map.of("testToken", testToken);

        return TestUtils.readObjectFromFilePath("integration/probe/measurementResult/measurementResult-Request.json", MeasurementRequest.class, parameters);
    }

    private void postQosTestRequest() throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.set(X_NETTEST_CLIENT_HEADER, "nt");

        var request = MeasurementQosParametersRequest.builder()
                .uuid(PROBE_ID)
                .build();
        var requestEntity = new HttpEntity<>(request, headers);
        var expectedResponse = getQosTestRequestResponse();
        var response = testRestTemplate.postForEntity(URIConstants.MEASUREMENT_QOS_REQUEST,
                requestEntity,
                MeasurementQosParametersResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
    }

    private MeasurementQosParametersResponse getQosTestRequestResponse() throws IOException {
        String json = TestUtils.getJsonFromFilePath("integration/probe/qosTestRequest/qosTestRequest-Response.json");
        return TestUtils.readObjectFromJson(json, MeasurementQosParametersResponse.class);
    }

    private MeasurementRegistrationResponse postTestRequest(String providerIpAddress,
                                                            String providerName) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.set(X_NETTEST_CLIENT_HEADER, "nt");
        Optional.ofNullable(providerIpAddress)
                .ifPresent((ipAddress) -> {
                    headers.set(HeaderExtrudeUtil.HEADER_NGINX_X_REAL_IP, ipAddress);
                });

        var request = MeasurementRegistrationForProbeRequest.builder()
                .client(MeasurementServerType.RMBT)
                .port("sim1")
                .isOnNet(true)
                .uuid(PROBE_ID)
                .appVersion("0.0.1")
                .platform(Platform.PROBE)
                .build();

        HttpEntity<MeasurementRegistrationForProbeRequest> requestEntity = new HttpEntity<>(request, headers);

        var expectedResponse = getExpectedRequestResponse();
        var response = testRestTemplate.postForEntity(URIConstants.TEST_REQUEST,
                requestEntity,
                MeasurementRegistrationResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertThat(response.getBody())
                .usingRecursiveComparison()
                .ignoringFields("testUuid", "testToken", "testId")
                .isEqualTo(expectedResponse);

        return response.getBody();
    }

    private MeasurementRegistrationResponse getExpectedRequestResponse() throws IOException {
        String json = TestUtils.getJsonFromFilePath("integration/probe/testRequest/testRequest-Response.json");
        return TestUtils.readObjectFromJson(json, MeasurementRegistrationResponse.class);
    }
}
