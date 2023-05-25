package com.specure.mapper.sah.version;

import com.specure.dto.sah.version.ApplicationVersionFile;
import com.specure.response.sah.version.ApplicationVersionResponse;

public interface ApplicationVersionMapper {

    ApplicationVersionResponse applicationVersionFileToApplicationVersionResponse(ApplicationVersionFile applicationVersionFile);

    default ApplicationVersionResponse applicationVersionBlockToApplicationVersionResponse(ApplicationVersionFile.ApplicationVersionFileBlock applicationVersionFileBlock) {
        return ApplicationVersionResponse.builder()
                .regulatoryVersion(applicationVersionFileBlock.getRegulatoryVersion())
                .publicVersion(applicationVersionFileBlock.getPublicVersion())
                .publicBahrainVersion(applicationVersionFileBlock.getPublicBahrainVersion())
                .iosVersion(applicationVersionFileBlock.getIosVersion())
                .backendVersion(applicationVersionFileBlock.getBackendVersion())
                .androidVersion(applicationVersionFileBlock.getAndroidVersion())
                .flutterVersion(applicationVersionFileBlock.getFlutterVersion())
                .build();
    }
}
