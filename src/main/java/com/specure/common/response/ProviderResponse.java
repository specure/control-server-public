package com.specure.common.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ProviderResponse {
    private Long id;
    private String country;
    private String name;
    private boolean ispActive;
    private boolean mnoActive;
}
