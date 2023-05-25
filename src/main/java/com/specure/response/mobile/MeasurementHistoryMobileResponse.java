package com.specure.response.mobile;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.domain.Page;

import java.util.List;

@Builder
@Getter
@ToString
public class MeasurementHistoryMobileResponse {
    private final Page<BasicTestHistoryMobileResponse> history;
    private final List<String> error;
}
