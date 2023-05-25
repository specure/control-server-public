package com.specure.controller.sah;

import com.specure.advice.SahBackendAdvice;
import com.specure.exception.BadMobileTechnologyException;
import com.specure.response.sah.stats.NationalTableResponse;
import com.specure.service.sah.StatsService;
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

import static com.specure.constant.ErrorMessage.WRONG_MOBILE_TECHNOLOGY_PARAMETER;
import static com.specure.constant.URIConstants.NATIONAL_TABLE;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
public class StatsControllerTest {

    @MockBean
    private StatsService statsService;

    private MockMvc mockMvc;

    @Before
    public void
    setUp() {
        StatsController statsController = new StatsController(statsService);
        mockMvc = MockMvcBuilders
                .standaloneSetup(statsController)
                .setControllerAdvice(new SahBackendAdvice())
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((viewName, locale) -> new MappingJackson2JsonView())
                .build();
    }

    @Test
    public void getNationalTable_whenCalledWithoutTechParameter_ExpectAllAnd200Response() throws Exception {
        final String municipalityCode = "20140";

        when(statsService.getNationalTable("all", municipalityCode))
                .thenReturn(NationalTableResponse.builder().build());

        mockMvc
                .perform(MockMvcRequestBuilders
                        .get(NATIONAL_TABLE)
                        .param("code", municipalityCode)
                )
                .andExpect(status().isOk());

        verify(statsService).getNationalTable("all", municipalityCode);
    }

    @Test
    public void getNationalTable_whenCalledWithoutWrongTechParameter_Expect404Response() throws Exception {

        final String badMobileTech = "99G";

        doThrow(new BadMobileTechnologyException(badMobileTech))
                .when(statsService)
                .getNationalTable(badMobileTech, null);


        final var request = MockMvcRequestBuilders
                .get(NATIONAL_TABLE)
                .param("tech", badMobileTech);

        Object expectedErrorMessage = String.format(WRONG_MOBILE_TECHNOLOGY_PARAMETER, badMobileTech);

        mockMvc
                .perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(expectedErrorMessage));

    }
}
