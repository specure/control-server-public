package com.specure.mapper.mobile;

import com.specure.common.model.dto.qos.QosParams;
import com.specure.dto.sah.qos.QosTestObjective;
import com.specure.mapper.mobile.impl.MobileQosTestObjectiveMapperImpl;
import com.specure.response.mobile.MobileQosParamsResponse;
import com.specure.common.utils.testscript.TestScriptInterpreter;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;

class MobileQosTestObjectiveMapperTest {

    private MobileQosTestObjectiveMapper mapper = new MobileQosTestObjectiveMapperImpl();

    @Test
    void qosTestObjectiveToQosParamsResponse_shouldMapFields() {
        try (MockedStatic<TestScriptInterpreter> scriptInterpreterMockedStatic = Mockito.mockStatic(TestScriptInterpreter.class)) {
            scriptInterpreterMockedStatic.when(() -> TestScriptInterpreter.interprete(any(), any())).thenAnswer(i -> i.getArguments()[0]);
            // Arrange
            QosTestObjective qosTestObjective = new QosTestObjective();
            qosTestObjective.setId(1);
            qosTestObjective.setConcurrencyGroup(2);
            qosTestObjective.setTestServerAddress("testAddress");
            qosTestObjective.setTestServerPort("testPort");
            QosParams qosParams = new QosParams();
            qosParams.setPort("port");
            qosParams.setRequest("request");
            qosParams.setTimeout("timeout");
            qosParams.setUrl("url");
            qosParams.setOutNumPackets("outNumPackets");
            qosParams.setOutPort("outPort");
            qosParams.setDownloadTimeout("downloadTimeout");
            qosParams.setConnTimeout("connTimeout");
            qosParams.setRecord("record");
            qosParams.setHost("host");
            qosParams.setCallDuration("callDuration");
            qosParams.setInPort("inPort");
            qosParams.setResolver("resolver");
            qosParams.setRange("range");
            qosParams.setInNumPackets("inNumPackets");
            qosTestObjective.setParam(qosParams);

            // Act
            MobileQosParamsResponse result = mapper.qosTestObjectiveToQosParamsResponse(qosTestObjective);

            // Assert
            assertNotNull(result);
            assertEquals(String.valueOf(qosTestObjective.getId()), result.getQosTestUid());
            assertEquals(String.valueOf(qosTestObjective.getConcurrencyGroup()), result.getConcurrencyGroup());
            assertEquals(qosTestObjective.getTestServerAddress(), result.getServerAddress());
            assertEquals(qosTestObjective.getTestServerPort(), result.getServerPort());
            assertEquals(qosParams.getPort(), result.getPort());
            assertEquals(qosParams.getRequest(), result.getRequest());
            assertEquals(qosParams.getTimeout(), result.getTimeout());
            assertEquals(qosParams.getUrl(), result.getUrl());
            assertEquals(qosParams.getOutNumPackets(), result.getOutNumPackets());
            assertEquals(qosParams.getOutPort(), result.getOutPort());
            assertEquals(qosParams.getDownloadTimeout(), result.getDownloadTimeout());
            assertEquals(qosParams.getConnTimeout(), result.getConnTimeout());
            assertEquals(qosParams.getRecord(), result.getRecord());
            assertEquals(qosParams.getHost(), result.getHost());
            assertEquals(qosParams.getCallDuration(), result.getCallDuration());
            assertEquals(qosParams.getInPort(), result.getInPort());
            assertEquals(qosParams.getResolver(), result.getResolver());
            assertEquals(qosParams.getRange(), result.getRange());
            assertEquals(qosParams.getInNumPackets(), result.getInNumPackets());
        }

    }
}
