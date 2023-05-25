package com.specure.dto.mobile;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.specure.service.mobile.Smoothable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.ToString;

@AllArgsConstructor
@Builder
@ToString
public class MobileGraph implements Smoothable {
    public Double bytesTotal;
    public Double timeElapsed;

    public MobileGraph(long bytesTotal, long timeElapsed) {
        this.bytesTotal = (double) bytesTotal;
        this.timeElapsed = (double) timeElapsed;
    }

    @JsonIgnore
    @Override
    public double getXValue() {
        return timeElapsed;
    }

    @JsonIgnore
    @Override
    public double getYValue() {
        return bytesTotal;
    }

    @JsonProperty("bytes_total")
    public long getBytesTotal() {
        return bytesTotal.longValue();
    }

    @JsonProperty("time_elapsed")
    public long getTimeElapsed() {
        return Math.round(timeElapsed / 1e6);
    }
}
