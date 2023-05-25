package com.specure.mapper.sah.neutrality;

import com.specure.common.enums.NetNeutralityTestType;
import com.specure.common.enums.NetworkType;
import com.specure.common.exception.ResourceNotFoundException;
import com.specure.common.model.jpa.neutrality.NetNeutralitySetting;
import com.specure.common.model.jpa.neutrality.NetNeutralityUdpSetting;
import com.specure.common.repository.GeoLocationRepository;
import com.specure.common.repository.NetNeutralitySettingRepository;
import com.specure.common.response.neutrality.crud.NetNeutralitySettingResponse;
import com.specure.common.response.neutrality.crud.NetNeutralityUdpSettingResponse;
import com.specure.common.service.UUIDGenerator;
import com.specure.common.service.digger.DiggerService;
import com.specure.constant.ErrorMessage;
import com.specure.enums.NetNeutralityStatus;
import com.specure.mapper.mobile.BasicTestMobileMapper;
import com.specure.mapper.sah.RadioSignalMapper;
import com.specure.model.elastic.neutrality.NetNeutralityResult;
import com.specure.model.elastic.neutrality.NetNeutralityUdpResult;
import com.specure.repository.mobile.SimOperatorRepository;
import com.specure.request.mobile.LocationRequest;
import com.specure.request.sah.neutrality.result.NetNeutralityMeasurementResultRequest;
import com.specure.request.sah.neutrality.result.NetNeutralityTestResultRequest;
import com.specure.request.sah.neutrality.result.NetNeutralityUdpTestResultRequest;
import com.specure.response.sah.neutrality.parameters.NetNeutralityTestParameterResponse;
import com.specure.response.sah.neutrality.parameters.NetNeutralityUdpTestParametersResponse;
import com.specure.response.sah.neutrality.result.NetNeutralityResultResponse;
import com.specure.response.sah.neutrality.result.NetNeutralityUdpResultResponse;
import com.specure.service.admin.MobileModelService;
import com.specure.service.admin.RawProviderService;
import com.specure.service.sah.NationalOperatorService;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Component
public class NetNeutralityUdpProcessorImpl extends AbstractNetNeutralityProcessor {

    private final UUIDGenerator uuidGenerator;
    private final NetNeutralitySettingRepository netNeutralitySettingRepository;
    private final Clock clock;

    public NetNeutralityUdpProcessorImpl(GeoLocationRepository geoLocationRepository,
                                         NationalOperatorService nationalOperatorService,
                                         SimOperatorRepository simOperatorRepository,
                                         RawProviderService rawProviderService,
                                         MobileModelService mobileModelService,
                                         RadioSignalMapper radioSignalMapper,
                                         DiggerService diggerService,
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
        NetNeutralityUdpTestResultRequest netNeutralityUdpTestResultRequest = (NetNeutralityUdpTestResultRequest) netNeutralityTestResultRequest;
        NetNeutralityUdpSetting netNeutralityUdpSetting = (NetNeutralityUdpSetting) netNeutralitySettingRepository.findByIdAndType(netNeutralityTestResultRequest.getId(), netNeutralityTestResultRequest.getType())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessage.NET_NEUTRALITY_SETTING_NOT_FOUND_BY_ID_AND_TYPE, netNeutralityTestResultRequest.getId(), netNeutralityUdpTestResultRequest.getType()));
        NetNeutralityStatus success = NetNeutralityStatus.FAIL;

        if (netNeutralityUdpTestResultRequest.getNumberOfPacketsReceived() >= netNeutralityUdpSetting.getMinNumberOfPacketsReceived()) {
            success = NetNeutralityStatus.SUCCEED;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ISO_INSTANT;
        String measurementDate = formatter.format(clock.instant());

        return new NetNeutralityUdpResult(uuidGenerator.generateUUID().toString(),
                netNeutralityTestResultRequest.getOpenTestUuid(),
                netNeutralityTestResultRequest.getClientUuid(),
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
                netNeutralityUdpSetting.getPortNumber(),
                netNeutralityUdpTestResultRequest.getNumberOfPacketsReceived(),
                netNeutralityUdpSetting.getNumberOfPacketsSent(),
                netNeutralityUdpSetting.getMinNumberOfPacketsReceived());
    }

    @Override
    public NetNeutralityResultResponse toElasticResponse(NetNeutralityResult netNeutralityResult) {
        NetNeutralityUdpResult netNeutralityUdpResult = (NetNeutralityUdpResult) netNeutralityResult;
        return new NetNeutralityUdpResultResponse(netNeutralityUdpResult.getUuid(),
                netNeutralityUdpResult.getOpenTestUuid(),
                netNeutralityUdpResult.getClientUuid(),
                netNeutralityResult.getDurationNs(),
                netNeutralityUdpResult.getTestStatus(),
                netNeutralityUdpResult.getMeasurementDate(),
                BasicTestMobileMapper.getNetworkType(netNeutralityResult),
                BasicTestMobileMapper.getNetworkName(netNeutralityResult),
                netNeutralityResult.getDevice(),
                BasicTestMobileMapper.getLocation(netNeutralityResult),
                netNeutralityResult.getMeasurementServerName(),
                netNeutralityResult.getMeasurementServerCity(),
                netNeutralityResult.getIspName(),
                netNeutralityResult.getRadioSignals(),
                netNeutralityUdpResult.getPortNumber(),
                netNeutralityUdpResult.getNumberOfPacketsSent(),
                netNeutralityUdpResult.getActualNumberOfPacketsReceived(),
                netNeutralityUdpResult.getExpectedMinNumberOfPacketsReceived());
    }

    @Override
    public NetNeutralityTestType getType() {
        return NetNeutralityTestType.UDP;
    }

    @Override
    public NetNeutralityTestParameterResponse toNetNeutralityTestParameterResponse(NetNeutralitySetting netNeutralitySetting) {
        NetNeutralityUdpSetting udpTestJpa = (NetNeutralityUdpSetting) netNeutralitySetting;
        return new NetNeutralityUdpTestParametersResponse(udpTestJpa.getId(), udpTestJpa.getTimeout(), udpTestJpa.getPortNumber(), udpTestJpa.getNumberOfPacketsSent(), udpTestJpa.getMinNumberOfPacketsReceived());
    }

    @Override
    public NetNeutralitySettingResponse toNetNeutralitySettingResponse(NetNeutralitySetting netNeutralitySetting) {
        NetNeutralityUdpSetting netNeutralityUdpSetting = (NetNeutralityUdpSetting) netNeutralitySetting;
        return new NetNeutralityUdpSettingResponse(netNeutralityUdpSetting.getId(),
                netNeutralityUdpSetting.isActive(),
                netNeutralityUdpSetting.getTimeout(),
                netNeutralityUdpSetting.getPortNumber(),
                netNeutralityUdpSetting.getNumberOfPacketsSent(),
                netNeutralityUdpSetting.getMinNumberOfPacketsReceived());
    }
}
