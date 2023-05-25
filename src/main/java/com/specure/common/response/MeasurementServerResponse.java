package com.specure.common.response;

import com.specure.common.model.jpa.Provider;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Builder
@Data
public class MeasurementServerResponse {
    private Long id;
    private String uuid;

    private String name;
    private String webAddress;
    private Provider provider;

    private String secretKey;

    private String city;
    private String email;

    private String company;
    private Date expiration;
    private String ipAddress;
    private String comment;

    private String countries;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Timestamp lastSuccessfulMeasurement;

    private MeasurementServerLocationResponse location;

    private Double distance;

    private List<MeasurementServerTypeDetailResponse> serverTypeDetails;

    private boolean isOnNet;

    private boolean dedicated;

    private Boolean ipV4Support;

    private Boolean ipV6Support;
}
