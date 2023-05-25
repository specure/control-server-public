package com.specure.integration;

import com.specure.constant.URIConstants;
import com.specure.response.mobile.BasicTestHistoryMobileResponse;
import com.specure.response.mobile.MobileMeasurementRegistrationResponse;
import com.specure.sah.TestUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
@EnabledIf("integrationEnabled")
class MobileIntegrationTests extends AbstractIntegrationTest {

    @LocalServerPort
    private int randomServerPort;
    @Autowired
    TestRestTemplate testRestTemplate;


    @Test
    void mobileScenario_maxMindProvider() throws IOException, InterruptedException {
        //1 GET /measurementServer
        //2 POST /settings
        //3 POST /mobile/testRequest
        //4 POST /mobile/result
        //5 GET /mobile/history/{uuid}
        Long measurementServerId = getAllMeasurementServer();
        String clientUuid = getSettings();

        MobileMeasurementRegistrationResponse mobileMeasurementRegistrationResponse = executeMobileTestRequestMaxMind(clientUuid, measurementServerId);

        executeMobileResultMaxMind(mobileMeasurementRegistrationResponse.getTestToken(), mobileMeasurementRegistrationResponse.getTestUuid(), clientUuid);
        mobileHistoryByUuidMaxMind(mobileMeasurementRegistrationResponse.getTestUuid());
    }

    @Test
    void mobileScenario_simProvider() throws IOException, InterruptedException {
        //1 GET /measurementServer
        //2 POST /settings
        //3 POST /mobile/testRequest
        //4 POST /mobile/result
        //5 GET /mobile/history/{uuid}
        Long measurementServerId = getAllMeasurementServer();
        String clientUuid = getSettings();
        MobileMeasurementRegistrationResponse mobileMeasurementRegistrationResponse = executeMobileTestRequestSim(clientUuid, measurementServerId);

        executeMobileResultSim(mobileMeasurementRegistrationResponse.getTestToken(), mobileMeasurementRegistrationResponse.getTestUuid(), clientUuid);
        mobileHistoryByUuidSim(mobileMeasurementRegistrationResponse.getTestUuid());
    }

    @Test
    void mobileScenario_nullPermission() throws IOException, InterruptedException {
        //1 GET /measurementServer
        //2 POST /settings
        //3 POST /mobile/testRequest
        //4 POST /mobile/result
        //5 GET /mobile/history/{uuid}
        Long measurementServerId = getAllMeasurementServer();
        String clientUuid = getSettings();
        MobileMeasurementRegistrationResponse mobileMeasurementRegistrationResponse = executeMobileTestRequestPermissionNull(clientUuid, measurementServerId);

        executeMobileResultPermissionNull(mobileMeasurementRegistrationResponse.getTestToken(), mobileMeasurementRegistrationResponse.getTestUuid(), clientUuid);
        mobileHistoryByUuidPermissionNull(mobileMeasurementRegistrationResponse.getTestUuid());
    }

    @Test
    void mobileScenario_locationPermissionGrantedFalse() throws IOException, InterruptedException {
        //1 GET /measurementServer
        //2 POST /settings
        //3 POST /mobile/testRequest
        //4 POST /mobile/result
        //5 GET /mobile/history/{uuid}
        Long measurementServerId = getAllMeasurementServer();
        String clientUuid = getSettings();
        MobileMeasurementRegistrationResponse mobileMeasurementRegistrationResponse = executeMobileTestRequestLocationPermissionGrantedFalse(clientUuid, measurementServerId);

        executeMobileResultLocationPermissionGrantedFalse(mobileMeasurementRegistrationResponse.getTestToken(), mobileMeasurementRegistrationResponse.getTestUuid(), clientUuid);
        mobileHistoryByUuidLocationPermissionGrantedFalse(mobileMeasurementRegistrationResponse.getTestUuid());
    }

    private void executeMobileResultMaxMind(String testToken, String openTestUuid, String clientUuid) throws IOException, InterruptedException {
        Map<String, String> requestTemplateParams = Map.of("openTestUuid", openTestUuid,
                "testToken", testToken);
        Map<String, String> basicTestTemplateParams = Map.of("openTestUuid", openTestUuid,
                "clientUuid", clientUuid);
        mobileTestResult("integration/mobile/max-mind/mobileResult/mobileResult-Request.json",
                requestTemplateParams,
                "integration/mobile/max-mind/mobileResult/basicTest.json",
                basicTestTemplateParams,
                openTestUuid);
    }

    private void executeMobileResultSim(String testToken, String openTestUuid, String clientUuid) throws IOException, InterruptedException {
        Map<String, String> requestTemplateParams = Map.of("openTestUuid", openTestUuid,
                "testToken", testToken);
        Map<String, String> basicTestTemplateParams = Map.of("openTestUuid", openTestUuid,
                "clientUuid", clientUuid);
        mobileTestResult("integration/mobile/sim/mobileResult/mobileResult-Request.json",
                requestTemplateParams,
                "integration/mobile/sim/mobileResult/basicTest.json",
                basicTestTemplateParams,
                openTestUuid);
    }

    private void executeMobileResultPermissionNull(String testToken, String openTestUuid, String clientUuid) throws IOException, InterruptedException {
        Map<String, String> requestTemplateParams = Map.of("openTestUuid", openTestUuid,
                "testToken", testToken);
        Map<String, String> basicTestTemplateParams = Map.of("openTestUuid", openTestUuid,
                "clientUuid", clientUuid);
        mobileTestResult("integration/mobile/permission-null/mobileResult/mobileResult-Request.json",
                requestTemplateParams,
                "integration/mobile/permission-null/mobileResult/basicTest.json",
                basicTestTemplateParams,
                openTestUuid);
    }

    private void executeMobileResultLocationPermissionGrantedFalse(String testToken, String openTestUuid, String clientUuid) throws IOException, InterruptedException {
        Map<String, String> requestTemplateParams = Map.of("openTestUuid", openTestUuid,
                "testToken", testToken);
        Map<String, String> basicTestTemplateParams = Map.of("openTestUuid", openTestUuid,
                "clientUuid", clientUuid);
        mobileTestResult("integration/mobile/location-permission-granted-false/mobileResult/mobileResult-Request.json",
                requestTemplateParams,
                "integration/mobile/location-permission-granted-false/mobileResult/basicTest.json",
                basicTestTemplateParams,
                openTestUuid);
    }

    private MobileMeasurementRegistrationResponse executeMobileTestRequestMaxMind(String clientUuid, Long measurementServerId) throws IOException {
        Map<String, String> responseTemplateParams = Map.of("randomServerPort", String.valueOf(randomServerPort));

        Map<String, String> requestTemplateParams = Map.of("clientUuid", clientUuid,
                "measurementServerId", measurementServerId.toString());

        return mobileTestRequest("integration/mobile/max-mind/mobileTestRequest/mobileTestRequest-Response.json",
                responseTemplateParams,
                "integration/mobile/max-mind/mobileTestRequest/mobileTestRequest-Request.json",
                requestTemplateParams);
    }

    private MobileMeasurementRegistrationResponse executeMobileTestRequestSim(String clientUuid, Long measurementServerId) throws IOException {
        Map<String, String> responseTemplateParams = Map.of("randomServerPort", String.valueOf(randomServerPort));

        Map<String, String> requestTemplateParams = Map.of("clientUuid", clientUuid,
                "measurementServerId", measurementServerId.toString());

        return mobileTestRequest("integration/mobile/sim/mobileTestRequest/mobileTestRequest-Response.json",
                responseTemplateParams,
                "integration/mobile/sim/mobileTestRequest/mobileTestRequest-Request.json",
                requestTemplateParams);
    }

    private MobileMeasurementRegistrationResponse executeMobileTestRequestPermissionNull(String clientUuid, Long measurementServerId) throws IOException {
        Map<String, String> responseTemplateParams = Map.of("randomServerPort", String.valueOf(randomServerPort));

        Map<String, String> requestTemplateParams = Map.of("clientUuid", clientUuid,
                "measurementServerId", measurementServerId.toString());

        return mobileTestRequest("integration/mobile/permission-null/mobileTestRequest/mobileTestRequest-Response.json",
                responseTemplateParams,
                "integration/mobile/permission-null/mobileTestRequest/mobileTestRequest-Request.json",
                requestTemplateParams);
    }

    private MobileMeasurementRegistrationResponse executeMobileTestRequestLocationPermissionGrantedFalse(String clientUuid, Long measurementServerId) throws IOException {
        Map<String, String> responseTemplateParams = Map.of("randomServerPort", String.valueOf(randomServerPort));

        Map<String, String> requestTemplateParams = Map.of("clientUuid", clientUuid,
                "measurementServerId", measurementServerId.toString());

        return mobileTestRequest("integration/mobile/location-permission-granted-false/mobileTestRequest/mobileTestRequest-Response.json",
                responseTemplateParams,
                "integration/mobile/location-permission-granted-false/mobileTestRequest/mobileTestRequest-Request.json",
                requestTemplateParams);
    }

    private void mobileHistoryByUuidMaxMind(String testUuid) throws IOException {
        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("uuid", testUuid);
        UriComponents uriComponentsBuilder = UriComponentsBuilder.fromUriString(URIConstants.MOBILE + URIConstants.HISTORY_BY_UUID)
                .buildAndExpand(urlParams);
        HttpHeaders headers = new HttpHeaders();
        headers.set(X_NETTEST_CLIENT_HEADER, "nt");

        var requestHttpEntity = new HttpEntity<>(headers);
        var expectedResponse = getExpectedBasicTestHistoryMobileResponseMaxMind();

        var response = testRestTemplate.exchange(uriComponentsBuilder.toUri(), HttpMethod.GET, requestHttpEntity, BasicTestHistoryMobileResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(response.getBody())
                .usingRecursiveComparison()
                .ignoringFields("openTestUuid")
                .isEqualTo(expectedResponse);
    }

    private void mobileHistoryByUuidSim(String testUuid) throws IOException {
        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("uuid", testUuid);
        UriComponents uriComponentsBuilder = UriComponentsBuilder.fromUriString(URIConstants.MOBILE + URIConstants.HISTORY_BY_UUID)
                .buildAndExpand(urlParams);
        HttpHeaders headers = new HttpHeaders();
        headers.set(X_NETTEST_CLIENT_HEADER, "nt");

        var requestHttpEntity = new HttpEntity<>(headers);
        var expectedResponse = getExpectedBasicTestHistoryMobileResponseSim();

        var response = testRestTemplate.exchange(uriComponentsBuilder.toUri(), HttpMethod.GET, requestHttpEntity, BasicTestHistoryMobileResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(response.getBody())
                .usingRecursiveComparison()
                .ignoringFields("openTestUuid")
                .isEqualTo(expectedResponse);
    }

    private void mobileHistoryByUuidPermissionNull(String testUuid) throws IOException {
        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("uuid", testUuid);
        UriComponents uriComponentsBuilder = UriComponentsBuilder.fromUriString(URIConstants.MOBILE + URIConstants.HISTORY_BY_UUID)
                .buildAndExpand(urlParams);
        HttpHeaders headers = new HttpHeaders();
        headers.set(X_NETTEST_CLIENT_HEADER, "nt");

        var requestHttpEntity = new HttpEntity<>(headers);
        var expectedResponse = getExpectedBasicTestHistoryMobileResponsePermissionNull();

        var response = testRestTemplate.exchange(uriComponentsBuilder.toUri(), HttpMethod.GET, requestHttpEntity, BasicTestHistoryMobileResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(response.getBody())
                .usingRecursiveComparison()
                .ignoringFields("openTestUuid")
                .isEqualTo(expectedResponse);
    }

    private void mobileHistoryByUuidLocationPermissionGrantedFalse(String testUuid) throws IOException {
        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("uuid", testUuid);
        UriComponents uriComponentsBuilder = UriComponentsBuilder.fromUriString(URIConstants.MOBILE + URIConstants.HISTORY_BY_UUID)
                .buildAndExpand(urlParams);
        HttpHeaders headers = new HttpHeaders();
        headers.set(X_NETTEST_CLIENT_HEADER, "nt");

        var requestHttpEntity = new HttpEntity<>(headers);
        var expectedResponse = getExpectedBasicTestHistoryMobileResponseLocationPermissionGrantedFalse();

        var response = testRestTemplate.exchange(uriComponentsBuilder.toUri(), HttpMethod.GET, requestHttpEntity, BasicTestHistoryMobileResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(response.getBody())
                .usingRecursiveComparison()
                .ignoringFields("openTestUuid")
                .isEqualTo(expectedResponse);
    }

    private BasicTestHistoryMobileResponse getExpectedBasicTestHistoryMobileResponseSim() throws IOException {
        return TestUtils.readObjectFromFilePath("integration/mobile/sim/mobileHistoryByUuid/mobileHistoryByUuid-Response.json", BasicTestHistoryMobileResponse.class);
    }

    private BasicTestHistoryMobileResponse getExpectedBasicTestHistoryMobileResponseMaxMind() throws IOException {
        return TestUtils.readObjectFromFilePath("integration/mobile/max-mind/mobileHistoryByUuid/mobileHistoryByUuid-Response.json", BasicTestHistoryMobileResponse.class);
    }

    private BasicTestHistoryMobileResponse getExpectedBasicTestHistoryMobileResponsePermissionNull() throws IOException {
        return TestUtils.readObjectFromFilePath("integration/mobile/permission-null/mobileHistoryByUuid/mobileHistoryByUuid-Response.json", BasicTestHistoryMobileResponse.class);
    }

    private BasicTestHistoryMobileResponse getExpectedBasicTestHistoryMobileResponseLocationPermissionGrantedFalse() throws IOException {
        return TestUtils.readObjectFromFilePath("integration/mobile/location-permission-granted-false/mobileHistoryByUuid/mobileHistoryByUuid-Response.json", BasicTestHistoryMobileResponse.class);
    }
}
