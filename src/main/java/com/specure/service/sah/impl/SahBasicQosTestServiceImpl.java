package com.specure.service.sah.impl;

import com.specure.common.model.elastic.BasicQosTest;
import com.specure.common.model.jpa.Package;
import com.specure.common.model.jpa.ProbePort;
import com.specure.common.model.jpa.Site;
import com.specure.mapper.mobile.BasicTestMobileMapper;
import com.specure.multitenant.MultiTenantManager;
import com.specure.repository.sah.BasicQosTestRepository;
import com.specure.service.admin.ProviderService;
import com.specure.service.core.MeasurementServerService;
import com.specure.service.sah.BasicTestHistoryCacheService;
import com.specure.service.sah.BasicTestService;
import com.specure.service.sah.ProbePortService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service("sahBasicQosTestService")
public class SahBasicQosTestServiceImpl extends BasicQosTestServiceImpl {

    private final ProbePortService probePortService;
    private final MultiTenantManager multiTenantManager;
    private final ProviderService providerService;

    public SahBasicQosTestServiceImpl(BasicQosTestRepository basicQosTestRepository,
                                      @Qualifier("sahBasicTestService") BasicTestService measurementService,
                                      MeasurementServerService measurementServerService,
                                      ProbePortService probePortService,
                                      MultiTenantManager multiTenantManager,
                                      BasicTestMobileMapper basicTestMobileMapper,
                                      BasicTestHistoryCacheService basicTestHistoryCacheService,
                                      ProviderService providerService) {
        super(basicQosTestRepository, measurementService, measurementServerService, multiTenantManager, basicTestMobileMapper, basicTestHistoryCacheService);
        this.probePortService = probePortService;
        this.multiTenantManager = multiTenantManager;
        this.providerService = providerService;
    }

    protected void updateBasicQosTestWithProbe(String probePortName, String probeId, BasicQosTest basicQosTest) {
        log.debug("SahBasicQosTestServiceImpl:updateBasicQosTestWithProbe started with tenant = {}, probePortName = {}, probeId = {}, basicQosTest = {}",
                multiTenantManager.getCurrentTenant(), probePortName, probeId, basicQosTest);
        if (probePortName != null && probeId != null) {
            ProbePort probePort = probePortService.getProbePortByNameAndProbeId(probePortName, probeId);
            Package currentPackage = probePort.getAPackage();

            Site site = probePort.getProbe().getSite();
            if (!Objects.isNull(site)) {
                basicQosTest.setSiteAdvertisedId(site.getAdvertisedId());
                basicQosTest.setSiteId(site.getId());
            }

            if (currentPackage != null) {
                Long providerId = currentPackage.getProviderId();
                if (providerId != null) {
                    basicQosTest.setOperator(getProviderName(providerId));
                }
                basicQosTest.setPackageAdvertisedName(currentPackage.getAdvertisedName());
                basicQosTest.setPackageId(currentPackage.getId().toString());
                basicQosTest.setPackageType(currentPackage.getPackageType().getValue());
            }

            basicQosTest.setTypeOfProbePort(probePort.getType().toString());
            basicQosTest.setProbeId(probeId);
            basicQosTest.setProbePort(probePortName);
        }
        log.debug("SahBasicQosTestServiceImpl:updateBasicQosTestWithProbe finished with tenant = {}, basicQosTest = {}", multiTenantManager.getCurrentTenant(), basicQosTest);
    }

    private String getProviderName(Long providerId) {
        return providerService.getProviderById(providerId).getName();
    }
}
