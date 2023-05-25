package com.specure.request.mobile;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.specure.request.core.CapabilitiesRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@Builder
public class MobileQosMeasurementsRequest {
    @NotNull
    @ApiModelProperty(notes = "Uuid of the test", example = "c373f294-f332-4f1a-999e-a87a12523f4b")
    @JsonProperty("test_uuid")
    private UUID testUuid;

    @ApiModelProperty(notes = "Language code of the client language", example = "en")
    @JsonProperty("language")
    private String language;

    @ApiModelProperty(notes = "Capabilities")
    @JsonProperty("capabilities")
    private CapabilitiesRequest capabilities;
}
