package com.specure.mapper.sah;

import com.specure.model.dto.NationalTable;
import com.specure.response.sah.stats.NationalTableResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {StatsByOperatorMapper.class})
public interface NationalTableMapper {
    NationalTableResponse nationalTableToNationalTableResponse(NationalTable nationalTable);

}
