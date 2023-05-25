package com.specure.constant;

public interface URIConstants {
    String TEST_REQUEST_FOR_WEB_CLIENT = "/webTestRequest";
    String TEST_REQUEST_FOR_ADMIN = "/adminTestRequest";
    String MEASUREMENT_RESULT = "/measurementResult";
    String MEASUREMENT_RESULT_BY_UUID = MEASUREMENT_RESULT + "/{uuid}";
    String MEASUREMENT_RESULT_BY_UUID_FROM_DB = MEASUREMENT_RESULT + "/db/{uuid}";
    //Save qos result DONE
    String MEASUREMENT_RESULT_QOS = "/measurementQosResult";
    //Get qos params for qos test DONE
    String MEASUREMENT_QOS_REQUEST = "/qosTestRequest";
    String MEASUREMENT_SERVER = "/measurementServer";
    String MEASUREMENT_SERVER_WEB = "/measurementServerWeb";
    String REQUEST_DATA_COLLECTOR = "/requestDataCollector";
    String RESULT = "/result";
    String EXPORT_FULL = "/export/{fileExtension}";
    String EXPORT_MONTHLY = "/export/{year}-{month}/{fileExtension}";
    String MOBILE = "/mobile";
    String IP = "/ip";
    String TEST_REQUEST = "/testRequest";
    //Calculate qos counters
    String MEASUREMENT_QOS_RESULT = "/qosTestResult";
    //Save mobile qos result
    String RESULT_QOS_URL = "/resultQoS";
    //Calculate qos counters
    String QOS_BY_OPEN_TEST_UUID_AND_LANGUAGE = "/qos/O{open_test_uuid}/{lang}";
    //Calculate qos counters
    String QOS_BY_OPEN_TEST_UUID = "/qos/O{open_test_uuid}";
    String SETTINGS = "/settings";
    String TEST_RESULT = "/testresult";
    String TEST_RESULT_DETAIL = "/testresultdetail";
    String UUID = "/{uuid}";
    String ERROR = "/error";
    String NATIONAL_TABLE = "/nationalTable";
    String SIGNAL_REQUEST = "/signalRequest";
    String SIGNAL_RESULT = "/signalResult";
    String EN_HISTORY = "/en/history/";
    String HISTORY = "/history";
    String HISTORY_BY_UUID = "/history/{uuid}";
    String SETTINGS_MOBILE = "/settings/mobile";
    String EXPORT_MOBILE_FULL = "/exportMobile/{fileExtension}";
    String EXPORT_MOBILE_MONTHLY = "/exportMobile/{year}-{month}/{fileExtension}";
    String HEALTH = "/health";
    String VERSION = "/app/version";
    String NATIONAL_OPERATOR = "/nationalOperator";
    String NATIONAL_OPERATOR_BY_ID = "/nationalOperator/{id}";
    String V2 = "/v2";
    String NET_NEUTRALITY_TEST_REQUEST = "/netNeutralityTestRequest";
    String NET_NEUTRALITY_RESULT = "/netNeutralityResult";
    String NET_NEUTRALITY_RESULT_BY_UUID = "/reports/netNeutralityResult/history";
    String GRAPHS = "/graphs";
}
