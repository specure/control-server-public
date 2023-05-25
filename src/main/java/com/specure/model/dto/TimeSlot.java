package com.specure.model.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TimeSlot {
    private Integer slot;
    private Integer testWait;
}
