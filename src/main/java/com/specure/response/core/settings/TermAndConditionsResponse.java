package com.specure.response.core.settings;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@EqualsAndHashCode
@ToString
public class TermAndConditionsResponse {

    private final Long version;

    private final String url;

    @JsonProperty(value = "ndt_url")
    private final String ndtUrl;
}
