package com.specure.service.sah.impl;


import com.specure.exception.QosMeasurementFromOnNetServerException;
import com.specure.mapper.core.MeasurementQosMapper;
import com.specure.common.model.jpa.Measurement;
import com.specure.common.model.jpa.MeasurementQos;
import com.specure.multitenant.MultiTenantManager;
import com.specure.repository.core.MeasurementQosRepository;
import com.specure.common.repository.MeasurementServerRepository;
import com.specure.request.core.MeasurementQosRequest;
import com.specure.request.core.measurement.qos.request.VoipTestResultRequest;
import com.specure.sah.TestConstants;
import com.specure.service.core.MeasurementQosService;
import com.specure.service.core.MeasurementService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class SahMeasurementQosServiceImplTest {

    @MockBean
    private MeasurementQosMapper measurementQosMapper;
    @MockBean
    private MeasurementQosRepository measurementQosRepository;
    @MockBean
    private SahBasicQosTestServiceImpl basicQosTestService;
    @MockBean
    private MeasurementServerRepository measurementServerRepository;
    @MockBean
    private MeasurementService measurementService;
    @MockBean
    private MultiTenantManager multiTenantManager;

    private MeasurementQosService measurementQosService;

    @Before
    public void setUp() {
        measurementQosService = new SahMeasurementQosServiceImpl(measurementServerRepository, measurementQosRepository, measurementQosMapper, measurementService, basicQosTestService, multiTenantManager);
    }

    @Test(expected = QosMeasurementFromOnNetServerException.class)
    public void saveMeasurement_whenOnNetServer_expectException() {
        MeasurementQosRequest measurementQosRequest = getDefaultMeasurementQosRequest();
        MeasurementQos measurementQos = getDefaultMeasurementQos();
        Measurement measurement = Measurement.builder()
                .serverType("ON_NET")
                .openTestUuid(TestConstants.DEFAULT_OPEN_TEST_UUID_WITH_PREFIX)
                .measurementServerId(TestConstants.DEFAULT_MEASUREMENT_SERVER_ID)
                .build();
        when(measurementQosMapper.measurementQosRequestToMeasurementQos(measurementQosRequest))
                .thenReturn(measurementQos);
        when(measurementService.getMeasurementByToken(TestConstants.DEFAULT_TOKEN))
                .thenReturn(Optional.of(measurement));
        measurementQosService.saveMeasurementQos(measurementQosRequest);
    }

    private MeasurementQos getDefaultMeasurementQos() {
        return MeasurementQos.builder()
                .testToken(TestConstants.DEFAULT_TOKEN)
//                .openTestUuid(DEFAULT_OPEN_TEST_UUID)
//                .voipObjectiveCallDuration(DEFAULT_VOIP_OBJECTIVE_CALL_DURATION)
//                .voipObjectiveDelay(DEFAULT_VOIP_OBJECTIVE_DELAY)
//                .voipResultInMeanJitter(DEFAULT_VOIP_RESULT_IN_MEAN_JITTER)
//                .voipResultOutMeanJitter(DEFAULT_VOIP_RESULT_OUT_MEAN_JITTER)
//                .voipResultOutNumPackets(DEFAULT_VOIP_RESULT_OUT_NUM_PACKETS)
                .build();
    }

    private MeasurementQosRequest getDefaultMeasurementQosRequest() {
        VoipTestResultRequest voipTestResultRequest = VoipTestResultRequest.builder().build();
        return MeasurementQosRequest.builder()
                .testToken(TestConstants.DEFAULT_TOKEN)
                .qosResult(List.of(voipTestResultRequest))
                .build();
    }
}
