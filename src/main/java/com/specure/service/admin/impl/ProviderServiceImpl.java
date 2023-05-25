package com.specure.service.admin.impl;

import com.specure.common.exception.ProviderNotFoundException;
import com.specure.common.model.jpa.Provider;
import com.specure.common.repository.ProviderRepository;
import com.specure.multitenant.MultiTenantManager;
import com.specure.service.admin.ProviderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProviderServiceImpl implements ProviderService {

    private final ProviderRepository providerRepository;
    private final MultiTenantManager multiTenantManager;

    @Override
    public Provider getProviderById(long id) {
        log.debug("ProviderServiceImpl:getProviderById started with tenant = {}, providerId = {}", multiTenantManager.getCurrentTenant(), id);
        Provider provider = providerRepository
                .findById(id)
                .orElseThrow(() -> new ProviderNotFoundException(id));
        log.debug("ProviderServiceImpl:getProviderById finished with tenant = {}, provider = {}", multiTenantManager.getCurrentTenant(), provider);
        return provider;
    }
}
