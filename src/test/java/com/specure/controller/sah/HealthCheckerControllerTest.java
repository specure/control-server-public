package com.specure.controller.sah;

import com.specure.advice.SahBackendAdvice;
import com.specure.constant.URIConstants;
import com.specure.sah.TestConstants;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
public class HealthCheckerControllerTest {

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        HealthCheckerController healthCheckerController = new HealthCheckerController();
        mockMvc = MockMvcBuilders.standaloneSetup(healthCheckerController)
                .setControllerAdvice(new SahBackendAdvice())
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((viewName, locale) -> new MappingJackson2JsonView())
                .build();
    }

    @Test
    @Ignore
    public void getGraphs_WhenCalled_ExpectCorrectResponse() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URIConstants.HEALTH)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastDownPingSignal").value(TestConstants.DEFAULT_LAST_DOWN_PING));
    }
}
