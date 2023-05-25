package com.specure.mapper.core;

import com.specure.common.model.jpa.Ping;
import com.specure.request.core.measurement.request.PingRequest;
import com.specure.response.core.measurement.response.PingResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PingMapper {
    Ping pingRequestToPing(PingRequest pingRequest);
    PingResponse pingToPingResponse(Ping ping);
}
