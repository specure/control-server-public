package com.specure.service.sah.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.specure.config.ApplicationVersionConfig;
import com.specure.dto.sah.version.ApplicationVersionFile;
import com.specure.mapper.sah.version.ApplicationVersionMapper;
import com.specure.response.sah.version.ApplicationVersionResponse;
import com.specure.service.sah.ApplicationVersionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ApplicationVersionServiceImpl implements ApplicationVersionService {

    private final AmazonS3 amazonS3;
    private final ApplicationVersionConfig applicationVersionConfig;
    private final ApplicationVersionMapper applicationVersionMapper;

    @Override
    public ApplicationVersionResponse getApplicationVersion() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        S3Object s3Object = amazonS3.getObject(applicationVersionConfig.getBucketPath(), applicationVersionConfig.getFileName());
        ApplicationVersionFile applicationVersionFile = objectMapper.readValue(s3Object.getObjectContent(), ApplicationVersionFile.class);
        return applicationVersionMapper.applicationVersionFileToApplicationVersionResponse(applicationVersionFile);
    }
}
