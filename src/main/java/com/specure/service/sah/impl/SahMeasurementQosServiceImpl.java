package com.specure.service.sah.impl;

import com.specure.common.enums.ServerNetworkType;
import com.specure.common.exception.MeasurementNotFoundByUuidException;
import com.specure.common.model.jpa.MeasurementQos;
import com.specure.common.repository.MeasurementServerRepository;
import com.specure.exception.QosMeasurementFromOnNetServerException;
import com.specure.mapper.core.MeasurementQosMapper;
import com.specure.multitenant.MultiTenantManager;
import com.specure.repository.core.MeasurementQosRepository;
import com.specure.request.core.MeasurementQosRequest;
import com.specure.service.BasicQosTestService;
import com.specure.service.core.MeasurementService;
import com.specure.service.core.impl.MeasurementQosServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service("sahMeasurementQosService")
public class SahMeasurementQosServiceImpl extends MeasurementQosServiceImpl {

    private MeasurementQosRepository measurementQosRepository;
    @Qualifier("sahBasicQosTestService")
    private BasicQosTestService basicQosTestService;
    private MeasurementQosMapper measurementQosMapper;
    private MeasurementService measurementService;
    private MultiTenantManager multiTenantManager;

    public SahMeasurementQosServiceImpl(MeasurementServerRepository measurementServerRepository, MeasurementQosRepository measurementQosRepository,
                                        MeasurementQosMapper measurementQosMapper, MeasurementService measurementService, SahBasicQosTestServiceImpl sahBasicQosTestService,
                                        MultiTenantManager multiTenantManager) {
        super(measurementServerRepository, measurementQosRepository, measurementQosMapper, measurementService, multiTenantManager);
        this.measurementQosRepository = measurementQosRepository;
        this.basicQosTestService = sahBasicQosTestService;
        this.measurementQosMapper = measurementQosMapper;
        this.measurementService = measurementService;
        this.multiTenantManager = multiTenantManager;
    }

    @Override
    public void saveMeasurementQos(MeasurementQosRequest measurementQosRequest) {
        log.info("SahMeasurementQosServiceImpl:saveMeasurementQos started with tenant = {}, measurementQosRequest = {}", multiTenantManager.getCurrentTenant(), measurementQosRequest);
        MeasurementQos measurementQos = measurementQosMapper.measurementQosRequestToMeasurementQos(measurementQosRequest);
        String token = measurementQos.getTestToken();
        var measurement = measurementService
                .getMeasurementByToken(token)
                .orElseThrow(() -> new MeasurementNotFoundByUuidException(token));
        if (!Objects.isNull(measurement.getAdHocCampaign())) {
            measurementQos.setAdHocCampaign(measurement.getAdHocCampaign());
        }
        if (ServerNetworkType.ON_NET.toString().equals(measurement.getServerType())) {
            throw new QosMeasurementFromOnNetServerException(measurement.getOpenTestUuid(), measurement.getMeasurementServerId());
        }
        MeasurementQos savedMeasurementQos = measurementQosRepository.save(measurementQos);
        basicQosTestService.saveMeasurementQosToElastic(measurementQos);
        log.debug("SahMeasurementQosServiceImpl:saveMeasurementQos finished with tenant = {}, savedMeasurementQos = {}", multiTenantManager.getCurrentTenant(), savedMeasurementQos);
    }

}
