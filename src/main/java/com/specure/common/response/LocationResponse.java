package com.specure.common.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
@ToString
public class LocationResponse {
    @JsonProperty("lat")
    private Double latitude;

    @JsonProperty("long")
    private Double longitude;

    @JsonProperty(value = "city")
    private String city;

    @JsonProperty(value = "country")
    private String country;

    @JsonProperty(value = "county")
    private String county;

    @JsonProperty(value = "postalCode")
    private String postalCode;
}
