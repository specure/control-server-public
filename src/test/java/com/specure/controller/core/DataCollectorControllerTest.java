package com.specure.controller.core;

import com.specure.advice.ControllerErrorAdvice;
import com.specure.constant.URIConstants;
import com.specure.response.core.DataCollectorResponse;
import com.specure.service.core.DataCollectorService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
public class DataCollectorControllerTest {

    @MockBean
    DataCollectorService dataCollectorService;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        DataCollectorController dataCollectorController = new DataCollectorController(dataCollectorService);
        mockMvc = MockMvcBuilders.standaloneSetup(dataCollectorController)
                .setControllerAdvice(new ControllerErrorAdvice())
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((viewName, locale) -> new MappingJackson2JsonView())
                .build();
    }

    @Test
    public void getDataCollector_WhenCalled_ExpectCorrectResponse() throws Exception {
        DataCollectorResponse dataCollectorResponse = DataCollectorResponse.builder().build();
        when(dataCollectorService.extrudeData(any(), any())).thenReturn(dataCollectorResponse);
        mockMvc
                .perform(MockMvcRequestBuilders.get(URIConstants.REQUEST_DATA_COLLECTOR))
                .andExpect(status().isOk());
    }
}
