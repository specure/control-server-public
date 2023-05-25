package com.specure.response.sah;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class MobileTestResponse {
    private String openTestUuid;
    private String measurementDate;
    private String packageName;
    private String operator;
    private String technology;
    private Integer download;
    private Integer upload;
    private Float ping;
    private Integer signal;
    private Float jitter;
    private Float packetLoss;
    private String probeId;
}
