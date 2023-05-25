package com.specure.utils.sah;

import com.specure.dto.sah.NationalTableParams;
import com.specure.exception.BadMobileTechnologyException;
import lombok.experimental.UtilityClass;

import java.util.Collections;
import java.util.List;

import static com.specure.common.enums.NetworkType.*;

@UtilityClass
public class MobileTechnologyConverter {

    public NationalTableParams getNetworkTypesByMobileTechName(String mobileTechnology) throws BadMobileTechnologyException {
        switch (mobileTechnology) {
            case "2G":
                return new NationalTableParams(true, List.of(GPRS, CDMA, EDGE, IDEN, RTT, EVDO_0, EVDO_A, EVDO_B, EHRPD, GSM));
            case "3G":
                return new NationalTableParams(true, List.of(UMTS, HSDPA, HSUPA, HSPA, HSPA_PLUS, MOBILE_2G_3G, TD_SCDMA));
            case "4G":
                return new NationalTableParams(true, List.of(LTE, MOBILE_2G_3G_4G, MOBILE_2G_4G, MOBILE_3G_4G, LTE_CA));
            case "5G":
                return new NationalTableParams(true, List.of(NR, SIGNALLING_5G_ONLY, NRNSA));
            case "all":
                return new NationalTableParams(true, Collections.emptyList());
            case "all_mno":
                return new NationalTableParams(true, List.of(NR, SIGNALLING_5G_ONLY, NRNSA, LTE, MOBILE_2G_3G_4G, MOBILE_2G_4G,
                        MOBILE_3G_4G, LTE_CA, UMTS, HSDPA, HSUPA, HSPA, HSPA_PLUS, MOBILE_2G_3G, TD_SCDMA,
                        GPRS, CDMA, EDGE, IDEN, RTT, EVDO_0, EVDO_A, EVDO_B, EHRPD, GSM));
            case "WLAN":
            case "all_isp":
                return new NationalTableParams(false, List.of(WLAN));
        }
        throw new BadMobileTechnologyException(mobileTechnology);
    }
}
