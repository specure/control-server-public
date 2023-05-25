package com.specure.sah.utils;

import com.specure.common.config.MaxMindConfig;
import com.specure.common.service.digger.DiggerCommandPerformerService;
import com.specure.common.service.digger.DiggerService;
import com.specure.multitenant.MultiTenantManager;
import com.specure.utils.core.MeasurementCalculatorUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.InetAddress;

import static com.specure.sah.TestConstants.*;
import static org.mockito.Mockito.when;


@RunWith(SpringRunner.class)
public class DiggerServiceTest {

    @MockBean
    DiggerCommandPerformerService diggerCommandPerformerService;
    @MockBean
    MaxMindConfig maxMindConfig;
    @MockBean
    MultiTenantManager multiTenantManager;

    DiggerService diggerService;

    @Before
    public void setUp() {
        diggerService = new DiggerService(maxMindConfig, diggerCommandPerformerService, multiTenantManager);
    }

    @Test
    public void getProviderByASN_WhenCallSorted_ExpectProviderName() {
        when(diggerCommandPerformerService.dig("dig -t txt AS39608.asn.cymru.com. +short"))
                .thenReturn("\"39608 | UA | ripencc | 2006-03-24 | LANETUA-AS, UA\"\n");
        String provider = diggerService.getProviderByASN(DEFAULT_ASN);
        assert(provider).equals(DEFAULT_GEO_PROVIDER_NAME);
    }

    @Test
    public void digASN_WhenCall_ExpectReturnCorrectASN() throws Exception {
        InetAddress addr = InetAddress.getByName(DEFAULT_IP_FOR_PROVIDER);
        when(diggerCommandPerformerService.dig("dig -t txt 123.47.37.176.origin.asn.cymru.com +short"))
                .thenReturn("\"39608 | 176.36.0.0/14 | UA | ripencc | 2011-05-20\"\n");
        Long asn = diggerService.digASN(addr);
        assert(asn).equals(DEFAULT_ASN);
    }
    @Test
    public void GeoProviderDataUtil_WhenGetAsnWithIp4Addr_ExpectInvertedAddr() throws Exception {
        InetAddress addr = InetAddress.getByName(DEFAULT_IP_FOR_PROVIDER);
        String reversed = diggerService.reverseIp(addr);
        assert(reversed).equals("123.47.37.176");
    }
    @Test
    public void GeoProviderDataUtil_WhenGetAsnWithIp6Addr_ExpectInvertedAddr() throws Exception {
        InetAddress addr = InetAddress.getByName("2607:f0d0:1002:51::4");
        String reversed = diggerService.reverseIp(addr);
        assert(reversed).equals("4.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.1.5.0.0.2.0.0.1.0.d.0.f.7.0.6.2");
    }

    @Test
    public void getProviderNameFromDigResult_whenCall_expectProviderName() {
        System.out.println(MeasurementCalculatorUtil.getSpeedBitPerSec(4603697L, 7101033353L) / 1e3);
    }

}
