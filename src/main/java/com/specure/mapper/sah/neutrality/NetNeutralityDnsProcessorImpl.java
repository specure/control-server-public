package com.specure.mapper.sah.neutrality;

import com.specure.common.enums.DnsStatus;
import com.specure.common.enums.NetNeutralityTestType;
import com.specure.common.enums.NetworkType;
import com.specure.common.exception.ResourceNotFoundException;
import com.specure.common.model.jpa.neutrality.NetNeutralityDnsSetting;
import com.specure.common.model.jpa.neutrality.NetNeutralitySetting;
import com.specure.common.repository.GeoLocationRepository;
import com.specure.common.repository.NetNeutralitySettingRepository;
import com.specure.common.response.neutrality.crud.NetNeutralityDnsSettingResponse;
import com.specure.common.response.neutrality.crud.NetNeutralitySettingResponse;
import com.specure.common.service.UUIDGenerator;
import com.specure.common.service.digger.DiggerService;
import com.specure.constant.Constants;
import com.specure.constant.ErrorMessage;
import com.specure.enums.NetNeutralityStatus;
import com.specure.mapper.mobile.BasicTestMobileMapper;
import com.specure.mapper.sah.RadioSignalMapper;
import com.specure.model.elastic.neutrality.NetNeutralityDnsResult;
import com.specure.model.elastic.neutrality.NetNeutralityResult;
import com.specure.repository.mobile.SimOperatorRepository;
import com.specure.request.mobile.LocationRequest;
import com.specure.request.sah.neutrality.result.NetNeutralityDnsTestResultRequest;
import com.specure.request.sah.neutrality.result.NetNeutralityMeasurementResultRequest;
import com.specure.request.sah.neutrality.result.NetNeutralityTestResultRequest;
import com.specure.response.sah.neutrality.parameters.NetNeutralityDnsTestParametersResponse;
import com.specure.response.sah.neutrality.parameters.NetNeutralityTestParameterResponse;
import com.specure.response.sah.neutrality.result.NetNeutralityDnsResultResponse;
import com.specure.response.sah.neutrality.result.NetNeutralityResultResponse;
import com.specure.service.admin.MobileModelService;
import com.specure.service.admin.RawProviderService;
import com.specure.service.sah.NationalOperatorService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class NetNeutralityDnsProcessorImpl extends AbstractNetNeutralityProcessor {

    private final UUIDGenerator uuidGenerator;
    private final NetNeutralitySettingRepository netNeutralitySettingRepository;
    private final Clock clock;

    public NetNeutralityDnsProcessorImpl(GeoLocationRepository geoLocationRepository,
                                         NationalOperatorService nationalOperatorService,
                                         SimOperatorRepository simOperatorRepository,
                                         RawProviderService rawProviderService,
                                         DiggerService diggerService,
                                         MobileModelService mobileModelService,
                                         RadioSignalMapper radioSignalMapper,
                                         UUIDGenerator uuidGenerator,
                                         NetNeutralitySettingRepository netNeutralitySettingRepository,
                                         Clock clock) {
        super(geoLocationRepository, nationalOperatorService, simOperatorRepository, rawProviderService, diggerService, mobileModelService, radioSignalMapper);
        this.uuidGenerator = uuidGenerator;
        this.netNeutralitySettingRepository = netNeutralitySettingRepository;
        this.clock = clock;
    }

    @Override
    public NetNeutralityResult toElasticModel(NetNeutralityTestResultRequest netNeutralityTestResultRequest, NetNeutralityMeasurementResultRequest netNeutralityMeasurementResultRequest) {
        NetNeutralityDnsTestResultRequest dnsTestResultRequest = (NetNeutralityDnsTestResultRequest) netNeutralityTestResultRequest;
        NetNeutralityDnsSetting dnsTestParameters = (NetNeutralityDnsSetting) netNeutralitySettingRepository.findByIdAndType(netNeutralityTestResultRequest.getId(), netNeutralityTestResultRequest.getType())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessage.NET_NEUTRALITY_SETTING_NOT_FOUND_BY_ID_AND_TYPE, netNeutralityTestResultRequest.getId(), netNeutralityTestResultRequest.getType()));
        NetNeutralityStatus success;
        String failReason = null;

        if (Strings.isNotBlank(dnsTestParameters.getResolver())
                && !Objects.equals(dnsTestParameters.getResolver(), dnsTestResultRequest.getResolver())) {
            success = NetNeutralityStatus.FAIL;
        } else if (dnsTestResultRequest.getTimeoutExceeded()) {
            success = NetNeutralityStatus.FAIL;
            failReason = ErrorMessage.NET_NEUTRALITY_DNS_FAIL_REASON_TIMEOUT;
        } else if (!isDnsStatusValid(dnsTestResultRequest.getDnsEntries(), dnsTestParameters.getExpectedDnsStatus())
                || !Objects.equals(dnsTestResultRequest.getDnsStatus(), dnsTestParameters.getExpectedDnsStatus())) {
            success = NetNeutralityStatus.FAIL;
            failReason = ErrorMessage.NET_NEUTRALITY_DNS_FAIL_REASON_BAD_STATUS;
        } else if (Strings.isNotBlank(dnsTestParameters.getExpectedDnsEntries())
                && !isDnsEntriesEqual(dnsTestResultRequest.getDnsEntries(), dnsTestParameters)) {
            success = NetNeutralityStatus.FAIL;
            failReason = ErrorMessage.NET_NEUTRALITY_DNS_FAIL_REASON_DIFFERENT_IPS;
        } else {
            success = NetNeutralityStatus.SUCCEED;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ISO_INSTANT;
        String measurementDate = formatter.format(clock.instant());
        return new NetNeutralityDnsResult(uuidGenerator.generateUUID().toString(),
                dnsTestResultRequest.getOpenTestUuid(),
                dnsTestResultRequest.getClientUuid(),
                netNeutralityTestResultRequest.getDurationNs(),
                success,
                measurementDate,
                netNeutralityMeasurementResultRequest.getModel(),
                getDevice(netNeutralityMeasurementResultRequest),
                netNeutralityMeasurementResultRequest.getProduct(),
                netNeutralityMeasurementResultRequest.getNetworkChannelNumber(),
                NetworkType.fromValue(netNeutralityMeasurementResultRequest.getNetworkType()).name(),
                netNeutralityMeasurementResultRequest.getSignalStrength(),
                netNeutralityMeasurementResultRequest.getPlatform(),
                netNeutralityMeasurementResultRequest.getSimMccMnc(),
                netNeutralityMeasurementResultRequest.getSimOperatorName(),
                netNeutralityMeasurementResultRequest.getSimCountry(),
                netNeutralityMeasurementResultRequest.getNetworkMccMnc(),
                netNeutralityMeasurementResultRequest.getNetworkOperatorName(),
                netNeutralityMeasurementResultRequest.getNetworkCountry(),
                netNeutralityMeasurementResultRequest.getNetworkIsRoaming(),
                Optional.ofNullable(netNeutralityMeasurementResultRequest.getLocation()).map(LocationRequest::getCity).orElse(null),
                Optional.ofNullable(netNeutralityMeasurementResultRequest.getLocation()).map(LocationRequest::getCountry).orElse(null),
                getCounty(netNeutralityMeasurementResultRequest),
                Optional.ofNullable(netNeutralityMeasurementResultRequest.getLocation()).map(LocationRequest::getPostalCode).orElse(null),
                netNeutralityMeasurementResultRequest.getMeasurementServerName(),
                netNeutralityMeasurementResultRequest.getMeasurementServerCity(),
                buildLocation(netNeutralityMeasurementResultRequest),
                getIspName(netNeutralityMeasurementResultRequest),
                getRadioSignals(netNeutralityMeasurementResultRequest),
                dnsTestParameters.getTarget(),
                dnsTestResultRequest.getResolver(),
                dnsTestParameters.getResolver(),
                dnsTestParameters.getTimeout(),
                dnsTestResultRequest.getDnsStatus(),
                dnsTestParameters.getExpectedDnsStatus(),
                dnsTestParameters.getEntryType(),
                joinDnsEntries(dnsTestResultRequest.getDnsEntries()),
                dnsTestParameters.getExpectedDnsEntries(),
                dnsTestResultRequest.getTimeoutExceeded(),
                failReason);
    }


    @Override
    public NetNeutralityResultResponse toElasticResponse(NetNeutralityResult netNeutralityResult) {
        NetNeutralityDnsResult netNeutralityDnsResult = (NetNeutralityDnsResult) netNeutralityResult;
        return new NetNeutralityDnsResultResponse(netNeutralityDnsResult.getUuid(),
                netNeutralityDnsResult.getOpenTestUuid(),
                netNeutralityDnsResult.getClientUuid(),
                netNeutralityDnsResult.getDurationNs(),
                netNeutralityDnsResult.getTestStatus(),
                netNeutralityDnsResult.getMeasurementDate(),
                BasicTestMobileMapper.getNetworkType(netNeutralityResult),
                BasicTestMobileMapper.getNetworkName(netNeutralityResult),
                netNeutralityResult.getDevice(),
                BasicTestMobileMapper.getLocation(netNeutralityResult),
                netNeutralityResult.getMeasurementServerName(),
                netNeutralityResult.getMeasurementServerCity(),
                netNeutralityResult.getIspName(),
                netNeutralityResult.getRadioSignals(),
                netNeutralityDnsResult.getTarget(),
                netNeutralityDnsResult.getActualResolver(),
                netNeutralityDnsResult.getExpectedResolver(),
                netNeutralityDnsResult.getTimeout(),
                netNeutralityDnsResult.getActualDnsStatus(),
                netNeutralityDnsResult.getExpectedDnsStatus(),
                netNeutralityDnsResult.getEntryType(),
                netNeutralityDnsResult.getActualDnsResultEntriesFound(),
                netNeutralityDnsResult.getExpectedDnsResultEntriesFound(),
                netNeutralityDnsResult.isTimeoutExceeded(),
                netNeutralityDnsResult.getFailReason());
    }

    @Override
    public NetNeutralityTestType getType() {
        return NetNeutralityTestType.DNS;
    }

    @Override
    public NetNeutralityTestParameterResponse toNetNeutralityTestParameterResponse(NetNeutralitySetting netNeutralitySetting) {
        NetNeutralityDnsSetting netNeutralityDnsSetting = (NetNeutralityDnsSetting) netNeutralitySetting;
        return new NetNeutralityDnsTestParametersResponse(netNeutralityDnsSetting.getId(),
                netNeutralityDnsSetting.getTarget(),
                netNeutralityDnsSetting.getTimeout(),
                netNeutralityDnsSetting.getEntryType(),
                netNeutralityDnsSetting.getResolver());
    }

    @Override
    public NetNeutralitySettingResponse toNetNeutralitySettingResponse(NetNeutralitySetting netNeutralitySetting) {
        NetNeutralityDnsSetting netNeutralityDnsSetting = (NetNeutralityDnsSetting) netNeutralitySetting;
        return new NetNeutralityDnsSettingResponse(netNeutralityDnsSetting.getId(),
                netNeutralityDnsSetting.isActive(),
                netNeutralityDnsSetting.getTarget(),
                netNeutralityDnsSetting.getTimeout(),
                netNeutralityDnsSetting.getEntryType(),
                netNeutralityDnsSetting.getResolver(),
                netNeutralityDnsSetting.getExpectedDnsStatus(),
                netNeutralityDnsSetting.getExpectedDnsEntries());
    }

    private String joinDnsEntries(List<String> dnsEntries) {
        return Optional.ofNullable(dnsEntries)
                .filter(x -> !x.isEmpty())
                .map(x -> String.join(Constants.DNS_ENTRIES_DELIMITER, x))
                .orElse("");
    }

    private boolean isDnsEntriesEqual(List<String> dnsEntries, NetNeutralityDnsSetting dnsTestParameters) {
        String expectedDnsEntries = dnsTestParameters.getExpectedDnsEntries();
        if (Strings.isBlank(expectedDnsEntries)) {
            return true;
        }
        String[] expectedDnsEntriesArray = expectedDnsEntries.split(Constants.DNS_ENTRIES_DELIMITER);
        List<String> actualDnsEntries = dnsEntries.stream()
                .sorted(String::compareTo)
                .collect(Collectors.toList());
        if (expectedDnsEntriesArray.length == 1) {
            return actualDnsEntries.contains(expectedDnsEntriesArray[0]);
        }

        List<String> expectedDnsEntriesList = Arrays.stream(expectedDnsEntriesArray)
                .filter(Strings::isNotBlank)
                .sorted(String::compareTo)
                .collect(Collectors.toList());
        return actualDnsEntries.equals(expectedDnsEntriesList);
    }

    private boolean isDnsStatusValid(List<String> dnsEntries, String dnsStatus) {
        if (DnsStatus.NOERROR.name().equals(dnsStatus) && CollectionUtils.isEmpty(dnsEntries)) {
            return false;
        }
        if (DnsStatus.NXDOMAIN.name().equals(dnsStatus) && !CollectionUtils.isEmpty(dnsEntries)) {
            return false;

        }
        return true;
    }
}
