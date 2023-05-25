package com.specure.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Objects;

public enum Platform {
    BROWSER,
    ANDROID,
    APPLET,
    CLI,
    IOS,
    PROBE,
    NODEJS,
    DESKTOP,
    UNKNOWN;

    @JsonCreator
    public static Platform forValue(String value) {
        if (Objects.isNull(value)) {
            return null;
        }
        try {
            return valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) { // if value not found by name - try find by label
        }
        return UNKNOWN;
    }
}
