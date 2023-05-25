package com.specure.common.model.elastic;

import com.specure.common.model.jpa.Provider;
import com.specure.common.model.jpa.RawProvider;
import com.specure.response.sah.RadioSignalResponse;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;
import org.springframework.data.elasticsearch.core.geo.GeoJsonPoint;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.List;
import java.util.Objects;

@Builder
@AllArgsConstructor
@Getter
@Setter
@Document(indexName = "basic_test_bh", createIndex = false)
@ToString
@EqualsAndHashCode
@Slf4j
public class BasicTest implements MobileFields {

    public static class Fields {
        public static final String COUNTRY = "country";
        public static final String ISP_NAME = "ispName";
        public static final String ISP_RAW_ID = "ispRawId";
        public static final String ISP_RAW_ID_KEYWORD = "ispRawId.keyword";
    }

    @Id
    private String openTestUuid;
    private String clientUuid;

    /**
     * We get this field value from MaxMind response provider name directly using request ip address,
     * or from digger service that search by asn using MaxMind response provider asn,
     * or can be "unknown" if nothing found.
     * This field is null for mobile measurement.
     * This field settled in /adminTestRequest, /webTestRequest, /testRequest
     */
    private String clientProvider;
    private String clientType;

    /**
     * We get this field value from saved {@link RawProvider} in case MaxMind response using request ip address was not empty,
     * This field settled in /mobile/result, /adminTestRequest, /webTestRequest, /testRequest
     */
    @Field(name = Fields.ISP_RAW_ID)
    private String ispRawId;

    /**
     * We get this field from mapped {@link Provider} to saved {@link RawProvider#getAlias()}
     */
    @Field(name = Fields.ISP_NAME)
    private String ispName;

    private Long asn;

    @Field(type = FieldType.Date)
    private String timestamp;
    @Field(type = FieldType.Date)
    private String measurementDate;
    /**
     * We get this field value from rawProvider alias, e.g. from Provider mapped to RawProvider
     * or from measurement.networkOperator(
     * /mobile/result  from MaxMind response provider name directly using request ip address,
     * or from digger service that search by asn using MaxMind response provider asn,
     * or can be "unknown" if nothing found.
     * /testRequest from Provider settled in AdHocCampaign, Probe.Package
     * /adminTestRequest, /webTestRequest is null)
     * or from measurement.networkOperatorName(
     * /mobile/result from request telephonyNetworkOperatorName field value
     * or from  SimOperator#name mapped by mcc-mnc
     * by request telephonyNetworkOperator field value
     * or "unknown"
     * )
     */
    private String operator;
    private Integer download;
    private Integer upload;
    private Float ping;
    private Float jitter;
    private Integer signal;
    private Float packetLoss;

    private String probeId;
    private String probePort;
    private String packageId;
    private String packageType;
    private String groupId;
    private String packageNameStamp;
    private String device;
    private String manufacturer;
    private String category;
    private String browserName;
    private String networkType;

    private Long siteId;
    private String siteName;
    private String siteAdvertisedId;

    private String adHocCampaign;

    private Long measurementServerId;
    private String measurementServerName;
    private String measurementServerCity;

    private String typeOfProbePort;

    private String serverType;
    private Integer graphHour;

    private Integer numThreads;
    private String platform;
    private String municipality;
    private Integer municipalityType;

    private String city;
    @Field(name = Fields.COUNTRY, type = FieldType.Keyword)
    private String country;
    private String county;
    private String ipAddress;
    private String postalCode;

    @ToString.Exclude
    private List<SimpleSpeedDetail> speedDetail;

    @ToString.Exclude
    private List<RadioSignalResponse> radioSignals;

    private String status;

    @GeoPointField
    private GeoPoint location;

    @GeoShapeField
    private GeoJsonPoint geo_shape_location;

    private Integer lte_rsrp;
    private Integer lte_rsrq;
    private Boolean dualSim;
    private String simMccMnc;
    private String simOperatorName;
    private Boolean networkIsRoaming;
    private String networkMccMnc;
    private String networkOperatorName;
    private String networkCountry;
    private String simCountry;
    private Integer simCount;
    private String wifiSsid;
    private String model;
    private String clientName;
    private String clientVersion;
    private String appVersion;
    private Integer testNumThreadsUl;
    private String tag;
    private String loopModeUuid;

    private String internetProtocol;
    private Boolean telephonyPermissionGranted;
    private Boolean locationPermissionGranted;
    private Boolean uuidPermissionGranted;

    public Timestamp getTimestamp() {
        if (Objects.isNull(timestamp)) {
            return null;
        }
        try {
            long millis = Long.parseLong(timestamp);
            return new Timestamp(millis);
        } catch (NumberFormatException e) {
            try {
                return new Timestamp(DateUtils.parseDateStrictly(this.timestamp,
                        "yyyy-MM-dd HH:mm:ss.SSS", "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").getTime());
            } catch (ParseException x) {
                log.error("ERROR: Timestamp not parsed for " + openTestUuid);
                return null;
            }
        }
    }
}
