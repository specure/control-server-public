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
public class NetNeutralityWebResult extends NetNeutralityResult {

    private final String url;
    private final Long timeout;
    private final Long actualStatusCode;
    private final Long expectedStatusCode;
    private final boolean timeoutExceeded;

    public NetNeutralityWebResult(String uuid,
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
                                  String url,
                                  Long timeout,
                                  Long actualStatusCode,
                                  Long expectedStatusCode,
                                  boolean timeoutExceeded) {
        super(uuid,
                openTestUuid,
                clientUuid,
                NetNeutralityTestType.WEB,
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
        this.url = url;
        this.timeout = timeout;
        this.actualStatusCode = actualStatusCode;
        this.expectedStatusCode = expectedStatusCode;
        this.timeoutExceeded = timeoutExceeded;
    }
}
