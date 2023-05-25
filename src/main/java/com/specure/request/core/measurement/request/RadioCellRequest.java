package com.specure.request.core.measurement.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.specure.common.enums.NetworkGroupName;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;


@Getter
@Builder
public class RadioCellRequest {

    @JsonProperty(value = "active")
    private final boolean active;

    @JsonProperty(value = "area_code")
    private final Integer areaCode;

    @JsonProperty(value = "location_id")
    private final Long locationId;

    @JsonProperty(value = "mcc")
    private final Long mcc;

    @JsonProperty(value = "mnc")
    private final Long mnc;

    @JsonProperty(value = "primary_scrambling_code")
    private final Integer primaryScramblingCode;

    @JsonProperty(value = "registered")
    private final boolean registered;

    @JsonProperty(value = "technology")
    private final NetworkGroupName technology;

    @JsonProperty(value = "uuid")
    private final UUID uuid;

    @JsonProperty(value = "channel_number")
    private final Integer channelNumber;
}
