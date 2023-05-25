package com.specure.response.sah;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Builder
@Data
public class MeasurementServerDescriptionResponse {
    private String city;
    private String email;
    private String company;
    private Date expiration;
    private String ipAddress;
    private String comment;
}
