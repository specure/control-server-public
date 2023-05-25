package com.specure.mapper.mobile;

import com.specure.common.enums.NetworkType;
import com.specure.common.model.elastic.BasicQosTest;
import com.specure.common.model.elastic.BasicTest;
import com.specure.common.model.elastic.MobileFields;
import com.specure.common.response.LocationResponse;
import com.specure.response.mobile.BasicTestHistoryMobileResponse;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

import java.util.Optional;

public interface BasicTestMobileMapper {

    BasicTestHistoryMobileResponse basicTestResponseToBasicTestHistoryMobileResponse(BasicTest basicTest, BasicQosTest basicQosTest);

    static String getNetworkType(MobileFields mobileFields) {
        return Optional.ofNullable(mobileFields.getNetworkType())
                .map(NetworkType::valueOfSafely)
                .map(NetworkType::getCategory)
                .orElse(NetworkType.UNKNOWN.getCategory());
    }

    static String getNetworkName(MobileFields mobileFields) {
        return Optional.ofNullable(mobileFields.getNetworkType())
                .map(NetworkType::valueOfSafely)
                .map(NetworkType::getName)
                .orElse(null);
    }

    static LocationResponse getLocation(MobileFields mobileFields) {
        return LocationResponse.builder()
                .city(mobileFields.getCity())
                .country(mobileFields.getCountry())
                .county(mobileFields.getCounty())
                .postalCode(mobileFields.getPostalCode())
                .latitude(Optional.ofNullable(mobileFields.getLocation())
                        .map(GeoPoint::getLat)
                        .orElse(null))
                .longitude(Optional.ofNullable(mobileFields.getLocation())
                        .map(GeoPoint::getLon)
                        .orElse(null))
                .build();
    }
}
