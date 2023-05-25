package com.specure.service.sah.impl;

import com.specure.common.enums.ClientType;
import com.specure.common.exception.MeasurementNotFoundByUuidException;
import com.specure.common.model.elastic.BasicTest;
import com.specure.common.model.jpa.Measurement;
import com.specure.common.model.jpa.MeasurementDetails;
import com.specure.common.model.jpa.MeasurementServer;
import com.specure.constant.Constants;
import com.specure.common.enums.NetworkType;
import com.specure.exception.MeasurementHistoryNotAccessibleException;
import com.specure.mapper.core.SpeedDetailMapper;
import com.specure.multitenant.MultiTenantManager;
import com.specure.repository.BasicTestRepository;
import com.specure.common.repository.GeoLocationRepository;
import com.specure.request.mobile.MeasurementHistoryMobileRequest;
import com.specure.response.core.MeasurementHistoryResponse;
import com.specure.response.core.measurement.response.SpeedDetailResponse;
import com.specure.response.core.settings.HistorySettingsResponse;
import com.specure.response.sah.RadioSignalResponse;
import com.specure.service.core.FieldAnonymizerFilter;
import com.specure.service.core.MeasurementServerService;
import com.specure.service.mobile.LoopModeSettingsService;
import com.specure.service.sah.BasicTestService;
import com.specure.service.sah.NationalOperatorService;
import com.specure.service.sah.RadioSignalService;
import com.specure.service.sah.SpeedDetailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.InetAddressValidator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.geo.GeoJsonPoint;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service("basicTestService")
@RequiredArgsConstructor
public class BasicTestServiceImpl implements BasicTestService {

    private final BasicTestRepository basicTestRepository;
    @Qualifier(value = "basicMeasurementServerService")
    private final MeasurementServerService basicMeasurementServerService;
    private final MultiTenantManager multiTenantManager;
    private final GeoLocationRepository geoLocationRepository;
    private final LoopModeSettingsService loopModeSettingsService;
    private final FieldAnonymizerFilter fieldAnonymizerFilter;
    private final RadioSignalService radioSignalService;
    private final NationalOperatorService nationalOperatorService;
    private final SpeedDetailMapper speedDetailMapper;
    private final SpeedDetailService speedDetailService;

    @Override
    public BasicTest getBasicTestByUUID(String uuid) {
        log.debug("BasicTestServiceImpl:getBasicTestByUUID started with tenant = {}, uuid = {}", multiTenantManager.getCurrentTenant(), uuid);
        BasicTest basicTest = basicTestRepository.findByUUID(uuid);
        log.debug("BasicTestServiceImpl:getBasicTestByUUID finished with tenant = {}, basicTest = {}", multiTenantManager.getCurrentTenant(), basicTest);
        return basicTest;
    }

    @Override
    public MeasurementHistoryResponse getMeasurementDetailByUuidFromElasticSearch(String uuid) throws InterruptedException {
        log.debug("BasicTestServiceImpl:getMeasurementDetailByUuidFromElasticSearch started with tenant = {}, uuid ={}", multiTenantManager.getCurrentTenant(), uuid);
        BasicTest alternative;

        try {
            alternative = getBasicTestByUUID(uuid);
        } catch (MeasurementNotFoundByUuidException e) {
            // ES index is updated 1sec by default,
            // FE sends GET measurement request just after save
            // let's wait a little
            Thread.sleep(1000);
            alternative = getBasicTestByUUID(uuid);
        }
        log.debug("alternative = " + alternative);

        Float jitter = 0F;
        if (Objects.nonNull(alternative.getJitter())) {
            jitter = alternative.getJitter();
        }

        long ping = 0L;
        if (Objects.nonNull(alternative.getPing())) {
            ping = (long) (alternative.getPing() * 1000000F);
        }
        List<SpeedDetailResponse> speedDetails = Optional.ofNullable(alternative.getSpeedDetail())
                .orElse(speedDetailService.getSpeedDetailsBy(uuid))
                .stream()
                .map(speedDetailMapper::simpleSpeedDetailToSpeedDetailResponse)
                .collect(Collectors.toList());

        MeasurementHistoryResponse measurementHistoryResponse = MeasurementHistoryResponse.builder()
                .measurementServerName(alternative.getMeasurementServerName())
                .time(alternative.getTimestamp())
                .ipAddress(fieldAnonymizerFilter.getIpAddressFilter(alternative.getIpAddress(), alternative.getOpenTestUuid()))
                .clientProvider(alternative.getClientProvider())
                .speedDetail(Collections.emptyList())
                .geoLocations(Collections.emptyList())
                .jpl(new HashMap<>())
                .pings(Collections.emptyList())
                .signals(Collections.emptyList())
                .speedDownload(alternative.getDownload())
                .speedUpload(alternative.getUpload())
                .testSpeedDownload(alternative.getDownload())
                .testSpeedUpload(alternative.getUpload())
                .voip_result_jitter(jitter.toString())
                .openTestUuid(alternative.getOpenTestUuid())
                .numThreadsUl(alternative.getNumThreads())
                .speedDetail(speedDetails)
                .pingMedian(ping)
                .platform(alternative.getPlatform())
                .appVersion(alternative.getAppVersion())
                .build();
        log.debug("BasicTestServiceImpl:getMeasurementDetailByUuidFromElasticSearch finished with tenant = {}, measurementHistoryResponse = {}", multiTenantManager.getCurrentTenant(), measurementHistoryResponse);
        return measurementHistoryResponse;
    }

    @Override
    public Page<BasicTest> getFilteredBasicTestsByMeasurementHistoryMobileRequest(Pageable pageable, MeasurementHistoryMobileRequest request) {
        log.debug("BasicTestServiceImpl:getFilteredBasicTestsByMeasurementHistoryMobileRequest started with tenant = {}, pageable = {}, measurementHistoryMobileRequest = {}", multiTenantManager.getCurrentTenant(), pageable, request);
        if (Objects.isNull(request.getClientUuid()) || request.getClientUuid().isBlank()) {
            throw new MeasurementHistoryNotAccessibleException();
        }
        Page<BasicTest> basicTests = basicTestRepository.findByMeasurementHistoryMobileRequest(request, pageable);
        log.debug("BasicTestServiceImpl:getFilteredBasicTestsByMeasurementHistoryMobileRequest finished with tenant = {}, basicTests = {}", multiTenantManager.getCurrentTenant(), basicTests);
        return basicTests;
    }

    @Override
    public Page<List<BasicTest>> getFilteredLoopUuidAggregatedBasicTestsByMeasurementHistoryMobileRequest(Pageable pageable, MeasurementHistoryMobileRequest request) {
        log.debug("BasicTestServiceImpl:getFilteredLoopUuidAggregatedBasicTestsByMeasurementHistoryMobileRequest started with tenant = {}, pageable = {}, measurementHistoryMobileRequest = {}", multiTenantManager.getCurrentTenant(), pageable, request);
        if (Objects.isNull(request.getClientUuid()) || request.getClientUuid().isBlank()) {
            throw new MeasurementHistoryNotAccessibleException();
        }
        Page<List<BasicTest>> basicTests = basicTestRepository.findByMeasurementLoopUuidAggregatedHistoryMobileRequest(request, pageable);
        log.debug("BasicTestServiceImpl:getFilteredLoopUuidAggregatedBasicTestsByMeasurementHistoryMobileRequest finished with tenant = {}, basicTests = {}", multiTenantManager.getCurrentTenant(), basicTests);
        return basicTests;
    }

    @Override
    public void saveMeasurementToElastic(Measurement measurement) {
        log.info("BasicTestServiceImpl:saveMeasurementToElastic started with tenant = {}, measurement = {}", multiTenantManager.getCurrentTenant(), measurement);
        saveToElastic(measurement, BasicTest.builder());
        log.debug("BasicTestServiceImpl:saveMeasurementToElastic finished with tenant = {}", multiTenantManager.getCurrentTenant());
    }

    @Override
    public void saveMeasurementMobileToElastic(Measurement measurement) {
        log.info("BasicTestServiceImpl:saveMeasurementMobileToElastic started with tenant = {}, measurement = {}", multiTenantManager.getCurrentTenant(), measurement);
        BasicTest.BasicTestBuilder basicTestBuilder = BasicTest.builder()
                .clientType(ClientType.MOBILE.name());
        saveToElastic(measurement, basicTestBuilder);
        log.debug("BasicTestServiceImpl:saveMeasurementMobileToElastic finished with tenant = {}", multiTenantManager.getCurrentTenant());
    }

    @Override
    public HistorySettingsResponse getHistoryResponseByClientUuid(UUID uuid) {
        log.debug("BasicTestServiceImpl:getHistoryResponseByClientUuid started with tenant = {}, uuid = {}", multiTenantManager.getCurrentTenant(), uuid);
        if (Objects.nonNull(uuid)) {
            HistorySettingsResponse historySettingsResponse = basicTestRepository.getDistinctDevicesAndNetworkTypesByClientUuid(uuid.toString());
            log.debug("BasicTestServiceImpl:getHistoryResponseByClientUuid finished with tenant = {}, historySettingsResponse = {}", multiTenantManager.getCurrentTenant(), historySettingsResponse);
            return historySettingsResponse;
        }
        HistorySettingsResponse historySettingsResponse = HistorySettingsResponse.builder()
                .devices(Collections.emptyList())
                .networks(Collections.emptyList())
                .build();
        log.debug("BasicTestServiceImpl:getHistoryResponseByClientUuid finished with tenant = {}, historySettingsResponse = {}", multiTenantManager.getCurrentTenant(), historySettingsResponse);
        return historySettingsResponse;
    }

    private String getNetworkOperator(Measurement measurement) {
        for (Map.Entry<String, List<String>> entry : nationalOperatorService.getNationalOperatorsAliases().entrySet()) {
            if (entry.getValue().contains(measurement.getNetworkOperator())) {
                return entry.getKey();
            }
            if (entry.getValue().contains(measurement.getNetworkOperatorName())) {
                return entry.getKey();
            }
        }
        return measurement.getNetworkOperator() != null && !measurement.getNetworkOperator().equalsIgnoreCase("unknown")
                ? measurement.getNetworkOperator()
                : measurement.getNetworkOperatorName();
    }

    private void saveToElastic(Measurement measurement, BasicTest.BasicTestBuilder basicTestBuilder) {
        log.debug("BasicTestServiceImpl:saveToElastic started with tenant = {}, request = {}", multiTenantManager.getCurrentTenant(), measurement.toString());
        Float packetLoss = null;//100.0F;
        try {
            if (measurement.getVoip_result_packet_loss() != null && !measurement.getVoip_result_packet_loss().isEmpty())
                packetLoss = Float.parseFloat(measurement.getVoip_result_packet_loss().trim());
        } catch (NumberFormatException nfe) {
            log.error("NumberFormatException: " + nfe.getMessage());
        }
        Float jitter = null;//-1;
        try {
            if (measurement.getVoip_result_jitter() != null && !measurement.getVoip_result_jitter().isEmpty())
                jitter = Float.parseFloat(measurement.getVoip_result_jitter());
        } catch (NumberFormatException nfe) {
            log.error("NumberFormatException: " + nfe.getMessage());
        }

        basicTestBuilder
                .internetProtocol(getInternetProtocol(measurement))
                .status(measurement.getStatus().name())
                .openTestUuid(measurement.getOpenTestUuid())
                .clientUuid(measurement.getClientUuid())
                .clientProvider(measurement.getClientProvider())
                .networkMccMnc(measurement.getNetworkMccMnc())
                .ispRawId(measurement.getIspRawId())
                .ispName(measurement.getIspName())
                .asn(measurement.getAsn())
                .operator(getNetworkOperator(measurement))
                .download(measurement.getSpeedDownload())
                .upload(measurement.getSpeedUpload())
                .signal(measurement.getSignalStrength())
                .lte_rsrp(measurement.getLte_rsrp())
                .lte_rsrq(measurement.getLte_rsrq())
                .simCount(measurement.getSimCount())
                .dualSim(measurement.getDualSim())
                .wifiSsid(measurement.getWifiSsid())
                .model(measurement.getModel())
                .clientName(measurement.getClientName())
                .clientVersion(measurement.getClientVersion())
                .appVersion(Optional.ofNullable(measurement.getMeasurementDetails()).map(MeasurementDetails::getAppVersion).orElse(null))
                .numThreads(measurement.getTestNumThreads())
                .testNumThreadsUl(measurement.getNumThreadsUl())
                .tag(measurement.getTag())
                .device(measurement.getDevice())
                .manufacturer(Optional.ofNullable(measurement.getMeasurementDetails()).map(MeasurementDetails::getMobileModelManufacturer).orElse(null))
                .category(Optional.ofNullable(measurement.getMeasurementDetails()).map(MeasurementDetails::getMobileModelCategory).orElse(null))
                .browserName(measurement.getBrowserName())
                .networkType(NetworkType.fromValue(measurement.getNetworkType()).toString())
                .platform(measurement.getPlatform().name())
                .ipAddress(measurement.getIpAddress())
                .city(Optional.ofNullable(measurement.getMeasurementDetails()).map(MeasurementDetails::getCity).orElse(null))
                .country(Optional.ofNullable(measurement.getMeasurementDetails()).map(MeasurementDetails::getCountry).orElse(null))
                .county(Optional.ofNullable(measurement.getMeasurementDetails()).map(MeasurementDetails::getCounty).orElse(null))
                .postalCode(Optional.ofNullable(measurement.getMeasurementDetails()).map(MeasurementDetails::getPostalCode).orElse(null))
                .simMccMnc(Optional.ofNullable(measurement.getMeasurementDetails()).map(MeasurementDetails::getSimMccMnc).orElse(null))
                .simOperatorName(Optional.ofNullable(measurement.getMeasurementDetails()).map(MeasurementDetails::getSimOperatorName).orElse(null))
                .networkIsRoaming(Optional.ofNullable(measurement.getMeasurementDetails()).map(MeasurementDetails::getNetworkIsRoaming).orElse(null))
                .networkOperatorName(Optional.ofNullable(measurement.getMeasurementDetails()).map(MeasurementDetails::getNetworkOperatorName).orElse(null))
                .networkCountry(Optional.ofNullable(measurement.getMeasurementDetails()).map(MeasurementDetails::getNetworkCountry).orElse(null))
                .simCountry(Optional.ofNullable(measurement.getMeasurementDetails()).map(MeasurementDetails::getSimCountry).orElse(null))
                .loopModeUuid(loopModeSettingsService.getLoopModeSettingsUuidByOpenTestUuid(measurement.getOpenTestUuid()))
                .telephonyPermissionGranted(Optional.ofNullable(measurement.getMeasurementDetails()).map(MeasurementDetails::getTelephonyPermissionGranted).orElse(null))
                .uuidPermissionGranted(Optional.ofNullable(measurement.getMeasurementDetails()).map(MeasurementDetails::getUuidPermissionGranted).orElse(null))
                .locationPermissionGranted(Optional.ofNullable(measurement.getMeasurementDetails()).map(MeasurementDetails::getLocationPermissionGranted).orElse(null));

        if (measurement.getMeasurementServerId() != null) {
            MeasurementServer measurementServer = this.basicMeasurementServerService.getMeasurementServerById(measurement.getMeasurementServerId());
            basicTestBuilder.measurementServerId(measurementServer.getId());
            basicTestBuilder.measurementServerName(measurementServer.getName());
            basicTestBuilder.measurementServerCity(measurementServer.getMeasurementServerDescription().getCity());
        }

        if (measurement.getTime() != null) {
            basicTestBuilder.timestamp(String.valueOf(measurement.getTime().getTime()));
            basicTestBuilder.measurementDate(measurement.getTimeStampToElasticIso());
            basicTestBuilder.graphHour(measurement.getHourOfMeasurement());
        }

        if (measurement.getPingMedian() != null) {
            basicTestBuilder.ping((float) measurement.getPingMedian() / 1000000);
        }

        if (measurement.getVoip_result_jitter() != null) {
            basicTestBuilder.jitter(jitter);
        }

        if (measurement.getVoip_result_packet_loss() != null) {
            basicTestBuilder.packetLoss(packetLoss);
        }

        if (measurement.getServerType() != null) {
            basicTestBuilder.serverType(measurement.getServerType());
        }

        if (!Objects.isNull(measurement.getAdHocCampaign())) {
            basicTestBuilder.adHocCampaign(measurement.getAdHocCampaign().getId().trim());
        }

        if (Objects.nonNull(measurement.getLatitude()) && Objects.nonNull(measurement.getLongitude())) {
            GeoPoint geoPoint = new GeoPoint(measurement.getLatitude(), measurement.getLongitude());
            basicTestBuilder.location(geoPoint);
            basicTestBuilder.geo_shape_location(GeoJsonPoint.of(geoPoint));

            geoLocationRepository.getGeoShapeByCoordinates(measurement.getLongitude(), measurement.getLatitude())
                    .ifPresent(geoShape -> {
                        basicTestBuilder.county(geoShape.getCountySq());
                        basicTestBuilder.municipality(geoShape.getMunicipalitySq());
                    });
        }
        setRadioSignal(basicTestBuilder, measurement.getOpenTestUuid());

        BasicTest basicTest = basicTestBuilder.build();
        updateBasicTestWithProbe(measurement.getTag(), measurement.getDevice(), basicTest);
        log.debug("BasicTestServiceImpl:saveToElastic finished with tenant = {}, basicTest = {}", multiTenantManager.getCurrentTenant(), basicTest);
        basicTestRepository.save(basicTest);
    }

    private String getInternetProtocol(Measurement measurement) {
        InetAddressValidator inetAddressValidator = InetAddressValidator.getInstance();
        final String cachedIpAddress = fieldAnonymizerFilter.getIpAddressFilter(measurement.getIpAddress(), measurement.getOpenTestUuid());

        if (inetAddressValidator.isValidInet6Address(cachedIpAddress)) {
            return Constants.IP_V6;
        }
        if (inetAddressValidator.isValidInet4Address(cachedIpAddress)) {
            return Constants.IP_V4;
        }
        log.info("Internet protocol is undefined for ip address '{}'", cachedIpAddress);

        return null;
    }

    private void setRadioSignal(BasicTest.BasicTestBuilder basicTestBuilder, String openTestUuid) {
        List<RadioSignalResponse> radioSignals = radioSignalService.getRadioSignalsByOpenTestUuid(openTestUuid);
        basicTestBuilder.radioSignals(radioSignals);
    }

    protected void updateBasicTestWithProbe(String probePortName, String probeId, BasicTest basicTest) {
        //do nothing
    }
}
