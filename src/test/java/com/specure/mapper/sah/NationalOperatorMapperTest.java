//package com.specure.mapper.sah;
//
//import com.specure.sah.TestConstants;
//import com.specure.sah.TestObjects;
//import com.specure.request.sah.NationalOperatorRequest;
//import com.specure.response.sah.NationalOperatorResponse;
//import com.specure.mapper.sah.NationalOperatorMapperImpl;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//@ExtendWith(SpringExtension.class)
//class NationalOperatorMapperTest {
//
//    private com.specure.mapper.sah.NationalOperatorMapperImpl nationalOperatorMapper;
//
//    @BeforeEach
//    void setUp() {
//        nationalOperatorMapper = new NationalOperatorMapperImpl();
//    }
//
//    @Test
//    void toNationalOperatorResponse_correctInvocation_NationalOperatorResponse() {
//        NationalOperator nationalOperator = TestObjects.defaultNationalOperator();
//        NationalOperatorResponse nationalOperatorResponse = TestObjects.defaultNationalOperatorResponse();
//
//        NationalOperatorResponse actualResult = nationalOperatorMapper.toNationalOperatorResponse(nationalOperator);
//
//        assertEquals(nationalOperatorResponse, actualResult);
//    }
//
//    @Test
//    void toNationalOperator_correctInvocation_NationalOperator() {
//        NationalOperatorRequest nationalOperatorRequest = TestObjects.defaultNationalOperatorRequest();
//        NationalOperator nationalOperator = TestObjects.defaultNationalOperator();
//        nationalOperator.setId(null);
//
//        NationalOperator actualResult = nationalOperatorMapper.toNationalOperator(nationalOperatorRequest);
//
//        assertEquals(nationalOperator, actualResult);
//    }
//
//    @Test
//    void updateNationalOperator_correctInvocation_NationalOperator() {
//        var expectedResult = TestObjects.defaultNationalOperator();
//        var existingNationalOperator = NationalOperator.builder()
//                .id(TestConstants.DEFAULT_NATIONAL_OPERATOR_ID)
//                .build();
//        var request = TestObjects.defaultNationalOperatorRequest();
//
//        NationalOperator actualResult = nationalOperatorMapper.updateNationalOperator(existingNationalOperator, request);
//
//        assertEquals(expectedResult, actualResult);
//    }
//}