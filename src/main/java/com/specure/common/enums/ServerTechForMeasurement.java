package com.specure.common.enums;

import lombok.Getter;

@Getter
public enum ServerTechForMeasurement {
    RMBT_TECH("rmbt", 5231, 5232),
    WS_TECH("ws", 8080, 443),
    QOS_TECH("qos", 5235, 5235),
    HTTP_TECH("http", 443, 443);

    private final String name;
    private final Integer defaultNoSslPort;
    private final Integer defaultSslPort;

    ServerTechForMeasurement(
            String name,
            Integer defaultNoSslPort,
            Integer defaultSslPort
    ) {
        this.name = name;
        this.defaultNoSslPort = defaultNoSslPort;
        this.defaultSslPort = defaultSslPort;
    }
}
