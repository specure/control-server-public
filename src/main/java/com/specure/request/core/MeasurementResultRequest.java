package com.specure.request.core;

import com.specure.constant.ErrorMessage;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Builder
@Data
public class MeasurementResultRequest {
    private String language;
    private List<String> options;

    @NotNull(message = ErrorMessage.TEST_UUID_REQUIRED)
    private String testUuid;

    private String timezone;
}
