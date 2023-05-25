package com.specure.mapper.mobile.impl;

import com.specure.common.model.dto.qos.QosParams;
import com.specure.dto.sah.qos.QosTestObjective;
import com.specure.mapper.mobile.MobileQosTestObjectiveMapper;
import com.specure.sah.TestConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.Assert.assertEquals;

@ExtendWith(SpringExtension.class)
public class MobileQosTestObjectiveMapperImplTest {
    private MobileQosTestObjectiveMapper mobileQosTestObjectiveMapper;

    @BeforeEach
    public void setUp() {
        mobileQosTestObjectiveMapper = new MobileQosTestObjectiveMapperImpl();
    }

    @DisabledOnOs(OS.MAC)
    @Test
    public void qosTestObjectiveToQosParamsResponse_whenCommonData_expect() {
        var request = getQosTestObjective();

        var response = mobileQosTestObjectiveMapper.qosTestObjectiveToQosParamsResponse(request);

        assertEquals(String.valueOf(TestConstants.DEFAULT_ID), response.getQosTestUid());
        assertEquals(String.valueOf(TestConstants.DEFAULT_CONCURRENCY_GROUP), response.getConcurrencyGroup());
        assertEquals(TestConstants.DEFAULT_MEASUREMENT_SERVER_ADDRESS, response.getServerAddress());
        assertEquals(TestConstants.DEFAULT_PORT, response.getServerPort());
        assertEquals(TestConstants.DEFAULT_PORT, response.getPort());
        assertEquals(TestConstants.DEFAULT_REQUEST, response.getRequest());
        assertEquals(TestConstants.DEFAULT_TIMEOUT, response.getTimeout());
        assertEquals(TestConstants.DEFAULT_URL, response.getUrl());
        assertEquals(TestConstants.DEFAULT_OUT_NUM_PACKETS, response.getOutNumPackets());
        assertEquals(TestConstants.DEFAULT_OUT_PORT, response.getOutPort());
        assertEquals(TestConstants.DEFAULT_DOWNLOAD_TIMEOUT, response.getDownloadTimeout());
        assertEquals(TestConstants.DEFAULT_CONN_TIMEOUT, response.getConnTimeout());
        assertEquals(TestConstants.DEFAULT_RECORD, response.getRecord());
        assertEquals(TestConstants.DEFAULT_HOST, response.getHost());
        assertEquals(TestConstants.DEFAULT_CALL_DURATION, response.getCallDuration());
        assertEquals(TestConstants.DEFAULT_IN_PORT, response.getInPort());
        assertEquals(TestConstants.DEFAULT_RESOLVER, response.getResolver());
        assertEquals(TestConstants.DEFAULT_RANGE, response.getRange());
        assertEquals(TestConstants.DEFAULT_IN_NUM_PACKETS, response.getInNumPackets());
    }

    private QosTestObjective getQosTestObjective() {
        var qosParam = QosParams.builder()
                .callDuration(TestConstants.DEFAULT_CALL_DURATION)
                .connTimeout(TestConstants.DEFAULT_CONN_TIMEOUT)
                .downloadTimeout(TestConstants.DEFAULT_DOWNLOAD_TIMEOUT)
                .host(TestConstants.DEFAULT_HOST)
                .inNumPackets(TestConstants.DEFAULT_IN_NUM_PACKETS)
                .inPort(TestConstants.DEFAULT_IN_PORT)
                .outNumPackets(TestConstants.DEFAULT_OUT_NUM_PACKETS)
                .outPort(TestConstants.DEFAULT_OUT_PORT)
                .port(TestConstants.DEFAULT_PORT)
                .range(TestConstants.DEFAULT_RANGE)
                .record(TestConstants.DEFAULT_RECORD)
                .request(TestConstants.DEFAULT_REQUEST)
                .resolver(TestConstants.DEFAULT_RESOLVER)
                .timeout(TestConstants.DEFAULT_TIMEOUT)
                .url(TestConstants.DEFAULT_URL)
                .build();
        return QosTestObjective.builder()
                .id(TestConstants.DEFAULT_ID.intValue())
                .concurrencyGroup(TestConstants.DEFAULT_CONCURRENCY_GROUP)
                .testServerAddress(TestConstants.DEFAULT_MEASUREMENT_SERVER_ADDRESS)
                .testServerPort(TestConstants.DEFAULT_PORT)
                .param(qosParam)
                .build();
    }
}
