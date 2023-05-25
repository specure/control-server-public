package com.specure.constant;

import com.specure.common.enums.MeasurementServerType;

public interface Constants {
    String INET_4_IP_VERSION = "4";
    String INET_6_IP_VERSION = "6";
    String TEST_RESULT_DETAIL_OPEN_UUID_TEMPLATE = "P%s";
    String TEST_RESULT_DETAIL_OPEN_TEST_UUID_TEMPLATE = "O%s";
    String TIMEZONE_TEMPLATE = "UTC%sh";
    Double MILLISECONDS_TO_HOURS = 1000d * 60d * 60d;
    String TIMEZONE_PATTERN = "+0.##;-0.##";
    Integer SIGNIFICANT_PLACES = 2;
    String VALUE_AND_UNIT_TEMPLATE = "%s %s";
    String TEST_HISTORY_LOOP_UUID_TEMPLATE = "L%s";
    boolean DEFAULT_QOS_SUPPORTS_INFO = false;
    boolean DEFAULT_RMBT_HTTP = false;
    Integer DEFAULT_CLASSIFICATION_COUNT = 3;
    MeasurementServerType DEFAULT_MEASUREMENT_SERVER_TYPE = MeasurementServerType.RMBThttp;
    String ADMIN_TENANT_NAME = "admin";
    String UNKNOWN_INTERNET_PROTOCOL = "UNKNOWN";
    String IP_V6 = "IPv6";
    String IP_V4 = "IPv4";
    String DNS_ENTRIES_DELIMITER = ";";
}
