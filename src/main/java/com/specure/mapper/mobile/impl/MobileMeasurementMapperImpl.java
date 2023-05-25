package com.specure.mapper.mobile.impl;

import com.specure.common.enums.MeasurementStatus;
import com.specure.common.model.jpa.*;
import com.specure.mapper.core.GeoLocationMapper;
import com.specure.mapper.core.PingMapper;
import com.specure.mapper.core.SpeedDetailMapper;
import com.specure.mapper.mobile.MobileMeasurementMapper;
import com.specure.model.jpa.SimOperator;
import com.specure.repository.mobile.SimOperatorRepository;
import com.specure.request.mobile.MeasurementResultMobileRequest;
import com.specure.service.admin.MobileModelService;
import com.specure.service.core.FieldAnonymizerFilter;
import com.specure.service.sah.RadioSignalService;
import com.specure.service.sah.SpeedDetailService;
import com.specure.utils.core.MeasurementCalculatorUtil;
import com.specure.utils.mobile.MobileModelUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MobileMeasurementMapperImpl implements MobileMeasurementMapper {

    private final PingMapper pingMapper;
    private final SpeedDetailMapper speedDetailMapper;
    private final GeoLocationMapper geoLocationMapper;
    private final RadioSignalService radioSignalService;
    private final SimOperatorRepository simOperatorRepository;
    private final MobileModelService mobileModelService;
    private final FieldAnonymizerFilter fieldAnonymizerFilter;
    private final SpeedDetailService speedDetailService;

    @Override
    public Measurement measurementMobileResultRequestToMeasurement(MeasurementResultMobileRequest measurementRequest, Measurement measurement) {
        String openTestUuid = measurementRequest.getTestToken().split("_")[0];

        double speedDownload = MeasurementCalculatorUtil.getSpeedBitPerSec(measurementRequest.getTestBytesDownload(), measurementRequest.getDownloadDurationNanos()) / 1e3;
        double speedUpload = MeasurementCalculatorUtil.getSpeedBitPerSec(measurementRequest.getTestBytesUpload(), measurementRequest.getUploadDurationNanos()) / 1e3;

        List<Ping> pings = Collections.emptyList();
        if (measurementRequest.getPings() != null) {
            pings = measurementRequest.getPings()
                    .stream()
                    .map(pingMapper::pingRequestToPing)
                    .collect(Collectors.toList());
        }
        pings.forEach(p -> p.setMeasurement(measurement));
        List<SpeedDetail> details = Collections.emptyList();
        if (measurementRequest.getSpeedDetails() != null) {
            details = measurementRequest.getSpeedDetails()
                    .stream()
                    .map(speedDetailMapper::speedDetailRequestToSpeedDetail)
                    .collect(Collectors.toList());
        }
        details.forEach(d -> d.setMeasurement(measurement));
        speedDetailService.saveSpeedDetailsToCache(openTestUuid, details);
        List<GeoLocation> geoLocations = Collections.emptyList();
        if (measurementRequest.getGeoLocations() != null) {
            geoLocations = measurementRequest.getGeoLocations()
                    .stream()
                    .map(geoLocationMapper::geoLocationRequestToGeoLocation)
                    .collect(Collectors.toList());
        }
        geoLocations.forEach(g -> g.setMeasurement(measurement));

        if (Objects.nonNull(measurementRequest.getRadioInfo())) {
            radioSignalService.processRadioSignal(measurementRequest.getRadioInfo().getSignals(), measurement);
        }
        measurement.getMeasurementDetails().setSimMccMnc(measurementRequest.getSimMccMnc());
        measurement.getMeasurementDetails().setSimOperatorName(measurementRequest.getSimOperatorName());
        measurement.getMeasurementDetails().setSimCountry(measurementRequest.getSimCountry());
        measurement.getMeasurementDetails().setNetworkIsRoaming(measurementRequest.isNetworkIsRoaming());
        measurement.getMeasurementDetails().setNetworkOperatorName(measurementRequest.getNetworkOperatorName());
        measurement.getMeasurementDetails().setNetworkCountry(measurementRequest.getNetworkCountry());

        Optional<MobileModel> mobileModel = mobileModelService.getAndSaveNewIfNotExistMobileModelByModel(measurementRequest.getModel());
        measurement.getMeasurementDetails().setMobileModelCategory(MobileModelUtils.extractCategory(mobileModel));
        measurement.getMeasurementDetails().setMobileModelManufacturer(MobileModelUtils.extractManufacturer(mobileModel));

        return measurement.toBuilder()
                .pingMedian(MeasurementCalculatorUtil.median(measurementRequest.getPings(), measurementRequest.getPingShortest()))
                .token(measurementRequest.getTestToken())
                .openTestUuid(openTestUuid)
                .time(new Timestamp(measurementRequest.getTime()))
                .speedDownload(measurementRequest.getDownloadSpeed())
                .speedUpload(measurementRequest.getUploadSpeed())
                .device(MobileModelUtils.extractDevice(mobileModel, measurementRequest.getModel()))
                .tag(measurementRequest.getTag())
                .clientLanguage(measurementRequest.getClientLanguage())
                .clientName(measurementRequest.getClientName().getName())
                .clientVersion(measurementRequest.getClientVersion())
                .model(measurementRequest.getModel())
                .platform(MeasurementCalculatorUtil.getPlatform(measurementRequest.getPlatform(), measurementRequest.getModel()))
                .product(measurementRequest.getProduct())
                .testBytesDownload(measurementRequest.getTestBytesDownload())
                .testBytesUpload(measurementRequest.getTestBytesUpload())
                .testNsecDownload(measurementRequest.getDownloadDurationNanos())
                .testNsecUpload(measurementRequest.getUploadDurationNanos())
                .testNumThreads(measurementRequest.getTestNumThreads())
                .testPingShortest(measurementRequest.getPingShortest())
                .testSpeedDownload((int) speedDownload)
                .testSpeedUpload((int) speedUpload)
                .wifiSsid(fieldAnonymizerFilter.saveWifiSsidFilter(measurementRequest.getWifiSSID(), openTestUuid))
                .wifiBssid(measurementRequest.getWifiBSSID())
                .simCount(measurementRequest.getTelephonySimCount())
                .networkOperatorName(getNetworkOperatorName(measurementRequest))
                .networkType(measurementRequest.getNetworkType())
                .dualSim(measurementRequest.isDualSim())
                .pings(pings)
                .speedDetail(details)
                .geoLocations(geoLocations)
                .status(getStatus(measurementRequest))
                .voip_result_packet_loss(measurementRequest.getPacketLoss())
                .voip_result_jitter(measurementRequest.getJitter())
                .build();
    }

    private String getNetworkOperatorName(MeasurementResultMobileRequest measurementRequest) {
        if (Objects.nonNull(measurementRequest.getNetworkOperatorName())) {
            return measurementRequest.getNetworkOperatorName();
        } else {
            String simOperatorByCode = getNetworkOperatorNameByCode(measurementRequest);
            if (Objects.nonNull(simOperatorByCode)) {
                return simOperatorByCode;
            } else {
                return "unknown";
            }
        }
    }

    private String getNetworkOperatorNameByCode(MeasurementResultMobileRequest measurementRequest) {
        return Optional.ofNullable(measurementRequest)
                .map(MeasurementResultMobileRequest::getNetworkMccMnc)
                .flatMap(x -> simOperatorRepository.findByMccMncOrderByValidFromDesc(x).stream().findFirst())
                .map(SimOperator::getName)
                .orElse(null);
    }

    private MeasurementStatus getStatus(MeasurementResultMobileRequest resultRequest) {
        if (Objects.nonNull(resultRequest.getTestStatus())) {
            switch (resultRequest.getTestStatus()) {
                case "0":
                case "SUCCESS":
                    return MeasurementStatus.FINISHED;
                case "1":
                case "ERROR":
                    return MeasurementStatus.ERROR;
                case "2":
                case "ABORTED":
                    return MeasurementStatus.ABORTED;
            }
        }
        return MeasurementStatus.FINISHED;
    }
}
