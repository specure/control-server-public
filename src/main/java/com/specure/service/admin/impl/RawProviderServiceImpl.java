package com.specure.service.admin.impl;

import com.specure.common.constant.Constants;
import com.specure.common.model.jpa.RawProvider;
import com.specure.common.repository.RawProviderRepository;
import com.maxmind.geoip2.model.AbstractCountryResponse;
import com.maxmind.geoip2.model.CityResponse;
import com.specure.service.admin.RawProviderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RawProviderServiceImpl implements RawProviderService {

    private final RawProviderRepository rawProviderRepository;
    @Override
    public RawProvider getRawProvider(CityResponse cityResponse, Long asn, String networkMccMnc) {
        return getProviderByCityResponse(cityResponse, asn, networkMccMnc);
    }

    @Override
    public RawProvider getRawProvider(CityResponse cityResponse, Long asn) {
        return getProviderByCityResponse(cityResponse, asn, null);
    }

    @Override
    public RawProvider getRawProvider(String simRawProviderCountry, String simRawProviderName, String simRawProviderMccMnc) {
        RawProvider rawProvider = rawProviderRepository.findTopByCountryAndRawName(simRawProviderCountry, simRawProviderName)
                .orElseGet(() -> saveRawProvider(simRawProviderMccMnc, simRawProviderCountry, simRawProviderName, null));
        if (!Objects.equals(rawProvider.getMccMnc(), simRawProviderMccMnc)) {
            rawProvider.setMccMnc(simRawProviderMccMnc);
            rawProvider = rawProviderRepository.save(rawProvider);
        }

        return rawProvider;
    }

    private RawProvider saveRawProvider(String mccMnc, String country, String rawName, String asnString) {
        RawProvider newProvider = RawProvider.builder()
                .rawName(rawName)
                .country(country)
                .asn(asnString)
                .mccMnc(mccMnc)
                .build();
        return rawProviderRepository.save(newProvider);
    }

    private RawProvider getProviderByCityResponse(CityResponse cityResponse, Long asn, String networkMccMnc) {
        String country = cityResponse.getCountry().getName();
        String rawName = cityResponse.getTraits().getIsp();
        String mccMnc = Optional.ofNullable(cityResponse)
                .map(AbstractCountryResponse::getTraits)
                .filter(x -> Objects.nonNull(x.getMobileCountryCode()) && Objects.nonNull(x.getMobileNetworkCode()))
                .map(x -> String.join(Constants.MCC_MNC_JOINER, x.getMobileCountryCode(), x.getMobileNetworkCode()))
                .orElse(networkMccMnc);
        String asnString = Optional.ofNullable(asn).map(String::valueOf).orElse(null);

        return rawProviderRepository.findTopByCountryAndRawName(country, rawName)
                .orElseGet(() -> saveRawProvider(mccMnc, country, rawName, asnString));
    }
}
