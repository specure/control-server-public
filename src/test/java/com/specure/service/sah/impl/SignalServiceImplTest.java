package com.specure.service.sah.impl;


import com.specure.common.model.jpa.Client;
import com.specure.common.model.jpa.MobileMeasurement;
import com.specure.common.repository.MobileMeasurementRepository;
import com.specure.common.service.UUIDGenerator;
import com.specure.common.service.digger.DiggerService;
import com.specure.common.constant.HeaderConstants;
import com.specure.constant.URIConstants;
import com.specure.multitenant.MultiTenantManager;
import com.specure.repository.core.ClientRepository;
import com.specure.request.sah.SignalRequest;
import com.specure.response.sah.SignalResponse;
import com.specure.sah.TestConstants;
import com.specure.service.sah.SignalService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class SignalServiceImplTest {
    private SignalService signalService;

    @MockBean
    private MobileMeasurementRepository mobileMeasurementRepository;
    @MockBean
    private UUIDGenerator uuidGenerator;
    @MockBean
    private ClientRepository clientRepository;
    @MockBean
    private DiggerService diggerService;
    @MockBean
    private MultiTenantManager multiTenantManager;

    @Mock
    private SignalRequest signalRequest;
    @Mock
    private HttpServletRequest httpServletRequest;
    @Mock
    private MobileMeasurement savedTest;
    @Mock
    private Client rtrClient;

    @Before
    public void setUp() {
        signalService = new SignalServiceImpl(mobileMeasurementRepository, uuidGenerator,
                clientRepository, diggerService, multiTenantManager);
    }

    @Test
    public void registerSignal_whenCommonRequest_expectSignalResponse() {
        var expectedResponse = getRegisterSignalResponse();
        when(uuidGenerator.generateUUID()).thenReturn(UUID.fromString(TestConstants.DEFAULT_UUID));
        when(httpServletRequest.getRemoteAddr()).thenReturn(TestConstants.DEFAULT_IP);
        when(httpServletRequest.getHeader(HeaderConstants.URL)).thenReturn(TestConstants.DEFAULT_URL);
        when(signalRequest.getUuid()).thenReturn(UUID.fromString(TestConstants.DEFAULT_CLIENT_UUID_STRING));
        when(signalRequest.getTimezone()).thenReturn(TestConstants.DEFAULT_TIME_ZONE);
        when(clientRepository.findByUuid(TestConstants.DEFAULT_CLIENT_UUID_STRING.toString())).thenReturn(Optional.of(rtrClient));
        when(mobileMeasurementRepository.save(any())).thenReturn(savedTest);
        when(savedTest.getId()).thenReturn(TestConstants.DEFAULT_UID);
        when(savedTest.getUuid()).thenReturn(TestConstants.DEFAULT_UUID);
        when(savedTest.getPublicIpAsName()).thenReturn(TestConstants.DEFAULT_PROVIDER_NAME);

        var actualResponse = signalService.registerSignal(signalRequest, httpServletRequest);

        assertEquals(expectedResponse, actualResponse);
    }

    private SignalResponse getRegisterSignalResponse() {
        return SignalResponse.builder()
                .resultUrl(String.join(TestConstants.DEFAULT_URL, URIConstants.SIGNAL_RESULT))
                .clientRemoteIp(TestConstants.DEFAULT_IP)
                .provider(TestConstants.DEFAULT_PROVIDER_NAME)
                .testUUID(TestConstants.DEFAULT_UUID)
                .build();
    }
}
