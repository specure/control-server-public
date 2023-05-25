package com.specure.common.repository;

import com.specure.model.elastic.GeoShape;

import java.util.Optional;

public interface GeoLocationRepository {

    Optional<GeoShape> getGeoShapeByCoordinates(Double longitude, Double latitude);
}
