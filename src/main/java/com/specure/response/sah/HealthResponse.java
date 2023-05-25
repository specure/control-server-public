package com.specure.response.sah;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HealthResponse {
    long lastDownPingSignal;
}
