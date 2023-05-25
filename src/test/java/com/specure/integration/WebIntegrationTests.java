package com.specure.integration;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.specure.constant.URIConstants;
import com.specure.common.enums.Platform;
import com.specure.request.core.MeasurementRegistrationForAdminRequest;
import com.specure.response.core.MeasurementHistoryResponse;
import com.specure.response.core.MeasurementRegistrationResponse;
import com.specure.response.core.MeasurementResultRMBTClientResponse;
import com.specure.sah.TestConstants;
import com.specure.sah.TestUtils;
import com.specure.service.core.MeasurementService;
import com.specure.common.utils.HeaderExtrudeUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
@EnabledIf("integrationEnabled")
class WebIntegrationTests extends AbstractIntegrationTest {

    @Autowired
    TestRestTemplate testRestTemplate;
    @Autowired
    private MeasurementService measurementService;

    @Test
    void webMeasurementScenario() throws IOException, InterruptedException {
        //1 GET /measurementServer
        //2 POST /settings
        //3 GET /requestDataCollector
        //3 POST /adminTestRequest
        //4 POST /measurementResult
        //5 GET /measurementResult/58e13267-e9c8-45f3-a255-33c5e0a24c6d
        Long measurementServerId = getAllMeasurementServer();
        String clientUuid = getSettings();
        MeasurementRegistrationResponse measurementRegistrationResponse = adminTestRequest(measurementServerId,
                clientUuid,
                TestConstants.DEFAULT_IP_FOR_PROVIDER,
                "BATELCO");
        postMeasurementResult(measurementRegistrationResponse.getTestUuid(),
                clientUuid,
                measurementRegistrationResponse.getTestToken());
        getMeasurementResultByUuid(measurementRegistrationResponse.getTestUuid(), measurementRegistrationResponse.getTestToken());
    }

    @Test
    void webMeasurementScenario_locationPermissionGrantedFalse() throws IOException, InterruptedException {
        //1 GET /measurementServer
        //2 POST /settings
        //3 GET /requestDataCollector
        //3 POST /adminTestRequest
        //4 POST /measurementResult
        //5 GET /measurementResult/58e13267-e9c8-45f3-a255-33c5e0a24c6d
        Long measurementServerId = getAllMeasurementServer();
        String clientUuid = getSettings();
        MeasurementRegistrationResponse measurementRegistrationResponse = adminTestRequestLocationPermissionGrantedFalse(measurementServerId,
                clientUuid,
                TestConstants.DEFAULT_IP_FOR_PROVIDER,
                "BATELCO");
        postMeasurementResultLocationPermissionGrantedFalse(measurementRegistrationResponse.getTestUuid(),
                clientUuid,
                measurementRegistrationResponse.getTestToken());
        getMeasurementResultByUuid(measurementRegistrationResponse.getTestUuid(), measurementRegistrationResponse.getTestToken());
    }

    private void getMeasurementResultByUuid(String openTestUuid, String testToken) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.set(X_NETTEST_CLIENT_HEADER, "nt");

        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("uuid", openTestUuid);
        UriComponents uriComponentsBuilder = UriComponentsBuilder.fromUriString(URIConstants.MEASUREMENT_RESULT_BY_UUID).buildAndExpand(urlParams);

        var expectedResponse = getMeasurementHistoryResponse(openTestUuid, testToken);

        var requestHttpEntity = new HttpEntity<>(headers);
        var response = testRestTemplate.exchange(uriComponentsBuilder.toUri(), HttpMethod.GET, requestHttpEntity, MeasurementHistoryResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(response.getBody())
                .usingRecursiveComparison()
                .ignoringFields("time")
                .isEqualTo(expectedResponse);
    }

    private MeasurementHistoryResponse getMeasurementHistoryResponse(String openTestUuid, String testToken) throws IOException {
        Map<String, String> parameters = Map.of("openTestUuid", openTestUuid,
                "testToken", testToken);
        return TestUtils.readObjectFromFilePath("integration/web/measurementResultByUuid/measurementResultByUuid-Response.json", MeasurementHistoryResponse.class, parameters);
    }

    private void postMeasurementResult(String openTestUuid, String clientUuid, String testToken) throws IOException, InterruptedException {
        HttpHeaders headers = new HttpHeaders();
        headers.set(X_NETTEST_CLIENT_HEADER, "nt");

        var request = getMeasurementRequest(
                openTestUuid,
                clientUuid,
                testToken);

        var requestHttpEntity = new HttpEntity<>(request, headers);
        var expectedResponse = getMeasurementResultRMBTClientResponse();
        var response = testRestTemplate.postForEntity(URIConstants.MEASUREMENT_RESULT,
                requestHttpEntity,
                MeasurementResultRMBTClientResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, String> basicTestTemplateParams = Map.of("openTestUuid", openTestUuid,
                "clientUuid", clientUuid);
        verifyBasicTestSaved(openTestUuid, "integration/web/measurementResult/basicTest.json", basicTestTemplateParams);
    }

    private void postMeasurementResultLocationPermissionGrantedFalse(String openTestUuid, String clientUuid, String testToken) throws IOException, InterruptedException {
        HttpHeaders headers = new HttpHeaders();
        headers.set(X_NETTEST_CLIENT_HEADER, "nt");

        var request = getMeasurementRequest(
                openTestUuid,
                clientUuid,
                testToken);

        var requestHttpEntity = new HttpEntity<>(request, headers);
        var response = testRestTemplate.postForEntity(URIConstants.MEASUREMENT_RESULT,
                requestHttpEntity,
                MeasurementResultRMBTClientResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, String> basicTestTemplateParams = Map.of("openTestUuid", openTestUuid,
                "clientUuid", clientUuid);
        verifyBasicTestSaved(openTestUuid, "integration/web/measurementResult/basicTest-locationPermissionGrantedFalse.json", basicTestTemplateParams);
    }

    private MeasurementResultRMBTClientResponse getMeasurementResultRMBTClientResponse() throws IOException {
        return TestUtils.readObjectFromFilePath("integration/web/measurementResult/measurementResult-Response.json",
                MeasurementResultRMBTClientResponse.class);
    }

    private MeasurementRegistrationResponse adminTestRequest(Long measurementServerId,
                                                             String clientUuid,
                                                             String providerIpAddress,
                                                             String providerName) throws IOException {
        HttpEntity<MeasurementRegistrationForAdminRequest> requestEntity = getMeasurementRegistrationForAdminRequestHttpEntity(measurementServerId, clientUuid, providerIpAddress);

        var expectedMeasurementRegistrationResponse = getMeasurementRegistrationResponse();
        var response = testRestTemplate.postForEntity(URIConstants.TEST_REQUEST_FOR_ADMIN, requestEntity, MeasurementRegistrationResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(response.getBody())
                .usingRecursiveComparison()
                .ignoringFields("testUuid", "testToken", "testId")
                .isEqualTo(expectedMeasurementRegistrationResponse);

        validateSavedMeasurementInDatabase(response.getBody().getTestUuid(),
                clientUuid,
                providerName,
                measurementServerId);
        return response.getBody();
    }

    private MeasurementRegistrationResponse adminTestRequestLocationPermissionGrantedFalse(Long measurementServerId,
                                                                                           String clientUuid,
                                                                                           String providerIpAddress,
                                                                                           String providerName) throws IOException {
        HttpEntity<MeasurementRegistrationForAdminRequest> requestEntity = getMeasurementRegistrationForAdminRequestHttpEntityLocationPermissionGrantedFalse(measurementServerId, clientUuid, providerIpAddress);

        var expectedMeasurementRegistrationResponse = getMeasurementRegistrationResponse();
        var response = testRestTemplate.postForEntity(URIConstants.TEST_REQUEST_FOR_ADMIN, requestEntity, MeasurementRegistrationResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(response.getBody())
                .usingRecursiveComparison()
                .ignoringFields("testUuid", "testToken", "testId")
                .isEqualTo(expectedMeasurementRegistrationResponse);

        validateSavedMeasurementInDatabase(response.getBody().getTestUuid(),
                clientUuid,
                providerName,
                measurementServerId);
        return response.getBody();
    }

    private MeasurementRegistrationResponse getMeasurementRegistrationResponse() throws IOException {
        return TestUtils.readObjectFromFilePath("integration/web/adminTestRequest/adminTestRequest-Response.json",
                MeasurementRegistrationResponse.class);
    }

    private HttpEntity<MeasurementRegistrationForAdminRequest> getMeasurementRegistrationForAdminRequestHttpEntity(Long measurementServerId, String clientUuid, String providerIpAddress) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(X_NETTEST_CLIENT_HEADER, "nt");
        Optional.ofNullable(providerIpAddress).ifPresent((ipAddress) -> {
            headers.set(HeaderExtrudeUtil.HEADER_NGINX_X_REAL_IP, ipAddress);
        });

        var request = MeasurementRegistrationForAdminRequest.builder()
                .measurementServerId(measurementServerId)
                .client(TestConstants.DEFAULT_MEASUREMENT_SERVER_TYPE)
                .uuid(clientUuid)
                .locationPermissionGranted(true)
                .telephonyPermissionGranted(false)
                .uuidPermissionGranted(true)
                .appVersion("0.0.1")
                .platform(Platform.BROWSER)
                .build();

        return new HttpEntity<>(request, headers);
    }

    private HttpEntity<MeasurementRegistrationForAdminRequest> getMeasurementRegistrationForAdminRequestHttpEntityLocationPermissionGrantedFalse(Long measurementServerId, String clientUuid, String providerIpAddress) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(X_NETTEST_CLIENT_HEADER, "nt");
        Optional.ofNullable(providerIpAddress).ifPresent((ipAddress) -> {
            headers.set(HeaderExtrudeUtil.HEADER_NGINX_X_REAL_IP, ipAddress);
        });

        var request = MeasurementRegistrationForAdminRequest.builder()
                .measurementServerId(measurementServerId)
                .client(TestConstants.DEFAULT_MEASUREMENT_SERVER_TYPE)
                .uuid(clientUuid)
                .locationPermissionGranted(false)
                .telephonyPermissionGranted(false)
                .uuidPermissionGranted(true)
                .appVersion("0.0.1")
                .platform(Platform.BROWSER)
                .build();

        return new HttpEntity<>(request, headers);
    }

    private void validateSavedMeasurementInDatabase(String testUuid, String clientUuid, String providerName, Long measurementServerId) {
        var savedMeasurement = measurementService.findByOpenTestUuid(testUuid);
        assertEquals(clientUuid, savedMeasurement.getClientUuid());
        assertEquals(measurementServerId, savedMeasurement.getMeasurementServerId());
        assertEquals("Apache HttpClient", savedMeasurement.getBrowserName());
        assertEquals(providerName, savedMeasurement.getClientProvider());
    }

    @Getter
    public static class KeyCloakTokenResponse {
        @JsonProperty(value = "access_token")
        private String accessToken;
    }
}
