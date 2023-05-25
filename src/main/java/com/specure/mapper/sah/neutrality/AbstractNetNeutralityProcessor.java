package com.specure.mapper.sah.neutrality;

import com.specure.common.model.jpa.MobileModel;
import com.specure.common.model.jpa.RawProvider;
import com.specure.common.repository.GeoLocationRepository;
import com.specure.common.service.digger.DiggerService;
import com.maxmind.geoip2.model.AbstractCountryResponse;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.Traits;
import com.specure.constant.ErrorMessage;
import com.specure.mapper.sah.RadioSignalMapper;
import com.specure.model.elastic.GeoShape;
import com.specure.model.jpa.SimOperator;
import com.specure.repository.mobile.SimOperatorRepository;
import com.specure.request.mobile.LocationRequest;
import com.specure.request.sah.neutrality.result.NetNeutralityMeasurementResultRequest;
import com.specure.response.sah.RadioSignalResponse;
import com.specure.response.sah.neutrality.RadioInfoRequest;
import com.specure.service.admin.MobileModelService;
import com.specure.service.admin.RawProviderService;
import com.specure.service.sah.NationalOperatorService;
import com.specure.utils.mobile.MobileModelUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
public abstract class AbstractNetNeutralityProcessor implements NetNeutralityProcessor {

    private final GeoLocationRepository geoLocationRepository;
    private final NationalOperatorService nationalOperatorService;
    private final SimOperatorRepository simOperatorRepository;
    private final RawProviderService rawProviderService;
    private final DiggerService diggerService;
    private final MobileModelService mobileModelService;
    private final RadioSignalMapper radioSignalMapper;

    protected GeoPoint buildLocation(NetNeutralityMeasurementResultRequest netNeutralityMeasurementResultRequest) {
        return Optional.ofNullable(netNeutralityMeasurementResultRequest)
                .map(NetNeutralityMeasurementResultRequest::getLocation)
                .filter(x -> Objects.nonNull(x.getLatitude()) && Objects.nonNull(x.getLongitude()))
                .map(x -> new GeoPoint(x.getLatitude(), x.getLongitude()))
                .orElse(null);
    }

    protected String getIspName(NetNeutralityMeasurementResultRequest netNeutralityMeasurementResultRequest) {
        final String simRawProviderCountry = netNeutralityMeasurementResultRequest.getSimCountry();
        final String simRawProviderName = netNeutralityMeasurementResultRequest.getSimOperatorName();
        final String simRawProviderMccMnc = netNeutralityMeasurementResultRequest.getSimMccMnc();
        String networkOperator;
        String ispName;

        var cityResponse = diggerService.getCityResponseByIpAddress(netNeutralityMeasurementResultRequest.getTestIpLocal());
        if (Objects.nonNull(simRawProviderCountry) && Objects.nonNull(simRawProviderName)) {
            RawProvider rawProvider = rawProviderService.getRawProvider(simRawProviderCountry, simRawProviderName, simRawProviderMccMnc);
            networkOperator = simRawProviderName;
            ispName = Optional.ofNullable(rawProvider.getAlias())
                    .orElse(rawProvider.getRawName());

        } else {
            Long asn = cityResponse
                    .map(CityResponse::getTraits)
                    .map(Traits::getAutonomousSystemNumber)
                    .orElseGet(() -> diggerService.digASN(netNeutralityMeasurementResultRequest.getTestIpLocal()));

            networkOperator = cityResponse
                    .map(AbstractCountryResponse::getTraits)
                    .map(Traits::getIsp)
                    .or(() -> Optional.ofNullable(diggerService.getProviderByASN(asn)))
                    .orElse(ErrorMessage.UNKNOWN_PROVIDER);
            ispName = cityResponse
                    .map(x -> {
                        RawProvider rawProvider = rawProviderService.getRawProvider(x, asn, netNeutralityMeasurementResultRequest.getNetworkMccMnc());
                        return Optional.ofNullable(rawProvider.getAlias())
                                .orElse(rawProvider.getRawName());
                    })
                    .orElse(null);
        }
        if (Objects.nonNull(ispName)) {
            return ispName;
        }

        var networkOperatorName = getNetworkOperatorName(netNeutralityMeasurementResultRequest);
        for (Map.Entry<String, List<String>> entry : nationalOperatorService.getNationalOperatorsAliases().entrySet()) {
            if (entry.getValue().contains(networkOperator)) {
                return entry.getKey();
            }
            if (entry.getValue().contains(networkOperatorName)) {
                return entry.getKey();
            }
        }
        return !networkOperator.equalsIgnoreCase("unknown")
                ? networkOperator
                : networkOperatorName;
    }

    private String getNetworkOperatorName(NetNeutralityMeasurementResultRequest netNeutralityMeasurementResultRequest) {
        if (Objects.nonNull(netNeutralityMeasurementResultRequest.getNetworkOperatorName())) {
            return netNeutralityMeasurementResultRequest.getNetworkOperatorName();
        } else {
            String simOperatorByCode = getNetworkOperatorNameByCode(netNeutralityMeasurementResultRequest);
            if (Objects.nonNull(simOperatorByCode)) {
                return simOperatorByCode;
            } else {
                return "unknown";
            }
        }
    }

    protected String getCounty(NetNeutralityMeasurementResultRequest netNeutralityMeasurementResultRequest) {
        LocationRequest locationRequest = netNeutralityMeasurementResultRequest.getLocation();
        if (Objects.nonNull(locationRequest) && Objects.nonNull(locationRequest.getLatitude()) && Objects.nonNull(locationRequest.getLongitude())) {
            Optional<GeoShape> geoShape = geoLocationRepository.getGeoShapeByCoordinates(locationRequest.getLongitude(), locationRequest.getLatitude());
            if (geoShape.isPresent()) {
                return geoShape.get().getCountySq();
            }
        }
        return Optional.of(netNeutralityMeasurementResultRequest)
                .map(NetNeutralityMeasurementResultRequest::getLocation)
                .map(LocationRequest::getCounty)
                .orElse(null);
    }

    protected String getDevice(NetNeutralityMeasurementResultRequest netNeutralityMeasurementResultRequest) {
        Optional<MobileModel> mobileModel = mobileModelService.getAndSaveNewIfNotExistMobileModelByModel(netNeutralityMeasurementResultRequest.getModel());
        return MobileModelUtils.extractDevice(mobileModel, netNeutralityMeasurementResultRequest.getModel());
    }

    protected List<RadioSignalResponse> getRadioSignals(NetNeutralityMeasurementResultRequest netNeutralityMeasurementResultRequest) {
        return Stream.of(netNeutralityMeasurementResultRequest.getRadioInfo())
                .filter(Objects::nonNull)
                .map(RadioInfoRequest::getSignals)
                .flatMap(Collection::stream)
                .map(radioSignalMapper::radioSignalRequestToRadioSignalNN)
                .collect(Collectors.toList());
    }


    private String getNetworkOperatorNameByCode(NetNeutralityMeasurementResultRequest measurementRequest) {
        return Optional.ofNullable(measurementRequest)
                .map(NetNeutralityMeasurementResultRequest::getNetworkMccMnc)
                .flatMap(x -> simOperatorRepository.findByMccMncOrderByValidFromDesc(x).stream().findFirst())
                .map(SimOperator::getName)
                .orElse(null);
    }
}
