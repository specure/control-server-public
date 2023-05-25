package com.specure.sah.utils;

import com.specure.dto.sah.NationalTableParams;
import com.specure.exception.BadMobileTechnologyException;
import com.specure.utils.sah.MobileTechnologyConverter;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;

import static com.specure.common.enums.NetworkType.*;

@RunWith(SpringRunner.class)
public class MobileTechnologyConverterTest {

    @Test(expected = BadMobileTechnologyException.class)
    public void getNetworkTypesByMobileTechName_whenBadMobileTechnology_ExpectException() {
        MobileTechnologyConverter.getNetworkTypesByMobileTechName("99G");
    }

    @Test
    public void getNetworkTypesByMobileTechName_whenAll_ExpectEmptyList() {
        NationalTableParams nationalTableParams = MobileTechnologyConverter.getNetworkTypesByMobileTechName("all");
        Assert.assertEquals(Collections.emptyList(), nationalTableParams.getNetworkTypes());
        Assert.assertTrue(nationalTableParams.isMno());
    }

    @Test
    public void getNetworkTypesByMobileTechName_whenGetMobileTech_ExpectReturnProperNetworks() {
        NationalTableParams nationalTableParams = MobileTechnologyConverter.getNetworkTypesByMobileTechName("5G");
        Assert.assertEquals(List.of(NR, SIGNALLING_5G_ONLY, NRNSA), nationalTableParams.getNetworkTypes());

        nationalTableParams = MobileTechnologyConverter.getNetworkTypesByMobileTechName("2G");
        Assert.assertEquals(List.of(GPRS, CDMA, EDGE, IDEN, RTT, EVDO_0, EVDO_A, EVDO_B, EHRPD, GSM), nationalTableParams.getNetworkTypes());

        nationalTableParams = MobileTechnologyConverter.getNetworkTypesByMobileTechName("3G");
        Assert.assertEquals(List.of(UMTS, HSDPA, HSUPA, HSPA, HSPA_PLUS, MOBILE_2G_3G, TD_SCDMA), nationalTableParams.getNetworkTypes());

        nationalTableParams = MobileTechnologyConverter.getNetworkTypesByMobileTechName("4G");
        Assert.assertEquals(List.of(LTE, MOBILE_2G_3G_4G, MOBILE_2G_4G, MOBILE_3G_4G, LTE_CA), nationalTableParams.getNetworkTypes());
    }

    @Test
    public void getNetworkTypesByMobileTechName_whenAllIsp_expectedWlan() {
        NationalTableParams nationalTableParams = MobileTechnologyConverter.getNetworkTypesByMobileTechName("all_isp");
        Assert.assertEquals(List.of(WLAN), nationalTableParams.getNetworkTypes());
        Assert.assertFalse(nationalTableParams.isMno());
    }

    @Test
    public void getNetworkTypesByMobileTechName_whenWlan_expectedWlan() {
        NationalTableParams nationalTableParams = MobileTechnologyConverter.getNetworkTypesByMobileTechName("WLAN");
        Assert.assertEquals(List.of(WLAN), nationalTableParams.getNetworkTypes());
        Assert.assertFalse(nationalTableParams.isMno());
    }

    @Test
    public void getNetworkTypesByMobileTechName_whenAllMno_expectedWlan() {
        NationalTableParams nationalTableParams = MobileTechnologyConverter.getNetworkTypesByMobileTechName("all_mno");
        Assert.assertEquals(List.of(NR, SIGNALLING_5G_ONLY, NRNSA, LTE, MOBILE_2G_3G_4G, MOBILE_2G_4G,
                MOBILE_3G_4G, LTE_CA, UMTS, HSDPA, HSUPA, HSPA, HSPA_PLUS, MOBILE_2G_3G, TD_SCDMA,
                GPRS, CDMA, EDGE, IDEN, RTT, EVDO_0, EVDO_A, EVDO_B, EHRPD, GSM), nationalTableParams.getNetworkTypes());
        Assert.assertTrue(nationalTableParams.isMno());
    }
}
