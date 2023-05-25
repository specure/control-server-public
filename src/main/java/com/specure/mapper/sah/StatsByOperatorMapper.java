package com.specure.mapper.sah;

import com.specure.model.dto.StatsByOperator;
import com.specure.response.sah.stats.StatsByOperatorResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StatsByOperatorMapper {
    StatsByOperatorResponse statsByProviderToStatsByProviderResponse(StatsByOperator statsByOperator);
}
