package com.specure.controller.sah;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.specure.advice.SahBackendAdvice;
import com.specure.constant.URIConstants;
import com.specure.request.sah.SignalRequest;
import com.specure.response.sah.SignalResponse;
import com.specure.sah.TestConstants;
import com.specure.sah.TestUtils;
import com.specure.service.sah.SignalService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static com.specure.sah.TestConstants.DEFAULT_UUID;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
public class SignalControllerTest {
    private MockMvc mockMvc;

    @MockBean
    private SignalService signalService;
    @Captor
    private ArgumentCaptor<SignalRequest> signalRequestArgumentCaptor;

    @Before
    public void setUp() {
        Jackson2ObjectMapperBuilder mapperBuilder = new Jackson2ObjectMapperBuilder();
        mapperBuilder.propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        SignalController signalController = new SignalController(signalService);
        mockMvc = MockMvcBuilders.standaloneSetup(signalController)
                .setControllerAdvice(new SahBackendAdvice())
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setMessageConverters(new MappingJackson2HttpMessageConverter(mapperBuilder.build()))
                .build();
    }

    @Test
    public void registerSignal_whenCommonData_expectRegisterSignalCalled() throws Exception {
        var request = getRegisterSignalRequest();
        var response = getRegisterSignalResponse();
        when(signalService.registerSignal(any(), any())).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post(URIConstants.SIGNAL_REQUEST)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.asJsonString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.result_url").value(TestConstants.DEFAULT_RESULT_URL))
                .andExpect(jsonPath("$.client_remote_ip").value(TestConstants.DEFAULT_IP))
                .andExpect(jsonPath("$.provider").value(TestConstants.DEFAULT_PROVIDER_NAME))
                .andExpect(jsonPath("$.test_uuid").value(DEFAULT_UUID));

        verify(signalService).registerSignal(signalRequestArgumentCaptor.capture(), any());

        assertEquals(request, signalRequestArgumentCaptor.getValue());
    }


    private SignalResponse getRegisterSignalResponse() {
        return SignalResponse.builder()
                .resultUrl(TestConstants.DEFAULT_RESULT_URL)
                .clientRemoteIp(TestConstants.DEFAULT_IP)
                .provider(TestConstants.DEFAULT_PROVIDER_NAME)
                .testUUID(DEFAULT_UUID)
                .build();
    }

    private SignalRequest getRegisterSignalRequest() {
        return SignalRequest.builder()
                .time(TestConstants.DEFAULT_TIME)
                .timezone(TestConstants.DEFAULT_TIME_ZONE)
                .uuid(UUID.fromString(TestConstants.DEFAULT_CLIENT_UUID_STRING))
                .build();
    }
}
