package com.specure.mapper.sah.neutrality;

import com.specure.common.enums.DnsStatus;
import com.specure.common.enums.NetNeutralityTestType;
import com.specure.common.model.jpa.MobileModel;
import com.specure.common.model.jpa.RawProvider;
import com.specure.common.repository.GeoLocationRepository;
import com.specure.common.repository.NetNeutralitySettingRepository;
import com.specure.common.service.UUIDGenerator;
import com.specure.common.service.digger.DiggerService;
import com.specure.constant.ErrorMessage;
import com.specure.enums.NetNeutralityStatus;
import com.specure.mapper.sah.RadioSignalMapper;
import com.specure.repository.mobile.SimOperatorRepository;
import com.specure.sah.TestConstants;
import com.specure.sah.TestObjects;
import com.specure.service.admin.MobileModelService;
import com.specure.service.admin.RawProviderService;
import com.specure.service.sah.NationalOperatorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Clock;
import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class NetNeutralityDnsProcessorImplTest {

    @Mock
    private UUIDGenerator uuidGenerator;
    @Mock
    private NetNeutralitySettingRepository netNeutralitySettingRepository;
    @Mock
    private Clock clock;
    @Mock
    private GeoLocationRepository geoLocationRepository;
    @Mock
    private NationalOperatorService nationalOperatorService;
    @Mock
    private SimOperatorRepository simOperatorRepository;
    @Mock
    private RawProviderService rawProviderService;
    @Mock
    private DiggerService diggerService;
    @Mock
    private MobileModelService mobileModelService;
    @Mock
    private RadioSignalMapper radioSignalMapper;
    @InjectMocks
    private NetNeutralityDnsProcessorImpl netNeutralityDnsProcessor;
    @Mock
    private RawProvider rawProvider;
    @Mock
    private MobileModel mobileModel;

    @BeforeEach
    void setUp() {
        when(uuidGenerator.generateUUID()).thenReturn(UUID.fromString(TestConstants.NetNeutrality.DNS_UUID));
        when(clock.instant()).thenReturn(Instant.ofEpochMilli(TestConstants.NetNeutrality.DNS_TIMESTAMP_INSTANT_MS));
        when(radioSignalMapper.radioSignalRequestToRadioSignalNN(any())).thenReturn(TestObjects.radioSignalResponse());
    }

    @Test
    void toElasticModel_whenCorrectInvocation_expectedSuccess() {
        var netNeutralityDnsSetting = TestObjects.netNeutralityDnsSetting();
        var request = TestObjects.netNeutralityDnsTestResultRequest();
        var netNeutralityMeasurementResultRequest = TestObjects.netNeutralityMeasurementResultRequest(List.of(request));
        var expectedResult = TestObjects.netNeutralityDnsResult();
        when(netNeutralitySettingRepository.findByIdAndType(TestConstants.NetNeutrality.DNS_ID, NetNeutralityTestType.DNS)).thenReturn(Optional.of(netNeutralityDnsSetting));
        when(rawProviderService.getRawProvider(TestConstants.DEFAULT_SIM_COUNTRY, TestConstants.DEFAULT_SIM_OPERATOR_NAME, TestConstants.DEFAULT_SIM_MCC_MNC)).thenReturn(rawProvider);
        when(rawProvider.getAlias()).thenReturn(TestConstants.DEFAULT_PROVIDER_ISP_NAME);
        when(geoLocationRepository.getGeoShapeByCoordinates(anyDouble(), anyDouble())).thenReturn(Optional.empty());
        when(mobileModelService.getAndSaveNewIfNotExistMobileModelByModel(TestConstants.DEFAULT_MODEL)).thenReturn(Optional.of(mobileModel));
        when(mobileModel.getMarketingName()).thenReturn(TestConstants.DEFAULT_DEVICE);

        var result = netNeutralityDnsProcessor.toElasticModel(request, netNeutralityMeasurementResultRequest);

        assertEquals(expectedResult, result);
    }

    @Test
    void toElasticModel_whenExpectedResolverIsUndefined_expectedSuccess() {
        var netNeutralityDnsSetting = TestObjects.netNeutralityDnsSetting();
        netNeutralityDnsSetting.setResolver("");
        var request = TestObjects.netNeutralityDnsTestResultRequest();
        var netNeutralityMeasurementResultRequest = TestObjects.netNeutralityMeasurementResultRequest(List.of(request));
        var expectedResult = TestObjects.netNeutralityDnsResultBuilder()
                .withExpectedResolver("")
                .withFailedReason(null)
                .build();
        when(netNeutralitySettingRepository.findByIdAndType(TestConstants.NetNeutrality.DNS_ID, NetNeutralityTestType.DNS)).thenReturn(Optional.of(netNeutralityDnsSetting));
        when(rawProviderService.getRawProvider(TestConstants.DEFAULT_SIM_COUNTRY, TestConstants.DEFAULT_SIM_OPERATOR_NAME, TestConstants.DEFAULT_SIM_MCC_MNC)).thenReturn(rawProvider);
        when(rawProvider.getAlias()).thenReturn(TestConstants.DEFAULT_PROVIDER_ISP_NAME);
        when(geoLocationRepository.getGeoShapeByCoordinates(anyDouble(), anyDouble())).thenReturn(Optional.empty());
        when(mobileModelService.getAndSaveNewIfNotExistMobileModelByModel(TestConstants.DEFAULT_MODEL)).thenReturn(Optional.of(mobileModel));
        when(mobileModel.getMarketingName()).thenReturn(TestConstants.DEFAULT_DEVICE);

        var result = netNeutralityDnsProcessor.toElasticModel(request, netNeutralityMeasurementResultRequest);

        assertEquals(expectedResult, result);
    }

    @Test
    void toElasticModel_whenExpectedResolverNotNullAndActualResolverIsDifferent_expectedFail() {
        var netNeutralityDnsSetting = TestObjects.netNeutralityDnsSetting();
        var request = TestObjects.netNeutralityDnsTestResultRequestBuilder()
                .withResolver(TestConstants.DEFAULT_TEXT)
                .build();
        var netNeutralityMeasurementResultRequest = TestObjects.netNeutralityMeasurementResultRequest(List.of(request));
        var expectedResult = TestObjects.netNeutralityDnsResultBuilder()
                .withActualResolver(TestConstants.DEFAULT_TEXT)
                .withFailedReason(null)
                .withNetNeutralityStatus(NetNeutralityStatus.FAIL)
                .build();
        when(netNeutralitySettingRepository.findByIdAndType(TestConstants.NetNeutrality.DNS_ID, NetNeutralityTestType.DNS)).thenReturn(Optional.of(netNeutralityDnsSetting));
        when(rawProviderService.getRawProvider(TestConstants.DEFAULT_SIM_COUNTRY, TestConstants.DEFAULT_SIM_OPERATOR_NAME, TestConstants.DEFAULT_SIM_MCC_MNC)).thenReturn(rawProvider);
        when(rawProvider.getAlias()).thenReturn(TestConstants.DEFAULT_PROVIDER_ISP_NAME);
        when(geoLocationRepository.getGeoShapeByCoordinates(anyDouble(), anyDouble())).thenReturn(Optional.empty());
        when(mobileModelService.getAndSaveNewIfNotExistMobileModelByModel(TestConstants.DEFAULT_MODEL)).thenReturn(Optional.of(mobileModel));
        when(mobileModel.getMarketingName()).thenReturn(TestConstants.DEFAULT_DEVICE);

        var result = netNeutralityDnsProcessor.toElasticModel(request, netNeutralityMeasurementResultRequest);

        assertEquals(expectedResult, result);
    }

    @Test
    void toElasticModel_timeoutExceededAndEntriesEqual_expectedNetNeutralityResultAndStatusFail() {
        boolean isTimeoutExceeded = true;
        var netNeutralityDnsSetting = TestObjects.netNeutralityDnsSetting();

        var request = TestObjects.netNeutralityDnsTestResultRequestBuilder()
                .withTimeoutExceeded(isTimeoutExceeded)
                .build();
        var netNeutralityMeasurementResultRequest = TestObjects.netNeutralityMeasurementResultRequest(List.of(request));

        var expectedResult = TestObjects.netNeutralityDnsResultBuilder()
                .withNetNeutralityStatus(NetNeutralityStatus.FAIL)
                .withTimeoutExceeded(isTimeoutExceeded)
                .withFailedReason(ErrorMessage.NET_NEUTRALITY_DNS_FAIL_REASON_TIMEOUT)
                .build();

        when(netNeutralitySettingRepository.findByIdAndType(TestConstants.NetNeutrality.DNS_ID, NetNeutralityTestType.DNS)).thenReturn(Optional.of(netNeutralityDnsSetting));
        when(rawProviderService.getRawProvider(TestConstants.DEFAULT_SIM_COUNTRY, TestConstants.DEFAULT_SIM_OPERATOR_NAME, TestConstants.DEFAULT_SIM_MCC_MNC)).thenReturn(rawProvider);
        when(rawProvider.getAlias()).thenReturn(TestConstants.DEFAULT_PROVIDER_ISP_NAME);
        when(geoLocationRepository.getGeoShapeByCoordinates(anyDouble(), anyDouble())).thenReturn(Optional.empty());
        when(mobileModelService.getAndSaveNewIfNotExistMobileModelByModel(TestConstants.DEFAULT_MODEL)).thenReturn(Optional.of(mobileModel));
        when(mobileModel.getMarketingName()).thenReturn(TestConstants.DEFAULT_DEVICE);

        var result = netNeutralityDnsProcessor.toElasticModel(request, netNeutralityMeasurementResultRequest);

        assertEquals(expectedResult, result);
    }

    @Test
    void toElasticModel_noErrorAndNotCorrespondingDnsEntries_expectedBadStatus() {
        boolean isTimeoutExceeded = false;
        List<String> actualDnsEntriesList = Collections.emptyList();
        var actualDnsEntries = "";
        var expectedDnsEntries = TestConstants.NetNeutrality.DNS_ACTUAL_DNS_RESULT_ENTRIES;
        var netNeutralityDnsSetting = TestObjects.netNeutralityDnsSetting();
        netNeutralityDnsSetting.setExpectedDnsStatus(DnsStatus.NOERROR.name());
        netNeutralityDnsSetting.setExpectedDnsEntries(expectedDnsEntries);
        var request = TestObjects.netNeutralityDnsTestResultRequestBuilder()
                .withActualDnsEntriesList(actualDnsEntriesList)
                .build();
        var netNeutralityMeasurementResultRequest = TestObjects.netNeutralityMeasurementResultRequest(List.of(request));
        var expectedResult = TestObjects.netNeutralityDnsResultBuilder()
                .withActualDnsStatus(DnsStatus.NOERROR.name())
                .withNetNeutralityStatus(NetNeutralityStatus.FAIL)
                .withActualDnsResultEntries(actualDnsEntries)
                .withExpectedDnsResultEntries(expectedDnsEntries)
                .withTimeoutExceeded(isTimeoutExceeded)
                .withFailedReason(ErrorMessage.NET_NEUTRALITY_DNS_FAIL_REASON_BAD_STATUS)
                .build();
        when(netNeutralitySettingRepository.findByIdAndType(TestConstants.NetNeutrality.DNS_ID, NetNeutralityTestType.DNS)).thenReturn(Optional.of(netNeutralityDnsSetting));
        when(rawProviderService.getRawProvider(TestConstants.DEFAULT_SIM_COUNTRY, TestConstants.DEFAULT_SIM_OPERATOR_NAME, TestConstants.DEFAULT_SIM_MCC_MNC)).thenReturn(rawProvider);
        when(rawProvider.getAlias()).thenReturn(TestConstants.DEFAULT_PROVIDER_ISP_NAME);
        when(geoLocationRepository.getGeoShapeByCoordinates(anyDouble(), anyDouble())).thenReturn(Optional.empty());
        when(mobileModelService.getAndSaveNewIfNotExistMobileModelByModel(TestConstants.DEFAULT_MODEL)).thenReturn(Optional.of(mobileModel));
        when(mobileModel.getMarketingName()).thenReturn(TestConstants.DEFAULT_DEVICE);

        var result = netNeutralityDnsProcessor.toElasticModel(request, netNeutralityMeasurementResultRequest);

        assertEquals(expectedResult, result);
    }

    @Test
    void toElasticModel_actualNoErrorStatusAndNxDomainExpectedStatus_expectedBadStatus() {
        boolean isTimeoutExceeded = false;
        List<String> actualDnsEntriesList = Collections.emptyList();
        var actualDnsEntries = "";
        var expectedDnsEntries = "";
        var actualDnsStatus = DnsStatus.NOERROR.name();
        var expectedDnsStatus = DnsStatus.NXDOMAIN.name();
        var netNeutralityDnsSetting = TestObjects.netNeutralityDnsSetting();
        netNeutralityDnsSetting.setExpectedDnsStatus(expectedDnsStatus);
        netNeutralityDnsSetting.setExpectedDnsEntries(expectedDnsEntries);
        var request = TestObjects.netNeutralityDnsTestResultRequestBuilder()
                .withActualDnsEntriesList(actualDnsEntriesList)
                .withDnsStatus(actualDnsStatus)
                .build();
        var netNeutralityMeasurementResultRequest = TestObjects.netNeutralityMeasurementResultRequest(List.of(request));
        var expectedResult = TestObjects.netNeutralityDnsResultBuilder()
                .withActualDnsStatus(actualDnsStatus)
                .withExpectedDnsStatus(expectedDnsStatus)
                .withNetNeutralityStatus(NetNeutralityStatus.FAIL)
                .withActualDnsResultEntries(actualDnsEntries)
                .withExpectedDnsResultEntries(expectedDnsEntries)
                .withTimeoutExceeded(isTimeoutExceeded)
                .withFailedReason(ErrorMessage.NET_NEUTRALITY_DNS_FAIL_REASON_BAD_STATUS)
                .build();
        when(netNeutralitySettingRepository.findByIdAndType(TestConstants.NetNeutrality.DNS_ID, NetNeutralityTestType.DNS)).thenReturn(Optional.of(netNeutralityDnsSetting));
        when(rawProviderService.getRawProvider(TestConstants.DEFAULT_SIM_COUNTRY, TestConstants.DEFAULT_SIM_OPERATOR_NAME, TestConstants.DEFAULT_SIM_MCC_MNC)).thenReturn(rawProvider);
        when(rawProvider.getAlias()).thenReturn(TestConstants.DEFAULT_PROVIDER_ISP_NAME);
        when(geoLocationRepository.getGeoShapeByCoordinates(anyDouble(), anyDouble())).thenReturn(Optional.empty());
        when(mobileModelService.getAndSaveNewIfNotExistMobileModelByModel(TestConstants.DEFAULT_MODEL)).thenReturn(Optional.of(mobileModel));
        when(mobileModel.getMarketingName()).thenReturn(TestConstants.DEFAULT_DEVICE);

        var result = netNeutralityDnsProcessor.toElasticModel(request, netNeutralityMeasurementResultRequest);

        assertEquals(expectedResult, result);
    }


    @Test
    void toElasticModel_actualNxDomainStatusAndNotCorrespondingDnsEntries_expectedBadStatus() {
        boolean isTimeoutExceeded = false;
        var actualDnsEntriesList = TestConstants.NetNeutrality.DNS_ACTUAL_DNS_RESULT_ENTRIES_LIST;
        var actualDnsEntries = TestConstants.NetNeutrality.DNS_ACTUAL_DNS_RESULT_ENTRIES;
        var expectedDnsEntries = "";
        String actualDnsStatus = DnsStatus.NXDOMAIN.name();
        String expectedDnsStatus = DnsStatus.NXDOMAIN.name();
        var netNeutralityDnsSetting = TestObjects.netNeutralityDnsSetting();
        netNeutralityDnsSetting.setExpectedDnsStatus(expectedDnsStatus);
        netNeutralityDnsSetting.setExpectedDnsEntries(expectedDnsEntries);

        var request = TestObjects.netNeutralityDnsTestResultRequestBuilder()
                .withDnsStatus(actualDnsStatus)
                .withActualDnsEntriesList(actualDnsEntriesList)
                .build();
        var netNeutralityMeasurementResultRequest = TestObjects.netNeutralityMeasurementResultRequest(List.of(request));
        var expectedResult = TestObjects.netNeutralityDnsResultBuilder()
                .withNetNeutralityStatus(NetNeutralityStatus.FAIL)
                .withActualDnsStatus(actualDnsStatus)
                .withExpectedDnsStatus(expectedDnsStatus)
                .withTimeoutExceeded(isTimeoutExceeded)
                .withActualDnsResultEntries(actualDnsEntries)
                .withExpectedDnsResultEntries(expectedDnsEntries)
                .withFailedReason(ErrorMessage.NET_NEUTRALITY_DNS_FAIL_REASON_BAD_STATUS)
                .build();

        when(netNeutralitySettingRepository.findByIdAndType(TestConstants.NetNeutrality.DNS_ID, NetNeutralityTestType.DNS)).thenReturn(Optional.of(netNeutralityDnsSetting));
        when(rawProviderService.getRawProvider(TestConstants.DEFAULT_SIM_COUNTRY, TestConstants.DEFAULT_SIM_OPERATOR_NAME, TestConstants.DEFAULT_SIM_MCC_MNC)).thenReturn(rawProvider);
        when(rawProvider.getAlias()).thenReturn(TestConstants.DEFAULT_PROVIDER_ISP_NAME);
        when(geoLocationRepository.getGeoShapeByCoordinates(anyDouble(), anyDouble())).thenReturn(Optional.empty());
        when(mobileModelService.getAndSaveNewIfNotExistMobileModelByModel(TestConstants.DEFAULT_MODEL)).thenReturn(Optional.of(mobileModel));
        when(mobileModel.getMarketingName()).thenReturn(TestConstants.DEFAULT_DEVICE);

        var result = netNeutralityDnsProcessor.toElasticModel(request, netNeutralityMeasurementResultRequest);

        assertEquals(expectedResult, result);
    }

    @Test
    void toElasticModel_actualNxDomainStatusAndExpectedNoErrorStatus_expectedBadStatus() {
        boolean isTimeoutExceeded = false;
        var actualDnsEntriesList = TestConstants.NetNeutrality.DNS_ACTUAL_DNS_RESULT_ENTRIES_LIST;
        var actualDnsEntries = TestConstants.NetNeutrality.DNS_ACTUAL_DNS_RESULT_ENTRIES;
        var expectedDnsEntries = TestConstants.NetNeutrality.DNS_EXPECTED_DNS_ENTRIES;
        String actualDnsStatus = DnsStatus.NXDOMAIN.name();
        String expectedDnsStatus = DnsStatus.NOERROR.name();
        var netNeutralityDnsSetting = TestObjects.netNeutralityDnsSetting();
        netNeutralityDnsSetting.setExpectedDnsStatus(expectedDnsStatus);
        netNeutralityDnsSetting.setExpectedDnsEntries(expectedDnsEntries);

        var request = TestObjects.netNeutralityDnsTestResultRequestBuilder()
                .withDnsStatus(actualDnsStatus)
                .withActualDnsEntriesList(actualDnsEntriesList)
                .build();
        var netNeutralityMeasurementResultRequest = TestObjects.netNeutralityMeasurementResultRequest(List.of(request));
        var expectedResult = TestObjects.netNeutralityDnsResultBuilder()
                .withNetNeutralityStatus(NetNeutralityStatus.FAIL)
                .withActualDnsStatus(actualDnsStatus)
                .withExpectedDnsStatus(expectedDnsStatus)
                .withTimeoutExceeded(isTimeoutExceeded)
                .withActualDnsResultEntries(actualDnsEntries)
                .withExpectedDnsResultEntries(expectedDnsEntries)
                .withFailedReason(ErrorMessage.NET_NEUTRALITY_DNS_FAIL_REASON_BAD_STATUS)
                .build();

        when(netNeutralitySettingRepository.findByIdAndType(TestConstants.NetNeutrality.DNS_ID, NetNeutralityTestType.DNS)).thenReturn(Optional.of(netNeutralityDnsSetting));
        when(rawProviderService.getRawProvider(TestConstants.DEFAULT_SIM_COUNTRY, TestConstants.DEFAULT_SIM_OPERATOR_NAME, TestConstants.DEFAULT_SIM_MCC_MNC)).thenReturn(rawProvider);
        when(rawProvider.getAlias()).thenReturn(TestConstants.DEFAULT_PROVIDER_ISP_NAME);
        when(geoLocationRepository.getGeoShapeByCoordinates(anyDouble(), anyDouble())).thenReturn(Optional.empty());
        when(mobileModelService.getAndSaveNewIfNotExistMobileModelByModel(TestConstants.DEFAULT_MODEL)).thenReturn(Optional.of(mobileModel));
        when(mobileModel.getMarketingName()).thenReturn(TestConstants.DEFAULT_DEVICE);

        var result = netNeutralityDnsProcessor.toElasticModel(request, netNeutralityMeasurementResultRequest);

        assertEquals(expectedResult, result);
    }

    //if there are no IP targets, and we receive at least one we show it
    @Test
    void toElasticModel_expectedIpIsEmptyAndActualNotEmpty_expectedSucceed() {
        boolean isTimeoutExceeded = false;
        List<String> actualDnsEntriesList = List.of(TestConstants.DEFAULT_IP);
        var actualDnsEntries = TestConstants.DEFAULT_IP;
        var expectedDnsEntries = "";
        var actualDnsStatus = DnsStatus.NOERROR.name();
        var expectedDnsStatus = DnsStatus.NOERROR.name();
        var netNeutralityDnsSetting = TestObjects.netNeutralityDnsSetting();
        netNeutralityDnsSetting.setExpectedDnsStatus(expectedDnsStatus);
        netNeutralityDnsSetting.setExpectedDnsEntries(expectedDnsEntries);
        var request = TestObjects.netNeutralityDnsTestResultRequestBuilder()
                .withActualDnsEntriesList(actualDnsEntriesList)
                .withDnsStatus(actualDnsStatus)
                .build();
        var netNeutralityMeasurementResultRequest = TestObjects.netNeutralityMeasurementResultRequest(List.of(request));
        var expectedResult = TestObjects.netNeutralityDnsResultBuilder()
                .withActualDnsStatus(actualDnsStatus)
                .withExpectedDnsStatus(expectedDnsStatus)
                .withNetNeutralityStatus(NetNeutralityStatus.SUCCEED)
                .withActualDnsResultEntries(actualDnsEntries)
                .withExpectedDnsResultEntries(expectedDnsEntries)
                .withTimeoutExceeded(isTimeoutExceeded)
                .withFailedReason(null)
                .build();
        when(netNeutralitySettingRepository.findByIdAndType(TestConstants.NetNeutrality.DNS_ID, NetNeutralityTestType.DNS)).thenReturn(Optional.of(netNeutralityDnsSetting));
        when(rawProviderService.getRawProvider(TestConstants.DEFAULT_SIM_COUNTRY, TestConstants.DEFAULT_SIM_OPERATOR_NAME, TestConstants.DEFAULT_SIM_MCC_MNC)).thenReturn(rawProvider);
        when(rawProvider.getAlias()).thenReturn(TestConstants.DEFAULT_PROVIDER_ISP_NAME);
        when(geoLocationRepository.getGeoShapeByCoordinates(anyDouble(), anyDouble())).thenReturn(Optional.empty());
        when(mobileModelService.getAndSaveNewIfNotExistMobileModelByModel(TestConstants.DEFAULT_MODEL)).thenReturn(Optional.of(mobileModel));
        when(mobileModel.getMarketingName()).thenReturn(TestConstants.DEFAULT_DEVICE);

        var result = netNeutralityDnsProcessor.toElasticModel(request, netNeutralityMeasurementResultRequest);

        assertEquals(expectedResult, result);
    }

    //if there is one IP target, and we do not receive this IP in the response, it will fail
    @Test
    void toElasticModel_actualNoErrorStatusAndExpectedNoErrorStatusAndDifferentIps_expectedBadStatus() {
        boolean isTimeoutExceeded = false;
        List<String> actualDnsEntriesList = Collections.emptyList();
        var actualDnsEntries = "";
        var expectedDnsEntries = TestConstants.DEFAULT_IP;
        var actualDnsStatus = DnsStatus.NOERROR.name();
        var expectedDnsStatus = DnsStatus.NOERROR.name();
        var netNeutralityDnsSetting = TestObjects.netNeutralityDnsSetting();
        netNeutralityDnsSetting.setExpectedDnsStatus(expectedDnsStatus);
        netNeutralityDnsSetting.setExpectedDnsEntries(expectedDnsEntries);
        var request = TestObjects.netNeutralityDnsTestResultRequestBuilder()
                .withActualDnsEntriesList(actualDnsEntriesList)
                .withDnsStatus(actualDnsStatus)
                .build();
        var netNeutralityMeasurementResultRequest = TestObjects.netNeutralityMeasurementResultRequest(List.of(request));
        var expectedResult = TestObjects.netNeutralityDnsResultBuilder()
                .withActualDnsStatus(actualDnsStatus)
                .withExpectedDnsStatus(expectedDnsStatus)
                .withNetNeutralityStatus(NetNeutralityStatus.FAIL)
                .withActualDnsResultEntries(actualDnsEntries)
                .withExpectedDnsResultEntries(expectedDnsEntries)
                .withTimeoutExceeded(isTimeoutExceeded)
                .withFailedReason(ErrorMessage.NET_NEUTRALITY_DNS_FAIL_REASON_BAD_STATUS)
                .build();
        when(netNeutralitySettingRepository.findByIdAndType(TestConstants.NetNeutrality.DNS_ID, NetNeutralityTestType.DNS)).thenReturn(Optional.of(netNeutralityDnsSetting));
        when(rawProviderService.getRawProvider(TestConstants.DEFAULT_SIM_COUNTRY, TestConstants.DEFAULT_SIM_OPERATOR_NAME, TestConstants.DEFAULT_SIM_MCC_MNC)).thenReturn(rawProvider);
        when(rawProvider.getAlias()).thenReturn(TestConstants.DEFAULT_PROVIDER_ISP_NAME);
        when(geoLocationRepository.getGeoShapeByCoordinates(anyDouble(), anyDouble())).thenReturn(Optional.empty());
        when(mobileModelService.getAndSaveNewIfNotExistMobileModelByModel(TestConstants.DEFAULT_MODEL)).thenReturn(Optional.of(mobileModel));
        when(mobileModel.getMarketingName()).thenReturn(TestConstants.DEFAULT_DEVICE);

        var result = netNeutralityDnsProcessor.toElasticModel(request, netNeutralityMeasurementResultRequest);

        assertEquals(expectedResult, result);
    }

    // if we receive more IP it is still ok if the target is in the list
    @Test
    void toElasticModel_oneTargetIpAddressAndActualMultipleIpAddresses_expectedSucceed() {
        boolean isTimeoutExceeded = false;
        List<String> actualDnsEntriesList = TestConstants.NetNeutrality.DNS_ACTUAL_DNS_RESULT_ENTRIES_LIST;
        var actualDnsEntries = TestConstants.NetNeutrality.DNS_ACTUAL_DNS_RESULT_ENTRIES;
        var expectedDnsEntries = TestConstants.NetNeutrality.DNS_ACTUAL_DNS_RESULT_ENTRIES_LIST.get(1);
        var actualDnsStatus = DnsStatus.NOERROR.name();
        var expectedDnsStatus = DnsStatus.NOERROR.name();
        var netNeutralityDnsSetting = TestObjects.netNeutralityDnsSetting();
        netNeutralityDnsSetting.setExpectedDnsStatus(expectedDnsStatus);
        netNeutralityDnsSetting.setExpectedDnsEntries(expectedDnsEntries);
        var request = TestObjects.netNeutralityDnsTestResultRequestBuilder()
                .withActualDnsEntriesList(actualDnsEntriesList)
                .withDnsStatus(actualDnsStatus)
                .build();
        var netNeutralityMeasurementResultRequest = TestObjects.netNeutralityMeasurementResultRequest(List.of(request));
        var expectedResult = TestObjects.netNeutralityDnsResultBuilder()
                .withActualDnsStatus(actualDnsStatus)
                .withExpectedDnsStatus(expectedDnsStatus)
                .withNetNeutralityStatus(NetNeutralityStatus.SUCCEED)
                .withActualDnsResultEntries(actualDnsEntries)
                .withExpectedDnsResultEntries(expectedDnsEntries)
                .withTimeoutExceeded(isTimeoutExceeded)
                .withFailedReason(null)
                .build();
        when(netNeutralitySettingRepository.findByIdAndType(TestConstants.NetNeutrality.DNS_ID, NetNeutralityTestType.DNS)).thenReturn(Optional.of(netNeutralityDnsSetting));
        when(rawProviderService.getRawProvider(TestConstants.DEFAULT_SIM_COUNTRY, TestConstants.DEFAULT_SIM_OPERATOR_NAME, TestConstants.DEFAULT_SIM_MCC_MNC)).thenReturn(rawProvider);
        when(rawProvider.getAlias()).thenReturn(TestConstants.DEFAULT_PROVIDER_ISP_NAME);
        when(geoLocationRepository.getGeoShapeByCoordinates(anyDouble(), anyDouble())).thenReturn(Optional.empty());
        when(mobileModelService.getAndSaveNewIfNotExistMobileModelByModel(TestConstants.DEFAULT_MODEL)).thenReturn(Optional.of(mobileModel));
        when(mobileModel.getMarketingName()).thenReturn(TestConstants.DEFAULT_DEVICE);

        var result = netNeutralityDnsProcessor.toElasticModel(request, netNeutralityMeasurementResultRequest);

        assertEquals(expectedResult, result);
    }

    //if we more IP targets configured, then we must have all the targets in the response to succeed
    @Test
    void toElasticModel_multipleTargetIpAddressAndActualMultipleIpAddresses_expectedFailed() {
        boolean isTimeoutExceeded = false;
        List<String> actualDnsEntriesList = new ArrayList<>(TestConstants.NetNeutrality.DNS_ACTUAL_DNS_RESULT_ENTRIES_LIST);
        actualDnsEntriesList.add(TestConstants.DEFAULT_IP);
        var actualDnsEntries = TestConstants.NetNeutrality.DNS_ACTUAL_DNS_RESULT_ENTRIES + ";" + TestConstants.DEFAULT_IP;
        var expectedDnsEntries = TestConstants.NetNeutrality.DNS_EXPECTED_DNS_ENTRIES;
        var actualDnsStatus = DnsStatus.NOERROR.name();
        var expectedDnsStatus = DnsStatus.NOERROR.name();
        var netNeutralityDnsSetting = TestObjects.netNeutralityDnsSetting();
        netNeutralityDnsSetting.setExpectedDnsStatus(expectedDnsStatus);
        netNeutralityDnsSetting.setExpectedDnsEntries(expectedDnsEntries);
        var request = TestObjects.netNeutralityDnsTestResultRequestBuilder()
                .withActualDnsEntriesList(actualDnsEntriesList)
                .withDnsStatus(actualDnsStatus)
                .build();
        var netNeutralityMeasurementResultRequest = TestObjects.netNeutralityMeasurementResultRequest(List.of(request));
        var expectedResult = TestObjects.netNeutralityDnsResultBuilder()
                .withActualDnsStatus(actualDnsStatus)
                .withExpectedDnsStatus(expectedDnsStatus)
                .withNetNeutralityStatus(NetNeutralityStatus.FAIL)
                .withActualDnsResultEntries(actualDnsEntries)
                .withExpectedDnsResultEntries(expectedDnsEntries)
                .withTimeoutExceeded(isTimeoutExceeded)
                .withFailedReason(ErrorMessage.NET_NEUTRALITY_DNS_FAIL_REASON_DIFFERENT_IPS)
                .build();
        when(netNeutralitySettingRepository.findByIdAndType(TestConstants.NetNeutrality.DNS_ID, NetNeutralityTestType.DNS)).thenReturn(Optional.of(netNeutralityDnsSetting));
        when(rawProviderService.getRawProvider(TestConstants.DEFAULT_SIM_COUNTRY, TestConstants.DEFAULT_SIM_OPERATOR_NAME, TestConstants.DEFAULT_SIM_MCC_MNC)).thenReturn(rawProvider);
        when(rawProvider.getAlias()).thenReturn(TestConstants.DEFAULT_PROVIDER_ISP_NAME);
        when(geoLocationRepository.getGeoShapeByCoordinates(anyDouble(), anyDouble())).thenReturn(Optional.empty());
        when(mobileModelService.getAndSaveNewIfNotExistMobileModelByModel(TestConstants.DEFAULT_MODEL)).thenReturn(Optional.of(mobileModel));
        when(mobileModel.getMarketingName()).thenReturn(TestConstants.DEFAULT_DEVICE);

        var result = netNeutralityDnsProcessor.toElasticModel(request, netNeutralityMeasurementResultRequest);

        assertEquals(expectedResult, result);
    }

    @Test
    void toElasticResponse_correctInvocation_expectedNetNeutralityResultResponse() {
        var netNeutralityDnsResult = TestObjects.netNeutralityDnsResult();
        var netNeutralityResultResponse = TestObjects.netNeutralityDnsResultResponse();

        var result = netNeutralityDnsProcessor.toElasticResponse(netNeutralityDnsResult);

        assertEquals(netNeutralityResultResponse, result);
    }

    @Test
    void getType_correctInvocation_expectedDns() {
        assertEquals(NetNeutralityTestType.DNS, netNeutralityDnsProcessor.getType());
    }

    @Test
    void toNetNeutralityTestParameterResponse_correctInvocation_expectedNetNeutralityTestParameterResponse() {
        var netNeutralityDnsSetting = TestObjects.netNeutralityDnsSetting();
        var netNeutralityDnsTestParametersResponse = TestObjects.netNeutralityDnsTestParametersResponse();

        var result = netNeutralityDnsProcessor.toNetNeutralityTestParameterResponse(netNeutralityDnsSetting);

        assertEquals(netNeutralityDnsTestParametersResponse, result);
    }

    @Test
    void toNetNeutralitySettingResponse_correctInvocation_expectedNetNeutralitySettingResponse() {
        var netNeutralityDnsSetting = TestObjects.netNeutralityDnsSetting();
        var expectedResult = TestObjects.netNeutralityDnsSettingResponse();

        var result = netNeutralityDnsProcessor.toNetNeutralitySettingResponse(netNeutralityDnsSetting);

        assertEquals(expectedResult, result);
    }
}
