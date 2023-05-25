package com.specure.mapper.core.impl;

import com.specure.common.enums.TestType;
import com.specure.common.exception.HstoreParseException;
import com.specure.common.model.dto.qos.AbstractResult;
import com.specure.common.model.dto.qos.ResultDesc;
import com.specure.common.model.dto.qos.ResultOptions;
import com.specure.common.model.jpa.MeasurementQos;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.specure.common.model.jpa.qos.*;
import com.specure.config.ApplicationProperties;
import com.specure.dto.sah.qos.QosTestResult;
import com.specure.mapper.core.*;
import com.specure.mapper.mobile.MobileQosTestResultMapper;
import com.specure.repository.mobile.QosTestObjectiveRepository;
import com.specure.request.core.MeasurementQosRequest;
import com.specure.request.core.measurement.qos.request.*;
import com.specure.utils.mobile.QosUtil;
import com.specure.utils.sah.MessageUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Component
@Slf4j
@AllArgsConstructor
public class MeasurementQosMapperImpl implements MeasurementQosMapper {

    private final TcpTestResultMapper tcpTestResultMapper;
    private final VoipTestResultMapper voipTestResultMapper;
    private final UdpTestResultMapper udpTestResultMapper;
    private final HttpProxyTestResultMapper httpProxyTestResultMapper;
    private final NonTransparentProxyTestResultMapper nonTransparentProxyTestResultMapper;
    private final DnsTestResultMapper dnsTestResultMapper;
    private final TracerouteTestResultMapper tracerouteTestResultMapper;
    private final WebsiteTestResultMapper websiteTestResultMapper;
    private final MobileQosTestResultMapper mobileQosTestResultMapper;
    private final QosTestObjectiveRepository qosTestObjectiveRepository;
    private final ObjectMapper objectMapper;
    private final ApplicationProperties applicationProperties;

    @Override
    public MeasurementQos measurementQosRequestToMeasurementQos(MeasurementQosRequest measurementQosRequest) {
        MeasurementQos measurementQos = buildMeasurementQos(measurementQosRequest);
        Consumer<QosResult> peekFunction = p -> peekMeasurementQosTestResult(measurementQos, p);
        return processMeasurementQos(measurementQosRequest, measurementQos, peekFunction);
    }

    @Override
    public MeasurementQos measurementQosRequestToMeasurementQosMobile(MeasurementQosRequest measurementQosRequest) {
        Locale locale = MessageUtils.getLocaleFormLanguage(measurementQosRequest.getClientLanguage(), applicationProperties.getLanguage());
        MeasurementQos measurementQos = buildMeasurementQos(measurementQosRequest);
        Consumer<QosResult> peekFunction = p -> peekMeasurementQosTestResultMobile(measurementQos, p, locale);
        return processMeasurementQos(measurementQosRequest, measurementQos, peekFunction);
    }

    private MeasurementQos processMeasurementQos(MeasurementQosRequest measurementQosRequest, MeasurementQos measurementQos, Consumer<QosResult> peekFunction) {
        List<TcpTestResult> tcpTestResults = measurementQosRequest
                .getQosResult()
                .stream()
                .filter(e -> e.getTest_type().equals("tcp"))
                .map(e -> (TcpTestResultRequest) e)
                .map(tcpTestResultMapper::tcpTestResultToTcpTestResultRequest)
                .peek(peekFunction)
                .collect(Collectors.toList());

        List<VoipTestResult> voipTestResults = measurementQosRequest
                .getQosResult()
                .stream()
                .filter(e -> e.getTest_type().equals("voip"))
                .map(e -> (VoipTestResultRequest) e)
                .map(voipTestResultMapper::voipTestResultRequestToVoipTestResult)
                .peek(peekFunction)
                .collect(Collectors.toList());

        List<UdpTestResult> udpTestResults = measurementQosRequest
                .getQosResult()
                .stream()
                .filter(e -> e.getTest_type().equals("udp"))
                .map(e -> (UdpTestResultRequest) e)
                .map(udpTestResultMapper::udpTestResultRequestToUdpTestResult)
                .peek(peekFunction)
                .collect(Collectors.toList());

        List<HttpProxyTestResult> httpProxyTestResults = measurementQosRequest
                .getQosResult()
                .stream()
                .filter(e -> e.getTest_type().equals("http_proxy"))
                .map(e -> (HttpProxyTestResultRequest) e)
                .map(httpProxyTestResultMapper::httpProxyTestResultRequestToHttpProxyTestResult)
                .peek(peekFunction)
                .collect(Collectors.toList());

        List<NonTransparentProxyTestResult> nonTransparentProxyTestResults = measurementQosRequest
                .getQosResult()
                .stream()
                .filter(e -> e.getTest_type().equals("non_transparent_proxy"))
                .map(e -> (NonTransparentProxyTestResultRequest) e)
                .map(nonTransparentProxyTestResultMapper::nonTransparentProxyTestResultRequestToNonTransparentProxyTestResult)
                .peek(peekFunction)
                .collect(Collectors.toList());

        List<DnsTestResult> dnsTestResults = measurementQosRequest
                .getQosResult()
                .stream()
                .filter(e -> e.getTest_type().equals("dns"))
                .map(e -> (DnsTestResultRequest) e)
                .map(dnsTestResultMapper::dnsTestResultRequestToDnsTestResult)
                .peek(e -> {
                    peekFunction.accept(e);
                    if (!Objects.isNull(e.getDnsResultEntries())) {
                        e.getDnsResultEntries().forEach(entry -> entry.setDnsTestResult(e));
                    }
                })
                .collect(Collectors.toList());

        List<TracerouteTestResult> tracerouteTestResults = measurementQosRequest
                .getQosResult()
                .stream()
                .filter(e -> e.getTest_type().equals("traceroute"))
                .map(e -> (TracerouteTestResultRequest) e)
                .map(tracerouteTestResultMapper::tracerouteTestResultRequestToWebsiteTestResult)
                .peek(e -> {
                    peekFunction.accept(e);
                    e.setMeasurementQos(measurementQos);
                    if (!Objects.isNull(e.getTracerouteResultDetails())) {
                        e.getTracerouteResultDetails().forEach(entry -> entry.setTracerouteTestResult(e));
                    }
                })
                .collect(Collectors.toList());

        List<WebsiteTestResult> websiteTestResults = measurementQosRequest
                .getQosResult()
                .stream()
                .filter(e -> e.getTest_type().equals("website"))
                .map(e -> (WebsiteTestResultRequest) e)
                .map(websiteTestResultMapper::websiteTestResultRequestToWebsiteTestResult)
                .peek(peekFunction)
                .collect(Collectors.toList());

        measurementQos.setTcpTestResults(tcpTestResults);
        measurementQos.setVoipTestResults(voipTestResults);
        measurementQos.setUdpTestResults(udpTestResults);
        measurementQos.setHttpProxyTestResults(httpProxyTestResults);
        measurementQos.setNonTransparentProxyTestResults(nonTransparentProxyTestResults);
        measurementQos.setDnsTestResults(dnsTestResults);
        measurementQos.setTracerouteTestResults(tracerouteTestResults);
        measurementQos.setWebsiteTestResults(websiteTestResults);
        return measurementQos;
    }

    private void peekMeasurementQosTestResultMobile(MeasurementQos measurementQos, QosResult e, Locale locale) {
        e.setMeasurementQos(measurementQos);
        setCounter(e, locale);
    }

    private void peekMeasurementQosTestResult(MeasurementQos measurementQos, QosResult e) {
        e.setMeasurementQos(measurementQos);
    }

    private MeasurementQos buildMeasurementQos(MeasurementQosRequest measurementQosRequest) {
        String openTestUuid = measurementQosRequest.getTestToken().split("_")[0];
        return MeasurementQos.builder()
                .time(new Timestamp(measurementQosRequest.getTime()))
                .openTestUuid(openTestUuid)
                .clientLanguage(measurementQosRequest.getClientLanguage())
                .clientVersion(measurementQosRequest.getClientVersion())
                .clientName(measurementQosRequest.getClientName())
                .clientUuid(measurementQosRequest.getClientUuid())
                .testToken(measurementQosRequest.getTestToken())
                .build();
    }

    private void setCounter(QosResult qosResult, Locale locale) {
        QosTestResult qosTestResult = mobileQosTestResultMapper.testResultToQosTestResult(qosResult);
        qosTestResult.setQosTestObjective(qosTestObjectiveRepository.getOne(qosTestResult.getQosTestObjectiveId()));
        qosTestResult.setFailureCount(0);
        qosTestResult.setSuccessCount(0);
        TestType testType = qosTestResult.getQosTestObjective().getTestType();
        if (testType == null) {
            return;
        }
        Class<? extends AbstractResult<?>> clazz = testType.getClazz();
        Map<TestType, TreeSet<ResultDesc>> resultKeys = new HashMap<>();
        final ResultOptions resultOptions = new ResultOptions(locale);
        try {
            if (qosTestResult.getQosTestObjective().getResults() != null) {
                AbstractResult<?> result = objectMapper.readValue(qosTestResult.getResult(), clazz);
                result.setResultMap(objectMapper.readValue(qosTestResult.getResult(), new TypeReference<>() {
                }));
                qosTestResult.setResult(objectMapper.writeValueAsString(result));
                //compare test results
                QosUtil.compareTestResults(qosTestResult, result, resultKeys, testType, resultOptions, objectMapper);
            }
        } catch (final JSONException | IllegalArgumentException | JsonProcessingException | IllegalAccessException |
                       HstoreParseException e) {
            log.error(e.getMessage(), e);
        }
        qosResult.setFailureCount(qosTestResult.getFailureCount());
        qosResult.setSuccessCount(qosTestResult.getSuccessCount());
    }
}
