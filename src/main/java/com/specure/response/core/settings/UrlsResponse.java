package com.specure.response.core.settings;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UrlsResponse {
    private String controlIpv4Only;
    private String controlIpv6Only;
    private String openDataPrefix;
    private String statistics;
    private String urlIpv4Check;
    private String urlIpv6Check;
    private String urlShare;
    private String urlMapServer;
}
