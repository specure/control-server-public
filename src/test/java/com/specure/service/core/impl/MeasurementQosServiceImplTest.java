package com.specure.service.core.impl;


import com.specure.common.model.jpa.AdHocCampaign;
import com.specure.common.model.jpa.Measurement;
import com.specure.common.model.jpa.MeasurementQos;
import com.specure.common.model.jpa.MeasurementServer;
import com.specure.common.repository.MeasurementServerRepository;
import com.specure.exception.QoSMeasurementServerNotFoundByUuidException;
import com.specure.exception.QosMeasurementFromOnNetServerException;
import com.specure.mapper.core.MeasurementQosMapper;
import com.specure.multitenant.MultiTenantManager;
import com.specure.repository.core.MeasurementQosRepository;
import com.specure.request.core.MeasurementQosParametersRequest;
import com.specure.request.core.MeasurementQosRequest;
import com.specure.request.core.measurement.qos.request.VoipTestResultRequest;
import com.specure.response.core.measurement.qos.response.MeasurementQosParametersResponse;
import com.specure.sah.TestConstants;
import com.specure.service.core.MeasurementQosService;
import com.specure.service.core.MeasurementService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static com.specure.core.TestConstants.*;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class MeasurementQosServiceImplTest {

    @MockBean
    private MeasurementQosMapper measurementQosMapper;
    @MockBean
    private MeasurementQosRepository measurementQosRepository;
    @MockBean
    private MeasurementServerRepository measurementServerRepository;
    @MockBean
    private MeasurementService measurementService;
    @MockBean
    private MultiTenantManager multiTenantManager;
    @Mock
    private MeasurementQosParametersRequest request;
    @Mock
    private MeasurementServer measurementServer;

    @Qualifier("basicMeasurementQosService")
    private MeasurementQosService basicMeasurementQosService;

    @Before
    public void setUp() {
        basicMeasurementQosService = new MeasurementQosServiceImpl(measurementServerRepository, measurementQosRepository, measurementQosMapper, measurementService, multiTenantManager);
    }

    @Test
    public void saveMeasurementQos_WhenCall_ExpectCorrectResponse() {
        MeasurementQosRequest measurementQosRequest = getDefaultMeasurementQosRequest();
        MeasurementQos measurementQos = getDefaultMeasurementQos();
        AdHocCampaign adHocCampaign = AdHocCampaign.builder().id(TestConstants.DEFAULT_AD_HOC_CAMPAIGN_UUID_FIRST).build();
        Measurement measurement = Measurement.builder().adHocCampaign(adHocCampaign).build();

        when(measurementQosMapper.measurementQosRequestToMeasurementQos(measurementQosRequest))
                .thenReturn(measurementQos);
        when(measurementQosRepository.save(measurementQos))
                .thenReturn(null);
        when(measurementService.getMeasurementByToken(DEFAULT_TOKEN))
                .thenReturn(Optional.of(measurement));

        basicMeasurementQosService.saveMeasurementQos(measurementQosRequest);

        verify(measurementQosRepository).save(measurementQos);

    }

    @Test(expected = QosMeasurementFromOnNetServerException.class)
    public void saveMeasurement_whenOnNetServer_expectException() {
        MeasurementQosRequest measurementQosRequest = getDefaultMeasurementQosRequest();
        MeasurementQos measurementQos = getDefaultMeasurementQos();
        Measurement measurement = Measurement.builder()
                .serverType("ON_NET")
                .openTestUuid(DEFAULT_OPEN_TEST_UUID)
                .measurementServerId(DEFAULT_MEASUREMENT_SERVER_ID)
                .build();

        when(measurementQosMapper.measurementQosRequestToMeasurementQos(measurementQosRequest))
                .thenReturn(measurementQos);
        when(measurementService.getMeasurementByToken(DEFAULT_TOKEN))
                .thenReturn(Optional.of(measurement));

        basicMeasurementQosService.saveMeasurementQos(measurementQosRequest);
    }

    @Test
    public void getQosParameters_correctRequest_expectNotNullParameters() {
        when(request.getUuid()).thenReturn(DEFAULT_UUID);
        when(measurementServerRepository.findByClientUUID(DEFAULT_UUID))
                .thenReturn(Optional.of(measurementServer));
        MeasurementQosParametersResponse response = basicMeasurementQosService.getQosParameters(request);
        assertNotNull(response);
    }

    @Test(expected = QoSMeasurementServerNotFoundByUuidException.class)
    public void getQosParameters_correctRequest_expectException() {
        when(measurementServerRepository.findByClientUUID(DEFAULT_UUID))
                .thenReturn(Optional.of(measurementServer));
        basicMeasurementQosService.getQosParameters(request);
    }

    private MeasurementQos getDefaultMeasurementQos() {
        return MeasurementQos.builder()
                .testToken(DEFAULT_TOKEN)
                .build();
    }

    private MeasurementQosRequest getDefaultMeasurementQosRequest() {
        VoipTestResultRequest voipTestResultRequest = VoipTestResultRequest.builder().build();
        return MeasurementQosRequest.builder()
                .testToken(DEFAULT_TOKEN)
                .qosResult(List.of(voipTestResultRequest))
                .build();
    }
}
