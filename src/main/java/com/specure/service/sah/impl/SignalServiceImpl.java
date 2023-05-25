package com.specure.service.sah.impl;


import com.specure.common.enums.MeasurementStatus;
import com.specure.common.model.jpa.MobileMeasurement;
import com.specure.common.repository.MobileMeasurementRepository;
import com.specure.common.service.UUIDGenerator;
import com.specure.common.service.digger.DiggerService;
import com.google.common.net.InetAddresses;
import com.specure.common.constant.HeaderConstants;
import com.specure.constant.URIConstants;
import com.specure.exception.ClientNotFoundException;
import com.specure.multitenant.MultiTenantManager;
import com.specure.repository.core.ClientRepository;
import com.specure.request.sah.SignalRequest;
import com.specure.response.sah.SignalResponse;
import com.specure.service.sah.SignalService;
import com.specure.utils.sah.HelperFunctions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
public class SignalServiceImpl implements SignalService {

    private final MobileMeasurementRepository mobileMeasurementRepository;
    private final UUIDGenerator uuidGenerator;
    private final ClientRepository clientRepository;
    private final DiggerService diggerService;
    private final MultiTenantManager multiTenantManager;


    @Override
    public SignalResponse registerSignal(SignalRequest signalRequest, HttpServletRequest httpServletRequest) {
        var ip = getResultClientIp(httpServletRequest);
        log.info("SignalServiceImpl:registerSignal with tenant = {}, ip {}, signalRequest = {}", multiTenantManager.getCurrentTenant(), ip, signalRequest);
        UUID uuid = uuidGenerator.generateUUID();
        var openTestUUID = uuidGenerator.generateUUID();

        var client = clientRepository.findByUuid(signalRequest.getUuid().toString())
                .orElseThrow(() -> new ClientNotFoundException(signalRequest.getUuid().toString()));

        var clientAddress = InetAddresses.forString(ip);
        var clientIpString = InetAddresses.toAddrString(clientAddress);

        Long diggerAsn = diggerService.digASN(clientAddress);
        var measurement = MobileMeasurement.builder()
                .uuid(uuid.toString())
                .openTestUuid(openTestUUID.toString())
                .client(client)
                .clientPublicIp(clientIpString)
                .clientPublicIpAnonymized(HelperFunctions.anonymizeIp(clientAddress))
                .timezone(signalRequest.getTimezone())
                .clientTime(getClientTime(signalRequest))
                .publicIpAsn(diggerAsn)
                .publicIpAsName(HelperFunctions.getASName(diggerAsn))
                .countryAsn(HelperFunctions.getAScountry(diggerAsn))
                .publicIpRdns(HelperFunctions.getReverseDNS(clientAddress))
                .status(MeasurementStatus.STARTED)
                .lastSequenceNumber(-1)
                .build();

        var savedTest = mobileMeasurementRepository.save(measurement);

        return SignalResponse.builder()
                .provider(savedTest.getPublicIpAsName())
                .clientRemoteIp(ip)
                .resultUrl(getResultUrl(httpServletRequest))
                .testUUID(savedTest.getUuid())
                .build();
    }

    private LocalDateTime getClientTime(SignalRequest signalRequest) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(signalRequest.getTime()),
                ZoneId.of(signalRequest.getTimezone()));
    }

    private String getResultClientIp(HttpServletRequest req) {
        return Optional.ofNullable(req.getHeader(HeaderConstants.IP))
                .orElse(req.getRemoteAddr());
    }

    private String getResultUrl(HttpServletRequest req) {
        return Optional.ofNullable(req.getHeader(HeaderConstants.URL))
                .map(url -> String.join(HeaderConstants.URL, URIConstants.SIGNAL_RESULT))
                .orElse(getDefaultResultUrl(req));
    }

    private String getDefaultResultUrl(HttpServletRequest req) {
        return String.format("%s://%s:%s%s", req.getScheme(), req.getServerName(), req.getServerPort(), req.getRequestURI())
                .replace("Request", "Result");
    }
}
