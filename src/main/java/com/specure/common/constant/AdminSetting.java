package com.specure.common.constant;

import java.util.List;

public interface AdminSetting {
    String SIGNAL_RESULT_URL_KEY = "signal_result_url";
    String TEST_RESULT_QOS_URL_KEY = "test_result_qos_url";
    String TEST_RESULT_URL_KEY = "test_result_url";
    String MEASUREMENT_DURATION_KEY = "duration";
    String MEASUREMENT_NUM_PINGS_KEY = "num_pings";
    String CAMPAIGN_DURATION_DAYS = "campaign_duration_days";
    String MEASUREMENT_NUM_THREADS_ANDROID_KEY = "num_threads_android";
    String MEASUREMENT_NUM_THREADS_IOS_KEY = "num_threads_ios";
    String MEASUREMENT_NUM_THREADS_WEB_KEY = "num_threads_web";
    String MAP_SERVER_HOST_KEY = "host_map_server";
    String MAP_SERVER_SSL_KEY = "ssl_map_server";
    String MAP_SERVER_PORT_KEY = "port_map_server";
    String TERM_AND_CONDITION_VERSION_KEY = "tc_version";
    String TERM_AND_CONDITION_VERSION_ANDROID_KEY = "tc_version_android";
    String TERM_AND_CONDITION_VERSION_IOS_KEY = "tc_version_ios";
    String TERM_AND_CONDITION_URL_KEY = "tc_url";
    String TERM_AND_CONDITION_URL_IOS_KEY = "tc_url_ios";
    String TERM_AND_CONDITION_URL_ANDROID_KEY = "tc_url_android";
    String TERM_AND_CONDITION_NDT_URL_KEY = "tc_ndt_url_android";
    String URL_OPEN_DATA_PREFIX_KEY = "url_open_data_prefix";
    String URL_SHARE_KEY = "url_share";
    String URL_STATISTIC_KEY = "url_statistics";
    String URL_CONTROL_IPV4_ONLY_KEY = "control_ipv4_only";
    String URL_CONTROL_IPV6_ONLY_KEY = "control_ipv6_only";
    String URL_IPV4_CHECK_KEY = "url_ipv4_check";
    String URL_IPV6_CHECK_KEY = "url_ipv6_check";
    String URL_MAP_SERVER_KEY = "url_map_server";

    List<String> ADMIN_SETTINGS_KEYS = List.of(SIGNAL_RESULT_URL_KEY,
            TEST_RESULT_QOS_URL_KEY,
            TEST_RESULT_URL_KEY,
            MEASUREMENT_DURATION_KEY,
            MEASUREMENT_NUM_PINGS_KEY,
            MEASUREMENT_NUM_THREADS_ANDROID_KEY,
            MEASUREMENT_NUM_THREADS_IOS_KEY,
            MEASUREMENT_NUM_THREADS_WEB_KEY,
            MAP_SERVER_HOST_KEY,
            MAP_SERVER_SSL_KEY,
            MAP_SERVER_PORT_KEY,
            TERM_AND_CONDITION_VERSION_KEY,
            TERM_AND_CONDITION_VERSION_IOS_KEY,
            TERM_AND_CONDITION_VERSION_ANDROID_KEY,
            TERM_AND_CONDITION_URL_ANDROID_KEY,
            TERM_AND_CONDITION_URL_KEY,
            TERM_AND_CONDITION_URL_IOS_KEY,
            TERM_AND_CONDITION_URL_ANDROID_KEY,
            TERM_AND_CONDITION_NDT_URL_KEY,
            URL_OPEN_DATA_PREFIX_KEY,
            URL_SHARE_KEY,
            URL_STATISTIC_KEY,
            URL_CONTROL_IPV4_ONLY_KEY,
            URL_CONTROL_IPV6_ONLY_KEY,
            URL_IPV4_CHECK_KEY,
            URL_IPV6_CHECK_KEY,
            URL_MAP_SERVER_KEY
    );
}
