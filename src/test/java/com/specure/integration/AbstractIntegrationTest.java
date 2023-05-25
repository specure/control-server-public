package com.specure.integration;

import com.specure.common.model.elastic.BasicTest;
import com.specure.common.response.MeasurementServerResponse;
import com.specure.common.utils.HeaderExtrudeUtil;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.specure.constant.URIConstants;
import com.specure.model.elastic.GeoShape;
import com.specure.multitenant.MultiTenantManager;
import com.specure.request.core.MeasurementRequest;
import com.specure.request.core.SettingRequest;
import com.specure.request.mobile.MeasurementResultMobileRequest;
import com.specure.request.mobile.MobileMeasurementSettingRequest;
import com.specure.response.core.settings.SettingsResponse;
import com.specure.response.mobile.MeasurementResultMobileResponse;
import com.specure.response.mobile.MobileMeasurementRegistrationResponse;
import com.specure.response.sah.stats.NationalTableResponse;
import com.specure.sah.TestUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.http.*;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestComponent
@Slf4j
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AbstractIntegrationTest {

    public static boolean integrationEnabled() {
        return false;
    }

    protected final String X_NETTEST_CLIENT_HEADER = "x-nettest-client";
    public static final String BASIC_TEST_INDEX_NAME = "basic_test_nt_container_" + Instant.now().toEpochMilli();
    public static final String BASIC_QOS_TEST_INDEX_NAME = "basic_qos_test_nt_container_" + Instant.now().toEpochMilli();
    public static final int MYSQL_SERVICE_PORT = 3306;
    public static final int ELASTICSEARCH_SERVICE_PORT = 9200;
    public static final int REDIS_SERVICE_PORT = 6379;

    @Getter
    public static class KeyCloakTokenResponse {
        @JsonProperty(value = "access_token")
        private String accessToken;
    }

    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private MultiTenantManager multiTenantManager;

    @DynamicPropertySource
    public static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("client-tenant.clientTenantMapping.admin.url", () -> "jdbc:mysql://localhost:" + MYSQL_SERVICE_PORT + "/admin");
        registry.add("client-tenant.clientTenantMapping.admin.username", () -> "root");
        registry.add("client-tenant.clientTenantMapping.admin.password", () -> "root");
        registry.add("client-tenant.clientTenantMapping.nt.url", () -> "jdbc:mysql://localhost:" + MYSQL_SERVICE_PORT + "/sah");
        registry.add("client-tenant.clientTenantMapping.nt.username", () -> "root");
        registry.add("client-tenant.clientTenantMapping.nt.password", () -> "root");
        registry.add("elastic-index.elastic-credential.nt.host", () -> "localhost:" + ELASTICSEARCH_SERVICE_PORT);
        registry.add("elastic-index.basicTenantIndexes.nt", () -> BASIC_TEST_INDEX_NAME);
        registry.add("elastic-index.basicQosTenantIndexes.nt", () -> BASIC_QOS_TEST_INDEX_NAME);
        registry.add("redis.port", () -> REDIS_SERVICE_PORT);
    }

    @BeforeAll
    void testIsContainerRunning() {
        multiTenantManager.setCurrentTenant("nt");
        createIndexIfNotExist(BASIC_TEST_INDEX_NAME);
        createIndexIfNotExist(BASIC_QOS_TEST_INDEX_NAME);
    }

    @AfterAll
    void cleanAfterAll() {
        deleteIndex(BASIC_TEST_INDEX_NAME);
        deleteIndex(BASIC_QOS_TEST_INDEX_NAME);
    }

    private void deleteIndex(String indexName) {
        IndexOperations indexOperations = multiTenantManager.getCurrentTenantElastic().indexOps(IndexCoordinates.of(indexName));
        indexOperations.delete();
    }

    protected void createIndexIfNotExist(String indexName) {
        IndexOperations indexOperations = multiTenantManager.getCurrentTenantElastic().indexOps(IndexCoordinates.of(indexName));
        if (!indexOperations.exists()) {
            indexOperations.create();
            indexOperations.putMapping(BasicTest.class);
        }
    }

    protected void createShapeIndexIfNotExist() {
        IndexOperations indexOperations = multiTenantManager.getCurrentTenantElastic().indexOps(GeoShape.class);
        if (!indexOperations.exists()) {
            indexOperations.create();
            indexOperations.putMapping(GeoShape.class);
        }
    }

    protected String getToken() {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client_id", "nettest");
        map.add("username", "olektestacc123@gmail.com");
        map.add("password", "Admin123$%^");
        map.add("grant_type", "password");

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);

        ResponseEntity<KeyCloakTokenResponse> response = restTemplate.postForEntity("https://keycloak.specure.net/realms/specure-dev/protocol/openid-connect/token", entity, KeyCloakTokenResponse.class);
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Wrong token response status");
        assertNotNull(response.getBody(), "Missing token body");
        return response.getBody().getAccessToken();
    }

    protected void verifyBasicTestSaved(String openTestUuid,
                                        String basicTestTemplatePath,
                                        Map<String, String> basicTestTemplateParams) throws IOException, InterruptedException {
        BasicTest expectedBasicTest = getExpectedBasicTest(basicTestTemplatePath, basicTestTemplateParams);
        BasicTest basicTest = getBasicTestByOpenTestUuid(openTestUuid);

        assertThat(basicTest)
                .usingRecursiveComparison()
                .ignoringFields("measurementDate", "timestamp", "graphHour")
                .isEqualTo(expectedBasicTest);
    }


    private BasicTest getBasicTestByOpenTestUuid(String testUuid) throws InterruptedException {
        Thread.sleep(1000L);
        BoolQueryBuilder filteredQuery = boolQuery();
        filteredQuery.must(QueryBuilders.termQuery("openTestUuid.keyword", testUuid));
        Query query = new NativeSearchQueryBuilder()
                .withQuery(filteredQuery)
                .build();

        SearchHit<BasicTest> searchHit = multiTenantManager.getCurrentTenantElastic()
                .searchOne(query,
                        BasicTest.class,
                        IndexCoordinates.of(multiTenantManager.getCurrentTenantBasicIndex()));
        assertNotNull(searchHit);

        return searchHit.getContent();
    }

    private BasicTest getExpectedBasicTest(String basicTestTemplatePath, Map<String, String> basicTestTemplateParams) throws IOException {
        return TestUtils.readObjectFromFilePath(basicTestTemplatePath,
                BasicTest.class,
                basicTestTemplateParams);
    }

    protected MeasurementRequest getMeasurementRequest(String openTestUuid,
                                                       String clientUuid,
                                                       String testToken) throws IOException {
        Map<String, String> parameters = Map.of("openTestUuid", openTestUuid,
                "clientUuid", clientUuid,
                "testToken", testToken);
        return TestUtils.readObjectFromFilePath("integration/web/measurementResult/measurementResult-Request.json",
                MeasurementRequest.class,
                parameters);
    }

    protected Long getAllMeasurementServer() {
        HttpHeaders headers = new HttpHeaders();
        headers.set(X_NETTEST_CLIENT_HEADER, "nt");

        var requestEntity = new HttpEntity<>(headers);
        var response = testRestTemplate.exchange(com.specure.constant.URIConstants.MEASUREMENT_SERVER, HttpMethod.GET, requestEntity, MeasurementServerResponse[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Wrong get measurement servers response code");
        assertNotNull(response.getBody());
        return response.getBody()[0].getId();
    }

    protected String getSettings() {
        HttpHeaders headers = new HttpHeaders();
        headers.set(X_NETTEST_CLIENT_HEADER, "nt");
        var request = SettingRequest.builder().build();
        var requestEntity = new HttpEntity<>(request, headers);

        var response = testRestTemplate.postForEntity(URIConstants.SETTINGS, requestEntity, SettingsResponse.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getSettings());
        assertEquals(1, response.getBody().getSettings().size());
        return response.getBody().getSettings().get(0).getUuid();
    }

    //Purposed for /mobile/testRequest endpoint
    protected MobileMeasurementRegistrationResponse mobileTestRequest(String responseTemplatePath,
                                                                      Map<String, String> responseTemplateParams,
                                                                      String requestTemplatePath,
                                                                      Map<String, String> requestTemplateParams) throws IOException {
        var expectedResponse = getExpectedMobileMeasurementRegistrationResponse(responseTemplatePath,
                responseTemplateParams);
        var requestEntity = getMobileMeasurementSettingRequest(requestTemplatePath, requestTemplateParams);

        var response = testRestTemplate.postForEntity(URIConstants.MOBILE + URIConstants.TEST_REQUEST,
                requestEntity,
                MobileMeasurementRegistrationResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(response.getBody())
                .usingRecursiveComparison()
                .ignoringFields("testUuid", "testToken", "openTestUUID", "testId")
                .isEqualTo(expectedResponse);

        return response.getBody();
    }

    //Purposed for /mobile/result endpoint
    protected void mobileTestResult(String requestTemplatePath,
                                    Map<String, String> requestTemplateParams,
                                    String basicTestTemplatePath,
                                    Map<String, String> basicTestTemplateParams,
                                    String openTestUuid) throws IOException, InterruptedException {
        var requestEntity = getMeasurementResultMobileRequest(requestTemplatePath, requestTemplateParams);
        var response = testRestTemplate.postForEntity(URIConstants.MOBILE + URIConstants.RESULT,
                requestEntity,
                MeasurementResultMobileResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verifyBasicTestSaved(openTestUuid,
                basicTestTemplatePath,
                basicTestTemplateParams);
    }

    /**
     * Test method that call GET /nationalTable
     *
     * @return
     */
    protected NationalTableResponse getNationalTable(String mobileTechnology) {
        final String URL = "/nationalTable";
        UriComponentsBuilder urlBuilder = UriComponentsBuilder.fromUriString(URL);
        if (Objects.nonNull(mobileTechnology)) {
            urlBuilder.queryParam("tech", mobileTechnology);
        }

        UriComponents uriComponents = urlBuilder.build();
        HttpHeaders headers = new HttpHeaders();
        headers.set(X_NETTEST_CLIENT_HEADER, "nt");

        var requestEntity = new HttpEntity<>(headers);

        var response = testRestTemplate.exchange(uriComponents.toString(), HttpMethod.GET, requestEntity, NationalTableResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        return response.getBody();
    }

    private static class JsonPage<T> extends PageImpl<T> {
        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        public JsonPage(@JsonProperty("content") List<T> content, @JsonProperty("number") int number, @JsonProperty("size") int size,
                        @JsonProperty("totalElements") Long totalElements, @JsonProperty("pageable") JsonNode pageable, @JsonProperty("last") boolean last,
                        @JsonProperty("totalPages") int totalPages, @JsonProperty("sort") JsonNode sort, @JsonProperty("first") boolean first,
                        @JsonProperty("numberOfElements") int numberOfElements) {
            super(content, PageRequest.of(number, size), totalElements);
        }

        public JsonPage(List<T> content, Pageable pageable, long total) {
            super(content, pageable, total);
        }

        public JsonPage(List<T> content) {
            super(content);
        }

        public JsonPage() {
            super(new ArrayList<T>());
        }
    }

    private HttpEntity<MeasurementResultMobileRequest> getMeasurementResultMobileRequest(String requestTemplatePath,
                                                                                         Map<String, String> requestTemplateParams) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HeaderExtrudeUtil.HEADER_NGINX_X_REAL_IP, "178.240.184.173");
        headers.set(X_NETTEST_CLIENT_HEADER, "nt");

        var request = TestUtils.readObjectFromFilePath(requestTemplatePath,
                MeasurementResultMobileRequest.class,
                requestTemplateParams);

        return new HttpEntity<>(request, headers);
    }

    private MobileMeasurementRegistrationResponse getExpectedMobileMeasurementRegistrationResponse(String responseTemplatePath,
                                                                                                   Map<String, String> responseTemplateParams) throws IOException {
        return TestUtils.readObjectFromFilePath(responseTemplatePath,
                MobileMeasurementRegistrationResponse.class,
                responseTemplateParams);
    }

    private HttpEntity<MobileMeasurementSettingRequest> getMobileMeasurementSettingRequest(String requestTemplatePath,
                                                                                           Map<String, String> requestTemplateParams) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HeaderExtrudeUtil.HEADER_NGINX_X_REAL_IP, "178.240.184.173");
        headers.set(X_NETTEST_CLIENT_HEADER, "nt");

        var request = TestUtils.readObjectFromFilePath(requestTemplatePath,
                MobileMeasurementSettingRequest.class,
                requestTemplateParams);

        return new HttpEntity<>(request, headers);
    }
}
