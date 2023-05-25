package com.specure.service.core;

import com.specure.common.model.jpa.Client;
import com.specure.request.core.MobileSettingsRequest;

import java.util.UUID;

public interface ClientService {

    Client updateClientSettings(MobileSettingsRequest request);

    Client getClientByUUID(UUID uuid);
}
