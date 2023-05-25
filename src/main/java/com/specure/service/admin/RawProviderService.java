package com.specure.service.admin;

import com.specure.common.model.jpa.RawProvider;
import com.maxmind.geoip2.model.CityResponse;

public interface RawProviderService {

    RawProvider getRawProvider(CityResponse x, Long asn, String networkMccMnc);

    RawProvider getRawProvider(CityResponse x, Long asn);

    RawProvider getRawProvider(String simRawProviderCountry, String simRawProviderName, String simRawProviderMccMnc);
}
