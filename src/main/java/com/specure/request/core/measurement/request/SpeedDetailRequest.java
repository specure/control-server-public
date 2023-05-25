package com.specure.request.core.measurement.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.specure.common.enums.Direction;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Builder
@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SpeedDetailRequest {
    private Direction direction;
    private Integer thread;
    private Long time;
    private Integer bytes;
}
