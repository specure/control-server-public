package com.specure.response.mobile;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.specure.dto.mobile.SpeedCurve;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class MobileGraphResponse {
    @JsonProperty(value = "speed_curve")
    private final SpeedCurve speedCurve;
}
