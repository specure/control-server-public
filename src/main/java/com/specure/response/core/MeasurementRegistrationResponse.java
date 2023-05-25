package com.specure.response.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.specure.common.enums.Platform;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class MeasurementRegistrationResponse {

    @JsonProperty("test_uuid")
    private String testUuid;

    @JsonProperty("result_url")
    private String resultUrl;

    @JsonProperty("result_qos_url")
    private String resultQosUrl;

    @JsonProperty("test_duration")
    private Integer testDuration;

    @JsonProperty("test_server_name")
    private String testServerName;

    @JsonProperty("test_wait")
    private Integer testWait;

    @JsonProperty("test_server_address")
    private String testServerAddress;

    @JsonProperty("test_numthreads")
    private Integer testNumThreads;

    @JsonProperty("test_server_port")
    private Integer testServerPort;

    @JsonProperty("test_server_encryption")
    private boolean testServerEncryption;

    @JsonProperty("test_token")
    private String testToken;

    @JsonProperty("test_numpings")
    private Integer testNumPings;

    @JsonProperty("test_id")
    private Long testId;

    @JsonProperty("client_remote_ip")
    private String clientRemoteIp;

    private String provider;

    @JsonProperty("app_version")
    private String appVersion;

    @JsonProperty("platform")
    private Platform platform;

    private List<String> error;
}
