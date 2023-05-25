//package com.specure.sah.controller;
//
//import com.specure.controller.sah.NationalOperatorController;
//import com.specure.sah.TestConstants;
//import com.specure.sah.TestObjects;
//import com.specure.sah.TestUtils;
//import com.specure.advice.SahBackendAdvice;
//import com.specure.constant.ErrorMessage;
//import com.specure.constant.URIConstants;
//import com.specure.exception.NationalOperatorNotFoundException;
//import com.specure.service.sah.NationalOperatorService;
//import org.elasticsearch.common.collect.List;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.servlet.view.json.MappingJackson2JsonView;
//
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@ExtendWith(SpringExtension.class)
//class NationalOperatorControllerTest {
//
//    @MockBean
//    private NationalOperatorService nationalOperatorService;
//
//    private MockMvc mockMvc;
//
//    @BeforeEach
//    void setUp() {
//        NationalOperatorController nationalOperatorController = new NationalOperatorController(nationalOperatorService);
//        mockMvc = MockMvcBuilders.standaloneSetup(nationalOperatorController)
//                .setControllerAdvice(new SahBackendAdvice())
//                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
//                .setViewResolvers((viewName, locale) -> new MappingJackson2JsonView())
//                .build();
//    }
//
//    @Test
//    void getNationalOperators_correctInvocation_NationalOperatorResponses() throws Exception {
//        var nationalOperatorResponse = TestObjects.defaultNationalOperatorResponse();
////        when(nationalOperatorService.getNationalOperators(currentTenant)).thenReturn(List.of(nationalOperatorResponse));
//
//        mockMvc.perform(MockMvcRequestBuilders.get(URIConstants.NATIONAL_OPERATOR))
//                .andDo(print())
//                .andExpect(jsonPath("$[0].id").value(TestConstants.DEFAULT_NATIONAL_OPERATOR_ID))
//                .andExpect(jsonPath("$[0].name").value(TestConstants.DEFAULT_NATIONAL_OPERATOR_NAME))
//                .andExpect(jsonPath("$[0].alias").value(TestConstants.DEFAULT_NATIONAL_OPERATOR_ALIAS));
//
//        verify(nationalOperatorService).getNationalOperators(currentTenant);
//    }
//
//    @Test
//    void createNationalOperator_correctInvocation_NationalOperatorResponse() throws Exception {
//        var request = TestObjects.defaultNationalOperatorRequest();
//        var response = TestObjects.defaultNationalOperatorResponse();
//        when(nationalOperatorService.createNationalOperator(request)).thenReturn(response);
//
//        mockMvc.perform(MockMvcRequestBuilders.post(URIConstants.NATIONAL_OPERATOR)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(TestUtils.asJsonString(request)))
//                .andDo(print())
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.id").value(TestConstants.DEFAULT_NATIONAL_OPERATOR_ID))
//                .andExpect(jsonPath("$.name").value(TestConstants.DEFAULT_NATIONAL_OPERATOR_NAME))
//                .andExpect(jsonPath("$.alias").value(TestConstants.DEFAULT_NATIONAL_OPERATOR_ALIAS));
//
//        verify(nationalOperatorService).createNationalOperator(request);
//    }
//
//    @Test
//    void updateNationalOperator_correctInvocation_NationalOperatorResponse() throws Exception {
//        var request = TestObjects.defaultNationalOperatorRequest();
//        var response = TestObjects.defaultNationalOperatorResponse();
//        when(nationalOperatorService.updateNationalOperator(TestConstants.DEFAULT_NATIONAL_OPERATOR_ID, request)).thenReturn(response);
//
//        mockMvc.perform(MockMvcRequestBuilders.put(URIConstants.NATIONAL_OPERATOR_BY_ID, TestConstants.DEFAULT_NATIONAL_OPERATOR_ID)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(TestUtils.asJsonString(request)))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(TestConstants.DEFAULT_NATIONAL_OPERATOR_ID))
//                .andExpect(jsonPath("$.name").value(TestConstants.DEFAULT_NATIONAL_OPERATOR_NAME))
//                .andExpect(jsonPath("$.alias").value(TestConstants.DEFAULT_NATIONAL_OPERATOR_ALIAS));
//
//        verify(nationalOperatorService).updateNationalOperator(TestConstants.DEFAULT_NATIONAL_OPERATOR_ID, request);
//    }
//
//
//    @Test
//    void updateNationalOperator_NationalOperatorNotFoundException_NationalOperatorResponse() throws Exception {
//        var request = TestObjects.defaultNationalOperatorRequest();
//        doThrow(new NationalOperatorNotFoundException(TestConstants.DEFAULT_NATIONAL_OPERATOR_ID))
//                .when(nationalOperatorService).updateNationalOperator(TestConstants.DEFAULT_NATIONAL_OPERATOR_ID, request);
//
//
//        mockMvc.perform(MockMvcRequestBuilders.put(URIConstants.NATIONAL_OPERATOR_BY_ID, TestConstants.DEFAULT_NATIONAL_OPERATOR_ID)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(TestUtils.asJsonString(request)))
//                .andDo(print())
//                .andExpect(status().isBadRequest());
//
//        verify(nationalOperatorService).updateNationalOperator(TestConstants.DEFAULT_NATIONAL_OPERATOR_ID, request);
//    }
//
//    @Test
//    void deleteNationalOperator_correctInvocation_NationalOperatorResponse() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.delete(URIConstants.NATIONAL_OPERATOR_BY_ID, TestConstants.DEFAULT_NATIONAL_OPERATOR_ID))
//                .andDo(print())
//                .andExpect(status().isOk());
//
//        verify(nationalOperatorService).deleteNationOperatorById(TestConstants.DEFAULT_NATIONAL_OPERATOR_ID);
//    }
//
//    @Test
//    void deleteNationalOperator_NationalOperatorNotFoundException_NationalOperatorResponse() throws Exception {
//        var expectedErrorMessage = String.format(ErrorMessage.NATIONAL_OPERATOR_NOT_FOUND_BY_ID, TestConstants.DEFAULT_NATIONAL_OPERATOR_ID);
//        doThrow(new NationalOperatorNotFoundException(TestConstants.DEFAULT_NATIONAL_OPERATOR_ID))
//                .when(nationalOperatorService).deleteNationOperatorById(TestConstants.DEFAULT_NATIONAL_OPERATOR_ID);
//
//        mockMvc.perform(MockMvcRequestBuilders.delete(URIConstants.NATIONAL_OPERATOR_BY_ID, TestConstants.DEFAULT_NATIONAL_OPERATOR_ID))
//                .andDo(print())
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.message").value(expectedErrorMessage));
//
//        verify(nationalOperatorService).deleteNationOperatorById(TestConstants.DEFAULT_NATIONAL_OPERATOR_ID);
//    }
//}