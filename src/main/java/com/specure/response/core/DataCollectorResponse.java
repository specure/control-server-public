package com.specure.response.core;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Builder
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataCollectorResponse {
    private String agent;
    private String category;

    @JsonProperty("country_geoip")
    private String countryGeoIp;

    private String family;
    private Map<String, String> headers;
    private String ip;
    private List<String> languages;
    private String os;
    private int port;
    private String product;
    private String version;
    private String version_minor;

}
