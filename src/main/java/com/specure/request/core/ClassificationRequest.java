package com.specure.request.core;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClassificationRequest {

    @ApiModelProperty(value = "Amount of classification items supported by client", example = "5")
    private final int count;

}
