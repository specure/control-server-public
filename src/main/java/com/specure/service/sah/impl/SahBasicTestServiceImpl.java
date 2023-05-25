package com.specure.service.sah.impl;

import com.specure.common.model.elastic.BasicTest;
import com.specure.common.model.jpa.Package;
import com.specure.common.model.jpa.ProbePort;
import com.specure.common.model.jpa.Site;
import com.specure.common.repository.GeoLocationRepository;
import com.specure.mapper.core.SpeedDetailMapper;
import com.specure.multitenant.MultiTenantManager;
import com.specure.repository.BasicTestRepository;
import com.specure.service.core.FieldAnonymizerFilter;
import com.specure.service.core.MeasurementServerService;
import com.specure.service.mobile.LoopModeSettingsService;
import com.specure.service.sah.NationalOperatorService;
import com.specure.service.sah.ProbePortService;
import com.specure.service.sah.RadioSignalService;
import com.specure.service.sah.SpeedDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service("sahBasicTestService")
public class SahBasicTestServiceImpl extends BasicTestServiceImpl {

    private ProbePortService probePortService;
    private MultiTenantManager multiTenantManager;

    public SahBasicTestServiceImpl(BasicTestRepository basicTestRepository,
                                   ProbePortService probePortService,
                                   MeasurementServerService sahMeasurementServerService,
                                   NationalOperatorService nationalOperatorService,
                                   MultiTenantManager multiTenantManager,
                                   GeoLocationRepository geoLocationRepository,
                                   LoopModeSettingsService loopModeSettingsService,
                                   FieldAnonymizerFilter fieldAnonymizerFilter,
                                   RadioSignalService radioSignalService,
                                   SpeedDetailMapper speedDetailMapper,
                                   SpeedDetailService speedDetailService) {
        super(basicTestRepository,
                sahMeasurementServerService,
                multiTenantManager,
                geoLocationRepository,
                loopModeSettingsService,
                fieldAnonymizerFilter,
                radioSignalService,
                nationalOperatorService,
                speedDetailMapper,
                speedDetailService);
        this.probePortService = probePortService;
        this.multiTenantManager = multiTenantManager;
    }

    protected void updateBasicTestWithProbe(String probePortName, String probeId, BasicTest basicTest) {
        log.debug("SahBasicTestServiceImpl:updateBasicTestWithProbe started with tenant = {}, probePortName = {}, probeId = {}, basicTest = {}",
                multiTenantManager.getCurrentTenant(), probePortName, probeId, basicTest);
        if (probePortName != null && probeId != null) { //TODO
            ProbePort probePort = probePortService.getProbePortByNameAndProbeId(probePortName, probeId);
            basicTest.setTypeOfProbePort(probePort.getType().toString());

            Package currentPackage = probePort.getAPackage();
            Site site = probePort.getProbe().getSite();
            if (!Objects.isNull(site)) {
                basicTest.setSiteId(site.getId());
                basicTest.setSiteName(site.getName());
                basicTest.setSiteAdvertisedId(site.getAdvertisedId());
            }
            if (!Objects.isNull(currentPackage)) {
                basicTest.setPackageId(currentPackage.getId().toString());
                basicTest.setPackageNameStamp(currentPackage.getAdvertisedName());
                basicTest.setPackageType(currentPackage.getPackageType().getValue());
                basicTest.setGroupId(currentPackage.getGroupId().toString());
            }
            basicTest.setProbeId(probeId);
            basicTest.setProbePort(probePortName);
        }
        log.debug("SahBasicTestServiceImpl:updateBasicTestWithProbe finished with tenant = {}, basicTest = {}", multiTenantManager.getCurrentTenant(), basicTest);
    }

}
