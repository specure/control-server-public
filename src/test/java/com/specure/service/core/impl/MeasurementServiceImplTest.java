package com.specure.service.core.impl;

import com.specure.common.config.MeasurementServerConfig;
import com.specure.common.enums.MeasurementStatus;
import com.specure.common.model.jpa.*;
import com.specure.common.repository.MeasurementRepository;
import com.specure.common.service.digger.DiggerService;
import com.specure.common.utils.HeaderExtrudeUtil;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.*;
import com.specure.core.TestConstants;
import com.specure.mapper.core.MeasurementMapper;
import com.specure.model.dto.TimeSlot;
import com.specure.model.dto.internal.DataForMeasurementRegistration;
import com.specure.multitenant.MultiTenantManager;
import com.specure.request.core.MeasurementRequest;
import com.specure.service.admin.RawProviderService;
import com.specure.service.core.FieldAnonymizerFilter;
import com.specure.service.core.MeasurementService;
import com.specure.service.core.UserAgentExtractService;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.util.*;

import static com.specure.core.TestConstants.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class MeasurementServiceImplTest {

    @MockBean
    private MeasurementRepository measurementRepository;
    @MockBean
    private MeasurementMapper measurementMapper;
    @MockBean
    private MeasurementServerConfig measurementServerConfig;
    @MockBean
    private DiggerService diggerService;
    @MockBean
    private UserAgentExtractService userAgentExtractService;
    @MockBean
    private MultiTenantManager multiTenantManager;
    @MockBean
    private FieldAnonymizerFilter fieldAnonymizerFilter;
    @MockBean
    private RawProviderService providerService;

    private MeasurementService measurementService;

    @Captor
    private ArgumentCaptor<Measurement> measurementCaptor;

    @Captor
    private ArgumentCaptor<Pageable> pageableArgumentCaptor;
    @Mock
    private CityResponse cityResponse;
    @Mock
    private City city;
    @Mock
    private Country country;
    @Mock
    private Postal postalCode;
    @Mock
    private Subdivision subdivision;
    @Mock
    private Traits traits;
    @Mock
    private Measurement savedMeasurement;
    @Mock
    private RawProvider provider;
    @Mock
    private AdHocCampaign adHocCampaign;
    @Mock
    private Measurement measurementAfterMapping;

    @Before
    public void setUp() {
        // for Timestamp constructor
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        measurementService = new MeasurementServiceImpl(measurementRepository,
                measurementMapper,
                measurementServerConfig,
                diggerService,
                userAgentExtractService,
                multiTenantManager,
                fieldAnonymizerFilter,
                providerService);
    }

    @Test
    public void partialUpdateMeasurementFromProbeResult_WhenCall_ExpectSaveRepository() throws InterruptedException {
        MeasurementRequest measurementRequest = getMeasurementRequestDefault();

        Timestamp now = new Timestamp(System.currentTimeMillis());
        Measurement measurementFromDB = Measurement.builder()
                .openTestUuid(DEFAULT_OPEN_TEST_UUID)
                .longitude(com.specure.sah.TestConstants.DEFAULT_LONGITUDE)
                .latitude(com.specure.sah.TestConstants.DEFAULT_LATITUDE)
                .time(now).build();

        when(measurementRepository.findByToken(DEFAULT_TOKEN))
                .thenReturn(Optional.of(measurementFromDB));
        when(measurementMapper.measurementRequestToMeasurement(measurementRequest))
                .thenReturn(measurementAfterMapping);
        when(measurementAfterMapping.getSpeedDetail()).thenReturn(Collections.emptyList());
        when(measurementAfterMapping.getToken()).thenReturn(DEFAULT_TOKEN);

        measurementService.partialUpdateMeasurementFromProbeResult(measurementRequest, Map.of(HeaderExtrudeUtil.HEADER_NGINX_X_REAL_IP, com.specure.sah.TestConstants.DEFAULT_IP_V4));

        Thread.sleep(100);
        verify(measurementRepository).save(measurementAfterMapping);
        verify(measurementAfterMapping).setLatitude(com.specure.sah.TestConstants.DEFAULT_LATITUDE);
        verify(measurementAfterMapping).setLongitude(com.specure.sah.TestConstants.DEFAULT_LONGITUDE);
        verify(fieldAnonymizerFilter).refreshAnonymizedIpAddress(com.specure.sah.TestConstants.DEFAULT_IP_V4, DEFAULT_OPEN_TEST_UUID);
    }

    @Ignore // after async version of saving to DB was implemented
    @Test
    public void partialUpdateMeasurementFromProbeResult_WhenCall_ExpectPartialUpdate() {
        long id = 1L;
        MeasurementRequest measurementRequest = getMeasurementRequestDefault();

        Timestamp now = new Timestamp(System.currentTimeMillis());
        Measurement measurementFromDB = Measurement.builder()
                .id(id)
                .openTestUuid(DEFAULT_OPEN_TEST_UUID)
                .token(DEFAULT_TOKEN)
                .time(now)
                .build();
        Measurement afterPartialUpdate = getMeasurementDefault();
        Measurement measurementAfterMapping = getMeasurementDefault();


        when(measurementRepository.findByToken(DEFAULT_TOKEN))
                .thenReturn(Optional.of(measurementFromDB));
        when(measurementMapper.measurementRequestToMeasurement(measurementRequest))
                .thenReturn(measurementAfterMapping);
        when(measurementRepository.save(afterPartialUpdate))
                .thenReturn(null);

        afterPartialUpdate.setId(id);
        afterPartialUpdate.setTime(now);
        // we don't change this parameters
        afterPartialUpdate.setDevice(null);
        afterPartialUpdate.setTag(null);
        afterPartialUpdate.setStatus(MeasurementStatus.FINISHED);


        measurementService.partialUpdateMeasurementFromProbeResult(measurementRequest, Map.of(HeaderExtrudeUtil.HEADER_NGINX_X_REAL_IP, com.specure.sah.TestConstants.DEFAULT_IP_V4));

        verify(measurementRepository).save(afterPartialUpdate);
        verify(fieldAnonymizerFilter).refreshAnonymizedIpAddress(com.specure.sah.TestConstants.DEFAULT_IP_V4, DEFAULT_OPEN_TEST_UUID);
    }

    @Test
    public void registerMeasurement_WhenCallWithDataWithoutProvider_ExpectNullInProvider() {
        MeasurementServer measurementServer = MeasurementServer.builder()
                .secretKey(DEFAULT_MEASUREMENT_SERVER_SECRET_KEY)
                .build();
        final DataForMeasurementRegistration dataForMeasurementRegistration = getDataForMeasurementRegistration(measurementServer);

        when(measurementServerConfig.getHost()).thenReturn(DEFAULT_HOST);
        when(measurementServerConfig.getSlotWindow()).thenReturn(DEFAULT_SLOT_WINDOW);
        when(measurementRepository.countAllByTestSlot(anyInt())).thenReturn(0L);
        when(measurementRepository.save(any())).thenReturn(savedMeasurement);
        when(savedMeasurement.getMeasurementDetails()).thenReturn(MeasurementDetails.builder()
                .appVersion(com.specure.sah.TestConstants.DEFAULT_APP_VERSION)
                .build());

        measurementService.registerMeasurement(dataForMeasurementRegistration, DEFAULT_HEADER);
        verify(measurementRepository).save(measurementCaptor.capture());

        final Measurement measurement = measurementCaptor.getValue();
        assertNull(measurement.getNetworkOperator());

    }

    @Test
    public void registerMeasurement_WhenPermissionNotNull_ExpectSaveMeasurement() {
        MeasurementServer measurementServer = MeasurementServer.builder()
                .secretKey(DEFAULT_MEASUREMENT_SERVER_SECRET_KEY)
                .build();
        final DataForMeasurementRegistration dataForMeasurementRegistration = getDataForMeasurementRegistration(measurementServer);

        when(measurementServerConfig.getHost()).thenReturn(DEFAULT_HOST);
        when(measurementServerConfig.getSlotWindow()).thenReturn(DEFAULT_SLOT_WINDOW);
        when(measurementRepository.countAllByTestSlot(anyInt())).thenReturn(0L);
        when(measurementRepository.save(any())).thenReturn(savedMeasurement);
        when(savedMeasurement.getMeasurementDetails()).thenReturn(MeasurementDetails.builder()
                .appVersion(com.specure.sah.TestConstants.DEFAULT_APP_VERSION)
                .build());

        measurementService.registerMeasurement(dataForMeasurementRegistration, DEFAULT_HEADER);
        verify(measurementRepository).save(measurementCaptor.capture());

        final Measurement measurement = measurementCaptor.getValue();
        assertTrue(measurement.getMeasurementDetails().getLocationPermissionGranted());
        assertFalse(measurement.getMeasurementDetails().getUuidPermissionGranted());
        assertTrue(measurement.getMeasurementDetails().getTelephonyPermissionGranted());
    }

    @Test
    public void registerMeasurement_WhenCall_ExpectSaveMeasurement() {
        MeasurementServer measurementServer = MeasurementServer.builder()
                .secretKey(DEFAULT_MEASUREMENT_SERVER_SECRET_KEY)
                .name(DEFAULT_MEASUREMENT_SERVER_NAME)
                .webAddress(DEFAULT_MEASUREMENT_SERVER_ADDRESS)
                .build();
        final DataForMeasurementRegistration dataForMeasurementRegistration = getDataForMeasurementRegistration(measurementServer);

        when(measurementServerConfig.getHost()).thenReturn(DEFAULT_HOST);
        when(measurementServerConfig.getSlotWindow()).thenReturn(DEFAULT_SLOT_WINDOW);
        when(measurementRepository.countAllByTestSlot(anyInt())).thenReturn(0L);
        when(measurementRepository.save(any())).thenReturn(savedMeasurement);
        when(savedMeasurement.getMeasurementDetails()).thenReturn(MeasurementDetails.builder()
                .appVersion(com.specure.sah.TestConstants.DEFAULT_APP_VERSION)
                .build());

        measurementService.registerMeasurement(dataForMeasurementRegistration, DEFAULT_HEADER);

        verify(measurementRepository).save(any());
    }

    @Test
    public void registerMeasurement_WhenProbeRequest_ExpectSaveMeasurement() {
        Measurement savedMeasurement = Measurement.builder()
                .id(1L)
                .measurementDetails(MeasurementDetails.builder()
                        .appVersion(com.specure.sah.TestConstants.DEFAULT_APP_VERSION)
                        .build())
                .build();
        when(measurementRepository.save(any())).thenReturn(savedMeasurement);
        when(measurementServerConfig.getSlotWindow()).thenReturn(2L);
        var headers = Map.of("x-real-ip", TestConstants.DEFAULT_IP_ADDRESS);
        when(diggerService.getCityResponseByIpAddress(DEFAULT_IP_ADDRESS)).thenReturn(Optional.of(cityResponse));
        when(cityResponse.getCity()).thenReturn(city);
        when(cityResponse.getCountry()).thenReturn(country);
        when(cityResponse.getPostal()).thenReturn(postalCode);
        when(cityResponse.getSubdivisions()).thenReturn(List.of(subdivision));
        when(subdivision.getName()).thenReturn(TestConstants.DEFAULT_COUNTY);
        when(postalCode.getCode()).thenReturn(TestConstants.DEFAULT_POSTAL_CODE);
        when(city.getName()).thenReturn(DEFAULT_CITY);
        when(country.getName()).thenReturn(TestConstants.DEFAULT_COUNTRY);
        MeasurementServer measurementServer = MeasurementServer.builder()
                .secretKey(DEFAULT_MEASUREMENT_SERVER_SECRET_KEY)
                .name(DEFAULT_MEASUREMENT_SERVER_NAME)
                .webAddress(DEFAULT_MEASUREMENT_SERVER_ADDRESS)
                .build();
        var dataForMeasurementRegistration = getDataForMeasurementRegistration(measurementServer);
        dataForMeasurementRegistration.setPort(DEFAULT_PORT);
        dataForMeasurementRegistration.setProviderName(com.specure.sah.TestConstants.DEFAULT_PROVIDER_ISP_NAME);
        when(diggerService.digASN(DEFAULT_IP_ADDRESS)).thenReturn(DEFAULT_ASN_LONG);
        when(providerService.getRawProvider(cityResponse, DEFAULT_ASN_LONG)).thenReturn(provider);
        when(provider.getRawName()).thenReturn(com.specure.sah.TestConstants.DEFAULT_PROVIDER_ISP_RAW_ID);

        measurementService.registerMeasurement(dataForMeasurementRegistration, headers);
        verify(measurementRepository).save(measurementCaptor.capture());
        assertEquals(DEFAULT_COUNTY, measurementCaptor.getValue().getMeasurementDetails().getCounty());
        assertEquals(DEFAULT_COUNTRY, measurementCaptor.getValue().getMeasurementDetails().getCountry());
        assertEquals(DEFAULT_CITY, measurementCaptor.getValue().getMeasurementDetails().getCity());
        assertEquals(DEFAULT_POSTAL_CODE, measurementCaptor.getValue().getMeasurementDetails().getPostalCode());
        assertEquals(com.specure.sah.TestConstants.DEFAULT_PROVIDER_ISP_RAW_ID, measurementCaptor.getValue().getIspRawId());
        assertEquals(com.specure.sah.TestConstants.DEFAULT_PROVIDER_ISP_NAME, measurementCaptor.getValue().getIspName());
        assertEquals(com.specure.sah.TestConstants.DEFAULT_PROVIDER_ISP_NAME, measurementCaptor.getValue().getNetworkOperator());
        assertEquals(com.specure.sah.TestConstants.DEFAULT_PROVIDER_ISP_NAME, measurementCaptor.getValue().getClientProvider());
    }

    @Test
    public void registerMeasurement_WhenCall_ExpectGetProvider() {
        MeasurementServer measurementServer = MeasurementServer.builder()
                .secretKey(DEFAULT_MEASUREMENT_SERVER_SECRET_KEY)
                .name(DEFAULT_MEASUREMENT_SERVER_NAME)
                .webAddress(DEFAULT_MEASUREMENT_SERVER_ADDRESS)
                .build();
        final DataForMeasurementRegistration dataForMeasurementRegistration = getDataForMeasurementRegistration(measurementServer);

        when(measurementServerConfig.getSlotWindow()).thenReturn(DEFAULT_SLOT_WINDOW);
        when(measurementRepository.countAllByTestSlot(anyInt())).thenReturn(0L);
        when(measurementRepository.save(any())).thenReturn(savedMeasurement);
        when(measurementServerConfig.getHost()).thenReturn(DEFAULT_HOST);
        when(diggerService.getCityResponseByIpAddress(DEFAULT_IP_FOR_PROVIDER)).thenReturn(Optional.of(cityResponse));
        when(diggerService.digASN(DEFAULT_IP_FOR_PROVIDER)).thenReturn(DEFAULT_ASN_LONG);
        when(cityResponse.getTraits()).thenReturn(traits);
        when(traits.getAutonomousSystemNumber()).thenReturn(null);
        when(traits.getIsp()).thenReturn(DEFAULT_GEO_PROVIDER_NAME);
        when(providerService.getRawProvider(cityResponse, DEFAULT_ASN_LONG)).thenReturn(provider);
        when(savedMeasurement.getIspName()).thenReturn(DEFAULT_GEO_PROVIDER_NAME);
        when(savedMeasurement.getMeasurementDetails()).thenReturn(MeasurementDetails.builder()
                .appVersion(com.specure.sah.TestConstants.DEFAULT_APP_VERSION)
                .build());

        Map<String, String> header = new HashMap<>() {{
            put("x-forwarded-for", DEFAULT_IP_FOR_PROVIDER);
        }};
        var result = measurementService.registerMeasurement(dataForMeasurementRegistration, header);

        verify(measurementRepository).save(any());
        assertEquals(DEFAULT_GEO_PROVIDER_NAME, result.getProvider());
        assertEquals(com.specure.sah.TestConstants.DEFAULT_APP_VERSION, result.getAppVersion());
        verify(measurementMapper).updateMeasurementProviderInfo(any(), eq(provider));
    }

    @Test
    public void registerMeasurement_whenCityDetailsFound_expectCityDetailsSaved() {
        Measurement savedMeasurement = Measurement.builder()
                .id(1L)
                .measurementDetails(MeasurementDetails.builder()
                        .appVersion(com.specure.sah.TestConstants.DEFAULT_APP_VERSION)
                        .build())
                .build();
        when(measurementRepository.save(any())).thenReturn(savedMeasurement);
        when(measurementServerConfig.getSlotWindow()).thenReturn(2L);
        var headers = Map.of("x-real-ip", TestConstants.DEFAULT_IP_ADDRESS);
        when(diggerService.getCityResponseByIpAddress(DEFAULT_IP_ADDRESS)).thenReturn(Optional.of(cityResponse));
        when(cityResponse.getCity()).thenReturn(city);
        when(cityResponse.getCountry()).thenReturn(country);
        when(cityResponse.getPostal()).thenReturn(postalCode);
        when(cityResponse.getSubdivisions()).thenReturn(List.of(subdivision));
        when(subdivision.getName()).thenReturn(TestConstants.DEFAULT_COUNTY);
        when(postalCode.getCode()).thenReturn(TestConstants.DEFAULT_POSTAL_CODE);
        when(city.getName()).thenReturn(DEFAULT_CITY);
        when(country.getName()).thenReturn(TestConstants.DEFAULT_COUNTRY);
        MeasurementServer measurementServer = MeasurementServer.builder()
                .secretKey(DEFAULT_MEASUREMENT_SERVER_SECRET_KEY)
                .name(DEFAULT_MEASUREMENT_SERVER_NAME)
                .webAddress(DEFAULT_MEASUREMENT_SERVER_ADDRESS)
                .build();
        var dataForMeasurementRegistration = getDataForMeasurementRegistration(measurementServer);

        measurementService.registerMeasurement(dataForMeasurementRegistration, headers);
        verify(measurementRepository).save(measurementCaptor.capture());
        assertEquals(DEFAULT_COUNTY, measurementCaptor.getValue().getMeasurementDetails().getCounty());
        assertEquals(DEFAULT_COUNTRY, measurementCaptor.getValue().getMeasurementDetails().getCountry());
        assertEquals(DEFAULT_CITY, measurementCaptor.getValue().getMeasurementDetails().getCity());
        assertEquals(DEFAULT_POSTAL_CODE, measurementCaptor.getValue().getMeasurementDetails().getPostalCode());
    }

    @Test
    public void registerMeasurement_whenCityDetailsFoundAndLocationPermissionNotGranted_expectCityDetailsSaved() {
        Measurement savedMeasurement = Measurement.builder()
                .id(1L)
                .measurementDetails(MeasurementDetails.builder()
                        .appVersion(com.specure.sah.TestConstants.DEFAULT_APP_VERSION)
                        .build())
                .build();
        when(measurementRepository.save(any())).thenReturn(savedMeasurement);
        when(measurementServerConfig.getSlotWindow()).thenReturn(2L);
        var headers = Map.of("x-real-ip", TestConstants.DEFAULT_IP_ADDRESS);
        when(diggerService.getCityResponseByIpAddress(DEFAULT_IP_ADDRESS)).thenReturn(Optional.of(cityResponse));
        when(cityResponse.getCity()).thenReturn(city);
        when(cityResponse.getCountry()).thenReturn(country);
        when(cityResponse.getPostal()).thenReturn(postalCode);
        when(cityResponse.getSubdivisions()).thenReturn(List.of(subdivision));
        when(subdivision.getName()).thenReturn(TestConstants.DEFAULT_COUNTY);
        when(postalCode.getCode()).thenReturn(TestConstants.DEFAULT_POSTAL_CODE);
        when(city.getName()).thenReturn(DEFAULT_CITY);
        when(country.getName()).thenReturn(TestConstants.DEFAULT_COUNTRY);
        MeasurementServer measurementServer = MeasurementServer.builder()
                .secretKey(DEFAULT_MEASUREMENT_SERVER_SECRET_KEY)
                .name(DEFAULT_MEASUREMENT_SERVER_NAME)
                .webAddress(DEFAULT_MEASUREMENT_SERVER_ADDRESS)
                .build();
        var dataForMeasurementRegistration = getDataForMeasurementRegistration(measurementServer);
        dataForMeasurementRegistration.setLocationPermissionGranted(false);

        measurementService.registerMeasurement(dataForMeasurementRegistration, headers);
        verify(measurementRepository).save(measurementCaptor.capture());
        assertEquals(DEFAULT_COUNTRY, measurementCaptor.getValue().getMeasurementDetails().getCountry());
        assertNull(measurementCaptor.getValue().getMeasurementDetails().getCounty());
        assertNull(measurementCaptor.getValue().getMeasurementDetails().getCity());
        assertNull(measurementCaptor.getValue().getMeasurementDetails().getPostalCode());
    }

    @Test
    public void getTimeSlot_WhenCall_ExpectReturnTimeSlot() {
        long now = 1590671637123L;
        int waitExpected = 0;
        int slotExpected = Math.toIntExact(now / 1000);

        when(measurementRepository.countAllByTestSlot(slotExpected)).thenReturn(0L);
        when(measurementServerConfig.getSlotWindow()).thenReturn(DEFAULT_SLOT_WINDOW);

        TimeSlot timeSlot = measurementService.getTimeSlot(now);

        assertEquals(slotExpected, timeSlot.getSlot().intValue());
        assertEquals(waitExpected, timeSlot.getTestWait().intValue());
    }

    @Test
    public void getTimeSlot_WhenCall_ExpectReturnOnlyFreeTimeSlot() {
        long now = 1590671637123L;
        int waitExpected = 3;
        int slot = Math.toIntExact(now / 1000);
        long slotWindow = 5;

        when(measurementServerConfig.getSlotWindow()).thenReturn(slotWindow);

        when(measurementRepository.countAllByTestSlot(slot)).thenReturn(slotWindow);
        when(measurementRepository.countAllByTestSlot(slot + 1)).thenReturn(slotWindow);
        when(measurementRepository.countAllByTestSlot(slot + 2)).thenReturn(slotWindow);
        when(measurementRepository.countAllByTestSlot(slot + 3)).thenReturn(slotWindow - 1);

        TimeSlot timeSlot = measurementService.getTimeSlot(now);

        assertEquals(slot + 3, timeSlot.getSlot().intValue());
        assertEquals(waitExpected, timeSlot.getTestWait().intValue());
    }

    private MeasurementRequest getMeasurementRequestDefault() {
        return MeasurementRequest.builder()
                .speedDetail(Collections.emptyList())
                .build();
    }

    private Measurement getMeasurementDefault() {
        return Measurement.builder()
                .token(DEFAULT_TOKEN)
                .speedDownload(DEFAULT_SPEED_DOWNLOAD)
                .speedUpload(DEFAULT_SPEED_UPLOAD)
                .lte_rsrp(DEFAULT_LTE_RSRP)
                .voip_result_packet_loss(DEFAULT_VOIP_RESULT_PACKET_LOSS)
                .voip_result_jitter(DEFAULT_VOIP_RESULT_JITTER)
                .device(DEFAULT_DEVICE)
                .networkType(DEFAULT_NETWORK_TYPE)
                .tag(DEFAULT_TAG)
                .pingMedian(DEFAULT_PIN_MEDIAN)
                .signalStrength(DEFAULT_SIGNAL_STRENGTH)
                .build();
    }

    private DataForMeasurementRegistration getDataForMeasurementRegistration(MeasurementServer measurementServer) {
        return DataForMeasurementRegistration.builder()
                .measurementServer(measurementServer)
                .measurementServerType(DEFAULT_CLIENT)
                .telephonyPermissionGranted(true)
                .locationPermissionGranted(true)
                .uuidPermissionGranted(false)
                .appVersion(com.specure.sah.TestConstants.DEFAULT_APP_VERSION)
                .build();
    }
}
