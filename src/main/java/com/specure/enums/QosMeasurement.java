package com.specure.enums;

public enum QosMeasurement {
    WEBSITE("WEBSITE", "Web page"),
    HTTP_PROXY("HTTP_PROXY", "Unmodified content"),
    NON_TRANSPARENT_PROXY("NON_TRANSPARENT_PROXY", "Transparent connection"),
    DNS("DNS", "DNS"),
    TCP("TCP", "TCP ports"),
    UDP("UDP", "UDP ports"),
    VOIP("VOIP", "Voice over IP"),
    TRACEROUTE("TRACEROUTE", "Traceroute"),
    TRACEROUTE_MASKED("TRACEROUTE_MASKED", "Traceroute");

    private final String value;

    private final String description;

    QosMeasurement(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }
}
