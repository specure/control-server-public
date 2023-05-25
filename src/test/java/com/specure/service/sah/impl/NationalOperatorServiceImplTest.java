//package com.specure.service.sah.impl;
//
//import com.specure.sah.TestConstants;
//import com.specure.exception.NationalOperatorNotFoundException;
//import com.specure.request.sah.NationalOperatorRequest;
//import com.specure.response.sah.NationalOperatorResponse;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(SpringExtension.class)
//class NationalOperatorServiceImplTest {
//
//    @MockBean
//    private NationalOperatorRepository nationalOperatorRepository;
//    @MockBean
//    private NationalOperatorMapper nationalOperatorMapper;
//
//    private NationalOperatorServiceImpl nationalOperatorService;
//
//    @Mock
//    private NationalOperator nationalOperator;
//    @Mock
//    private NationalOperator nationalOperatorSecond;
//    @Mock
//    private NationalOperatorResponse nationalOperatorResponse;
//    @Mock
//    private NationalOperatorResponse nationalOperatorResponseSecond;
//    @Mock
//    private NationalOperatorRequest nationalOperatorRequest;
//    @Mock
//    private NationalOperator savedNationalOperator;
//    @Mock
//    private NationalOperator updatedNationalOperator;
//
//    @BeforeEach
//    void setUp() {
//        nationalOperatorService = new NationalOperatorServiceImpl(nationalOperatorRepository, nationalOperatorMapper);
//    }
//
//    @Test
//    void getNationalOperators_correctInvocation_NationalOperatorResponses() {
//        var expectedResult = List.of(nationalOperatorResponse, nationalOperatorResponseSecond);
//        var existingNationalOperator = List.of(nationalOperator, nationalOperatorSecond);
//        when(nationalOperatorRepository.findAll()).thenReturn(existingNationalOperator);
//        when(nationalOperatorMapper.toNationalOperatorResponse(nationalOperator)).thenReturn(nationalOperatorResponse);
//        when(nationalOperatorMapper.toNationalOperatorResponse(nationalOperatorSecond)).thenReturn(nationalOperatorResponseSecond);
//
//        var actualResult = nationalOperatorService.getNationalOperators(currentTenant);
//
//        assertEquals(expectedResult, actualResult);
//    }
//
//    @Test
//    void getNationalOperatorsAliases_correctInvocation_Map() {
//        Map<String, List<String>> expectedMap = expectedNationalOperator();
//        when(nationalOperatorRepository.findAll()).thenReturn(defaultNationalOperators());
//
//        var result = nationalOperatorService.getNationalOperatorsAliases();
//
//        assertEquals(expectedMap, result);
//    }
//
//    @Test
//    void deleteNationOperatorById_nationalOperatorExists_deleted() {
//        when(nationalOperatorRepository.findById(TestConstants.DEFAULT_NATIONAL_OPERATOR_ID)).thenReturn(Optional.of(nationalOperator));
//
//        nationalOperatorService.deleteNationOperatorById(TestConstants.DEFAULT_NATIONAL_OPERATOR_ID);
//        verify(nationalOperatorRepository).delete(nationalOperator);
//    }
//
//    @Test
//    void deleteNationOperatorById_nationalOperatorNotExists_NationalOperatorNotFoundException() {
//        when(nationalOperatorRepository.findById(TestConstants.DEFAULT_NATIONAL_OPERATOR_ID)).thenReturn(Optional.empty());
//
//        assertThrows(NationalOperatorNotFoundException.class, () -> nationalOperatorService.deleteNationOperatorById(TestConstants.DEFAULT_NATIONAL_OPERATOR_ID));
//    }
//
//    @Test
//    void createNationalOperator_correctInvocation_NationalOperatorResponse() {
//        when(nationalOperatorMapper.toNationalOperator(nationalOperatorRequest)).thenReturn(nationalOperator);
//        when(nationalOperatorRepository.save(nationalOperator)).thenReturn(savedNationalOperator);
//        when(nationalOperatorMapper.toNationalOperatorResponse(savedNationalOperator)).thenReturn(nationalOperatorResponse);
//
//        var result = nationalOperatorService.createNationalOperator(nationalOperatorRequest);
//
//        assertEquals(nationalOperatorResponse, result);
//    }
//
//    @Test
//    void updateNationalOperator_correctInvocation_NationalOperatorResponse() {
//        when(nationalOperatorRepository.findById(TestConstants.DEFAULT_NATIONAL_OPERATOR_ID)).thenReturn(Optional.of(nationalOperator));
//        when(nationalOperatorMapper.updateNationalOperator(nationalOperator, nationalOperatorRequest)).thenReturn(updatedNationalOperator);
//        when(nationalOperatorRepository.save(updatedNationalOperator)).thenReturn(savedNationalOperator);
//        when(nationalOperatorMapper.toNationalOperatorResponse(savedNationalOperator)).thenReturn(nationalOperatorResponse);
//
//        var result = nationalOperatorService.updateNationalOperator(TestConstants.DEFAULT_NATIONAL_OPERATOR_ID, nationalOperatorRequest);
//
//        assertEquals(nationalOperatorResponse, result);
//    }
//
//    @Test
//    void updateNationalOperator_correctInvocation_NationalOperatorNotFoundException() {
//        when(nationalOperatorRepository.findById(TestConstants.DEFAULT_NATIONAL_OPERATOR_ID)).thenReturn(Optional.empty());
//
//        assertThrows(NationalOperatorNotFoundException.class, () -> nationalOperatorService.updateNationalOperator(TestConstants.DEFAULT_NATIONAL_OPERATOR_ID, nationalOperatorRequest));
//    }
//
//    private Map<String, List<String>> expectedNationalOperator() {
//        return Map.of("Vodafone", List.of("Orange Mobile", "Vodafone AL", "VODAFONE AL", "Vodafone Albania"), "One Telecommunications", List.of("One.al", "Telekom.al", "Albanian Mobile Communications (AMC)"));
//    }
//
//    private List<NationalOperator> defaultNationalOperators() {
//        return List.of(NationalOperator.builder().name("Vodafone").alias("Orange Mobile").build(), NationalOperator.builder().name("Vodafone").alias("Vodafone AL").build(), NationalOperator.builder().name("Vodafone").alias("VODAFONE AL").build(), NationalOperator.builder().name("Vodafone").alias("Vodafone Albania").build(), NationalOperator.builder().name("One Telecommunications").alias("One.al").build(), NationalOperator.builder().name("One Telecommunications").alias("Telekom.al").build(), NationalOperator.builder().name("One Telecommunications").alias("Albanian Mobile Communications (AMC)").build());
//    }
//}