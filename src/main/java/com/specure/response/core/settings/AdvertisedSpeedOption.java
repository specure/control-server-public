package com.specure.response.core.settings;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AdvertisedSpeedOption {
    private boolean enabled;
    private int maxSpeedDownKbps;
    private int maxSpeedUpKbps;
    private int minSpeedDownKbps;
    private int minSpeedUpKbps;
    private String name;
    private double uid;
}
