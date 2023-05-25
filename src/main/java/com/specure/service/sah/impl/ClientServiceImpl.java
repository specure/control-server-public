package com.specure.service.sah.impl;

import com.specure.common.model.jpa.Client;
import com.specure.common.service.UUIDGenerator;
import com.specure.repository.core.ClientRepository;
import com.specure.request.core.MobileSettingsRequest;
import com.specure.service.core.ClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final UUIDGenerator uuidGenerator;

    @Override
    public Client updateClientSettings(MobileSettingsRequest request) {
        log.debug("ClientServiceImpl:updateClientSettings request={}", request.toString());
        LocalDateTime now = LocalDateTime.now();
        boolean isTermAndConditionAccepted = isTermAndConditionAccepted(request);
        Client client = Optional.ofNullable(request.getUuid())
                .map(UUID::toString)
                .map(clientRepository::findByUuid)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .orElse(null);
        if (Objects.nonNull(client)) {
            if (Optional.ofNullable(client.getTermsAndConditionsAcceptedVersion())
                    .orElse(0L) < request.getTermsAndConditionsAcceptedVersion()) {
                client.setTermsAndConditionsAcceptedVersion(request.getTermsAndConditionsAcceptedVersion());
                client.setTermsAndConditionsAcceptedTimestamp(now);
            }
            client.setLastSeen(now);
            Client savedClient = clientRepository.save(client);
            log.debug("ClientServiceImpl:updateClientSettings client updated: {}", savedClient);
            return savedClient;
        } else if (isTermAndConditionAccepted) {
            client = new Client();
            client.setUuid(uuidGenerator.generateUUID().toString());
            client.setClientType(request.getType());
            client.setTermsAndConditionsAccepted(isTermAndConditionAccepted);
            client.setTermsAndConditionsAcceptedVersion(request.getTermsAndConditionsAcceptedVersion());
            client.setLastSeen(now);
            Client savedClient = clientRepository.save(client);
            log.debug("ClientServiceImpl:updateClientSettings client created: {}", savedClient);
            return savedClient;
        } else {
            log.debug("ClientServiceImpl:updateClientSettings client not found and terms is not accepted");
            return null;
        }
    }

    @Override
    public Client getClientByUUID(UUID uuid) {
        log.debug("ClientServiceImpl:getClientByUUID uuid = {}", uuid);
        Client client = clientRepository.findByUuid(uuid.toString())
                .orElse(null);
        log.debug("ClientServiceImpl:getClientByUUID found client {}", client);
        return client;
    }

    private boolean isTermAndConditionAccepted(MobileSettingsRequest mobileSettingsRequest) {
        boolean isTermAndConditionAccepted;
        if (mobileSettingsRequest.getTermsAndConditionsAcceptedVersion() > 0) {
            isTermAndConditionAccepted = true;
        } else {
            isTermAndConditionAccepted = mobileSettingsRequest.isTermsAndConditionsAccepted();
        }
        log.trace("ClientServiceImpl:isTermAndConditionAccepted = {}", isTermAndConditionAccepted);
        return isTermAndConditionAccepted;
    }
}
