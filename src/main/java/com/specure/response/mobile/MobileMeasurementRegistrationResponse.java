package com.specure.response.mobile;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.specure.common.enums.MeasurementServerType;
import com.specure.common.enums.Platform;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class MobileMeasurementRegistrationResponse {

    @JsonProperty("test_uuid")
    private String testUuid;

    @JsonProperty("open_test_uuid")
    private String openTestUUID;

    @JsonProperty("result_url")
    private String resultUrl;

    @JsonProperty("result_qos_url")
    private String resultQosUrl;

    @JsonProperty("test_duration")
    private String testDuration;

    @JsonProperty("test_server_name")
    private String testServerName;

    @JsonProperty("test_server_type")
    @ApiModelProperty(notes = "Type of the measurement server", example = "RMBT")
    private MeasurementServerType testServerType;

    @JsonProperty("test_wait")
    private Integer testWait;

    @JsonProperty("test_server_address")
    private String testServerAddress;

    @JsonProperty("test_numthreads")
    private String testNumThreadsMobile;

    @JsonProperty("test_server_port")
    private Integer testServerPort;

    @JsonProperty("test_server_encryption")
    private boolean testServerEncryption;

    @JsonProperty("test_token")
    private String testToken;

    @JsonProperty("test_numpings")
    private String testNumPings;

    @JsonProperty("test_id")
    private Long testId;

    @JsonProperty("client_remote_ip")
    private String clientRemoteIp;

    @JsonProperty("loop_uuid")
    private String loopUuid;

    @JsonProperty("platform")
    private Platform platform;

    @JsonProperty("app_version")
    private String appVersion;

    private List<String> error;
}
