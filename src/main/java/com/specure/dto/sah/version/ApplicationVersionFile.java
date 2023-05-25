package com.specure.dto.sah.version;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ApplicationVersionFile {

    private final ApplicationVersionFileBlock dev;

    private final ApplicationVersionFileBlock beta;

    private final ApplicationVersionFileBlock prod;


    @Builder
    @Getter
    public static class ApplicationVersionFileBlock {

        @JsonProperty(value = "backend")
        private final String backendVersion;

        @JsonProperty(value = "regulatory")
        private final String regulatoryVersion;

        @JsonProperty(value = "public")
        private final String publicVersion;

        @JsonProperty(value = "public_bahrain")
        private final String publicBahrainVersion;

        @JsonProperty(value = "ios")
        private final String iosVersion;

        @JsonProperty(value = "android")
        private final String androidVersion;

        @JsonProperty(value = "flutter")
        private final String flutterVersion;
    }
}
