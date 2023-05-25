package com.specure.response.core.measurement.result.web.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class MeasurementTestResultForWeb {
    private long geo_lat;
    private long geo_long;
    private String location;
    private List<MeasurementDataResult> measurement;
    private List<NetworkDataResult> net;
    int network_type;
    private String open_test_uuid;
    private String open_uuid;
    private String share_subject;
    private String share_text;
    private long time;
    private String time_string;
    private String timezone;
}
