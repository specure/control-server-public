package com.specure.mapper.mobile.impl;

import com.specure.common.enums.NetworkType;
import com.specure.common.model.dto.TestResultCounter;
import com.specure.common.model.elastic.BasicQosTest;
import com.specure.common.model.elastic.BasicTest;
import com.specure.common.response.LocationResponse;
import com.specure.common.response.userexperience.UserExperienceMetric;
import com.specure.mapper.mobile.BasicTestMobileMapper;
import com.specure.sah.TestConstants;
import com.specure.service.sah.UserExperienceService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class BasicTestMobileMapperImplTest {
    private BasicTestMobileMapper basicTestMobileMapper;
    @Mock
    private UserExperienceService userExperienceService;

    private BasicTest basicTest = getBasicTest();

    @Before
    public void setUp() {
        when(userExperienceService.getBasicTestUserExperience(basicTest))
                .thenReturn(getBasicTestUserExperience());
        basicTestMobileMapper = new BasicTestMobileMapperImpl(userExperienceService);
    }

    @Test
    public void basicTestResponseToBasicTestHistoryMobileResponse_whenCommonData_expectBasicTestHistoryMobileResponse() {
        var response = basicTestMobileMapper.basicTestResponseToBasicTestHistoryMobileResponse(basicTest, getBasicQosTest());
        var expectedLocation = LocationResponse.builder()
                .longitude(TestConstants.DEFAULT_LONGITUDE)
                .latitude(TestConstants.DEFAULT_LATITUDE)
                .postalCode(TestConstants.DEFAULT_POSTAL_CODE)
                .county(TestConstants.DEFAULT_COUNTY)
                .country(TestConstants.DEFAULT_COUNTRY)
                .city(TestConstants.DEFAULT_CITY)
                .build();

        assertEquals(TestConstants.DEFAULT_SPEED_DOWNLOAD, response.getDownloadSpeed());
        assertEquals(TestConstants.DEFAULT_SPEED_UPLOAD, response.getUploadSpeed());
        assertEquals(TestConstants.DEFAULT_JITTER, response.getJitter());
        assertEquals(TestConstants.DEFAULT_PACKET_LOSS, response.getPacketLoss());
        assertEquals(TestConstants.DEFAULT_NETWORK_TYPE_LTE_CATEGORY, response.getNetworkType());
        assertEquals(TestConstants.DEFAULT_UUID, response.getOpenTestUuid());
        assertEquals(TestConstants.DEFAULT_PING, response.getPing());
        assertEquals(TestConstants.DEFAULT_OVERALL_QOS_PERCENTAGE, response.getQos());
        assertEquals(getTestResultCounterDtoList(), response.getQosTestResultCounters());
        assertEquals(TestConstants.DEFAULT_MEASUREMENT_DATE, response.getMeasurementDate());
        assertEquals(TestConstants.DEFAULT_DEVICE, response.getDevice());
        assertEquals(TestConstants.DEFAULT_LOOP_MODE_UUID_STRING, response.getLoopModeUuid());
        assertEquals(getBasicTestUserExperience(), response.getUserExperienceMetrics());
        assertEquals(TestConstants.DEFAULT_MEASUREMENT_SERVER_NAME, response.getMeasurementServerName());
        assertEquals(expectedLocation, response.getLocation());
        assertEquals(TestConstants.DEFAULT_NETWORK_TYPE_LTE_NAME, response.getNetworkName());
        assertEquals(TestConstants.DEFAULT_OPERATOR, response.getOperator());
        assertEquals(TestConstants.DEFAULT_CITY, response.getMeasurementServerCity());
        assertEquals(TestConstants.DEFAULT_TEST_PLATFORM.name(), response.getPlatform());
        assertEquals(TestConstants.DEFAULT_APP_VERSION, response.getAppVersion());
    }

    @Test
    public void basicTestResponseToBasicTestHistoryMobileResponse_whenOverallQosIsNullCommonData_expectBasicTestHistoryMobileResponse() {
        var response = basicTestMobileMapper.basicTestResponseToBasicTestHistoryMobileResponse(basicTest, null);

        assertEquals(TestConstants.DEFAULT_SPEED_DOWNLOAD, response.getDownloadSpeed());
        assertEquals(TestConstants.DEFAULT_SPEED_UPLOAD, response.getUploadSpeed());
        assertEquals(TestConstants.DEFAULT_JITTER, response.getJitter());
        assertEquals(TestConstants.DEFAULT_PACKET_LOSS, response.getPacketLoss());
        assertEquals(TestConstants.DEFAULT_NETWORK_TYPE_LTE_CATEGORY, response.getNetworkType());
        assertEquals(TestConstants.DEFAULT_UUID, response.getOpenTestUuid());
        assertEquals(TestConstants.DEFAULT_PING, response.getPing());
        assertNull(response.getQos());
        assertTrue(response.getQosTestResultCounters().isEmpty());
        assertEquals(TestConstants.DEFAULT_MEASUREMENT_DATE, response.getMeasurementDate());
        assertEquals(TestConstants.DEFAULT_DEVICE, response.getDevice());
        assertEquals(getBasicTestUserExperience(), response.getUserExperienceMetrics());
    }

    @Test
    public void basicTestResponseToBasicTestHistoryMobileResponse_whenUnknownNetworkType_expectedUnknown() {
        basicTest.setNetworkType(TestConstants.DEFAULT_TEXT);

        var response = basicTestMobileMapper.basicTestResponseToBasicTestHistoryMobileResponse(basicTest, null);

        assertEquals(NetworkType.UNKNOWN.getCategory(), response.getNetworkType());
        assertEquals(NetworkType.UNKNOWN.getName(), response.getNetworkName());
    }

    private BasicTest getBasicTest() {
        return BasicTest.builder()
                .download(TestConstants.DEFAULT_SPEED_DOWNLOAD)
                .upload(TestConstants.DEFAULT_SPEED_UPLOAD)
                .jitter(TestConstants.DEFAULT_JITTER)
                .packetLoss(TestConstants.DEFAULT_PACKET_LOSS)
                .networkType(TestConstants.DEFAULT_NETWORK_TYPE_LTE.name())
                .openTestUuid(TestConstants.DEFAULT_UUID)
                .ping(TestConstants.DEFAULT_PING)
                .measurementDate(TestConstants.DEFAULT_MEASUREMENT_DATE)
                .device(TestConstants.DEFAULT_DEVICE)
                .loopModeUuid(TestConstants.DEFAULT_LOOP_MODE_UUID_STRING)
                .measurementServerName(TestConstants.DEFAULT_MEASUREMENT_SERVER_NAME)
                .city(TestConstants.DEFAULT_CITY)
                .county(TestConstants.DEFAULT_COUNTY)
                .postalCode(TestConstants.DEFAULT_POSTAL_CODE)
                .country(TestConstants.DEFAULT_COUNTRY)
                .location(new GeoPoint(TestConstants.DEFAULT_LATITUDE, TestConstants.DEFAULT_LONGITUDE))
                .operator(TestConstants.DEFAULT_OPERATOR)
                .measurementServerCity(TestConstants.DEFAULT_CITY)
                .appVersion(TestConstants.DEFAULT_APP_VERSION)
                .platform(TestConstants.DEFAULT_TEST_PLATFORM.name())
                .build();
    }

    private BasicQosTest getBasicQosTest() {
        return BasicQosTest.builder()
                .overallQos(TestConstants.DEFAULT_OVERALL_QOS)
                .qosTestResultCounters(getTestResultCounterDtoList())
                .build();
    }

    private List<TestResultCounter> getTestResultCounterDtoList() {
        return List.of(
                TestResultCounter.builder()
                        .successCount(TestConstants.DEFAULT_SUCCESS_COUNT)
                        .totalCount(TestConstants.DEFAULT_TOTAL_COUNT)
                        .build()
        );
    }

    private List<UserExperienceMetric> getBasicTestUserExperience() {
        return List.of(
                UserExperienceMetric.builder()
                        .category(TestConstants.DEFAULT_USER_EXPERIENCE_CATEGORY)
                        .quality(TestConstants.DEFAULT_USER_EXPERIENCE_QUALITY)
                        .build()
        );
    }
}
