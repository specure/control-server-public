package com.specure.common.enums;


import com.specure.common.exception.PackageTypeException;

public enum PackageType {
    FIXED_BROADBAND("Fixed broadband"),
    FIXED_WIRELESS_BROADBAND("Fixed wireless broadband"),
    MOBILE_PREPAID("Mobile prepaid"),
    MOBILE_BROADBAND("Mobile broadband");

    private final String value;

    PackageType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static PackageType fromString(String packageTypeName) {
        for (PackageType b : PackageType.values()) {
            if (b.getValue().equals(packageTypeName)) {
                return b;
            }
        }
        throw new PackageTypeException(packageTypeName);
    }
}
