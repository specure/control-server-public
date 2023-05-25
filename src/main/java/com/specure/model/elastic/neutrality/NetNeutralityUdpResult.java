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
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class NetNeutralityUdpResult extends NetNeutralityResult {

    private final Long portNumber;
    private final Long actualNumberOfPacketsReceived;
    private final Long numberOfPacketsSent;
    private final Long expectedMinNumberOfPacketsReceived;

    public NetNeutralityUdpResult(String uuid,
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
                                  Long portNumber,
                                  Long actualNumberOfPacketsReceived,
                                  Long numberOfPacketsSent,
                                  Long expectedMinNumberOfPacketsReceived) {
        super(uuid,
                openTestUuid,
                clientUuid,
                NetNeutralityTestType.UDP,
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
        this.portNumber = portNumber;
        this.actualNumberOfPacketsReceived = actualNumberOfPacketsReceived;
        this.numberOfPacketsSent = numberOfPacketsSent;
        this.expectedMinNumberOfPacketsReceived = expectedMinNumberOfPacketsReceived;
    }
}
