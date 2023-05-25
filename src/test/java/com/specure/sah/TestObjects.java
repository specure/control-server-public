package com.specure.sah;

import com.specure.common.enums.NetNeutralityTestType;
import com.specure.common.model.jpa.*;
import com.specure.common.model.jpa.neutrality.NetNeutralityDnsSetting;
import com.specure.common.model.jpa.neutrality.NetNeutralityUdpSetting;
import com.specure.common.model.jpa.neutrality.NetNeutralityWebSetting;
import com.specure.common.response.LocationResponse;
import com.specure.common.response.neutrality.crud.NetNeutralityDnsSettingResponse;
import com.specure.common.response.neutrality.crud.NetNeutralityUdpSettingResponse;
import com.specure.common.response.neutrality.crud.NetNeutralityWebSettingResponse;
import com.specure.enums.NetNeutralityStatus;
import com.specure.model.elastic.neutrality.NetNeutralityDnsResult;
import com.specure.model.elastic.neutrality.NetNeutralityUdpResult;
import com.specure.model.elastic.neutrality.NetNeutralityWebResult;
import com.specure.request.core.measurement.request.RadioSignalRequest;
import com.specure.request.mobile.LocationRequest;
import com.specure.request.sah.neutrality.result.*;
import com.specure.response.sah.RadioSignalResponse;
import com.specure.response.sah.neutrality.RadioInfoRequest;
import com.specure.response.sah.neutrality.parameters.*;
import com.specure.response.sah.neutrality.result.NetNeutralityDnsResultResponse;
import com.specure.response.sah.neutrality.result.NetNeutralityOverallTestResultResponse;
import com.specure.response.sah.neutrality.result.NetNeutralityUdpResultResponse;
import com.specure.response.sah.neutrality.result.NetNeutralityWebResultResponse;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

import java.util.List;
import java.util.Map;


@UtilityClass
public class TestObjects {

    public Pageable getDefaultPageable() {
        return PageRequest.of(TestConstants.DEFAULT_PAGE, TestConstants.DEFAULT_PAGE_SIZE);
    }

    public static Provider defaultProvider() {
        return Provider.builder()
                .id(TestConstants.DEFAULT_PROVIDER_ID)
                .name(TestConstants.DEFAULT_PROVIDER_NAME)
                .country(TestConstants.DEFAULT_COUNTRY)
                .build();
    }

    public static RawProvider defaultRawProvider() {
        return RawProvider.builder()
                .id(TestConstants.DEFAULT_PROVIDER_ID)
                .rawName(TestConstants.DEFAULT_PROVIDER_NAME)
                .country(TestConstants.DEFAULT_COUNTRY)
                .provider(Provider.builder()
                        .name(TestConstants.DEFAULT_PROVIDER_ISP_NAME)
                        .build())
                .asn(TestConstants.DEFAULT_PROVIDER_ASN_STRING)
                .mccMnc(TestConstants.DEFAULT_MCC_MNC)
                .build();
    }

    public static NetNeutralityDnsSettingResponse netNeutralityDnsSettingResponse() {
        return new NetNeutralityDnsSettingResponse(TestConstants.NetNeutrality.DNS_ID,
                TestConstants.NetNeutrality.DNS_IS_ACTIVE,
                TestConstants.NetNeutrality.DNS_TARGET,
                TestConstants.NetNeutrality.DNS_TIMEOUT,
                TestConstants.NetNeutrality.DNS_ENTRY_TYPE,
                TestConstants.NetNeutrality.DNS_EXPECTED_RESOLVER,
                TestConstants.NetNeutrality.DNS_EXPECTED_DNS_STATUS.name(),
                TestConstants.NetNeutrality.DNS_EXPECTED_DNS_ENTRIES);
    }

    public static NetNeutralityUdpSettingResponse netNeutralityUdpSettingResponse() {
        return new NetNeutralityUdpSettingResponse(TestConstants.NetNeutrality.UDP_ID,
                TestConstants.NetNeutrality.UDP_IS_ACTIVE,
                TestConstants.NetNeutrality.UDP_TIMEOUT,
                TestConstants.NetNeutrality.UDP_PORT_NUMBER,
                TestConstants.NetNeutrality.UDP_NUMBER_OF_PACKETS_SENT,
                TestConstants.NetNeutrality.UDP_MIN_NUMBER_OF_PACKETS_RECEIVED);
    }

    public static NetNeutralityWebSettingResponse netNeutralityWebSettingResponse() {
        return new NetNeutralityWebSettingResponse(TestConstants.NetNeutrality.WEB_ID,
                TestConstants.NetNeutrality.WEB_IS_ACTIVE,
                TestConstants.NetNeutrality.WEB_TARGET,
                TestConstants.NetNeutrality.WEB_TIMEOUT,
                TestConstants.NetNeutrality.WEB_EXPECTED_STATUS_CODE);
    }

    public static NetNeutralityDnsTestParametersResponse netNeutralityDnsTestParametersResponse() {
        return new NetNeutralityDnsTestParametersResponse(TestConstants.NetNeutrality.DNS_ID,
                TestConstants.NetNeutrality.DNS_TARGET,
                TestConstants.NetNeutrality.DNS_TIMEOUT,
                TestConstants.NetNeutrality.DNS_ENTRY_TYPE,
                TestConstants.NetNeutrality.DNS_ACTUAL_RESOLVER);
    }

    public static NetNeutralityUdpTestParametersResponse netNeutralityUdpTestParametersResponse() {
        return new NetNeutralityUdpTestParametersResponse(TestConstants.NetNeutrality.UDP_ID,
                TestConstants.NetNeutrality.UDP_TIMEOUT,
                TestConstants.NetNeutrality.UDP_PORT_NUMBER,
                TestConstants.NetNeutrality.UDP_NUMBER_OF_PACKETS_SENT,
                TestConstants.NetNeutrality.UDP_MIN_NUMBER_OF_PACKETS_RECEIVED);
    }

    public static NetNeutralityWebTestParametersResponse netNeutralityWebTestParametersResponse() {
        return new NetNeutralityWebTestParametersResponse(TestConstants.NetNeutrality.WEB_ID,
                TestConstants.NetNeutrality.WEB_TARGET,
                TestConstants.NetNeutrality.WEB_TIMEOUT);
    }

    public static NetNeutralityOverallTestResultResponse neutralityOverallQosTestResult() {
        return NetNeutralityOverallTestResultResponse.builder()
                .build();
    }

    public static NetNeutralityDnsSetting netNeutralityDnsSetting() {
        NetNeutralityDnsSetting netNeutralityDnsSetting = new NetNeutralityDnsSetting();
        netNeutralityDnsSetting.setType(NetNeutralityTestType.DNS);
        netNeutralityDnsSetting.setId(TestConstants.NetNeutrality.DNS_ID);
        netNeutralityDnsSetting.setTarget(TestConstants.NetNeutrality.DNS_TARGET);
        netNeutralityDnsSetting.setTimeout(TestConstants.NetNeutrality.DNS_TIMEOUT);
        netNeutralityDnsSetting.setEntryType(TestConstants.NetNeutrality.DNS_ENTRY_TYPE);
        netNeutralityDnsSetting.setResolver(TestConstants.NetNeutrality.DNS_EXPECTED_RESOLVER);
        netNeutralityDnsSetting.setExpectedDnsStatus(TestConstants.NetNeutrality.DNS_EXPECTED_DNS_STATUS.name());
        netNeutralityDnsSetting.setExpectedDnsEntries(TestConstants.NetNeutrality.DNS_EXPECTED_DNS_ENTRIES);
        netNeutralityDnsSetting.setActive(TestConstants.NetNeutrality.DNS_IS_ACTIVE);
        return netNeutralityDnsSetting;
    }

    public static NetNeutralityWebSetting netNeutralityWebSetting() {
        NetNeutralityWebSetting netNeutralityWebSetting = new NetNeutralityWebSetting();
        netNeutralityWebSetting.setId(TestConstants.NetNeutrality.WEB_ID);
        netNeutralityWebSetting.setType(NetNeutralityTestType.WEB);
        netNeutralityWebSetting.setTarget(TestConstants.NetNeutrality.WEB_TARGET);
        netNeutralityWebSetting.setTimeout(TestConstants.NetNeutrality.WEB_TIMEOUT);
        netNeutralityWebSetting.setExpectedStatusCode(TestConstants.NetNeutrality.WEB_EXPECTED_STATUS_CODE);
        netNeutralityWebSetting.setActive(TestConstants.NetNeutrality.WEB_IS_ACTIVE);
        return netNeutralityWebSetting;
    }

    public static NetNeutralityWebSetting netNeutralityWebSetting(Long expectedStatusCode) {
        NetNeutralityWebSetting netNeutralityWebSetting = new NetNeutralityWebSetting();
        netNeutralityWebSetting.setId(TestConstants.NetNeutrality.WEB_ID);
        netNeutralityWebSetting.setType(NetNeutralityTestType.WEB);
        netNeutralityWebSetting.setTarget(TestConstants.NetNeutrality.WEB_TARGET);
        netNeutralityWebSetting.setTimeout(TestConstants.NetNeutrality.WEB_TIMEOUT);
        netNeutralityWebSetting.setExpectedStatusCode(expectedStatusCode);
        netNeutralityWebSetting.setActive(TestConstants.NetNeutrality.WEB_IS_ACTIVE);
        return netNeutralityWebSetting;
    }

    public static NetNeutralityUdpSetting netNeutralityUdpSetting() {
        NetNeutralityUdpSetting netNeutralityUdpSetting = new NetNeutralityUdpSetting();
        netNeutralityUdpSetting.setId(TestConstants.NetNeutrality.UDP_ID);
        netNeutralityUdpSetting.setType(NetNeutralityTestType.UDP);
        netNeutralityUdpSetting.setTimeout(TestConstants.NetNeutrality.UDP_TIMEOUT);
        netNeutralityUdpSetting.setPortNumber(TestConstants.NetNeutrality.UDP_PORT_NUMBER);
        netNeutralityUdpSetting.setNumberOfPacketsSent(TestConstants.NetNeutrality.UDP_NUMBER_OF_PACKETS_SENT);
        netNeutralityUdpSetting.setMinNumberOfPacketsReceived(TestConstants.NetNeutrality.UDP_MIN_NUMBER_OF_PACKETS_RECEIVED);
        netNeutralityUdpSetting.setActive(TestConstants.NetNeutrality.UDP_IS_ACTIVE);
        return netNeutralityUdpSetting;
    }

    public static NetNeutralityUdpSetting netNeutralityUdpSetting(Long mindNumberOfPacketsSent) {
        NetNeutralityUdpSetting netNeutralityUdpSetting = new NetNeutralityUdpSetting();
        netNeutralityUdpSetting.setId(TestConstants.NetNeutrality.UDP_ID);
        netNeutralityUdpSetting.setType(NetNeutralityTestType.UDP);
        netNeutralityUdpSetting.setTimeout(TestConstants.NetNeutrality.UDP_TIMEOUT);
        netNeutralityUdpSetting.setPortNumber(TestConstants.NetNeutrality.UDP_PORT_NUMBER);
        netNeutralityUdpSetting.setNumberOfPacketsSent(TestConstants.NetNeutrality.UDP_NUMBER_OF_PACKETS_SENT);
        netNeutralityUdpSetting.setMinNumberOfPacketsReceived(mindNumberOfPacketsSent);
        netNeutralityUdpSetting.setActive(TestConstants.NetNeutrality.UDP_IS_ACTIVE);
        return netNeutralityUdpSetting;
    }

    public static NetNeutralityDnsResultResponse netNeutralityDnsResultResponse() {
        return new NetNeutralityDnsResultResponse(TestConstants.NetNeutrality.DNS_UUID,
                TestConstants.DEFAULT_OPEN_TEST_UUID_STRING,
                TestConstants.DEFAULT_CLIENT_UUID_STRING,
                TestConstants.NetNeutrality.DNS_DURATION_NS,
                TestConstants.NetNeutrality.DNS_SUCCESS,
                TestConstants.NetNeutrality.DNS_MEASUREMENT_DATE,
                TestConstants.DEFAULT_NETWORK_TYPE_LTE.getCategory(),
                TestConstants.DEFAULT_NETWORK_TYPE_LTE.getName(),
                TestConstants.DEFAULT_DEVICE,
                TestObjects.locationResponse(),
                TestConstants.DEFAULT_MEASUREMENT_SERVER_NAME,
                TestConstants.DEFAULT_MEASUREMENT_SERVER_CITY,
                TestConstants.DEFAULT_PROVIDER_ISP_NAME,
                List.of(TestObjects.radioSignalResponse()),
                TestConstants.NetNeutrality.DNS_TARGET,
                TestConstants.NetNeutrality.DNS_ACTUAL_RESOLVER,
                TestConstants.NetNeutrality.DNS_EXPECTED_RESOLVER,
                TestConstants.NetNeutrality.DNS_TIMEOUT,
                TestConstants.NetNeutrality.DNS_ACTUAL_DNS_STATUS,
                TestConstants.NetNeutrality.DNS_EXPECTED_DNS_STATUS.name(),
                TestConstants.NetNeutrality.DNS_ENTRY_TYPE,
                TestConstants.NetNeutrality.DNS_ACTUAL_DNS_RESULT_ENTRIES,
                TestConstants.NetNeutrality.DNS_EXPECTED_DNS_ENTRIES,
                TestConstants.NetNeutrality.DNS_TIMEOUT_EXCEEDED,
                null);
    }

    public static NetNeutralityUdpResultResponse netNeutralityUdpTestResultResponse() {
        return new NetNeutralityUdpResultResponse(TestConstants.NetNeutrality.UDP_UUID,
                TestConstants.DEFAULT_OPEN_TEST_UUID_STRING,
                TestConstants.DEFAULT_CLIENT_UUID_STRING,
                TestConstants.NetNeutrality.UDP_DURATION_NS,
                TestConstants.NetNeutrality.UDP_SUCCESS,
                TestConstants.NetNeutrality.UDP_MEASUREMENT_DATE,
                TestConstants.DEFAULT_NETWORK_TYPE_LTE.getCategory(),
                TestConstants.DEFAULT_NETWORK_TYPE_LTE.getName(),
                TestConstants.DEFAULT_DEVICE,
                TestObjects.locationResponse(),
                TestConstants.DEFAULT_MEASUREMENT_SERVER_NAME,
                TestConstants.DEFAULT_MEASUREMENT_SERVER_CITY,
                TestConstants.DEFAULT_PROVIDER_ISP_NAME,
                List.of(TestObjects.radioSignalResponse()),
                TestConstants.NetNeutrality.UDP_PORT_NUMBER,
                TestConstants.NetNeutrality.UDP_NUMBER_OF_PACKETS_SENT,
                TestConstants.NetNeutrality.UDP_ACTUAL_NUMBER_OF_PACKETS_RECEIVED,
                TestConstants.NetNeutrality.UDP_EXPECTED_NUMBER_OF_PACKETS_RECEIVED);
    }

    public static NetNeutralityWebResultResponse netNeutralityWebResultResponse() {
        return new NetNeutralityWebResultResponse(TestConstants.NetNeutrality.WEB_UUID,
                TestConstants.DEFAULT_OPEN_TEST_UUID_STRING,
                TestConstants.DEFAULT_CLIENT_UUID_STRING,
                TestConstants.NetNeutrality.WEB_DURATION_NS,
                TestConstants.NetNeutrality.WEB_SUCCESS,
                TestConstants.NetNeutrality.WEB_MEASUREMENT_DATE,
                TestConstants.DEFAULT_NETWORK_TYPE_LTE.getCategory(),
                TestConstants.DEFAULT_NETWORK_TYPE_LTE.getName(),
                TestConstants.DEFAULT_DEVICE,
                TestObjects.locationResponse(),
                TestConstants.DEFAULT_MEASUREMENT_SERVER_NAME,
                TestConstants.DEFAULT_MEASUREMENT_SERVER_CITY,
                TestConstants.DEFAULT_PROVIDER_ISP_NAME,
                List.of(TestObjects.radioSignalResponse()),
                TestConstants.NetNeutrality.WEB_TARGET,
                TestConstants.NetNeutrality.WEB_TIMEOUT,
                TestConstants.NetNeutrality.WEB_ACTUAL_STATUS_CODE,
                TestConstants.NetNeutrality.WEB_EXPECTED_STATUS_CODE,
                TestConstants.NetNeutrality.WEB_TIMEOUT_EXCEEDED);
    }

    public static LocationResponse locationResponse() {
        return LocationResponse.builder()
                .latitude(TestConstants.DEFAULT_LATITUDE)
                .longitude(TestConstants.DEFAULT_LONGITUDE)
                .city(TestConstants.DEFAULT_CITY)
                .country(TestConstants.DEFAULT_COUNTRY)
                .county(TestConstants.DEFAULT_COUNTY)
                .postalCode(TestConstants.DEFAULT_POSTAL_CODE)
                .build();
    }

    public static NetNeutralityOverallTestResultResponse netNeutralityOverallTestResultResponse() {
        var netNeutralityResultResponses = List.of(TestObjects.netNeutralityDnsResultResponse(),
                TestObjects.netNeutralityUdpTestResultResponse(),
                TestObjects.netNeutralityWebResultResponse());
        return NetNeutralityOverallTestResultResponse.builder()
                .netNeutralityResultResponse(netNeutralityResultResponses)
                .build();
    }

    public static NetNeutralityMeasurementResultRequest netNeutralityMeasurementResultRequest() {
        var testResults = List.of(TestObjects.netNeutralityDnsTestResultRequest(),
                TestObjects.netNeutralityUdpTestResultRequest(),
                TestObjects.netNeutralityWebTestResultRequest());
        return new NetNeutralityMeasurementResultRequest(testResults,
                TestConstants.DEFAULT_MODEL,
                TestConstants.DEFAULT_DEVICE,
                TestObjects.locationRequest(),
                TestConstants.DEFAULT_PRODUCT,
                TestConstants.DEFAULT_NETWORK_CHANNEL_NUMBER,
                TestConstants.DEFAULT_NETWORK_TYPE,
                TestConstants.DEFAULT_SIGNAL_STRENGTH,
                TestConstants.DEFAULT_IP,
                TestConstants.DEFAULT_TEST_PLATFORM,
                TestConstants.DEFAULT_SIM_MCC_MNC,
                TestConstants.DEFAULT_SIM_OPERATOR_NAME,
                TestConstants.DEFAULT_SIM_COUNTRY,
                TestConstants.DEFAULT_NETWORK_MCC_MNC,
                TestConstants.DEFAULT_NETWORK_OPERATOR_NAME,
                TestConstants.DEFAULT_NETWORK_COUNTRY,
                TestConstants.DEFAULT_NETWORK_IS_ROAMING,
                TestConstants.DEFAULT_MEASUREMENT_SERVER_NAME,
                TestConstants.DEFAULT_MEASUREMENT_SERVER_CITY,
                TestObjects.radioInfo());
    }

    public static LocationRequest locationRequest() {
        return LocationRequest.builder()
                .latitude(TestConstants.DEFAULT_LATITUDE)
                .longitude(TestConstants.DEFAULT_LONGITUDE)
                .city(TestConstants.DEFAULT_CITY)
                .country(TestConstants.DEFAULT_COUNTRY)
                .county(TestConstants.DEFAULT_COUNTY)
                .postalCode(TestConstants.DEFAULT_POSTAL_CODE)
                .build();
    }

    public static NetNeutralityMeasurementResultRequest netNeutralityMeasurementResultRequest(List<NetNeutralityTestResultRequest> testResults) {
        return new NetNeutralityMeasurementResultRequest(testResults,
                TestConstants.DEFAULT_MODEL,
                TestConstants.DEFAULT_DEVICE,
                TestObjects.locationRequest(),
                TestConstants.DEFAULT_PRODUCT,
                TestConstants.DEFAULT_NETWORK_CHANNEL_NUMBER,
                TestConstants.DEFAULT_NETWORK_TYPE_LTE_ID,
                TestConstants.DEFAULT_SIGNAL_STRENGTH,
                TestConstants.DEFAULT_IP,
                TestConstants.DEFAULT_TEST_PLATFORM,
                TestConstants.DEFAULT_SIM_MCC_MNC,
                TestConstants.DEFAULT_SIM_OPERATOR_NAME,
                TestConstants.DEFAULT_SIM_COUNTRY,
                TestConstants.DEFAULT_NETWORK_MCC_MNC,
                TestConstants.DEFAULT_NETWORK_OPERATOR_NAME,
                TestConstants.DEFAULT_NETWORK_COUNTRY,
                TestConstants.DEFAULT_NETWORK_IS_ROAMING,
                TestConstants.DEFAULT_MEASUREMENT_SERVER_NAME,
                TestConstants.DEFAULT_MEASUREMENT_SERVER_CITY,
                TestObjects.radioInfo());
    }

    public static RadioInfoRequest radioInfo() {
        return RadioInfoRequest.builder()
                .signals(List.of(TestObjects.radioSignalRequest()))
                .build();
    }

    public static RadioSignalRequest radioSignalRequest() {
        return RadioSignalRequest.builder()
                .signalStrength(TestConstants.DEFAULT_SIGNAL_STRENGTH)
                .networkTypeId(TestConstants.DEFAULT_NETWORK_TYPE_LTE_ID)
                .timeNs(TestConstants.DEFAULT_TIME)
                .build();
    }

    public static NetNeutralityWebTestResultRequest netNeutralityWebTestResultRequest() {
        return new NetNeutralityWebTestResultRequest(TestConstants.NetNeutrality.WEB_ID,
                TestConstants.DEFAULT_OPEN_TEST_UUID_STRING,
                TestConstants.DEFAULT_CLIENT_UUID_STRING,
                TestConstants.NetNeutrality.WEB_DURATION_NS,
                TestConstants.NetNeutrality.WEB_EXPECTED_STATUS_CODE,
                TestConstants.NetNeutrality.WEB_TIMEOUT_EXCEEDED);
    }

    public static NetNeutralityWebTestResultRequest netNeutralityWebTestResultRequest(Long actualStatusCode,
                                                                                      boolean isTimeoutExceeded) {
        return new NetNeutralityWebTestResultRequest(TestConstants.NetNeutrality.WEB_ID,
                TestConstants.DEFAULT_OPEN_TEST_UUID_STRING,
                TestConstants.DEFAULT_CLIENT_UUID_STRING,
                TestConstants.NetNeutrality.WEB_DURATION_NS,
                actualStatusCode,
                isTimeoutExceeded);
    }

    public static NetNeutralityUdpTestResultRequest netNeutralityUdpTestResultRequest() {
        return new NetNeutralityUdpTestResultRequest(TestConstants.NetNeutrality.UDP_ID,
                TestConstants.DEFAULT_OPEN_TEST_UUID_STRING,
                TestConstants.DEFAULT_CLIENT_UUID_STRING,
                TestConstants.NetNeutrality.UDP_DURATION_NS,
                TestConstants.NetNeutrality.UDP_ACTUAL_NUMBER_OF_PACKETS_RECEIVED);
    }

    public static NetNeutralityUdpTestResultRequest netNeutralityUdpTestResultRequest(Long numberOfPacketsReceived) {
        return new NetNeutralityUdpTestResultRequest(TestConstants.NetNeutrality.UDP_ID,
                TestConstants.DEFAULT_OPEN_TEST_UUID_STRING,
                TestConstants.DEFAULT_CLIENT_UUID_STRING,
                TestConstants.NetNeutrality.UDP_DURATION_NS,
                numberOfPacketsReceived);
    }

    public static NetNeutralityDnsTestResultRequest netNeutralityDnsTestResultRequest() {
        return new NetNeutralityDnsTestResultRequest(TestConstants.NetNeutrality.DNS_ID,
                TestConstants.DEFAULT_OPEN_TEST_UUID_STRING,
                TestConstants.DEFAULT_CLIENT_UUID_STRING,
                TestConstants.NetNeutrality.DNS_DURATION_NS,
                TestConstants.NetNeutrality.DNS_TIMEOUT_EXCEEDED,
                TestConstants.NetNeutrality.DNS_ACTUAL_DNS_STATUS,
                TestConstants.NetNeutrality.DNS_ACTUAL_DNS_RESULT_ENTRIES_LIST,
                TestConstants.NetNeutrality.DNS_ACTUAL_RESOLVER);
    }

    public static NetNeutralityDnsTestResultRequestBuilder netNeutralityDnsTestResultRequestBuilder() {
        return new NetNeutralityDnsTestResultRequestBuilder();
    }

    public static class NetNeutralityDnsTestResultRequestBuilder {

        private String resolver = TestConstants.NetNeutrality.DNS_ACTUAL_RESOLVER;
        private List<String> actualDnsEntriesList = TestConstants.NetNeutrality.DNS_ACTUAL_DNS_RESULT_ENTRIES_LIST;
        private String dnsStatus = TestConstants.NetNeutrality.DNS_ACTUAL_DNS_STATUS;
        private boolean isTimeoutExceeded = TestConstants.NetNeutrality.DNS_TIMEOUT_EXCEEDED;

        public NetNeutralityDnsTestResultRequestBuilder withResolver(String resolver) {
            this.resolver = resolver;
            return this;
        }

        public NetNeutralityDnsTestResultRequestBuilder withActualDnsEntriesList(List<String> actualDnsEntriesList) {
            this.actualDnsEntriesList = actualDnsEntriesList;
            return this;
        }

        public NetNeutralityDnsTestResultRequestBuilder withDnsStatus(String dnsStatus) {
            this.dnsStatus = dnsStatus;
            return this;
        }

        public NetNeutralityDnsTestResultRequestBuilder withTimeoutExceeded(boolean isTimeoutExceeded) {
            this.isTimeoutExceeded = isTimeoutExceeded;
            return this;

        }

        public NetNeutralityDnsTestResultRequest build() {
            return new NetNeutralityDnsTestResultRequest(TestConstants.NetNeutrality.DNS_ID,
                    TestConstants.DEFAULT_OPEN_TEST_UUID_STRING,
                    TestConstants.DEFAULT_CLIENT_UUID_STRING,
                    TestConstants.NetNeutrality.DNS_DURATION_NS,
                    this.isTimeoutExceeded,
                    this.dnsStatus,
                    this.actualDnsEntriesList,
                    this.resolver);
        }
    }

    public static NetNeutralityDnsResult netNeutralityDnsResult() {
        return new NetNeutralityDnsResult(TestConstants.NetNeutrality.DNS_UUID,
                TestConstants.DEFAULT_OPEN_TEST_UUID_STRING,
                TestConstants.DEFAULT_CLIENT_UUID_STRING,
                TestConstants.NetNeutrality.DNS_DURATION_NS,
                TestConstants.NetNeutrality.DNS_SUCCESS,
                TestConstants.NetNeutrality.DNS_MEASUREMENT_DATE,
                TestConstants.DEFAULT_MODEL,
                TestConstants.DEFAULT_DEVICE,
                TestConstants.DEFAULT_PRODUCT,
                TestConstants.DEFAULT_NETWORK_CHANNEL_NUMBER,
                TestConstants.DEFAULT_NETWORK_TYPE_LTE.name(),
                TestConstants.DEFAULT_SIGNAL_STRENGTH,
                TestConstants.DEFAULT_TEST_PLATFORM,
                TestConstants.DEFAULT_SIM_MCC_MNC,
                TestConstants.DEFAULT_SIM_OPERATOR_NAME,
                TestConstants.DEFAULT_SIM_COUNTRY,
                TestConstants.DEFAULT_NETWORK_MCC_MNC,
                TestConstants.DEFAULT_NETWORK_OPERATOR_NAME,
                TestConstants.DEFAULT_NETWORK_COUNTRY,
                TestConstants.DEFAULT_NETWORK_IS_ROAMING,
                TestConstants.DEFAULT_CITY,
                TestConstants.DEFAULT_COUNTRY,
                TestConstants.DEFAULT_COUNTY,
                TestConstants.DEFAULT_POSTAL_CODE,
                TestConstants.DEFAULT_MEASUREMENT_SERVER_NAME,
                TestConstants.DEFAULT_MEASUREMENT_SERVER_CITY,
                TestObjects.location(),
                TestConstants.DEFAULT_PROVIDER_ISP_NAME,
                List.of(TestObjects.radioSignalResponse()),
                TestConstants.NetNeutrality.DNS_TARGET,
                TestConstants.NetNeutrality.DNS_ACTUAL_RESOLVER,
                TestConstants.NetNeutrality.DNS_EXPECTED_RESOLVER,
                TestConstants.NetNeutrality.DNS_TIMEOUT,
                TestConstants.NetNeutrality.DNS_ACTUAL_DNS_STATUS,
                TestConstants.NetNeutrality.DNS_EXPECTED_DNS_STATUS.name(),
                TestConstants.NetNeutrality.DNS_ENTRY_TYPE,
                TestConstants.NetNeutrality.DNS_ACTUAL_DNS_RESULT_ENTRIES,
                TestConstants.NetNeutrality.DNS_EXPECTED_DNS_ENTRIES,
                TestConstants.NetNeutrality.DNS_TIMEOUT_EXCEEDED,
                null);
    }

    public static class NetNeutralityDnsResultBuilder {
        private NetNeutralityStatus testStatus = TestConstants.NetNeutrality.DNS_SUCCESS;
        private String actualDnsResultEntries = TestConstants.NetNeutrality.DNS_ACTUAL_DNS_RESULT_ENTRIES;
        private String expectedDnsResultEntries = TestConstants.NetNeutrality.DNS_ACTUAL_DNS_RESULT_ENTRIES;
        private boolean isTimeoutExceeded = TestConstants.NetNeutrality.DNS_TIMEOUT_EXCEEDED;
        private String actualDnsStatus = TestConstants.NetNeutrality.DNS_ACTUAL_DNS_STATUS;
        private String expectedDnsStatus = TestConstants.NetNeutrality.DNS_EXPECTED_DNS_STATUS.name();
        private String failedReason = TestConstants.NetNeutrality.DNS_FAIL_REASON;
        private String actualResolver = TestConstants.NetNeutrality.DNS_ACTUAL_RESOLVER;
        private String expectedResolver = TestConstants.NetNeutrality.DNS_EXPECTED_RESOLVER;

        public static NetNeutralityDnsResultBuilder builder() {
            return new NetNeutralityDnsResultBuilder();
        }

        public NetNeutralityDnsResultBuilder withNetNeutralityStatus(NetNeutralityStatus netNeutralityStatus) {
            this.testStatus = netNeutralityStatus;
            return this;
        }

        public NetNeutralityDnsResultBuilder withActualDnsResultEntries(String actualDnsResultEntries) {
            this.actualDnsResultEntries = actualDnsResultEntries;
            return this;
        }

        public NetNeutralityDnsResultBuilder withExpectedDnsResultEntries(String expectedDnsResultEntries) {
            this.expectedDnsResultEntries = expectedDnsResultEntries;
            return this;
        }

        public NetNeutralityDnsResultBuilder withTimeoutExceeded(boolean isTimeoutExceeded) {
            this.isTimeoutExceeded = isTimeoutExceeded;
            return this;
        }


        public NetNeutralityDnsResultBuilder withActualDnsStatus(String actualDnsStatus) {
            this.actualDnsStatus = actualDnsStatus;
            return this;
        }

        public NetNeutralityDnsResultBuilder withExpectedDnsStatus(String expectedDnsStatus) {
            this.expectedDnsStatus = expectedDnsStatus;
            return this;
        }

        public NetNeutralityDnsResultBuilder withFailedReason(String failedReason) {
            this.failedReason = failedReason;
            return this;
        }

        public NetNeutralityDnsResultBuilder withActualResolver(String actualResolver) {
            this.actualResolver = actualResolver;
            return this;
        }

        public NetNeutralityDnsResultBuilder withExpectedResolver(String expectedResolver) {
            this.expectedResolver = expectedResolver;
            return this;
        }

        public NetNeutralityDnsResult build() {
            return new NetNeutralityDnsResult(TestConstants.NetNeutrality.DNS_UUID,
                    TestConstants.DEFAULT_OPEN_TEST_UUID_STRING,
                    TestConstants.DEFAULT_CLIENT_UUID_STRING,
                    TestConstants.NetNeutrality.DNS_DURATION_NS,
                    this.testStatus,
                    TestConstants.NetNeutrality.DNS_MEASUREMENT_DATE,
                    TestConstants.DEFAULT_MODEL,
                    TestConstants.DEFAULT_DEVICE,
                    TestConstants.DEFAULT_PRODUCT,
                    TestConstants.DEFAULT_NETWORK_CHANNEL_NUMBER,
                    TestConstants.DEFAULT_NETWORK_TYPE_LTE.name(),
                    TestConstants.DEFAULT_SIGNAL_STRENGTH,
                    TestConstants.DEFAULT_TEST_PLATFORM,
                    TestConstants.DEFAULT_SIM_MCC_MNC,
                    TestConstants.DEFAULT_SIM_OPERATOR_NAME,
                    TestConstants.DEFAULT_SIM_COUNTRY,
                    TestConstants.DEFAULT_NETWORK_MCC_MNC,
                    TestConstants.DEFAULT_NETWORK_OPERATOR_NAME,
                    TestConstants.DEFAULT_NETWORK_COUNTRY,
                    TestConstants.DEFAULT_NETWORK_IS_ROAMING,
                    TestConstants.DEFAULT_CITY,
                    TestConstants.DEFAULT_COUNTRY,
                    TestConstants.DEFAULT_COUNTY,
                    TestConstants.DEFAULT_POSTAL_CODE,
                    TestConstants.DEFAULT_MEASUREMENT_SERVER_NAME,
                    TestConstants.DEFAULT_MEASUREMENT_SERVER_CITY,
                    TestObjects.location(),
                    TestConstants.DEFAULT_PROVIDER_ISP_NAME,
                    List.of(TestObjects.radioSignalResponse()),
                    TestConstants.NetNeutrality.DNS_TARGET,
                    this.actualResolver,
                    this.expectedResolver,
                    TestConstants.NetNeutrality.DNS_TIMEOUT,
                    this.actualDnsStatus,
                    this.expectedDnsStatus,
                    TestConstants.NetNeutrality.DNS_ENTRY_TYPE,
                    this.actualDnsResultEntries,
                    this.expectedDnsResultEntries,
                    this.isTimeoutExceeded,
                    this.failedReason);
        }
    }

    public static NetNeutralityDnsResultBuilder netNeutralityDnsResultBuilder() {
        return NetNeutralityDnsResultBuilder.builder();
    }

    public static NetNeutralityUdpResult netNeutralityUdpResult() {
        return new NetNeutralityUdpResult(TestConstants.NetNeutrality.UDP_UUID,
                TestConstants.DEFAULT_OPEN_TEST_UUID_STRING,
                TestConstants.DEFAULT_CLIENT_UUID_STRING,
                TestConstants.NetNeutrality.UDP_DURATION_NS,
                TestConstants.NetNeutrality.UDP_SUCCESS,
                TestConstants.NetNeutrality.UDP_MEASUREMENT_DATE,
                TestConstants.DEFAULT_MODEL,
                TestConstants.DEFAULT_DEVICE,
                TestConstants.DEFAULT_PRODUCT,
                TestConstants.DEFAULT_NETWORK_CHANNEL_NUMBER,
                TestConstants.DEFAULT_NETWORK_TYPE_LTE.name(),
                TestConstants.DEFAULT_SIGNAL_STRENGTH,
                TestConstants.DEFAULT_TEST_PLATFORM,
                TestConstants.DEFAULT_SIM_MCC_MNC,
                TestConstants.DEFAULT_SIM_OPERATOR_NAME,
                TestConstants.DEFAULT_SIM_COUNTRY,
                TestConstants.DEFAULT_NETWORK_MCC_MNC,
                TestConstants.DEFAULT_NETWORK_OPERATOR_NAME,
                TestConstants.DEFAULT_NETWORK_COUNTRY,
                TestConstants.DEFAULT_NETWORK_IS_ROAMING,
                TestConstants.DEFAULT_CITY,
                TestConstants.DEFAULT_COUNTRY,
                TestConstants.DEFAULT_COUNTY,
                TestConstants.DEFAULT_POSTAL_CODE,
                TestConstants.DEFAULT_MEASUREMENT_SERVER_NAME,
                TestConstants.DEFAULT_MEASUREMENT_SERVER_CITY,
                TestObjects.location(),
                TestConstants.DEFAULT_PROVIDER_ISP_NAME,
                List.of(TestObjects.radioSignalResponse()),
                TestConstants.NetNeutrality.UDP_PORT_NUMBER,
                TestConstants.NetNeutrality.UDP_ACTUAL_NUMBER_OF_PACKETS_RECEIVED,
                TestConstants.NetNeutrality.UDP_NUMBER_OF_PACKETS_SENT,
                TestConstants.NetNeutrality.UDP_EXPECTED_NUMBER_OF_PACKETS_RECEIVED);
    }

    public static RadioSignalResponse radioSignalResponse() {
        return RadioSignalResponse.builder()
                .signal(TestConstants.DEFAULT_SIGNAL_STRENGTH)
                .technology(TestConstants.DEFAULT_NETWORK_TYPE_LTE.name())
                .timeNs(TestConstants.DEFAULT_TIME)
                .build();
    }

    private static GeoPoint location() {
        return new GeoPoint(TestConstants.DEFAULT_LATITUDE, TestConstants.DEFAULT_LONGITUDE);
    }

    public static NetNeutralityUdpResult netNeutralityUdpResult(NetNeutralityStatus netNeutralityStatus,
                                                                Long actualNumberOfPacketsReceived,
                                                                Long expectedMinNumberOfPacketsReceived) {
        return new NetNeutralityUdpResult(TestConstants.NetNeutrality.UDP_UUID,
                TestConstants.DEFAULT_OPEN_TEST_UUID_STRING,
                TestConstants.DEFAULT_CLIENT_UUID_STRING,
                TestConstants.NetNeutrality.UDP_DURATION_NS,
                netNeutralityStatus,
                TestConstants.NetNeutrality.UDP_MEASUREMENT_DATE,
                TestConstants.DEFAULT_MODEL,
                TestConstants.DEFAULT_DEVICE,
                TestConstants.DEFAULT_PRODUCT,
                TestConstants.DEFAULT_NETWORK_CHANNEL_NUMBER,
                TestConstants.DEFAULT_NETWORK_TYPE_LTE.name(),
                TestConstants.DEFAULT_SIGNAL_STRENGTH,
                TestConstants.DEFAULT_TEST_PLATFORM,
                TestConstants.DEFAULT_SIM_MCC_MNC,
                TestConstants.DEFAULT_SIM_OPERATOR_NAME,
                TestConstants.DEFAULT_SIM_COUNTRY,
                TestConstants.DEFAULT_NETWORK_MCC_MNC,
                TestConstants.DEFAULT_NETWORK_OPERATOR_NAME,
                TestConstants.DEFAULT_NETWORK_COUNTRY,
                TestConstants.DEFAULT_NETWORK_IS_ROAMING,
                TestConstants.DEFAULT_CITY,
                TestConstants.DEFAULT_COUNTRY,
                TestConstants.DEFAULT_COUNTY,
                TestConstants.DEFAULT_POSTAL_CODE,
                TestConstants.DEFAULT_MEASUREMENT_SERVER_NAME,
                TestConstants.DEFAULT_MEASUREMENT_SERVER_CITY,
                TestObjects.location(),
                TestConstants.DEFAULT_PROVIDER_ISP_NAME,
                List.of(TestObjects.radioSignalResponse()),
                TestConstants.NetNeutrality.UDP_PORT_NUMBER,
                actualNumberOfPacketsReceived,
                TestConstants.NetNeutrality.UDP_NUMBER_OF_PACKETS_SENT,
                expectedMinNumberOfPacketsReceived);
    }

    public static NetNeutralityUdpResultResponse netNeutralityUdpResultResponse() {
        return new NetNeutralityUdpResultResponse(TestConstants.NetNeutrality.UDP_UUID,
                TestConstants.DEFAULT_OPEN_TEST_UUID_STRING,
                TestConstants.DEFAULT_CLIENT_UUID_STRING,
                TestConstants.NetNeutrality.UDP_DURATION_NS,
                TestConstants.NetNeutrality.UDP_SUCCESS,
                TestConstants.NetNeutrality.UDP_MEASUREMENT_DATE,
                TestConstants.DEFAULT_NETWORK_TYPE_LTE.getCategory(),
                TestConstants.DEFAULT_NETWORK_TYPE_LTE.getName(),
                TestConstants.DEFAULT_DEVICE,
                TestObjects.locationResponse(),
                TestConstants.DEFAULT_MEASUREMENT_SERVER_NAME,
                TestConstants.DEFAULT_MEASUREMENT_SERVER_CITY,
                TestConstants.DEFAULT_PROVIDER_ISP_NAME,
                List.of(TestObjects.radioSignalResponse()),
                TestConstants.NetNeutrality.UDP_PORT_NUMBER,
                TestConstants.NetNeutrality.UDP_NUMBER_OF_PACKETS_SENT,
                TestConstants.NetNeutrality.UDP_ACTUAL_NUMBER_OF_PACKETS_RECEIVED,
                TestConstants.NetNeutrality.UDP_EXPECTED_NUMBER_OF_PACKETS_RECEIVED);
    }

    public static NetNeutralityWebResult netNeutralityWebResult() {
        return new NetNeutralityWebResult(TestConstants.NetNeutrality.WEB_UUID,
                TestConstants.DEFAULT_OPEN_TEST_UUID_STRING,
                TestConstants.DEFAULT_CLIENT_UUID_STRING,
                TestConstants.NetNeutrality.WEB_DURATION_NS,
                TestConstants.NetNeutrality.WEB_SUCCESS,
                TestConstants.NetNeutrality.WEB_MEASUREMENT_DATE,
                TestConstants.DEFAULT_MODEL,
                TestConstants.DEFAULT_DEVICE,
                TestConstants.DEFAULT_PRODUCT,
                TestConstants.DEFAULT_NETWORK_CHANNEL_NUMBER,
                TestConstants.DEFAULT_NETWORK_TYPE_LTE.name(),
                TestConstants.DEFAULT_SIGNAL_STRENGTH,
                TestConstants.DEFAULT_TEST_PLATFORM,
                TestConstants.DEFAULT_SIM_MCC_MNC,
                TestConstants.DEFAULT_SIM_OPERATOR_NAME,
                TestConstants.DEFAULT_SIM_COUNTRY,
                TestConstants.DEFAULT_NETWORK_MCC_MNC,
                TestConstants.DEFAULT_NETWORK_OPERATOR_NAME,
                TestConstants.DEFAULT_NETWORK_COUNTRY,
                TestConstants.DEFAULT_NETWORK_IS_ROAMING,
                TestConstants.DEFAULT_CITY,
                TestConstants.DEFAULT_COUNTRY,
                TestConstants.DEFAULT_COUNTY,
                TestConstants.DEFAULT_POSTAL_CODE,
                TestConstants.DEFAULT_MEASUREMENT_SERVER_NAME,
                TestConstants.DEFAULT_MEASUREMENT_SERVER_CITY,
                TestObjects.location(),
                TestConstants.DEFAULT_PROVIDER_ISP_NAME,
                List.of(TestObjects.radioSignalResponse()),
                TestConstants.NetNeutrality.WEB_TARGET,
                TestConstants.NetNeutrality.WEB_TIMEOUT,
                TestConstants.NetNeutrality.WEB_ACTUAL_STATUS_CODE,
                TestConstants.NetNeutrality.WEB_EXPECTED_STATUS_CODE,
                TestConstants.NetNeutrality.WEB_TIMEOUT_EXCEEDED);
    }

    public static NetNeutralityWebResult netNeutralityWebResult(NetNeutralityStatus testStatus,
                                                                Long actualStatusCode,
                                                                Long expectedStatusCode,
                                                                boolean isTimeoutExceeded) {
        return new NetNeutralityWebResult(TestConstants.NetNeutrality.WEB_UUID,
                TestConstants.DEFAULT_OPEN_TEST_UUID_STRING,
                TestConstants.DEFAULT_CLIENT_UUID_STRING,
                TestConstants.NetNeutrality.WEB_DURATION_NS,
                testStatus,
                TestConstants.NetNeutrality.WEB_MEASUREMENT_DATE,
                TestConstants.DEFAULT_MODEL,
                TestConstants.DEFAULT_DEVICE,
                TestConstants.DEFAULT_PRODUCT,
                TestConstants.DEFAULT_NETWORK_CHANNEL_NUMBER,
                TestConstants.DEFAULT_NETWORK_TYPE_LTE.name(),
                TestConstants.DEFAULT_SIGNAL_STRENGTH,
                TestConstants.DEFAULT_TEST_PLATFORM,
                TestConstants.DEFAULT_SIM_MCC_MNC,
                TestConstants.DEFAULT_SIM_OPERATOR_NAME,
                TestConstants.DEFAULT_SIM_COUNTRY,
                TestConstants.DEFAULT_NETWORK_MCC_MNC,
                TestConstants.DEFAULT_NETWORK_OPERATOR_NAME,
                TestConstants.DEFAULT_NETWORK_COUNTRY,
                TestConstants.DEFAULT_NETWORK_IS_ROAMING,
                TestConstants.DEFAULT_CITY,
                TestConstants.DEFAULT_COUNTRY,
                TestConstants.DEFAULT_COUNTY,
                TestConstants.DEFAULT_POSTAL_CODE,
                TestConstants.DEFAULT_MEASUREMENT_SERVER_NAME,
                TestConstants.DEFAULT_MEASUREMENT_SERVER_CITY,
                TestObjects.location(),
                TestConstants.DEFAULT_PROVIDER_ISP_NAME,
                List.of(TestObjects.radioSignalResponse()),
                TestConstants.NetNeutrality.WEB_TARGET,
                TestConstants.NetNeutrality.WEB_TIMEOUT,
                actualStatusCode,
                expectedStatusCode,
                isTimeoutExceeded);
    }

    public static NetNeutralityParametersResponse netNeutralityParametersResponse(Map<NetNeutralityTestType, List<NetNeutralityTestParameterResponse>> netNeutralityTestTypeParameters) {
        return NetNeutralityParametersResponse.builder()
                .netNeutralityParameters(netNeutralityTestTypeParameters)
                .build();
    }

    private static MeasurementServerTypeDetail measurementServerTypeDetail() {
        return new MeasurementServerTypeDetail(null, TestConstants.DEFAULT_MEASUREMENT_SERVER_TYPE,
                TestConstants.DEFAULT_MEASUREMENT_SERVER_PORT,
                TestConstants.DEFAULT_MEASUREMENT_SERVER_PORT,
                true, null);
    }

    public static MeasurementServerDescription measurementServerDescription() {
        return new MeasurementServerDescription();
    }

    public static MeasurementServer measurementServerOffNet() {
        return new MeasurementServer(null,
                TestConstants.DEFAULT_MEASUREMENT_SERVER_NAME,
                TestConstants.DEFAULT_MEASUREMENT_SERVER_ADDRESS,
                TestConstants.DEFAULT_MEASUREMENT_SERVER_SECRET_KEY,
                null,
                TestObjects.measurementServerDescription(),
                List.of(TestObjects.measurementServerTypeDetail()),
                null,
                null,
                TestConstants.DEFAULT_MEASUREMENT_SERVER_PORT,
                TestConstants.DEFAULT_MEASUREMENT_SERVER_PORT_SSL,
                TestConstants.DEFAULT_LATITUDE,
                TestConstants.DEFAULT_LONGITUDE,
                false,
                true,
                true,
                false);
    }
}
