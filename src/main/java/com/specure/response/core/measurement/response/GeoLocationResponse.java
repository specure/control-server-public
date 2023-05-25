package com.specure.response.core.measurement.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Data
@Builder
@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GeoLocationResponse {
    private Double geoLat;
    private Double geoLong;
    private Double accuracy;
    private Double altitude;
    private Double bearing;
    private Double speed;
    @JsonProperty("tstamp")
    private Date timestamp;
    private String provider;
}
