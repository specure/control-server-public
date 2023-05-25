package com.specure.common.model.elastic;

import org.springframework.data.elasticsearch.core.geo.GeoPoint;

public interface MobileFields {
    String getCity();

    String getCountry();

    String getCounty();

    String getPostalCode();

    GeoPoint getLocation();

    String getNetworkType();
}
