package com.specure.service.sah.impl;

import com.specure.common.exception.ProbePortNotFoundByNameAndProbeIdException;
import com.specure.common.model.jpa.ProbePort;
import com.specure.common.repository.ProbePortRepository;
import com.specure.multitenant.MultiTenantManager;
import com.specure.service.sah.ProbePortService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProbePortServiceImpl implements ProbePortService {

    private final ProbePortRepository probePortRepository;
    private final MultiTenantManager multiTenantManager;

    @Override
    public ProbePort getProbePortByNameAndProbeId(String name, String probeId) {
        log.debug("ProbePortServiceImpl:getProbePortByNameAndProbeId started with tenant = {}, name = {}, probeId = {}", multiTenantManager.getCurrentTenant(), name, probeId);
        ProbePort probePort = probePortRepository
                .findByNameAndProbeId(name, probeId)
                .orElseThrow(() -> new ProbePortNotFoundByNameAndProbeIdException(name, probeId));
        log.debug("ProbePortServiceImpl:getProbePortByNameAndProbeId finished with tenant = {}, probePort = {}", multiTenantManager.getCurrentTenant(), probePort);
        return probePort;
    }
}
