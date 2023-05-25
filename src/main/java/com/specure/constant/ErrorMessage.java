package com.specure.constant;

public interface ErrorMessage {
    String CLIENT_UUID_REQUIRED = "Client UUID is mandatory.";
    String TEST_TOKEN_REQUIRED = "Test token is mandatory.";
    String TEST_UUID_REQUIRED = "Test UUID is mandatory.";
    String MEASUREMENT_SERVER_NOT_FOUND = "There is no measurement server was found by id %s.";
    String MEASUREMENT_SERVER_NOT_ACCESSIBLE_FOR_ON_NET = "There is no measurement server accessible for ON-net measurement.";
    String MEASUREMENT_HISTORY_IS_NOT_AVAILABLE = "The history of your measurements is not accessible.";
    String MEASUREMENT_SERVER_ON_OFF_NET_NOT_FOUND = "There was not found measurement server ( isOnNet = %s ) for provider %s. Pls check configuration.";
    String MEASUREMENT_RESULT_WRONG_FORMAT = "The measurement result has wrong format: %s.";
    String PORT_NAME_REQUIRED = "Port name is mandatory.";
    String PROVIDER_IN_PACKAGE_REQUIRED = "provider was not found in package id %s";
    String PROVIDER_NOT_FOUND_BY_ID = "Provider was not found by id %s";
    String PROVIDER_WITH_ID_ALREADY_EXISTS = "Attempt create new Provider with existing id %s";
    String QOS_MEASUREMENT_SERVER_FOR_UUID_NOT_FOUND = "QoS measurement server was not found for uuid %s.";
    String UNSUPPORTED_FILE_EXTENSION = "Unsupported file extension %s";
    String QOS_MEASUREMENT_FROM_ON_NET_SERVER = "The attempt to save QoS measurement (uuid =%s) from ON_NET server (id=%s).";
    String UNSUPPORTED_CLIENT_NAME = "Unsupported client name %s";
    String CLIENT_NOT_FOUND = "No client found by id %s.";
    String DATA_STREAM_SOURCE = "there was not found stream data source with label '%s' pls create proper spring component.";
    String GEO_SHAPE_NOT_FOUND_BY_CODE = "Geo shape not found by code %s";
    String NATIONAL_OPERATOR_NOT_FOUND_BY_ID = "National operator not found by id %d";
    String NETWORK_TYPE_REQUIRED = "Network type is required";
    String TEST_NSEC_DOWNLOAD_REQUIRED = "'testNsecDownload' is required";
    String TEST_NSEC_UPLOAD_REQUIRED = "'testNsecUpload' is required";
    String TIME_REQUIRED = "'time' is required";
    String TEST_BYTES_DOWNLOAD_REQUIRED = "'testBytesDownload' is required";
    String TEST_BYTES_UPLOAD_REQUIRED = "'testBytesUpload' is required";
    String ATTEMPT_TO_VIOLATE_DATA_INTEGRITY = "Attempt to violate data integrity.";
    String WRONG_MOBILE_TECHNOLOGY_PARAMETER = "Mobile technology should be 2G, 3G, 4G, 5G or all but not '%s'";
    String PROBE_STILL_CONNECT_TO_SITE = "Probe id = %s is still connected to site";
    String QOS_TEST_RESULT_FOR_TEST_NOT_FOUND = "Qos measurement result not found for measurement with uuid %s";
    String WRONG_TENANT = "No such tenant '%s'.";
    String NET_NEUTRALITY_DNS_FAIL_REASON_TIMEOUT = "timeout";
    String NET_NEUTRALITY_DNS_FAIL_REASON_BAD_STATUS = "badStatus";
    String NET_NEUTRALITY_DNS_FAIL_REASON_DIFFERENT_IPS = "diffIpAddress";
    String UNKNOWN_PROVIDER = "unknown";
    String NET_NEUTRALITY_SETTING_NOT_FOUND_BY_ID_AND_TYPE = "Net neutrality setting with id %s and type %s not found";
}
