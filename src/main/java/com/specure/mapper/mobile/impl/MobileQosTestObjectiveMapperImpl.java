package com.specure.mapper.mobile.impl;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.specure.mapper.mobile.MobileQosTestObjectiveMapper;
import com.specure.response.mobile.MobileQosParamsResponse;
import com.specure.dto.sah.qos.QosTestObjective;
import com.specure.common.utils.testscript.TestScriptInterpreter;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class MobileQosTestObjectiveMapperImpl implements MobileQosTestObjectiveMapper {

    @Override
    public MobileQosParamsResponse qosTestObjectiveToQosParamsResponse(QosTestObjective qosTestObjective) {
        MobileQosParamsResponse mobileQosParamsResponse = MobileQosParamsResponse.builder()
                .qosTestUid(String.valueOf(qosTestObjective.getId()))
                .concurrencyGroup(String.valueOf(qosTestObjective.getConcurrencyGroup()))
                .serverAddress(qosTestObjective.getTestServerAddress())
                .serverPort(String.valueOf(qosTestObjective.getTestServerPort()))
                .port(qosTestObjective.getParam().getPort())
                .request(qosTestObjective.getParam().getRequest())
                .timeout(qosTestObjective.getParam().getTimeout())
                .url(qosTestObjective.getParam().getUrl())
                .outNumPackets(qosTestObjective.getParam().getOutNumPackets())
                .outPort(qosTestObjective.getParam().getOutPort())
                .downloadTimeout(qosTestObjective.getParam().getDownloadTimeout())
                .connTimeout(qosTestObjective.getParam().getConnTimeout())
                .record(qosTestObjective.getParam().getRecord())
                .host(qosTestObjective.getParam().getHost())
                .callDuration(qosTestObjective.getParam().getCallDuration())
                .inPort(qosTestObjective.getParam().getInPort())
                .resolver(qosTestObjective.getParam().getResolver())
                .range(qosTestObjective.getParam().getRange())
                .inNumPackets(qosTestObjective.getParam().getInNumPackets())
                .build();
        return interpret(mobileQosParamsResponse);
    }

    private MobileQosParamsResponse interpret(MobileQosParamsResponse mobileQosParamsResponse) {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> qosTestObjectiveMap = mapper.convertValue(mobileQosParamsResponse, Map.class);
        return mapper.convertValue(qosTestObjectiveMap.entrySet()
                .stream()
                .filter(entry -> Objects.nonNull(entry.getValue()))
                .map(l -> Pair.of(l.getKey(), String.valueOf(TestScriptInterpreter.interprete(l.getValue(), null))))
                .collect(Collectors.toMap(Pair::getKey, Pair::getValue)), MobileQosParamsResponse.class);
    }
}
