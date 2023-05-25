package com.specure.service.core.impl;

import com.specure.common.enums.MeasurementServerType;
import com.specure.common.exception.MeasurementServerNotFoundException;
import com.specure.common.model.jpa.MeasurementServer;
import com.specure.common.repository.MeasurementServerRepository;
import com.specure.mapper.core.MeasurementServerMapper;
import com.specure.model.dto.internal.DataForMeasurementRegistration;
import com.specure.multitenant.MultiTenantManager;
import com.specure.request.core.ClientLocationRequest;
import com.specure.request.core.MeasurementRegistrationForAdminRequest;
import com.specure.request.core.MeasurementRegistrationForProbeRequest;
import com.specure.request.core.MeasurementRegistrationForWebClientRequest;
import com.specure.response.core.MeasurementServerForWebResponse;
import com.specure.response.core.MeasurementServerResponseForSettings;
import com.specure.response.core.NearestMeasurementServersResponse;
import com.specure.service.core.MeasurementServerService;
import com.specure.service.core.MeasurementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service("basicMeasurementServerService")
public class MeasurementServerServiceImpl implements MeasurementServerService {

    protected final MeasurementServerRepository measurementServerRepository;
    private final MeasurementServerMapper measurementServerMapper;
    private final MultiTenantManager multiTenantManager;

    public MeasurementServerServiceImpl(MeasurementServerRepository measurementServerRepository,
                                        MeasurementServerMapper measurementServerMapper,
                                        MeasurementService measurementService,
                                        MultiTenantManager multiTenantManager) {
        this.measurementServerRepository = measurementServerRepository;
        this.measurementServerMapper = measurementServerMapper;
        this.multiTenantManager = multiTenantManager;
    }

    @Override
    public DataForMeasurementRegistration getDataFromProbeMeasurementRegistrationRequest(MeasurementRegistrationForProbeRequest measurementRegistrationForProbeRequest) {
        log.debug("MeasurementServerServiceImpl:getDataFromProbeMeasurementRegistrationRequest started with tenant = {}, request = {}", multiTenantManager.getCurrentTenant(), measurementRegistrationForProbeRequest);
        log.debug("MeasurementServerServiceImpl:getDataFromProbeMeasurementRegistrationRequest tenant = {}, return ALWAYS null", multiTenantManager.getCurrentTenant());
        return null;
    }

    @Override
    public DataForMeasurementRegistration getMeasurementServerForWebClient(MeasurementRegistrationForWebClientRequest measurementRegistrationForWebClientRequest) {
        log.debug("MeasurementServerServiceImpl:getMeasurementServerForWebClient started with tenant = {}, measurementRegistrationForWebClientRequest = {}", multiTenantManager.getCurrentTenant(), measurementRegistrationForWebClientRequest);
        // it's only mock
        // get first measurement server
        // TODO find nearest server

        MeasurementServer measurementServer = measurementServerRepository
                .findAll()
                .stream()
                .findFirst()
                .orElseThrow(() -> new MeasurementServerNotFoundException(0L));

        DataForMeasurementRegistration dataForMeasurementRegistration = DataForMeasurementRegistration.builder()
                .measurementServer(measurementServer)
                .clientUuid(measurementRegistrationForWebClientRequest.getUuid())
                .measurementServerType(measurementRegistrationForWebClientRequest.getClient())
                .build();
        log.debug("MeasurementServerServiceImpl:getMeasurementServerForWebClient finished with tenant = {}, response = {}", multiTenantManager.getCurrentTenant(), dataForMeasurementRegistration);
        return dataForMeasurementRegistration;
    }

    @Override
    public DataForMeasurementRegistration getMeasurementServerById(MeasurementRegistrationForAdminRequest measurementRegistrationForAdminRequest) {
        log.debug("MeasurementServerServiceImpl:getMeasurementServerById started with tenant = {}, MeasurementRegistrationForAdminRequest = {}", multiTenantManager.getCurrentTenant(), measurementRegistrationForAdminRequest);
        Long id = measurementRegistrationForAdminRequest.getMeasurementServerId();

        MeasurementServer serverChosenByAdmin = measurementServerRepository.findById(id)
                .orElseThrow(() -> new MeasurementServerNotFoundException(id));

        DataForMeasurementRegistration dataForMeasurementRegistration = DataForMeasurementRegistration.builder()
                .measurementServer(serverChosenByAdmin)
                .measurementServerType(measurementRegistrationForAdminRequest.getClient())
                .clientUuid(measurementRegistrationForAdminRequest.getUuid())
                .uuidPermissionGranted(measurementRegistrationForAdminRequest.getUuidPermissionGranted())
                .telephonyPermissionGranted(measurementRegistrationForAdminRequest.getTelephonyPermissionGranted())
                .locationPermissionGranted(measurementRegistrationForAdminRequest.getLocationPermissionGranted())
                .appVersion(measurementRegistrationForAdminRequest.getAppVersion())
                .platform(measurementRegistrationForAdminRequest.getPlatform())
                .build();
        log.debug("MeasurementServerServiceImpl:getMeasurementServerById finished with tenant = {}, response = {}", multiTenantManager.getCurrentTenant(), dataForMeasurementRegistration);
        return dataForMeasurementRegistration;
    }

    @Override
    public NearestMeasurementServersResponse getNearestServers(ClientLocationRequest clientLocationRequest) {
        log.debug("MeasurementServerServiceImpl:getNearestServers started with tenant = {}, request = {}", multiTenantManager.getCurrentTenant(), clientLocationRequest);
        List<MeasurementServer> all = measurementServerRepository.findAll();

        List<MeasurementServerForWebResponse> serversForWeb = all
                .stream()
                .map(measurementServer -> measurementServerMapper.measurementServersToMeasurementServerForWebResponse(measurementServer, clientLocationRequest.getClient()))
                .collect(Collectors.toList());

        NearestMeasurementServersResponse nearestMeasurementServersResponse = NearestMeasurementServersResponse.builder()
                .error(Collections.emptyList())
                .servers(serversForWeb)
                .build();
        log.debug("MeasurementServerServiceImpl:getNearestServers finished with tenant  = {}, response = {}", multiTenantManager.getCurrentTenant(), nearestMeasurementServersResponse);
        return nearestMeasurementServersResponse;
    }

    @Override
    public MeasurementServer getMeasurementServerById(long id) {
        log.debug("MeasurementServerServiceImpl:getMeasurementServerById started with tenant = {}, id = {}", multiTenantManager.getCurrentTenant(), id);
        MeasurementServer measurementServer = measurementServerRepository
                .findById(id)
                .orElseThrow(() -> new MeasurementServerNotFoundException(id));
        log.debug("MeasurementServerServiceImpl:getMeasurementServerById finished with tenant = {}, response = {}", multiTenantManager.getCurrentTenant(), measurementServer);
        return measurementServer;
    }

    @Override
    public void deleteByServerById(long id) {
        log.debug("MeasurementServerServiceImpl:deleteByServerById started with tenant = {}, id = {}", multiTenantManager.getCurrentTenant(), id);
        MeasurementServer measurementServer = measurementServerRepository
                .findById(id)
                .orElseThrow(() -> new MeasurementServerNotFoundException(id));
        measurementServerRepository.delete(measurementServer);
        log.debug("MeasurementServerServiceImpl:deleteByServerById tenant ={}, finished successfully", multiTenantManager.getCurrentTenant());
    }

    @Override
    public MeasurementServer getMeasurementServerByIdOrGetDefault(Long preferredServer) {
        if (Objects.nonNull(preferredServer)) {
            return getMeasurementServerById(preferredServer);
        } else {
            return measurementServerRepository.findAll().stream()
                    .findFirst()
                    .orElseThrow(() -> new MeasurementServerNotFoundException(0L));
        }
    }

    @Override
    public List<MeasurementServerResponseForSettings> getServers(List<MeasurementServerType> serverTypes) {
        log.debug("MeasurementServerServiceImpl:getServers started with tenant = {}, serverTypes = {}", multiTenantManager.getCurrentTenant(), serverTypes);
        List<MeasurementServerResponseForSettings> measurementServerResponseForSettings = measurementServerRepository.getByServerTypeDetails_ServerTypeIn(serverTypes).stream()
                .map(measurementServerMapper::measurementServersToMeasurementServerResponseForSettings)
                .collect(Collectors.toList());
        log.debug("MeasurementServerServiceImpl:getServers finished with tenant = {}, response = {}", multiTenantManager.getCurrentTenant(), measurementServerResponseForSettings);
        return measurementServerResponseForSettings;
    }
}
