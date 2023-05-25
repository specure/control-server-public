package com.specure.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class NationalTable {
    List<StatsByOperator> statsByOperator;
    Double averageUpload;
    Double averageDownload;
    Double averageLatency;
    Long allMeasurements;
}
