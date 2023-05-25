package com.specure.service.sah.impl;

import com.specure.common.model.elastic.BasicTest;
import com.specure.common.model.jpa.Package;
import com.specure.common.model.jpa.Probe;
import com.specure.common.model.jpa.ProbePort;
import com.specure.common.model.jpa.Site;
import com.specure.common.repository.GeoLocationRepository;
import com.specure.mapper.core.SpeedDetailMapper;
import com.specure.multitenant.MultiTenantManager;
import com.specure.repository.BasicTestRepository;
import com.specure.sah.TestConstants;
import com.specure.service.core.FieldAnonymizerFilter;
import com.specure.service.core.MeasurementServerService;
import com.specure.service.mobile.LoopModeSettingsService;
import com.specure.service.sah.NationalOperatorService;
import com.specure.service.sah.ProbePortService;
import com.specure.service.sah.RadioSignalService;
import com.specure.service.sah.SpeedDetailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class SahBasicTestServiceImplTest {


    @MockBean
    private BasicTestRepository basicTestRepository;
    @MockBean
    private ProbePortService probePortService;
    @MockBean
    private MeasurementServerService measurementServerService;
    @MockBean
    private MultiTenantManager multiTenantManager;
    @MockBean
    private GeoLocationRepository geoLocationRepository;
    @MockBean
    private LoopModeSettingsService loopModeSettingsService;
    @MockBean
    private FieldAnonymizerFilter fieldAnonymizerFilter;
    @MockBean
    private RadioSignalService radioSignalService;
    @MockBean
    private NationalOperatorService nationalOperatorService;
    @MockBean
    private SpeedDetailMapper speedDetailMapper;
    @MockBean
    private SpeedDetailService speedDetailService;


    private SahBasicTestServiceImpl sahBasicTestService;

    @Mock
    private BasicTest basicTest;
    @Mock
    private ProbePort probePort;
    @Mock
    private Package aPackage;
    @Mock
    private Probe probe;
    @Mock
    private Site site;

    @BeforeEach
    void setUp() {
        sahBasicTestService = new SahBasicTestServiceImpl(basicTestRepository,
                probePortService,
                measurementServerService,
                nationalOperatorService,
                multiTenantManager,
                geoLocationRepository,
                loopModeSettingsService,
                fieldAnonymizerFilter,
                radioSignalService,
                speedDetailMapper,
                speedDetailService
        );
    }

    @Test
    void updateBasicTestWithProbe_correctInvocation_basicTestUpdated() {
        when(probePortService.getProbePortByNameAndProbeId(TestConstants.DEFAULT_PROBE_PORT, TestConstants.DEFAULT_PROBE_ID))
                .thenReturn(probePort);
        when(probePort.getType()).thenReturn(TestConstants.DEFAULT_PORT_TYPE);
        when(probePort.getAPackage()).thenReturn(aPackage);
        when(probePort.getProbe()).thenReturn(probe);
        when(aPackage.getId()).thenReturn(TestConstants.DEFAULT_PACKAGE_ID);
        when(aPackage.getAdvertisedName()).thenReturn(TestConstants.DEFAULT_ADVERTISED_NAME);
        when(aPackage.getPackageType()).thenReturn(TestConstants.DEFAULT_PACKAGE_TYPE);
        when(aPackage.getGroupId()).thenReturn(TestConstants.DEFAULT_GROUP_ID);
        when(probe.getSite()).thenReturn(site);
        when(site.getId()).thenReturn(TestConstants.DEFAULT_SITE_ID);
        when(site.getName()).thenReturn(TestConstants.DEFAULT_SITE_NAME);
        when(site.getAdvertisedId()).thenReturn(TestConstants.DEFAULT_SITE_ADVERTISED_ID);


        sahBasicTestService.updateBasicTestWithProbe(TestConstants.DEFAULT_PROBE_PORT, TestConstants.DEFAULT_PROBE_ID, basicTest);

        verify(basicTest).setTypeOfProbePort(TestConstants.DEFAULT_PORT_TYPE.toString());
        verify(basicTest).setSiteId(TestConstants.DEFAULT_SITE_ID);
        verify(basicTest).setSiteName(TestConstants.DEFAULT_SITE_NAME);
        verify(basicTest).setSiteAdvertisedId(TestConstants.DEFAULT_SITE_ADVERTISED_ID);
        verify(basicTest).setPackageId(TestConstants.DEFAULT_PACKAGE_ID.toString());
        verify(basicTest).setPackageNameStamp(TestConstants.DEFAULT_ADVERTISED_NAME);
        verify(basicTest).setPackageType(TestConstants.DEFAULT_PACKAGE_TYPE.getValue());
        verify(basicTest).setGroupId(TestConstants.DEFAULT_GROUP_ID.toString());
    }
}
