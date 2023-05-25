package com.specure.response.mobile;


import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.domain.Page;

import java.util.List;

@Builder
@Getter
@ToString
@EqualsAndHashCode
public class MeasurementHistoryLoopUuidResponse {
    private final Page<BasicTestHistoryMobileResponseContainer> measurements;
    private final List<String> error;
}
