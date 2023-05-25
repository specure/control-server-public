package com.specure.mapper.core;

import com.specure.common.enums.MeasurementServerType;
import com.specure.common.model.jpa.MeasurementServer;
import com.specure.common.model.jpa.MeasurementServerDescription;
import com.specure.response.core.MeasurementServerForWebResponse;
import com.specure.response.core.MeasurementServerResponseForSettings;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {
        MeasurementServerDescription.class
})
public abstract class MeasurementServerMapper {

    @Mapping(target = "address", source = "measurementServer.webAddress")
    @Mapping(source = "measurementServerType.serverTechForMeasurement.defaultSslPort", target = "port")
    public abstract MeasurementServerForWebResponse measurementServersToMeasurementServerForWebResponse(MeasurementServer measurementServer, MeasurementServerType measurementServerType);

    public abstract MeasurementServerResponseForSettings measurementServersToMeasurementServerResponseForSettings(MeasurementServer measurementServer);
}
