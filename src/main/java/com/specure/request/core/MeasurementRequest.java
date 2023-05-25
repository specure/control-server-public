package com.specure.request.core;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.specure.constant.ErrorMessage;
import com.specure.request.core.measurement.request.GeoLocationRequest;
import com.specure.request.core.measurement.request.PingRequest;
import com.specure.request.core.measurement.request.SpeedDetailRequest;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Builder
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MeasurementRequest {

    private String clientLanguage;
    private String clientName;
    private String clientVersion;

    @ToString.Exclude
    @JsonProperty("geoLocations")
    private List<GeoLocationRequest> geoLocations;

    private String model;
    @NotNull(message = ErrorMessage.NETWORK_TYPE_REQUIRED)
    private String networkType;
    @JsonProperty("platform")
    @JsonAlias({"plattform", "platform"})
    private String platform;
    private String product;

    @ToString.Exclude
    private ArrayList<PingRequest> pings;

    @NotNull(message = ErrorMessage.TEST_BYTES_DOWNLOAD_REQUIRED)
    private Long testBytesDownload;
    @NotNull(message = ErrorMessage.TEST_BYTES_UPLOAD_REQUIRED)
    private Long testBytesUpload;

    @NotNull(message = ErrorMessage.TEST_NSEC_DOWNLOAD_REQUIRED)
    private Long testNsecDownload;
    @NotNull(message = ErrorMessage.TEST_NSEC_UPLOAD_REQUIRED)
    private Long testNsecUpload;
    private Integer testNumThreads;
    private Integer numThreadsUl;
    private Long testPingShortest;
    private Integer testSpeedDownload;
    private Integer testSpeedUpload;

    @NotNull(message = ErrorMessage.TEST_TOKEN_REQUIRED)
    private String testToken;
    @NotNull(message = ErrorMessage.TIME_REQUIRED)
    private Long time;
    private String timezone;
    private String type;
    private String versionCode;
    @ToString.Exclude
    private List<SpeedDetailRequest> speedDetail;
    private Integer userServerSelection;
    private String loopUuid;

    // QOS ?
    private String device;
    private String tag;
    private String telephonyNetworkCountry;
    private String telephonyNetworkSimCountry;
    private Integer testPortRemote;
    @ToString.Exclude
    private Map<String, String> jpl;
    @ToString.Exclude
    private List<Map<String, Integer>> signals;
}
