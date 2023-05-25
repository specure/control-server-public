package com.specure.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

import java.util.Objects;

@Getter
public enum MeasurementServerType {
    RMBTws("RMBTws", ServerTechForMeasurement.WS_TECH),
    RMBT("RMBT", ServerTechForMeasurement.RMBT_TECH),
    QoS("QoS", ServerTechForMeasurement.QOS_TECH),
    RMBThttp("HTTP", ServerTechForMeasurement.QOS_TECH),
    RMBTel("RMBTel", ServerTechForMeasurement.RMBT_TECH);

    private final String name;
    private final ServerTechForMeasurement serverTechForMeasurement;

    MeasurementServerType(
            String name,
            ServerTechForMeasurement serverTechForMeasurement
    ) {
        this.name = name;
        this.serverTechForMeasurement = serverTechForMeasurement;
    }

    @JsonCreator
    public static MeasurementServerType forValue(String value) {
        if (Objects.isNull(value)) {
            return null;
        }
        for (MeasurementServerType measurementServerType : values()) {
            if (measurementServerType.name.equalsIgnoreCase(value))
                return measurementServerType;
        }
        throw new IllegalArgumentException();
    }
}
