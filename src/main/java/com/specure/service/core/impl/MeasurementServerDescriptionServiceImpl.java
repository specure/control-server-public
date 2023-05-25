package com.specure.service.core.impl;

import com.specure.common.model.jpa.MeasurementServerDescription;
import com.specure.multitenant.MultiTenantManager;
import com.specure.common.repository.MeasurementServerDescriptionRepository;
import com.specure.service.core.MeasurementServerDescriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
@Slf4j
public class MeasurementServerDescriptionServiceImpl implements MeasurementServerDescriptionService {
    final private MeasurementServerDescriptionRepository measurementServerDescriptionRepository;
    private final MultiTenantManager multiTenantManager;

    @Override
    public MeasurementServerDescription save(MeasurementServerDescription measurementServerDescription) {
        log.debug("MeasurementServerDescriptionServiceImpl:save started with tenant = {}, MeasurementServerDescription = {}", multiTenantManager.getCurrentTenant(), measurementServerDescription);
        MeasurementServerDescription savedMeasurementServerDescription = measurementServerDescriptionRepository.save(measurementServerDescription);
        log.debug("MeasurementServerDescriptionServiceImpl:save finished with tenant = {}, saved MeasurementServerDescription = {}", multiTenantManager.getCurrentTenant(), savedMeasurementServerDescription);
        return savedMeasurementServerDescription;
    }

    @Override
    public void deleteById(Long id) {
        log.debug("MeasurementServerDescriptionServiceImpl:deleteById started with tenant = {}, id = {}", multiTenantManager.getCurrentTenant(), id);
        measurementServerDescriptionRepository.deleteById(id);
        log.debug("MeasurementServerDescriptionServiceImpl:deleteById finished successfully tenant = {}", multiTenantManager.getCurrentTenant());
    }

    @Override
    public boolean existById(Long id) {
        log.debug("MeasurementServerDescriptionServiceImpl:existById started with tenant = {}, id {}", multiTenantManager.getCurrentTenant(), id);
        boolean isExistById = measurementServerDescriptionRepository.existsById(id);
        log.debug("MeasurementServerDescriptionServiceImpl:existById finished with tenant = {}, response = {}", multiTenantManager.getCurrentTenant(), isExistById);
        return isExistById;
    }
}
