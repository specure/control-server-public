package com.specure.model.dto.internal;

import com.specure.common.enums.MeasurementServerType;
import com.specure.common.enums.Platform;
import com.specure.common.model.jpa.AdHocCampaign;
import com.specure.common.model.jpa.MeasurementServer;
import lombok.*;

@Builder
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class DataForMeasurementRegistration {
    private MeasurementServerType measurementServerType;
    private MeasurementServer measurementServer;
    private String port;
    private String deviceOrProbeId;
    private Boolean isOnNet;
    private String clientUuid;
    private String providerName;
    private AdHocCampaign adHocCampaign;
    private Boolean telephonyPermissionGranted;
    private Boolean locationPermissionGranted;
    private Boolean uuidPermissionGranted;
    private Double latitude;
    private Double longitude;
    private String appVersion;
    private Platform platform;
}
