package com.specure.model.elastic.neutrality;

import com.specure.common.enums.NetNeutralityTestType;
import com.specure.common.enums.Platform;
import com.specure.common.model.elastic.MobileFields;
import com.specure.enums.NetNeutralityStatus;
import com.specure.response.sah.RadioSignalResponse;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.GeoPointField;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

import java.util.List;

@Document(indexName = "net_neutrality", createIndex = false)
@RequiredArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class NetNeutralityResult implements MobileFields {

    @Id
    private final String uuid;

    private final String openTestUuid;

    private final String clientUuid;

    private final NetNeutralityTestType type;

    private final Long durationNs;

    private final NetNeutralityStatus testStatus;

    private final String measurementDate;

    private final String model;

    private final String device;

    private final String product;

    private final String networkChannelNumber;

    private final String networkType;

    private final Integer signalStrength;

    private final Platform platform;

    private final String simMccMnc;

    private final String simOperatorName;

    private final String simCountry;

    private final String networkMccMnc;

    private final String networkOperatorName;

    private final String networkCountry;

    private final Boolean networkIsRoaming;

    private final String city;

    private final String country;

    private final String county;

    private final String postalCode;

    private final String measurementServerName;

    private final String measurementServerCity;

    @GeoPointField
    private final GeoPoint location;

    private final String ispName;

    private final List<RadioSignalResponse> radioSignals;
}
