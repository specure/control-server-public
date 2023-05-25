package com.specure.constant;

import com.specure.common.enums.MeasurementServerType;

import java.util.List;

public interface Config {
    List<String> SUPPORTED_CLIENT_NAMES = List.of("RMBT",
            "RMBTjs",
            "Open-RMBT",
            "RMBTws",
            "HW-PROBE");

    List<MeasurementServerType> SERVER_TEST_SERVER_TYPES = List.of(MeasurementServerType.RMBT);
    List<MeasurementServerType> SERVER_QOS_TEST_SERVER_TYPES = List.of(MeasurementServerType.QoS);
    List<MeasurementServerType> SERVER_HTTP_TEST_SERVER_TYPES = List.of(MeasurementServerType.RMBT);
    List<MeasurementServerType> SERVER_WS_TEST_SERVER_TYPES = List.of(MeasurementServerType.RMBTws);
    String SPEED_DETAIL_CACHE_NAME = "speedDetail";
    String ANONYMIZE_CACHE_NAME = "anonymize";
    long SPEED_DETAIL_CACHE_EXPIRE_SECONDS = 60;
    long ANONYMIZE_CACHE_EXPIRE_HOURS = 12;
}
