package com.specure.model.elastic.neutrality;

import com.specure.common.enums.NetNeutralityTestType;
import com.specure.common.enums.Platform;
import com.specure.enums.NetNeutralityStatus;
import com.specure.response.sah.RadioSignalResponse;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

import java.util.List;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class NetNeutralityDnsResult extends NetNeutralityResult {

    private final String target;
    private final String actualResolver;
    private final String expectedResolver;
    private final Long timeout;
    private final String actualDnsStatus;
    private final String expectedDnsStatus;
    private final String entryType;
    private final String actualDnsResultEntriesFound;
    private final String expectedDnsResultEntriesFound;
    private final boolean timeoutExceeded;

    private final String failReason;

    public NetNeutralityDnsResult(String uuid,
                                  String openTestUuid,
                                  String clientUuid,
                                  Long durationNs,
                                  NetNeutralityStatus testStatus,
                                  String measurementDate,
                                  String model,
                                  String device,
                                  String product,
                                  String networkChannelNumber,
                                  String networkType,
                                  Integer signalStrength,
                                  Platform platform,
                                  String simMccMnc,
                                  String simOperatorName,
                                  String simCountry,
                                  String networkMccMnc,
                                  String networkOperatorName,
                                  String networkCountry,
                                  Boolean networkIsRoaming,
                                  String city,
                                  String country,
                                  String county,
                                  String postalCode,
                                  String measurementServerName,
                                  String measurementServerCity,
                                  GeoPoint location,
                                  String ispName,
                                  List<RadioSignalResponse> radioSignals,
                                  String target,
                                  String actualResolver,
                                  String expectedResolver,
                                  Long timeout,
                                  String actualDnsStatus,
                                  String expectedDnsStatus,
                                  String entryType,
                                  String actualDnsResultEntriesFound,
                                  String expectedDnsResultEntriesFound,
                                  boolean timeoutExceeded,
                                  String failReason) {
        super(uuid,
                openTestUuid,
                clientUuid,
                NetNeutralityTestType.DNS,
                durationNs,
                testStatus,
                measurementDate,
                model,
                device,
                product,
                networkChannelNumber,
                networkType,
                signalStrength,
                platform,
                simMccMnc,
                simOperatorName,
                simCountry,
                networkMccMnc,
                networkOperatorName,
                networkCountry,
                networkIsRoaming,
                city,
                country,
                county,
                postalCode,
                measurementServerName,
                measurementServerCity,
                location,
                ispName,
                radioSignals);
        this.target = target;
        this.actualResolver = actualResolver;
        this.expectedResolver = expectedResolver;
        this.timeout = timeout;
        this.actualDnsStatus = actualDnsStatus;
        this.expectedDnsStatus = expectedDnsStatus;
        this.entryType = entryType;
        this.actualDnsResultEntriesFound = actualDnsResultEntriesFound;
        this.expectedDnsResultEntriesFound = expectedDnsResultEntriesFound;
        this.timeoutExceeded = timeoutExceeded;
        this.failReason = failReason;
    }
}
