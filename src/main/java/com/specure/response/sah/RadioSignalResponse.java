package com.specure.response.sah;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@EqualsAndHashCode
@ToString
public class RadioSignalResponse {

    private final Integer signal;
    private final String technology;
    private final Long timeNs;
}
