package com.specure.service.sah;

import com.specure.response.sah.version.ApplicationVersionResponse;

import java.io.IOException;

public interface ApplicationVersionService {
    ApplicationVersionResponse getApplicationVersion() throws IOException;
}
