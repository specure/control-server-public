package com.specure.mapper.mobile.impl;

import com.specure.common.model.jpa.*;
import com.specure.mapper.core.GeoLocationMapper;
import com.specure.mapper.core.PingMapper;
import com.specure.mapper.core.SpeedDetailMapper;
import com.specure.mapper.mobile.MobileMeasurementMapper;
import com.specure.model.jpa.SimOperator;
import com.specure.repository.mobile.SimOperatorRepository;
import com.specure.request.core.measurement.request.*;
import com.specure.request.mobile.MeasurementResultMobileRequest;
import com.specure.sah.TestConstants;
import com.specure.service.admin.MobileModelService;
import com.specure.service.core.FieldAnonymizerFilter;
import com.specure.service.sah.RadioSignalService;
import com.specure.service.sah.SpeedDetailService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class MobileMeasurementMapperImplTest {

    private MobileMeasurementMapper measurementMapper;

    @MockBean
    private PingMapper pingMapper;
    @MockBean
    private SpeedDetailMapper speedDetailMapper;
    @MockBean
    private GeoLocationMapper geoLocationMapper;
    @MockBean
    private SimOperatorRepository simOperatorRepository;
    @MockBean
    private SpeedDetailService speedDetailService;
    @Mock
    private PingRequest pingRequest;
    @Mock
    private SpeedDetailRequest speedDetailRequest;
    @Mock
    private Ping ping;
    @Mock
    private SpeedDetail speedDetail;
    @Mock
    private GeoLocationRequest geoLocationRequest;
    @Mock
    private GeoLocation geoLocation;
    @Mock
    private RadioSignalService radioSignalService;
    @Mock
    private MobileModelService mobileModelService;
    @Mock
    private FieldAnonymizerFilter fieldAnonymizerFilter;

    private final Measurement measurement = Measurement.builder()
            .measurementDetails(new MeasurementDetails())
            .build();
    @Mock
    private SimOperator simOperator;
    @Mock
    private MobileModel mobileModel;


    @Before
    public void setUp() {
        measurementMapper = new MobileMeasurementMapperImpl(pingMapper, speedDetailMapper,
                geoLocationMapper, radioSignalService,
                simOperatorRepository, mobileModelService,
                fieldAnonymizerFilter, speedDetailService);
    }

    @Test
    public void measurementMobileResultRequestToMeasurement_whenCommonData_expectMeasurement() {
        var measurementResultMobileRequest = getMeasurementResultMobileRequest();
        when(pingRequest.getValue()).thenReturn(TestConstants.DEFAULT_PING_SHORTEST);
        when(pingMapper.pingRequestToPing(pingRequest)).thenReturn(ping);
        when(speedDetailMapper.speedDetailRequestToSpeedDetail(speedDetailRequest)).thenReturn(speedDetail);
        when(geoLocationMapper.geoLocationRequestToGeoLocation(geoLocationRequest)).thenReturn(geoLocation);
        when(mobileModelService.getAndSaveNewIfNotExistMobileModelByModel(TestConstants.DEFAULT_MODEL)).thenReturn(Optional.of(mobileModel));
        when(mobileModel.getMarketingName()).thenReturn(TestConstants.MobileModel.MARKETING_NAME);
        when(mobileModel.getCategory()).thenReturn(TestConstants.MobileModel.CATEGORY);
        when(mobileModel.getManufacturer()).thenReturn(TestConstants.MobileModel.MANUFACTURER);
        when(fieldAnonymizerFilter.saveWifiSsidFilter(TestConstants.DEFAULT_WIFI_SSID, TestConstants.DEFAULT_OPEN_TEST_UUID_STRING)).thenReturn(TestConstants.DEFAULT_WIFI_SSID);

        var updatedMeasurement = measurementMapper.measurementMobileResultRequestToMeasurement(measurementResultMobileRequest, measurement);

        assertEquals(TestConstants.DEFAULT_PING_SHORTEST, updatedMeasurement.getPingMedian());
        assertEquals(TestConstants.DEFAULT_MOBILE_MEASUREMENT_TOKEN, updatedMeasurement.getToken());
        assertEquals(TestConstants.DEFAULT_OPEN_TEST_UUID_STRING, updatedMeasurement.getOpenTestUuid());
        assertEquals(new Timestamp(TestConstants.DEFAULT_TIME), updatedMeasurement.getTime());
        assertEquals(TestConstants.DEFAULT_DOWNLOAD_SPEED, updatedMeasurement.getSpeedDownload());
        assertEquals(TestConstants.DEFAULT_UPLOAD_SPEED, updatedMeasurement.getSpeedUpload());
        assertEquals(TestConstants.MobileModel.MARKETING_NAME, updatedMeasurement.getDevice());
        assertEquals(TestConstants.MobileModel.CATEGORY, updatedMeasurement.getMeasurementDetails().getMobileModelCategory());
        assertEquals(TestConstants.MobileModel.MANUFACTURER, updatedMeasurement.getMeasurementDetails().getMobileModelManufacturer());
        assertEquals(TestConstants.DEFAULT_TAG, updatedMeasurement.getTag());
        assertEquals(TestConstants.DEFAULT_NETWORK_TYPE, updatedMeasurement.getNetworkType());
        assertEquals(TestConstants.DEFAULT_LANGUAGE, updatedMeasurement.getClientLanguage());
        assertEquals(TestConstants.DEFAULT_CLIENT.name(), updatedMeasurement.getClientName());
        assertEquals(TestConstants.DEFAULT_CLIENT_VERSION, updatedMeasurement.getClientVersion());
        assertEquals(TestConstants.DEFAULT_MODEL, updatedMeasurement.getModel());
        assertEquals(TestConstants.DEFAULT_PLATFORM_MOBILE, updatedMeasurement.getPlatform());
        assertEquals(TestConstants.DEFAULT_PRODUCT, updatedMeasurement.getProduct());
        assertEquals(TestConstants.DEFAULT_TEST_BYTES_DOWNLOAD, updatedMeasurement.getTestBytesDownload());
        assertEquals(TestConstants.DEFAULT_TEST_BYTES_UPLOAD, updatedMeasurement.getTestBytesUpload());
        assertEquals(TestConstants.DEFAULT_DOWNLOAD_DURATION_NANOS, updatedMeasurement.getTestNsecDownload());
        assertEquals(TestConstants.DEFAULT_UPLOAD_DURATION_NANOS, updatedMeasurement.getTestNsecUpload());
        assertEquals(TestConstants.DEFAULT_NUM_TEST_THREADS, updatedMeasurement.getTestNumThreads());
        assertEquals(TestConstants.DEFAULT_PING_SHORTEST, updatedMeasurement.getTestPingShortest());
        assertEquals(TestConstants.DEFAULT_SPEED_DOWNLOAD, updatedMeasurement.getTestSpeedDownload());
        assertEquals(TestConstants.DEFAULT_SPEED_UPLOAD, updatedMeasurement.getTestSpeedUpload());
        assertEquals(TestConstants.DEFAULT_WIFI_SSID, updatedMeasurement.getWifiSsid());
        assertEquals(TestConstants.DEFAULT_WIFI_BSSID, updatedMeasurement.getWifiBssid());
        assertEquals(TestConstants.DEFAULT_SIM_COUNT, updatedMeasurement.getSimCount());
        assertEquals(TestConstants.DEFAULT_NETWORK_OPERATOR_NAME, updatedMeasurement.getNetworkOperatorName());
        assertEquals(List.of(ping), updatedMeasurement.getPings());
        assertEquals(List.of(speedDetail), updatedMeasurement.getSpeedDetail());
        assertEquals(List.of(geoLocation), updatedMeasurement.getGeoLocations());
        assertEquals(TestConstants.DEFAULT_STATUS, updatedMeasurement.getStatus());
        assertEquals(String.valueOf(TestConstants.DEFAULT_JITTER), updatedMeasurement.getVoip_result_jitter());
        assertEquals(String.valueOf(TestConstants.DEFAULT_PACKET_LOSS), updatedMeasurement.getVoip_result_packet_loss());
        assertEquals(TestConstants.DEFAULT_FLAG_TRUE, updatedMeasurement.getDualSim());
        assertEquals(TestConstants.DEFAULT_SIM_MCC_MNC, updatedMeasurement.getMeasurementDetails().getSimMccMnc());
        assertEquals(TestConstants.DEFAULT_SIM_OPERATOR_NAME, updatedMeasurement.getMeasurementDetails().getSimOperatorName());
        assertEquals(TestConstants.DEFAULT_FLAG_TRUE, updatedMeasurement.getMeasurementDetails().getNetworkIsRoaming());
        assertEquals(TestConstants.DEFAULT_NETWORK_OPERATOR_NAME, updatedMeasurement.getNetworkOperatorName());
        assertEquals(TestConstants.DEFAULT_NETWORK_COUNTRY, updatedMeasurement.getMeasurementDetails().getNetworkCountry());
        assertEquals(TestConstants.DEFAULT_SIM_COUNTRY, updatedMeasurement.getMeasurementDetails().getSimCountry());
        verify(speedDetailService).saveSpeedDetailsToCache(eq(TestConstants.DEFAULT_MOBILE_MEASUREMENT_TOKEN.split("_")[0]), any());
    }

    @Test
    public void measurementMobileResultRequestToMeasurement_whenTelephonyOperatorNameIsNull_expectMeasurement() {
        MeasurementResultMobileRequest measurementResultMobileRequest = MeasurementResultMobileRequest.builder()
                .pingShortest(TestConstants.DEFAULT_PING_SHORTEST)
                .testToken(TestConstants.DEFAULT_MOBILE_MEASUREMENT_TOKEN)
                .networkMccMnc(TestConstants.DEFAULT_TELEPHONY_NETWORK_OPERATOR)
                .testBytesDownload(TestConstants.DEFAULT_TEST_BYTES_DOWNLOAD)
                .testBytesUpload(TestConstants.DEFAULT_TEST_BYTES_UPLOAD)
                .downloadDurationNanos(TestConstants.DEFAULT_DOWNLOAD_DURATION_NANOS)
                .uploadDurationNanos(TestConstants.DEFAULT_UPLOAD_DURATION_NANOS)
                .time(TestConstants.DEFAULT_TIME)
                .clientName(TestConstants.DEFAULT_CLIENT)
                .build();
        when(simOperatorRepository.findByMccMncOrderByValidFromDesc(TestConstants.DEFAULT_TELEPHONY_NETWORK_OPERATOR))
                .thenReturn(List.of(simOperator));
        when(simOperator.getName()).thenReturn(TestConstants.DEFAULT_TELEPHONY_NETWORK_OPERATOR_NAME);

        var updatedMeasurement = measurementMapper.measurementMobileResultRequestToMeasurement(measurementResultMobileRequest, measurement);

        assertEquals(TestConstants.DEFAULT_TELEPHONY_NETWORK_OPERATOR_NAME, updatedMeasurement.getNetworkOperatorName());
        verify(speedDetailService).saveSpeedDetailsToCache(eq(TestConstants.DEFAULT_MOBILE_MEASUREMENT_TOKEN.split("_")[0]), any());
    }

    private MeasurementResultMobileRequest getMeasurementResultMobileRequest() {
        var signals = List.of(
                RadioSignalRequest.builder()
                        .signalStrength(TestConstants.DEFAULT_SIGNAL_STRENGTH_FIRST)
                        .lteRSRP(TestConstants.DEFAULT_LTE_RSRP_FIRST)
                        .lteRSRQ(TestConstants.DEFAULT_LTE_RSRQ_FIRST)
                        .build(),
                RadioSignalRequest.builder()
                        .signalStrength(TestConstants.DEFAULT_SIGNAL_STRENGTH_SECOND)
                        .lteRSRP(TestConstants.DEFAULT_LTE_RSRP_SECOND)
                        .lteRSRQ(TestConstants.DEFAULT_LTE_RSRQ_SECOND)
                        .build()
        );
        var radioInfo = RadioInfoRequest.builder()
                .signals(signals)
                .build();
        return MeasurementResultMobileRequest.builder()
                .pingShortest(TestConstants.DEFAULT_PING_SHORTEST)
                .testToken(TestConstants.DEFAULT_MOBILE_MEASUREMENT_TOKEN)
                .time(TestConstants.DEFAULT_TIME)
                .downloadDurationNanos(TestConstants.DEFAULT_DOWNLOAD_DURATION_NANOS)
                .uploadDurationNanos(TestConstants.DEFAULT_UPLOAD_DURATION_NANOS)
                .testBytesDownload(TestConstants.DEFAULT_TEST_BYTES_DOWNLOAD)
                .testBytesUpload(TestConstants.DEFAULT_TEST_BYTES_UPLOAD)
                .device(TestConstants.DEFAULT_DEVICE)
                .tag(TestConstants.DEFAULT_TAG)
                .networkType(TestConstants.DEFAULT_NETWORK_TYPE)
                .clientLanguage(TestConstants.DEFAULT_LANGUAGE)
                .clientName(TestConstants.DEFAULT_CLIENT)
                .clientVersion(TestConstants.DEFAULT_CLIENT_VERSION)
                .testNumThreads(TestConstants.DEFAULT_NUM_TEST_THREADS)
                .downloadSpeed(TestConstants.DEFAULT_DOWNLOAD_SPEED)
                .uploadSpeed(TestConstants.DEFAULT_UPLOAD_SPEED)
                .wifiSSID(TestConstants.DEFAULT_WIFI_SSID)
                .wifiBSSID(TestConstants.DEFAULT_WIFI_BSSID)
                .telephonySimCount(TestConstants.DEFAULT_SIM_COUNT)
                .dualSim(TestConstants.DEFAULT_FLAG_TRUE)
                .simMccMnc(TestConstants.DEFAULT_SIM_MCC_MNC)
                .simOperatorName(TestConstants.DEFAULT_SIM_OPERATOR_NAME)
                .simCountry(TestConstants.DEFAULT_SIM_COUNTRY)
                .networkIsRoaming(true)
                .networkMccMnc(TestConstants.DEFAULT_NETWORK_MCC_MNC)
                .networkOperatorName(TestConstants.DEFAULT_NETWORK_OPERATOR_NAME)
                .networkCountry(TestConstants.DEFAULT_NETWORK_COUNTRY)
                .testStatus(TestConstants.DEFAULT_STATUS.name())
                .platform(TestConstants.DEFAULT_PLATFORM_MOBILE.name())
                .pings(List.of(pingRequest))
                .speedDetails(List.of(speedDetailRequest))
                .geoLocations(List.of(geoLocationRequest))
                .model(TestConstants.DEFAULT_MODEL)
                .product(TestConstants.DEFAULT_PRODUCT)
                .jitter(String.valueOf(TestConstants.DEFAULT_JITTER))
                .packetLoss(String.valueOf(TestConstants.DEFAULT_PACKET_LOSS))
                .radioInfo(radioInfo)
                .build();
    }
}
