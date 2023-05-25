package com.specure.mapper.core;

import com.specure.common.model.jpa.GeoLocation;
import com.specure.request.core.measurement.request.GeoLocationRequest;
import com.specure.response.core.measurement.response.GeoLocationResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GeoLocationMapper {
    GeoLocation geoLocationRequestToGeoLocation(GeoLocationRequest geoLocationRequest);
    GeoLocationResponse geoLocationToGeoLocationResponse(GeoLocation geoLocation);
}
