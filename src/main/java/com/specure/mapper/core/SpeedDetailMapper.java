package com.specure.mapper.core;

import com.specure.common.model.elastic.SimpleSpeedDetail;
import com.specure.common.model.jpa.SpeedDetail;
import com.specure.request.core.measurement.request.SpeedDetailRequest;
import com.specure.response.core.measurement.response.SpeedDetailResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SpeedDetailMapper {
    SpeedDetail speedDetailRequestToSpeedDetail(SpeedDetailRequest speedDetailRequest);

    SpeedDetailResponse speedDetailToSpeedDetailResponse(SpeedDetail speedDetail);

    SpeedDetailResponse simpleSpeedDetailToSpeedDetailResponse(SimpleSpeedDetail simpleSpeedDetail);

    SimpleSpeedDetail speedDetailToSimpleSpeedDetail(SpeedDetail speedDetail);
}
