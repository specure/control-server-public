package com.specure.common.response.userexperience;

import com.specure.common.response.PackageResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class UserExperienceMetricsResponse {

    @JsonProperty("package")
    private PackageResponse aPackage;
    private List<UserExperienceParameter> parameters;
    private List<UserExperienceMetric> metrics;
}
