package com.specure.service.sah.impl;

import com.specure.common.model.elastic.BasicQosTest;
import com.specure.common.model.jpa.Package;
import com.specure.common.model.jpa.Probe;
import com.specure.common.model.jpa.ProbePort;
import com.specure.common.model.jpa.Site;
import com.specure.mapper.mobile.BasicTestMobileMapper;
import com.specure.multitenant.MultiTenantManager;
import com.specure.repository.sah.BasicQosTestRepository;
import com.specure.sah.TestConstants;
import com.specure.service.admin.ProviderService;
import com.specure.service.core.MeasurementServerService;
import com.specure.service.sah.BasicTestHistoryCacheService;
import com.specure.service.sah.BasicTestService;
import com.specure.service.sah.ProbePortService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class SahBasicQosTestServiceImplTest {

    @MockBean
    private BasicQosTestRepository basicQosTestRepository;
    @MockBean
    private BasicTestService basicTestService;
    @MockBean
    private MeasurementServerService measurementServerService;
    @MockBean
    private ProbePortService probePortService;
    @MockBean
    private MultiTenantManager multiTenantManager;
    @MockBean
    private BasicTestMobileMapper basicTestMobileMapper;
    @MockBean
    private BasicTestHistoryCacheService basicTestHistoryCacheService;
    @MockBean
    private ProviderService providerService;

    private SahBasicQosTestServiceImpl sahBasicQosTestService;

    @Mock
    private BasicQosTest basicQosTest;
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
        sahBasicQosTestService = new SahBasicQosTestServiceImpl(basicQosTestRepository,
                basicTestService,
                measurementServerService,
                probePortService,
                multiTenantManager,
                basicTestMobileMapper,
                basicTestHistoryCacheService,
                providerService);
    }

    @Test
    void updateBasicQosTestWithProbe_correctInvocation_basicQosTestUpdated() {
        when(probePortService.getProbePortByNameAndProbeId(TestConstants.DEFAULT_PROBE_PORT, TestConstants.DEFAULT_PROBE_ID))
                .thenReturn(probePort);
        when(probePort.getAPackage()).thenReturn(aPackage);
        when(probePort.getProbe()).thenReturn(probe);
        when(probe.getSite()).thenReturn(site);
        when(site.getAdvertisedId()).thenReturn(TestConstants.DEFAULT_SITE_ADVERTISED_ID);
        when(site.getId()).thenReturn(TestConstants.DEFAULT_SITE_ID);
        when(aPackage.getProviderId()).thenReturn(TestConstants.DEFAULT_PROVIDER_ID);
        when(aPackage.getAdvertisedName()).thenReturn(TestConstants.DEFAULT_PACKAGE_ADVERTISED_NAME);
        when(aPackage.getId()).thenReturn(TestConstants.DEFAULT_PACKAGE_ID);
        when(aPackage.getPackageType()).thenReturn(TestConstants.DEFAULT_PACKAGE_TYPE);
        when(providerService.getProviderById(TestConstants.DEFAULT_PROVIDER_ID)).thenReturn(TestConstants.DEFAULT_PROVIDER);
        when(probePort.getType()).thenReturn(TestConstants.DEFAULT_PORT_TYPE);

        sahBasicQosTestService.updateBasicQosTestWithProbe(TestConstants.DEFAULT_PROBE_PORT, TestConstants.DEFAULT_PROBE_ID, basicQosTest);

        verify(basicQosTest).setSiteAdvertisedId(TestConstants.DEFAULT_SITE_ADVERTISED_ID);
        verify(basicQosTest).setSiteId(TestConstants.DEFAULT_SITE_ID);
        verify(basicQosTest).setOperator(TestConstants.DEFAULT_PROVIDER_NAME);
        verify(basicQosTest).setPackageAdvertisedName(TestConstants.DEFAULT_PACKAGE_ADVERTISED_NAME);
        verify(basicQosTest).setPackageId(TestConstants.DEFAULT_PACKAGE_ID.toString());
        verify(basicQosTest).setPackageType(TestConstants.DEFAULT_PACKAGE_TYPE.getValue());
        verify(basicQosTest).setTypeOfProbePort(TestConstants.DEFAULT_PORT_TYPE.toString());
        verify(basicQosTest).setProbeId(TestConstants.DEFAULT_PROBE_ID);
        verify(basicQosTest).setProbePort(TestConstants.DEFAULT_PROBE_PORT);
    }
}
