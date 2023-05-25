package com.specure.mapper.core.impl;

import com.specure.common.enums.NetworkType;
import com.specure.common.model.jpa.Measurement;
import com.specure.common.model.jpa.RawProvider;
import com.specure.mapper.core.GeoLocationMapper;
import com.specure.mapper.core.PingMapper;
import com.specure.mapper.core.SpeedDetailMapper;
import com.specure.request.core.MeasurementRequest;
import com.specure.sah.TestConstants;
import com.specure.sah.TestObjects;
import com.specure.service.core.FieldAnonymizerFilter;
import com.specure.service.sah.SpeedDetailService;
import com.specure.utils.core.MeasurementCalculatorUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static com.specure.core.TestConstants.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class MeasurementMapperImplTest {

    @MockBean
    private PingMapper pingMapper;
    @MockBean
    private SpeedDetailMapper speedDetailMapper;
    @MockBean
    private GeoLocationMapper geoLocationMapper;
    @MockBean
    private FieldAnonymizerFilter fieldAnonymizerFilter;
    @MockBean
    private SpeedDetailService speedDetailService;

    private MeasurementMapperImpl measurementMapper;
    @Mock
    private Measurement measurement;

    @Before
    public void setUp() {
        measurementMapper = new MeasurementMapperImpl(pingMapper, speedDetailMapper, geoLocationMapper, fieldAnonymizerFilter, speedDetailService);
    }

    @Test
    public void measurementRequestToMeasurement() {
        Map<String, Integer> signalReport = new HashMap<>();
        signalReport.put("lte_rsrp", DEFAULT_LTE_RSRP);
        signalReport.put("lte_rsrq", DEFAULT_LTE_RSRQ);
        List<Map<String, Integer>> signals = List.of(signalReport);

        Map<String, String> jpl = new HashMap<>();
        jpl.put("voip_result_in_mean_jitter", DEFAULT_VOIP_RESULT_IN_MEAN_JITTER.toString());
        jpl.put("voip_result_out_mean_jitter", DEFAULT_VOIP_RESULT_OUT_MEAN_JITTER.toString());
        jpl.put("voip_objective_delay", DEFAULT_VOIP_OBJECTIVE_DELAY.toString());
        jpl.put("voip_objective_call_duration", DEFAULT_VOIP_OBJECTIVE_CALL_DURATION.toString());
        jpl.put("voip_result_in_num_packets", DEFAULT_VOIP_RESULT_IN_NUM_PACKETS.toString());
        jpl.put("voip_result_out_num_packets", DEFAULT_VOIP_RESULT_OUT_NUM_PACKETS.toString());


        Long timeStamp = System.currentTimeMillis();
        MeasurementRequest measurementRequest = MeasurementRequest.builder().testToken(DEFAULT_TOKEN).device(DEFAULT_DEVICE).tag(DEFAULT_TAG).signals(signals).jpl(jpl).time(timeStamp).testPingShortest(DEFAULT_PING.longValue()).testBytesDownload(DEFAULT_BYTES_DOWNLOAD).testNsecDownload(DEFAULT_N_SEC_DOWNLOAD).testBytesUpload(DEFAULT_BYTES_UPLOAD).testNsecUpload(DEFAULT_N_SEC_UPLOAD).networkType(DEFAULT_NETWORK_TYPE.toString()).build();

        double speedDownload = MeasurementCalculatorUtil.getSpeedBitPerSec(DEFAULT_BYTES_DOWNLOAD, DEFAULT_N_SEC_DOWNLOAD) / 1e3;
        double speedUpload = MeasurementCalculatorUtil.getSpeedBitPerSec(DEFAULT_BYTES_UPLOAD, DEFAULT_N_SEC_UPLOAD) / 1e3;
        String resultJitter = MeasurementCalculatorUtil.calculateMeanJitterInMms(DEFAULT_VOIP_RESULT_IN_MEAN_JITTER, DEFAULT_VOIP_RESULT_OUT_MEAN_JITTER);
        String packetLoss = MeasurementCalculatorUtil.calculateMeanPacketLossInPercent(DEFAULT_VOIP_OBJECTIVE_DELAY, DEFAULT_VOIP_OBJECTIVE_CALL_DURATION, DEFAULT_VOIP_RESULT_IN_NUM_PACKETS, DEFAULT_VOIP_RESULT_OUT_NUM_PACKETS);

        Measurement mapped = measurementMapper.measurementRequestToMeasurement(measurementRequest);
        assertEquals(DEFAULT_TOKEN.split("_")[0], mapped.getOpenTestUuid());
        assertEquals(DEFAULT_TOKEN, mapped.getToken());
        assertEquals(Math.round(speedDownload), mapped.getSpeedDownload().longValue());
        assertEquals(Math.round(speedUpload), mapped.getSpeedUpload().longValue());
        assertEquals(DEFAULT_PING.longValue(), mapped.getPingMedian().longValue());
        assertEquals(DEFAULT_DEVICE, mapped.getDevice());
        assertEquals(DEFAULT_TAG, mapped.getTag());
        assertEquals(DEFAULT_NETWORK_TYPE, mapped.getNetworkType());
        assertEquals(resultJitter, mapped.getVoip_result_jitter());
        assertEquals(packetLoss, mapped.getVoip_result_packet_loss());
        assertEquals(DEFAULT_LTE_RSRP, mapped.getSignalStrength());
        verify(speedDetailService).saveSpeedDetailsToCache(eq(DEFAULT_TOKEN.split("_")[0]), any());
    }

    @Test
    public void getSignalStrength_WhenGet101Network_returnSignalStrength() {
        NetworkType networkType = NetworkType.fromValue(101);
        Map<String, Integer> signals = new HashMap<>() {{
            put("signal_strength", -105);
        }};
        Optional<Integer> signalStrength = MeasurementMapperImpl.getSignalStrength(Collections.singletonList(signals), networkType);
        assertEquals(Optional.of(-105), signalStrength);
    }

    @Test
    public void getSignalStrength_WhenGet101NetworkAndBadKey_returnEmpty() {
        NetworkType networkType = NetworkType.fromValue(101);
        Map<String, Integer> signals = new HashMap<>() {{
            put("smth_else", -105);
        }};
        Optional<Integer> signalStrength = MeasurementMapperImpl.getSignalStrength(Collections.singletonList(signals), networkType);
        assertEquals(Optional.empty(), signalStrength);
    }

    @Test
    public void getSignalStrength_WhenGet97Network_returnEmpty() {
        NetworkType networkType = NetworkType.fromValue(97);
        Map<String, Integer> signals = Collections.emptyMap();
        Optional<Integer> signalStrength = MeasurementMapperImpl.getSignalStrength(Collections.singletonList(signals), networkType);
        assertEquals(Optional.empty(), signalStrength);
    }

    @Test
    public void getSignalStrength_WhenGet98Network_returnEmpty() {
        NetworkType networkType = NetworkType.fromValue(98);
        Map<String, Integer> signals = Collections.emptyMap();
        Optional<Integer> signalStrength = MeasurementMapperImpl.getSignalStrength(Collections.singletonList(signals), networkType);
        assertEquals(Optional.empty(), signalStrength);
    }

    @Test
    public void getSignalStrength_WhenGet13Network_let_rsrp() {
        NetworkType networkType = NetworkType.fromValue(13);
        Map<String, Integer> signals = new HashMap<>() {{
            put("smth_else", -105);
            put("lte_rsrp", -104);
        }};
        Optional<Integer> signalStrength = MeasurementMapperImpl.getSignalStrength(Collections.singletonList(signals), networkType);
        assertEquals(Optional.of(-104), signalStrength);
    }

    @Test
    public void updateMeasurementProviderInfo_correctInvocation_UpdatedMeasurement() {
        RawProvider provider = TestObjects.defaultRawProvider();
        Measurement measurement = new Measurement();

        var result = measurementMapper.updateMeasurementProviderInfo(measurement, provider);

        assertEquals(TestConstants.DEFAULT_PROVIDER_ASN, result.getAsn());
        assertEquals(TestConstants.DEFAULT_PROVIDER_ISP_NAME, result.getIspName());
        assertEquals(TestConstants.DEFAULT_PROVIDER_NAME, result.getIspRawId());
    }

    @Test
    public void updateMeasurementProviderInfo_measurementIsNull_Null() {
        RawProvider provider = TestObjects.defaultRawProvider();
        Measurement measurement = null;

        var result = measurementMapper.updateMeasurementProviderInfo(measurement, provider);

        assertNull(result);
    }


    @Test
    public void updateMeasurementProviderInfo_providerIsNull_NotChangedMeasurement() {
        RawProvider provider = null;

        var result = measurementMapper.updateMeasurementProviderInfo(measurement, provider);

        verifyNoInteractions(measurement);
    }
}
