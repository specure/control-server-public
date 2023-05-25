package com.specure.mapper.mobile.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.specure.dto.sah.qos.QosTestObjective;
import com.specure.dto.sah.qos.QosTestResult;
import com.specure.mapper.mobile.MobileQosTestResultMapper;
import com.specure.common.model.jpa.qos.DnsTestResult;
import com.specure.sah.TestConstants;
import com.specure.sah.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
public class MobileQosTestResultMapperImplTest {
    private MobileQosTestResultMapper mobileQosTestResultMapper;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void setUp() {
        mobileQosTestResultMapper = new MobileQosTestResultMapperImpl(objectMapper);
    }

    @Test
    public void toQosTestResultItem_whenCommonData_expectQosTestResultItem() {
        var request = getQosTestResult();
        var response = mobileQosTestResultMapper.toQosTestResultItem(request, false);


        assertEquals(TestConstants.DEFAULT_UID, response.getUid());
        assertEquals(TestConstants.DEFAULT_TEST_TYPE, response.getTestType());
        assertEquals(TestConstants.DEFAULT_TEST_RESULT_KEY, response.getResult().entrySet().iterator().next().getKey());
        assertEquals(TestConstants.DEFAULT_TEST_RESULT_VALUE, response.getResult().entrySet().iterator().next().getValue());
        assertEquals(TestConstants.DEFAULT_TEST_DESCRIPTION, response.getTestDesc());
        assertEquals(TestConstants.DEFAULT_FAILURE_COUNT, response.getFailureCount());
        assertEquals(TestConstants.DEFAULT_SUCCESS_COUNT, response.getSuccessCount());
        assertEquals(TestConstants.DEFAULT_TEST_SUMMARY, response.getTestSummary());
        assertEquals(Set.of(TestConstants.DEFAULT_TEST_RESULT_KEY), response.getTestResultKeys());
        assertEquals(Map.of(TestConstants.DEFAULT_TEST_RESULT_KEY, TestConstants.DEFAULT_TEST_RESULT_VALUE), response.getTestResultKeyMap());
    }

    @Test
    public void testResultToQosTestResult_whenCommonData_expectQosTestResult() throws JsonProcessingException {
        var request = getQosResult();

        var response = mobileQosTestResultMapper.testResultToQosTestResult(request);

        assertEquals(TestConstants.DEFAULT_ID, response.getId());
        assertEquals(TestConstants.DEFAULT_FAILURE_COUNT, response.getFailureCount());
        assertEquals(TestConstants.DEFAULT_SUCCESS_COUNT, response.getSuccessCount());
        assertEquals(TestConstants.DEFAULT_QOS_TEST_OBJECTIVE_ID, response.getQosTestObjectiveId());
        assertEquals(TestUtils.asJsonString(request), response.getResult());
    }

    private DnsTestResult getQosResult() {
        return DnsTestResult.builder()
                .id(TestConstants.DEFAULT_ID)
                .failureCount(TestConstants.DEFAULT_FAILURE_COUNT)
                .successCount(TestConstants.DEFAULT_SUCCESS_COUNT)
                .qosTestUid(TestConstants.DEFAULT_QOS_TEST_OBJECTIVE_ID)
                .build();
    }


    private QosTestResult getQosTestResult() {
        var qosTestObjective = QosTestObjective.builder()
                .testType(TestConstants.DEFAULT_TEST_TYPE)
                .build();
        var qosTestResult = QosTestResult.builder()
                .id(TestConstants.DEFAULT_ID)
                .qosTestObjective(qosTestObjective)
                .result(TestConstants.DEFAULT_RESULT)
                .testDescription(TestConstants.DEFAULT_TEST_DESCRIPTION)
                .successCount(TestConstants.DEFAULT_SUCCESS_COUNT)
                .failureCount(TestConstants.DEFAULT_FAILURE_COUNT)
                .testSummary(TestConstants.DEFAULT_TEST_SUMMARY)
                .build();
        qosTestResult.getResultKeyMap().put(TestConstants.DEFAULT_TEST_RESULT_KEY, TestConstants.DEFAULT_TEST_RESULT_VALUE);
        return qosTestResult;
    }
}
