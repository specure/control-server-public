package com.specure.response.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.specure.response.core.measurement.response.GeoLocationResponse;
import com.specure.response.core.measurement.response.PingResponse;
import com.specure.response.core.measurement.response.SpeedDetailResponse;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import static com.specure.constant.ErrorMessage.CLIENT_UUID_REQUIRED;
import static com.specure.constant.ErrorMessage.TEST_TOKEN_REQUIRED;

@Builder
@Data
@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MeasurementHistoryResponse {

    private Long pingMedian;
    private Integer signalStrength;
    private String token;
    private String openTestUuid;
    private Integer speedDownload;
    private Integer speedUpload;
    private Integer lte_rsrp;
    private Integer lte_rsrq;

    private String voip_result_jitter;
    private String voip_result_packet_loss;

    private String clientLanguage;
    private String clientName;
    private String clientVersion;
    private String networkOperator;
    private String clientProvider;

    @NotNull(message = CLIENT_UUID_REQUIRED)
    private String clientUuid;

    @JsonProperty("geoLocations")
    private List<GeoLocationResponse> geoLocations;

    private String model;
    private Integer networkType;
    private String platform;
    private String appVersion;
    private String product;

    private String ipAddress;

    private List<PingResponse> pings;

    private Long testBytesDownload;
    private Long testBytesUpload;

    private Long testNsecDownload;
    private Long testNsecUpload;
    private Integer testNumThreads;
    private Integer numThreadsUl;
    private Long testPingShortest;
    private Integer testSpeedDownload;
    private Integer testSpeedUpload;

    private Long measurementServerId;
    private String measurementServerName;

    @NotNull(message = TEST_TOKEN_REQUIRED)
    private String testToken;
    private String testUUuid;
    private Timestamp time;
    private String timezone;
    private String type;
    private String versionCode;
    private List<SpeedDetailResponse> speedDetail;
    private Integer userServerSelection;
    private String loopUuid;

    // QOS ?
    private String device;
    private String tag;
    private String telephonyNetworkCountry;
    private String telephonyNetworkSimCountry;
    private Integer testPortRemote;
    private Long testTotalBytesDownload;
    private Long testTotalBytesUpload;
    private String testEncryption;
    private String testIpLocal;
    private String testIpServer;
    private Integer testIfBytesDownload;
    private Integer testIfBytesUpload;
    private Integer testdlIfBytesDownload;
    private Integer testdlIfBytesUpload;
    private Integer testulIfBytesDownload;
    private Integer testulIfBytesUpload;
    private Map<String, String> jpl;
    private List<Map<String, Integer>> signals;
}
