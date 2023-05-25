package com.specure.response.core.measurement.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.specure.common.enums.Direction;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SpeedDetailResponse {
    private Direction direction;
    private Integer thread;
    private Long time;
    private Integer bytes;
}
