package com.specure.request.sah.neutrality.result;

import com.specure.common.enums.Platform;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.specure.request.mobile.LocationRequest;
import com.specure.response.sah.neutrality.RadioInfoRequest;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class NetNeutralityMeasurementResultRequest {

    @JsonProperty("testResults")
    @NotNull(message = "[testResults] must not be null")
    @Valid
    private final List<NetNeutralityTestResultRequest> testResults;

    @JsonProperty("model")
    private final String model;

    @JsonProperty("device")
    private final String device;

    @JsonProperty("location")
    private final LocationRequest location;

    @JsonProperty("product")
    private final String product;

    @JsonProperty("networkChannelNumber")
    private final String networkChannelNumber;

    @NotNull(message = "[networkType] must not be null")
    @JsonProperty("networkType")
    private final Integer networkType;

    @JsonProperty("signalStrength")
    private final Integer signalStrength;

    @JsonProperty("testIpLocal")
    private final String testIpLocal;

    @JsonProperty("platform")
    private final Platform platform;

    @JsonProperty("simMccMnc")
    private final String simMccMnc;

    @JsonProperty("simOperatorName")
    private final String simOperatorName;

    @JsonProperty("simCountry")
    private final String simCountry;

    @JsonProperty("networkMccMnc")
    private final String networkMccMnc;

    @JsonProperty("networkOperatorName")
    private final String networkOperatorName;

    @JsonProperty("networkCountry")
    private final String networkCountry;

    @JsonProperty("networkIsRoaming")
    private final Boolean networkIsRoaming;

    @JsonProperty("measurementServerName")
    private final String measurementServerName;

    @JsonProperty("measurementServerCity")
    private final String measurementServerCity;

    @JsonProperty("radioInfo")
    private final RadioInfoRequest radioInfo;
}
