package com.specure.integration;

import com.specure.response.mobile.MobileMeasurementRegistrationResponse;
import com.specure.response.sah.stats.NationalTableResponse;
import com.specure.sah.TestUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
@EnabledIf("integrationEnabled")
class NationalStatisticsIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    TestRestTemplate testRestTemplate;
    @LocalServerPort
    private int randomServerPort;

    @Test
    void nationalStatisticScenario() throws IOException, InterruptedException {
        //Multiple mobile measurements
        String clientUuid = getSettings();
        Long measurementServerId = getAllMeasurementServer();
        MobileMeasurementRegistrationResponse mobileMeasurementRegistrationResponse = executeMobileTestRequest(clientUuid, measurementServerId);
        log.info("mobileMeasurementRegistrationResponse = " + mobileMeasurementRegistrationResponse);
        executeMobileTestResult(mobileMeasurementRegistrationResponse.getTestToken(), mobileMeasurementRegistrationResponse.getTestUuid(), clientUuid);

        MobileMeasurementRegistrationResponse mobileMeasurementRegistrationResponseSecond = executeMobileTestRequestSecond(clientUuid, measurementServerId);
        log.info("mobileMeasurementRegistrationResponseSecond = " + mobileMeasurementRegistrationResponseSecond);
        executeMobileTestResultSecond(mobileMeasurementRegistrationResponseSecond.getTestToken(), mobileMeasurementRegistrationResponseSecond.getTestUuid(), clientUuid);

        MobileMeasurementRegistrationResponse mobileMeasurementRegistrationResponse2G = executeMobileTestRequestSecond2G(clientUuid, measurementServerId);
        log.info("mobileMeasurementRegistrationResponse2G = " + mobileMeasurementRegistrationResponse2G);
        executeMobileTestResult2G(mobileMeasurementRegistrationResponse2G.getTestToken(), mobileMeasurementRegistrationResponse2G.getTestUuid(), clientUuid);

        MobileMeasurementRegistrationResponse mobileMeasurementRegistrationResponse3G = executeMobileTestRequestSecond3G(clientUuid, measurementServerId);
        log.info("mobileMeasurementRegistrationResponse3G = " + mobileMeasurementRegistrationResponse3G);
        executeMobileTestResult3G(mobileMeasurementRegistrationResponse3G.getTestToken(), mobileMeasurementRegistrationResponse3G.getTestUuid(), clientUuid);

        MobileMeasurementRegistrationResponse mobileMeasurementRegistrationResponse5G = executeMobileTestRequestSecond5G(clientUuid, measurementServerId);
        log.info("mobileMeasurementRegistrationResponse5G = " + mobileMeasurementRegistrationResponse5G);
        executeMobileTestResult5G(mobileMeasurementRegistrationResponse5G.getTestToken(), mobileMeasurementRegistrationResponse5G.getTestUuid(), clientUuid);

        MobileMeasurementRegistrationResponse mobileMeasurementRegistrationResponseWLAN = executeMobileTestRequestSecondWLAN(clientUuid, measurementServerId);
        log.info("mobileMeasurementRegistrationResponseWLAN = " + mobileMeasurementRegistrationResponseWLAN);
        executeMobileTestResultWLAN(mobileMeasurementRegistrationResponseWLAN.getTestToken(), mobileMeasurementRegistrationResponseWLAN.getTestUuid(), clientUuid);

        executeMnoNationalTable();
    }

    private void executeMnoNationalTable() throws IOException {
        compareStatistics(null, "integration/nationalStatistic/statistics/null.json");
        compareStatistics("all", "integration/nationalStatistic/statistics/all.json");
        compareStatistics("all_isp", "integration/nationalStatistic/statistics/all_isp.json");
        compareStatistics("all_mno", "integration/nationalStatistic/statistics/all_mno.json");
        compareStatistics("2G", "integration/nationalStatistic/statistics/2G.json");
        compareStatistics("3G", "integration/nationalStatistic/statistics/3G.json");
        compareStatistics("4G", "integration/nationalStatistic/statistics/4G.json");
        compareStatistics("5G", "integration/nationalStatistic/statistics/5G.json");
        compareStatistics("WLAN", "integration/nationalStatistic/statistics/WLAN.json");
    }

    private void compareStatistics(String mobileTechnology, String path) throws IOException {
        NationalTableResponse actualNationalTableResponse = super.getNationalTable(mobileTechnology);
        NationalTableResponse expectedNationalTableResponse = TestUtils.readObjectFromFilePath(path, NationalTableResponse.class);
        assertThat(actualNationalTableResponse)
                .usingRecursiveComparison()
                .isEqualTo(expectedNationalTableResponse);
    }

    private void executeMobileTestResult(String testToken, String openTestUuid, String clientUuid) throws IOException, InterruptedException {
        Instant measurementDate = Instant.now();
        Map<String, String> requestTemplateParams = Map.of("openTestUuid", openTestUuid,
                "testToken", testToken,
                "time", String.valueOf(measurementDate.toEpochMilli()));
        Map<String, String> basicTestTemplateParams = Map.of("openTestUuid", openTestUuid,
                "clientUuid", clientUuid,
                "timestamp", String.valueOf(measurementDate.toEpochMilli()),
                "measurementDate", measurementDate.toString());
        mobileTestResult("integration/nationalStatistic/mobileResult/mobileResult-Request.json",
                requestTemplateParams,
                "integration/nationalStatistic/mobileResult/basicTest.json",
                basicTestTemplateParams,
                openTestUuid);
    }

    private void executeMobileTestResultSecond(String testToken, String openTestUuid, String clientUuid) throws IOException, InterruptedException {
        Instant measurementDate = Instant.now();
        Map<String, String> requestTemplateParams = Map.of("openTestUuid", openTestUuid,
                "testToken", testToken,
                "time", String.valueOf(measurementDate.toEpochMilli()));
        Map<String, String> basicTestTemplateParams = Map.of("openTestUuid", openTestUuid,
                "clientUuid", clientUuid,
                "timestamp", String.valueOf(measurementDate.toEpochMilli()),
                "measurementDate", measurementDate.toString());
        mobileTestResult("integration/nationalStatistic/mobileResultSecond/mobileResult-Request.json",
                requestTemplateParams,
                "integration/nationalStatistic/mobileResultSecond/basicTest.json",
                basicTestTemplateParams,
                openTestUuid);
    }

    private void executeMobileTestResult2G(String testToken, String openTestUuid, String clientUuid) throws IOException, InterruptedException {
        Instant measurementDate = Instant.now();
        Map<String, String> requestTemplateParams = Map.of("openTestUuid", openTestUuid,
                "testToken", testToken,
                "time", String.valueOf(measurementDate.toEpochMilli()));
        Map<String, String> basicTestTemplateParams = Map.of("openTestUuid", openTestUuid,
                "clientUuid", clientUuid,
                "timestamp", String.valueOf(measurementDate.toEpochMilli()),
                "measurementDate", measurementDate.toString());
        mobileTestResult("integration/nationalStatistic/mobileResult2G/mobileResult-Request.json",
                requestTemplateParams,
                "integration/nationalStatistic/mobileResult2G/basicTest.json",
                basicTestTemplateParams,
                openTestUuid);
    }

    private void executeMobileTestResult3G(String testToken, String openTestUuid, String clientUuid) throws IOException, InterruptedException {
        Instant measurementDate = Instant.now();
        Map<String, String> requestTemplateParams = Map.of("openTestUuid", openTestUuid,
                "testToken", testToken,
                "time", String.valueOf(measurementDate.toEpochMilli()));
        Map<String, String> basicTestTemplateParams = Map.of("openTestUuid", openTestUuid,
                "clientUuid", clientUuid,
                "timestamp", String.valueOf(measurementDate.toEpochMilli()),
                "measurementDate", measurementDate.toString());
        mobileTestResult("integration/nationalStatistic/mobileResult3G/mobileResult-Request.json",
                requestTemplateParams,
                "integration/nationalStatistic/mobileResult3G/basicTest.json",
                basicTestTemplateParams,
                openTestUuid);
    }

    private void executeMobileTestResult5G(String testToken, String openTestUuid, String clientUuid) throws IOException, InterruptedException {
        Instant measurementDate = Instant.now();
        Map<String, String> requestTemplateParams = Map.of("openTestUuid", openTestUuid,
                "testToken", testToken,
                "time", String.valueOf(measurementDate.toEpochMilli()));
        Map<String, String> basicTestTemplateParams = Map.of("openTestUuid", openTestUuid,
                "clientUuid", clientUuid,
                "timestamp", String.valueOf(measurementDate.toEpochMilli()),
                "measurementDate", measurementDate.toString());
        mobileTestResult("integration/nationalStatistic/mobileResult5G/mobileResult-Request.json",
                requestTemplateParams,
                "integration/nationalStatistic/mobileResult5G/basicTest.json",
                basicTestTemplateParams,
                openTestUuid);
    }

    private void executeMobileTestResultWLAN(String testToken, String openTestUuid, String clientUuid) throws IOException, InterruptedException {
        Instant measurementDate = Instant.now();
        Map<String, String> requestTemplateParams = Map.of("openTestUuid", openTestUuid,
                "testToken", testToken,
                "time", String.valueOf(measurementDate.toEpochMilli()));
        Map<String, String> basicTestTemplateParams = Map.of("openTestUuid", openTestUuid,
                "clientUuid", clientUuid,
                "timestamp", String.valueOf(measurementDate.toEpochMilli()),
                "measurementDate", measurementDate.toString());
        mobileTestResult("integration/nationalStatistic/mobileResultWLAN/mobileResult-Request.json",
                requestTemplateParams,
                "integration/nationalStatistic/mobileResultWLAN/basicTest.json",
                basicTestTemplateParams,
                openTestUuid);
    }

    private MobileMeasurementRegistrationResponse executeMobileTestRequest(String clientUuid, Long measurementServerId) throws IOException {
        Map<String, String> responseTemplateParams = Map.of("randomServerPort", String.valueOf(randomServerPort));

        Map<String, String> requestTemplateParams = Map.of("clientUuid", clientUuid,
                "measurementServerId", measurementServerId.toString());

        return mobileTestRequest("integration/nationalStatistic/mobileTestRequest/mobileTestRequest-Response.json",
                responseTemplateParams,
                "integration/nationalStatistic/mobileTestRequest/mobileTestRequest-Request.json",
                requestTemplateParams);
    }

    private MobileMeasurementRegistrationResponse executeMobileTestRequestSecond(String clientUuid, Long measurementServerId) throws IOException {
        Map<String, String> responseTemplateParams = Map.of("randomServerPort", String.valueOf(randomServerPort));

        Map<String, String> requestTemplateParams = Map.of("clientUuid", clientUuid,
                "measurementServerId", measurementServerId.toString());

        return mobileTestRequest("integration/nationalStatistic/mobileTestRequestSecond/mobileTestRequest-Response.json",
                responseTemplateParams,
                "integration/nationalStatistic/mobileTestRequestSecond/mobileTestRequest-Request.json",
                requestTemplateParams);
    }

    private MobileMeasurementRegistrationResponse executeMobileTestRequestSecond2G(String clientUuid, Long measurementServerId) throws IOException {
        Map<String, String> responseTemplateParams = Map.of("randomServerPort", String.valueOf(randomServerPort));

        Map<String, String> requestTemplateParams = Map.of("clientUuid", clientUuid,
                "measurementServerId", measurementServerId.toString());

        return mobileTestRequest("integration/nationalStatistic/mobileTestRequest2G/mobileTestRequest-Response.json",
                responseTemplateParams,
                "integration/nationalStatistic/mobileTestRequest2G/mobileTestRequest-Request.json",
                requestTemplateParams);
    }

    private MobileMeasurementRegistrationResponse executeMobileTestRequestSecond3G(String clientUuid, Long measurementServerId) throws IOException {
        Map<String, String> responseTemplateParams = Map.of("randomServerPort", String.valueOf(randomServerPort));

        Map<String, String> requestTemplateParams = Map.of("clientUuid", clientUuid,
                "measurementServerId", measurementServerId.toString());

        return mobileTestRequest("integration/nationalStatistic/mobileTestRequest3G/mobileTestRequest-Response.json",
                responseTemplateParams,
                "integration/nationalStatistic/mobileTestRequest3G/mobileTestRequest-Request.json",
                requestTemplateParams);
    }

    private MobileMeasurementRegistrationResponse executeMobileTestRequestSecond5G(String clientUuid, Long measurementServerId) throws IOException {
        Map<String, String> responseTemplateParams = Map.of("randomServerPort", String.valueOf(randomServerPort));

        Map<String, String> requestTemplateParams = Map.of("clientUuid", clientUuid,
                "measurementServerId", measurementServerId.toString());

        return mobileTestRequest("integration/nationalStatistic/mobileTestRequest5G/mobileTestRequest-Response.json",
                responseTemplateParams,
                "integration/nationalStatistic/mobileTestRequest5G/mobileTestRequest-Request.json",
                requestTemplateParams);
    }

    private MobileMeasurementRegistrationResponse executeMobileTestRequestSecondWLAN(String clientUuid, Long measurementServerId) throws IOException {
        Map<String, String> responseTemplateParams = Map.of("randomServerPort", String.valueOf(randomServerPort));

        Map<String, String> requestTemplateParams = Map.of("clientUuid", clientUuid,
                "measurementServerId", measurementServerId.toString());

        return mobileTestRequest("integration/nationalStatistic/mobileTestRequestWLAN/mobileTestRequest-Response.json",
                responseTemplateParams,
                "integration/nationalStatistic/mobileTestRequestWLAN/mobileTestRequest-Request.json",
                requestTemplateParams);
    }
}
