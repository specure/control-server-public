package com.specure.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Getter
@Slf4j
public enum NetworkType {
    UNKNOWN("Unknown", "Unknown", 0),
    GPRS("GPRS", "2G", 1),
    EDGE("EDGE", "2G", 2),
    UMTS("UMTS", "3G", 3),
    CDMA("CDMA", "2G", 4),
    EVDO_0("EVDO_0", "2G", 5),
    EVDO_A("EVDO_A", "2G", 6),
    RTT("1xRTT", "2G", 7),
    HSDPA("HSDPA", "3G", 8),
    HSUPA("HSUPA", "3G", 9),
    HSPA("HSPA", "3G", 10),
    IDEN("HSPA", "2G", 11),
    EVDO_B("EVDO_B", "2G", 12),
    LTE("LTE", "4G", 13),
    EHRPD("EHRPD", "2G", 14),
    HSPA_PLUS("HSPA+", "3G", 15),
    GSM("GSM", "2G", 16),
    TD_SCDMA("TD_SCDMA", "3G", 17),
    IWLAN("IWLAN", "UNKNOWN", 18),
    LTE_CA("LTE CA", "4G", 19),
    NR("NR", "5G", 20),
    SIGNALLING_5G_ONLY("LTE NR", "4G(+5G)", 40),
    NRNSA("NRNSA", "5G", 41),
    CLI("CLI", "CLI", 97),
    LAN("LAN", "LAN", 98),
    WLAN(null, "WLAN", 99),
    MOBILE_2G_3G("2G/3G", "2G/3G", 101),
    MOBILE_3G_4G("3G/4G", "3G/4G", 102),
    MOBILE_2G_4G("2G/4G", "2G/4G", 103),
    MOBILE_2G_3G_4G("2G/3G/4G", "2G/3G/4G", 104),
    CELLULAR_ANY(null, "Mobile", 105),
    ETHERNET("Ethernet", "Ethernet", 106),
    BLUETOOTH("Bluetooth", "Bluetooth", 107);

    private final String name;
    private final String category;
    private final int value;

    public static NetworkType fromValue(int i) {
        NetworkType[] testEnums = NetworkType.values();
        for (NetworkType testEnum : testEnums) {
            if (testEnum.value == i) {
                return testEnum;
            }
        }
        return UNKNOWN;
    }

    public static List<NetworkType> fromCategoryValue(String i) {
        List<NetworkType> categories = new ArrayList();
        NetworkType[] testEnums = NetworkType.values();
        for (NetworkType testEnum : testEnums) {
            if (testEnum.category.equals(i)) {
                categories.add(testEnum);
            }
        }
        return categories;
    }

    public static NetworkType fromStringValue(String value) {
        NetworkType[] testEnums = NetworkType.values();
        for (NetworkType testEnum : testEnums) {
            if (String.valueOf(testEnum.value).equals(value) || testEnum.name.equals(value)) {
                return testEnum;
            }
        }
        return UNKNOWN;
    }

    public static NetworkType valueOfSafely(String name) {
        try {
            return valueOf(name);
        } catch (IllegalArgumentException exception) {
            log.error("Unknown network type '{}'", name, exception);
        }
        return UNKNOWN;
    }
}
