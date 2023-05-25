package com.specure.controller.core;

import com.specure.constant.URIConstants;
import com.specure.core.TestUtils;
import com.specure.multitenant.MultiTenantManager;
import com.specure.request.core.ClientLocationRequest;
import com.specure.response.core.NearestMeasurementServersResponse;
import com.specure.security.constants.SecurityConstants;
import com.specure.service.core.MeasurementServerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.adapters.springboot.KeycloakSpringBootProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.specure.core.TestConstants.DEFAULT_CLIENT;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MeasurementServerController.class)
@ExtendWith(SpringExtension.class)
@EnableConfigurationProperties(KeycloakSpringBootProperties.class)
class MeasurementServerControllerTest {

    @MockBean(name = "basicMeasurementServerService")
    private MeasurementServerService measurementServerService;
    @MockBean
    private MultiTenantManager multiTenantManager;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        when(multiTenantManager.isTenantExist("nt")).thenReturn(true);
    }

    @Test
    @WithMockUser
    void getMeasurementServersForWebClient_WhenCallUpdate_ExpectCorrectResponse() throws Exception {
        ClientLocationRequest clientLocationRequest = ClientLocationRequest.builder().client(DEFAULT_CLIENT).build();
        NearestMeasurementServersResponse nearestMeasurementServersResponse = NearestMeasurementServersResponse.builder().build();

        when(measurementServerService.getNearestServers(clientLocationRequest)).thenReturn(nearestMeasurementServersResponse);

        mockMvc
                .perform(MockMvcRequestBuilders.post(URIConstants.MEASUREMENT_SERVER_WEB)
                        .header(SecurityConstants.CLIENT_NAME, "nt")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.convertObjectToJsonBytes(clientLocationRequest))
                ).andExpect(status().isOk());
    }
}
