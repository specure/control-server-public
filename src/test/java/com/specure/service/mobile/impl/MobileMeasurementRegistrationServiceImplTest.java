package com.specure.service.mobile.impl;

import com.specure.common.constant.AdminSetting;
import com.specure.common.enums.MeasurementServerType;
import com.specure.common.enums.MeasurementStatus;
import com.specure.common.enums.Platform;
import com.specure.common.model.jpa.*;
import com.specure.common.repository.MeasurementServerRepository;
import com.specure.common.service.digger.DiggerService;
import com.specure.config.ApplicationProperties;
import com.specure.constant.Constants;
import com.specure.enums.MeasurementType;
import com.specure.model.dto.TimeSlot;
import com.specure.multitenant.MultiTenantManager;
import com.specure.request.mobile.LocationRequest;
import com.specure.request.mobile.MobileMeasurementSettingRequest;
import com.specure.sah.TestConstants;
import com.specure.service.admin.ProviderService;
import com.specure.service.core.*;
import com.specure.service.mobile.LoopModeSettingsService;
import com.specure.service.mobile.MobileMeasurementRegistrationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class MobileMeasurementRegistrationServiceImplTest {

    @MockBean
    private ClientService clientService;
    @MockBean
    private MeasurementServerRepository measurementServerRepository;
    @MockBean
    private MeasurementService measurementService;
    @MockBean
    private MessageSource messageSource;
    @MockBean
    private DiggerService diggerService;
    @MockBean
    private MultiTenantManager multiTenantManager;
    @MockBean
    private LoopModeSettingsService loopModeSettingsService;
    @MockBean
    private FieldAnonymizerFilter fieldAnonymizerFilter;
    @MockBean
    private SettingsService settingsService;
    @MockBean
    private MeasurementServerService measurementServerService;
    @MockBean
    private ProviderService providerService;

    @Mock
    private Client client;
    @Mock
    private MeasurementServer measurementServer;
    @Mock
    private Measurement savedMeasurement;
    @Captor
    private ArgumentCaptor<Measurement> measurementArgumentCaptor;

    private MockHttpServletRequest request;
    private final Map<String, String> headers = new HashMap<>();
    private static MobileMeasurementSettingRequest.LoopModeInfo loopModeInfo = MobileMeasurementSettingRequest.LoopModeInfo.builder()
            .build();
    private MobileMeasurementSettingRequest testSettingsRequest;

    private static final ApplicationProperties applicationProperties = new ApplicationProperties(
            new ApplicationProperties.LanguageProperties(Set.of("en", "de"), "en"),
            Set.of("RMBT", "RMBTjs", "Open-RMBT", "RMBTws", "HW-PROBE"),
            "0.1.0 || 0.3.0 || ^1.0.0",
            1,
            TestConstants.DEFAULT_TEST_DURATION,
            TestConstants.DEFAULT_TEST_NUM_PINGS,
            10000,
            2000
    );

    private MobileMeasurementRegistrationService mobileMeasurementRegistrationService;

    @Mock
    private MeasurementServerTypeDetail serverTypeDetails;

    @Before
    public void setUp() throws Exception {
        mobileMeasurementRegistrationService = new MobileMeasurementRegistrationServiceImpl(clientService,
                measurementService,
                applicationProperties,
                messageSource,
                multiTenantManager,
                loopModeSettingsService,
                fieldAnonymizerFilter,
                settingsService,
                measurementServerService);
        request = new MockHttpServletRequest();
        request.setLocalAddr(TestConstants.DEFAULT_IP);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        headers.put("x-real-ip", TestConstants.DEFAULT_IP);
        testSettingsRequest = MobileMeasurementSettingRequest.builder()
                .platform(Platform.ANDROID)
                .preferredServer(TestConstants.DEFAULT_MEASUREMENT_SERVER_ID)
                .numberOfThreads(TestConstants.DEFAULT_NUM_TEST_THREADS)
                .location(getLocation())
                .timezone(TestConstants.DEFAULT_TIME_ZONE)
                .clientVersion(TestConstants.DEFAULT_CLIENT_VERSION)
                .appVersion(TestConstants.DEFAULT_APP_VERSION)
                .clientName(TestConstants.DEFAULT_CLIENT_NAME)
                .clientUuid(TestConstants.DEFAULT_CLIENT_UUID_STRING)
                .language(TestConstants.DEFAULT_LANGUAGE)
                .measurementType(MeasurementType.DEDICATED)
                .measurementServerType(MeasurementServerType.RMBTws)
                .loopModeInfo(loopModeInfo)
                .preferredServer(TestConstants.DEFAULT_MEASUREMENT_SERVER_ID)
                .locationPermissionGranted(true)
                .build();
    }

    @Test
    public void registerMobileMeasurement_whenCommonData_expectMobileMeasurementRegistrationResponse() {
        TimeSlot timeSlot = TimeSlot.builder()
                .slot(TestConstants.DEFAULT_SLOT)
                .testWait(TestConstants.DEFAULT_WAIT)
                .build();
        when(clientService.getClientByUUID(UUID.fromString(TestConstants.DEFAULT_CLIENT_UUID_STRING))).thenReturn(client);
        when(client.getUuid()).thenReturn(TestConstants.DEFAULT_CLIENT_UUID_STRING);
        when(measurementServerService.getMeasurementServerByIdOrGetDefault(TestConstants.DEFAULT_MEASUREMENT_SERVER_ID)).thenReturn(measurementServer);
        when(measurementServer.getSecretKey()).thenReturn(TestConstants.DEFAULT_TEXT);
        when(measurementServer.getPort()).thenReturn(TestConstants.DEFAULT_MEASUREMENT_SERVER_PORT);
        when(measurementServer.getWebAddress()).thenReturn(TestConstants.DEFAULT_MEASUREMENT_SERVER_ADDRESS);
        when(measurementServer.getName()).thenReturn(TestConstants.DEFAULT_MEASUREMENT_SERVER_NAME);
        when(measurementServer.getId()).thenReturn(TestConstants.DEFAULT_TEST_SERVER_ID);
        when(measurementServer.getServerTypeDetails()).thenReturn(List.of(serverTypeDetails));
        when(serverTypeDetails.getServerType()).thenReturn(TestConstants.DEFAULT_MEASUREMENT_SERVER_TYPE);
        when(serverTypeDetails.isEncrypted()).thenReturn(TestConstants.DEFAULT_FLAG_TRUE);
        when(serverTypeDetails.getPortSsl()).thenReturn(TestConstants.DEFAULT_MEASUREMENT_SERVER_PORT);
        when(measurementService.save(any())).thenReturn(savedMeasurement);
        when(measurementService.getTimeSlot(anyLong())).thenReturn(timeSlot);
        when(savedMeasurement.getId()).thenReturn(TestConstants.DEFAULT_TEST_ID);
        when(savedMeasurement.getPlatform()).thenReturn(Platform.ANDROID);
        when(savedMeasurement.getMeasurementDetails()).thenReturn(MeasurementDetails.builder()
                .appVersion(TestConstants.DEFAULT_APP_VERSION)
                .build());

        var response = mobileMeasurementRegistrationService.registerMobileMeasurement(testSettingsRequest, headers, request);

        assertEquals(TestConstants.DEFAULT_IP, response.getClientRemoteIp());
        assertEquals(TestConstants.DEFAULT_MEASUREMENT_SERVER_ADDRESS, response.getTestServerAddress());
        assertEquals(TestConstants.DEFAULT_MEASUREMENT_SERVER_PORT, response.getTestServerPort());
        assertEquals(TestConstants.DEFAULT_MEASUREMENT_SERVER_NAME, response.getTestServerName());
        assertEquals(TestConstants.DEFAULT_FLAG_TRUE, response.isTestServerEncryption());
        assertEquals(String.valueOf(TestConstants.DEFAULT_TEST_DURATION), response.getTestDuration());
        assertEquals(String.valueOf(TestConstants.DEFAULT_NUM_TEST_THREADS), response.getTestNumThreadsMobile());
        assertEquals(String.valueOf(TestConstants.DEFAULT_TEST_NUM_PINGS), response.getTestNumPings());
        assertEquals(TestConstants.DEFAULT_TEST_ID, response.getTestId());
        assertEquals(TestConstants.DEFAULT_MEASUREMENT_SERVER_TYPE, response.getTestServerType());
        assertEquals(Platform.ANDROID, response.getPlatform());
        assertEquals(TestConstants.DEFAULT_APP_VERSION, response.getAppVersion());

        verify(measurementService, times(2)).save(measurementArgumentCaptor.capture());
        assertEquals(TestConstants.DEFAULT_CLIENT_UUID_STRING, measurementArgumentCaptor.getAllValues().get(0).getClientUuid());
        assertEquals(TestConstants.DEFAULT_CLIENT_NAME, measurementArgumentCaptor.getAllValues().get(0).getClientName());
        assertEquals(TestConstants.DEFAULT_CLIENT_VERSION, measurementArgumentCaptor.getAllValues().get(0).getClientVersion());
        assertEquals(TestConstants.DEFAULT_APP_VERSION, measurementArgumentCaptor.getAllValues().get(0).getMeasurementDetails().getAppVersion());
        assertEquals(TestConstants.DEFAULT_LANGUAGE, measurementArgumentCaptor.getAllValues().get(0).getClientLanguage());
        assertEquals(TestConstants.DEFAULT_TEST_SERVER_ID, measurementArgumentCaptor.getAllValues().get(0).getMeasurementServerId());
        assertEquals(TestConstants.DEFAULT_TIME_ZONE, measurementArgumentCaptor.getAllValues().get(0).getTimezone());
        assertEquals(TestConstants.DEFAULT_NUM_TEST_THREADS, measurementArgumentCaptor.getAllValues().get(0).getTestNumThreads());
        assertEquals(MeasurementStatus.STARTED, measurementArgumentCaptor.getAllValues().get(0).getStatus());
        assertEquals(MeasurementType.DEDICATED.toString(), measurementArgumentCaptor.getAllValues().get(0).getType());
        assertEquals(TestConstants.DEFAULT_COORDINATES_LATITUDE.doubleValue(), measurementArgumentCaptor.getAllValues().get(0).getLatitude(), 0);
        assertEquals(TestConstants.DEFAULT_COORDINATES_LONGITUDE.doubleValue(), measurementArgumentCaptor.getAllValues().get(0).getLongitude(), 0);
    }

    @Test
    public void registerMobileMeasurement_whenCommonDataLegacy_expectMobileMeasurementRegistrationResponse() {
        TimeSlot timeSlot = TimeSlot.builder()
                .slot(TestConstants.DEFAULT_SLOT)
                .testWait(TestConstants.DEFAULT_WAIT)
                .build();
        when(clientService.getClientByUUID(UUID.fromString(TestConstants.DEFAULT_CLIENT_UUID_STRING))).thenReturn(client);
        when(client.getUuid()).thenReturn(TestConstants.DEFAULT_CLIENT_UUID_STRING);
        when(measurementServerService.getMeasurementServerByIdOrGetDefault(TestConstants.DEFAULT_MEASUREMENT_SERVER_ID)).thenReturn(measurementServer);
        when(measurementServer.getSecretKey()).thenReturn(TestConstants.DEFAULT_TEXT);
        when(measurementServer.getPort()).thenReturn(TestConstants.DEFAULT_MEASUREMENT_SERVER_PORT);
        when(measurementServer.getWebAddress()).thenReturn(TestConstants.DEFAULT_MEASUREMENT_SERVER_ADDRESS);
        when(measurementServer.getName()).thenReturn(TestConstants.DEFAULT_MEASUREMENT_SERVER_NAME);
        when(measurementServer.getId()).thenReturn(TestConstants.DEFAULT_TEST_SERVER_ID);
        when(measurementServer.getServerType()).thenReturn(TestConstants.DEFAULT_MEASUREMENT_SERVER_TYPE);
        when(measurementService.save(any())).thenReturn(savedMeasurement);
        when(savedMeasurement.getMeasurementDetails()).thenReturn(MeasurementDetails.builder()
                .build());
        when(measurementService.getTimeSlot(anyLong())).thenReturn(timeSlot);
        when(savedMeasurement.getId()).thenReturn(TestConstants.DEFAULT_TEST_ID);

        var response = mobileMeasurementRegistrationService.registerMobileMeasurement(testSettingsRequest, headers, request);

        assertEquals(TestConstants.DEFAULT_IP, response.getClientRemoteIp());
        assertEquals(TestConstants.DEFAULT_MEASUREMENT_SERVER_ADDRESS, response.getTestServerAddress());
        assertEquals(TestConstants.DEFAULT_MEASUREMENT_SERVER_PORT, response.getTestServerPort());
        assertEquals(TestConstants.DEFAULT_MEASUREMENT_SERVER_NAME, response.getTestServerName());
        assertTrue(response.isTestServerEncryption());
        assertEquals(String.valueOf(TestConstants.DEFAULT_TEST_DURATION), response.getTestDuration());
        assertEquals(String.valueOf(TestConstants.DEFAULT_NUM_TEST_THREADS), response.getTestNumThreadsMobile());
        assertEquals(String.valueOf(TestConstants.DEFAULT_TEST_NUM_PINGS), response.getTestNumPings());
        assertEquals(TestConstants.DEFAULT_TEST_ID, response.getTestId());
        assertEquals(TestConstants.DEFAULT_MEASUREMENT_SERVER_TYPE, response.getTestServerType());

        verify(measurementService, times(2)).save(measurementArgumentCaptor.capture());
        assertEquals(TestConstants.DEFAULT_CLIENT_UUID_STRING, measurementArgumentCaptor.getAllValues().get(0).getClientUuid());
        assertEquals(TestConstants.DEFAULT_CLIENT_NAME, measurementArgumentCaptor.getAllValues().get(0).getClientName());
        assertEquals(TestConstants.DEFAULT_CLIENT_VERSION, measurementArgumentCaptor.getAllValues().get(0).getClientVersion());
        assertEquals(TestConstants.DEFAULT_LANGUAGE, measurementArgumentCaptor.getAllValues().get(0).getClientLanguage());
        assertEquals(TestConstants.DEFAULT_TEST_SERVER_ID, measurementArgumentCaptor.getAllValues().get(0).getMeasurementServerId());
        assertEquals(TestConstants.DEFAULT_TIME_ZONE, measurementArgumentCaptor.getAllValues().get(0).getTimezone());
        assertEquals(TestConstants.DEFAULT_NUM_TEST_THREADS, measurementArgumentCaptor.getAllValues().get(0).getTestNumThreads());
        assertEquals(MeasurementStatus.STARTED, measurementArgumentCaptor.getAllValues().get(0).getStatus());
        assertEquals(MeasurementType.DEDICATED.toString(), measurementArgumentCaptor.getAllValues().get(0).getType());
        assertEquals(TestConstants.DEFAULT_COORDINATES_LATITUDE.doubleValue(), measurementArgumentCaptor.getAllValues().get(0).getLatitude(), 0);
        assertEquals(TestConstants.DEFAULT_COORDINATES_LONGITUDE.doubleValue(), measurementArgumentCaptor.getAllValues().get(0).getLongitude(), 0);
    }

    @Test
    public void registerMobileMeasurement_whenLoopMode_expectLoopModeSettingsSaved() {
        TimeSlot timeSlot = TimeSlot.builder()
                .slot(TestConstants.DEFAULT_SLOT)
                .testWait(TestConstants.DEFAULT_WAIT)
                .build();
        when(clientService.getClientByUUID(UUID.fromString(TestConstants.DEFAULT_CLIENT_UUID_STRING))).thenReturn(client);
        when(measurementServerService.getMeasurementServerByIdOrGetDefault(TestConstants.DEFAULT_MEASUREMENT_SERVER_ID)).thenReturn(measurementServer);
        when(measurementServer.getSecretKey()).thenReturn(TestConstants.DEFAULT_TEXT);
        when(measurementService.getTimeSlot(anyLong())).thenReturn(timeSlot);
        when(measurementService.save(any())).thenReturn(savedMeasurement);
        when(savedMeasurement.getMeasurementDetails()).thenReturn(MeasurementDetails.builder()
                .build());
        when(client.getUuid()).thenReturn(TestConstants.DEFAULT_CLIENT_UUID_STRING);


        mobileMeasurementRegistrationService.registerMobileMeasurement(testSettingsRequest, headers, request);

        verify(loopModeSettingsService).processLoopModeSettingsInfo(eq(TestConstants.DEFAULT_CLIENT_UUID_STRING), eq(loopModeInfo), any());
    }

    @Test
    public void registerMobileMeasurement_whenServerTypeEmpty_expectDefaultMeasurementServerType() {
        TimeSlot timeSlot = TimeSlot.builder()
                .slot(TestConstants.DEFAULT_SLOT)
                .testWait(TestConstants.DEFAULT_WAIT)
                .build();
        when(clientService.getClientByUUID(UUID.fromString(TestConstants.DEFAULT_CLIENT_UUID_STRING))).thenReturn(client);
        when(measurementServerService.getMeasurementServerByIdOrGetDefault(TestConstants.DEFAULT_MEASUREMENT_SERVER_ID)).thenReturn(measurementServer);
        when(measurementServer.getSecretKey()).thenReturn(TestConstants.DEFAULT_TEXT);
        when(measurementService.getTimeSlot(anyLong())).thenReturn(timeSlot);
        when(measurementService.save(any())).thenReturn(savedMeasurement);
        when(savedMeasurement.getMeasurementDetails()).thenReturn(MeasurementDetails.builder()
                .build());

        var response = mobileMeasurementRegistrationService.registerMobileMeasurement(testSettingsRequest, headers, request);

        assertEquals(Constants.DEFAULT_MEASUREMENT_SERVER_TYPE, response.getTestServerType());
    }

    @Test
    public void registerMobileMeasurement_whenSettingsNotEmptyAndPlatformIsAndroid_expectResponse() {
        testSettingsRequest.setNumberOfThreads(null);
        testSettingsRequest.setPlatform(Platform.ANDROID);
        TimeSlot timeSlot = TimeSlot.builder()
                .slot(TestConstants.DEFAULT_SLOT)
                .testWait(TestConstants.DEFAULT_WAIT)
                .build();
        when(clientService.getClientByUUID(UUID.fromString(TestConstants.DEFAULT_CLIENT_UUID_STRING))).thenReturn(client);
        when(measurementServerService.getMeasurementServerByIdOrGetDefault(TestConstants.DEFAULT_MEASUREMENT_SERVER_ID)).thenReturn(measurementServer);
        when(measurementServer.getSecretKey()).thenReturn(TestConstants.DEFAULT_TEXT);
        when(measurementService.getTimeSlot(anyLong())).thenReturn(timeSlot);
        when(measurementService.save(any())).thenReturn(savedMeasurement);
        when(savedMeasurement.getMeasurementDetails()).thenReturn(MeasurementDetails.builder()
                .build());
        when(settingsService.getSettingsMap()).thenReturn(getSettingsMap());

        var response = mobileMeasurementRegistrationService.registerMobileMeasurement(testSettingsRequest, headers, request);

        assertEquals(TestConstants.DEFAULT_MEASUREMENT_REQUEST_MEASUREMENT_NUM_PINGS, response.getTestNumPings());
        assertEquals(TestConstants.DEFAULT_MEASUREMENT_REQUEST_MEASUREMENT_NUM_THREADS_ANDROID, response.getTestNumThreadsMobile());
        assertEquals(TestConstants.DEFAULT_MEASUREMENT_REQUEST_MEASUREMENT_DURATION, response.getTestDuration());
    }

    @Test
    public void registerMobileMeasurement_whenSettingsNotEmptyAndPlatformIsIos_expectResponse() {
        testSettingsRequest.setNumberOfThreads(null);
        testSettingsRequest.setPlatform(Platform.IOS);
        TimeSlot timeSlot = TimeSlot.builder()
                .slot(TestConstants.DEFAULT_SLOT)
                .testWait(TestConstants.DEFAULT_WAIT)
                .build();
        when(clientService.getClientByUUID(UUID.fromString(TestConstants.DEFAULT_CLIENT_UUID_STRING))).thenReturn(client);
        when(measurementServerService.getMeasurementServerByIdOrGetDefault(TestConstants.DEFAULT_MEASUREMENT_SERVER_ID)).thenReturn(measurementServer);
        when(measurementServer.getSecretKey()).thenReturn(TestConstants.DEFAULT_TEXT);
        when(measurementService.getTimeSlot(anyLong())).thenReturn(timeSlot);
        when(measurementService.save(any())).thenReturn(savedMeasurement);
        when(savedMeasurement.getMeasurementDetails()).thenReturn(MeasurementDetails.builder()
                .build());
        when(settingsService.getSettingsMap()).thenReturn(getSettingsMap());

        var response = mobileMeasurementRegistrationService.registerMobileMeasurement(testSettingsRequest, headers, request);

        assertEquals(TestConstants.DEFAULT_MEASUREMENT_REQUEST_MEASUREMENT_NUM_PINGS, response.getTestNumPings());
        assertEquals(TestConstants.DEFAULT_MEASUREMENT_REQUEST_MEASUREMENT_NUM_THREADS_IOS, response.getTestNumThreadsMobile());
        assertEquals(TestConstants.DEFAULT_MEASUREMENT_REQUEST_MEASUREMENT_DURATION, response.getTestDuration());
    }

    @Test
    public void registerMobileMeasurement_whenPermissionNotNull_expectTest() {
        TimeSlot timeSlot = TimeSlot.builder()
                .slot(TestConstants.DEFAULT_SLOT)
                .testWait(TestConstants.DEFAULT_WAIT)
                .build();
        when(clientService.getClientByUUID(UUID.fromString(TestConstants.DEFAULT_CLIENT_UUID_STRING))).thenReturn(client);
        when(measurementServerService.getMeasurementServerByIdOrGetDefault(TestConstants.DEFAULT_MEASUREMENT_SERVER_ID)).thenReturn(measurementServer);
        when(measurementServer.getSecretKey()).thenReturn(TestConstants.DEFAULT_TEXT);
        when(measurementService.getTimeSlot(anyLong())).thenReturn(timeSlot);
        when(measurementService.save(any())).thenReturn(savedMeasurement);
        when(savedMeasurement.getMeasurementDetails()).thenReturn(MeasurementDetails.builder()
                .build());
        testSettingsRequest.setUuidPermissionGranted(true);
        testSettingsRequest.setLocationPermissionGranted(false);
        testSettingsRequest.setTelephonyPermissionGranted(true);

        mobileMeasurementRegistrationService.registerMobileMeasurement(testSettingsRequest, headers, request);

        verify(measurementService, times(2)).save(measurementArgumentCaptor.capture());
        assertEquals(true, measurementArgumentCaptor.getAllValues().get(0).getMeasurementDetails().getUuidPermissionGranted());
        assertEquals(false, measurementArgumentCaptor.getAllValues().get(0).getMeasurementDetails().getLocationPermissionGranted());
        assertEquals(true, measurementArgumentCaptor.getAllValues().get(0).getMeasurementDetails().getTelephonyPermissionGranted());
    }

    @Test
    public void registerMobileMeasurement_whenLocationNotNull_expectTest() {
        TimeSlot timeSlot = TimeSlot.builder()
                .slot(TestConstants.DEFAULT_SLOT)
                .testWait(TestConstants.DEFAULT_WAIT)
                .build();
        when(clientService.getClientByUUID(UUID.fromString(TestConstants.DEFAULT_CLIENT_UUID_STRING))).thenReturn(client);
        when(measurementServerService.getMeasurementServerByIdOrGetDefault(TestConstants.DEFAULT_MEASUREMENT_SERVER_ID)).thenReturn(measurementServer);
        when(measurementServer.getSecretKey()).thenReturn(TestConstants.DEFAULT_TEXT);
        when(measurementService.getTimeSlot(anyLong())).thenReturn(timeSlot);
        when(measurementService.save(any())).thenReturn(savedMeasurement);
        when(savedMeasurement.getMeasurementDetails()).thenReturn(MeasurementDetails.builder()
                .build());
        testSettingsRequest.setLocation(LocationRequest.builder()
                .city(TestConstants.DEFAULT_CITY)
                .country(TestConstants.DEFAULT_COUNTRY)
                .county(TestConstants.DEFAULT_COUNTY)
                .postalCode(TestConstants.DEFAULT_POSTAL_CODE)
                .latitude(TestConstants.DEFAULT_LATITUDE)
                .longitude(TestConstants.DEFAULT_LONGITUDE)
                .build());

        mobileMeasurementRegistrationService.registerMobileMeasurement(testSettingsRequest, headers, request);

        verify(measurementService, times(2)).save(measurementArgumentCaptor.capture());
        assertEquals(TestConstants.DEFAULT_COUNTRY, measurementArgumentCaptor.getAllValues().get(0).getMeasurementDetails().getCountry());
        assertEquals(TestConstants.DEFAULT_CITY, measurementArgumentCaptor.getAllValues().get(0).getMeasurementDetails().getCity());
        assertEquals(TestConstants.DEFAULT_POSTAL_CODE, measurementArgumentCaptor.getAllValues().get(0).getMeasurementDetails().getPostalCode());
        assertEquals(TestConstants.DEFAULT_COUNTY, measurementArgumentCaptor.getAllValues().get(0).getMeasurementDetails().getCounty());
        assertEquals(TestConstants.DEFAULT_LATITUDE, measurementArgumentCaptor.getAllValues().get(0).getLatitude());
        assertEquals(TestConstants.DEFAULT_LONGITUDE, measurementArgumentCaptor.getAllValues().get(0).getLongitude());
    }

    @Test
    public void registerMobileMeasurement_whenLocationNotNullAndLocationPermissionNotGranted_expectTest() {
        TimeSlot timeSlot = TimeSlot.builder()
                .slot(TestConstants.DEFAULT_SLOT)
                .testWait(TestConstants.DEFAULT_WAIT)
                .build();
        when(clientService.getClientByUUID(UUID.fromString(TestConstants.DEFAULT_CLIENT_UUID_STRING))).thenReturn(client);
        when(measurementServerService.getMeasurementServerByIdOrGetDefault(TestConstants.DEFAULT_MEASUREMENT_SERVER_ID)).thenReturn(measurementServer);
        when(measurementServer.getSecretKey()).thenReturn(TestConstants.DEFAULT_TEXT);
        when(measurementService.getTimeSlot(anyLong())).thenReturn(timeSlot);
        when(measurementService.save(any())).thenReturn(savedMeasurement);
        when(savedMeasurement.getMeasurementDetails()).thenReturn(MeasurementDetails.builder()
                .build());
        testSettingsRequest.setLocation(LocationRequest.builder()
                .city(TestConstants.DEFAULT_CITY)
                .country(TestConstants.DEFAULT_COUNTRY)
                .county(TestConstants.DEFAULT_COUNTY)
                .postalCode(TestConstants.DEFAULT_POSTAL_CODE)
                .build());
        testSettingsRequest.setLocationPermissionGranted(false);

        mobileMeasurementRegistrationService.registerMobileMeasurement(testSettingsRequest, headers, request);

        verify(measurementService, times(2)).save(measurementArgumentCaptor.capture());
        assertEquals(TestConstants.DEFAULT_COUNTRY, measurementArgumentCaptor.getAllValues().get(0).getMeasurementDetails().getCountry());
        assertNull(measurementArgumentCaptor.getAllValues().get(0).getMeasurementDetails().getCity());
        assertNull(measurementArgumentCaptor.getAllValues().get(0).getMeasurementDetails().getPostalCode());
        assertNull(measurementArgumentCaptor.getAllValues().get(0).getMeasurementDetails().getCounty());
        assertNull(measurementArgumentCaptor.getAllValues().get(0).getLatitude());
        assertNull(measurementArgumentCaptor.getAllValues().get(0).getLongitude());
    }

    private Map<String, String> getSettingsMap() {
        return Map.of(AdminSetting.MEASUREMENT_DURATION_KEY, TestConstants.DEFAULT_MEASUREMENT_REQUEST_MEASUREMENT_DURATION,
                AdminSetting.MEASUREMENT_NUM_THREADS_ANDROID_KEY, TestConstants.DEFAULT_MEASUREMENT_REQUEST_MEASUREMENT_NUM_THREADS_ANDROID,
                AdminSetting.MEASUREMENT_NUM_THREADS_IOS_KEY, TestConstants.DEFAULT_MEASUREMENT_REQUEST_MEASUREMENT_NUM_THREADS_IOS,
                AdminSetting.MEASUREMENT_NUM_PINGS_KEY, TestConstants.DEFAULT_MEASUREMENT_REQUEST_MEASUREMENT_NUM_PINGS);
    }

    private static LocationRequest getLocation() {
        return LocationRequest.builder()
                .latitude(TestConstants.DEFAULT_COORDINATES_LATITUDE.doubleValue())
                .longitude(TestConstants.DEFAULT_COORDINATES_LONGITUDE.doubleValue())
                .build();
    }
}
