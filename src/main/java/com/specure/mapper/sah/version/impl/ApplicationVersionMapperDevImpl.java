package com.specure.mapper.sah.version.impl;

import com.specure.dto.sah.version.ApplicationVersionFile;
import com.specure.mapper.sah.version.ApplicationVersionMapper;
import com.specure.response.sah.version.ApplicationVersionResponse;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile(value = {"dev", "national & !beta & !prod"})
public class ApplicationVersionMapperDevImpl implements ApplicationVersionMapper {

    @Override
    public ApplicationVersionResponse applicationVersionFileToApplicationVersionResponse(ApplicationVersionFile applicationVersionFile) {
        return applicationVersionBlockToApplicationVersionResponse(applicationVersionFile.getDev());
    }
}
