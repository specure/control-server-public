package com.specure.service.sah.impl;

import com.specure.common.enums.ClientType;
import com.specure.common.enums.PortType;
import com.specure.common.enums.TestType;
import com.specure.common.model.dto.TestResultCounter;
import com.specure.common.model.elastic.BasicQosTest;
import com.specure.common.model.elastic.BasicTest;
import com.specure.common.model.jpa.MeasurementQos;
import com.specure.common.model.jpa.MeasurementServer;
import com.specure.common.model.jpa.qos.*;
import com.specure.mapper.mobile.BasicTestMobileMapper;
import com.specure.multitenant.MultiTenantManager;
import com.specure.repository.sah.BasicQosTestRepository;
import com.specure.response.mobile.BasicTestHistoryMobileResponse;
import com.specure.service.BasicQosTestService;
import com.specure.service.core.MeasurementServerService;
import com.specure.service.sah.BasicTestHistoryCacheService;
import com.specure.service.sah.BasicTestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("basicQosTest")
@Primary
@RequiredArgsConstructor
@Slf4j
public class BasicQosTestServiceImpl implements BasicQosTestService {

    private final BasicQosTestRepository basicQosTestRepository;
    @Qualifier(value = "basicTestService")
    private final BasicTestService basicTestService;
    @Qualifier(value = "basicMeasurementServerService")
    private final MeasurementServerService basicMeasurementServerService;
    private final MultiTenantManager multiTenantManager;
    private final BasicTestMobileMapper basicTestMobileMapper;
    private final BasicTestHistoryCacheService basicTestHistoryCacheService;

    @Override
    public List<BasicQosTest> getBasicQosTestsByBasicTestUuid(Collection<String> uuid) {
        log.debug("BasicQosTestServiceImpl:getBasicQosTestsByBasicTestUuid started with tenant = {}, uuids = {}", multiTenantManager.getCurrentTenant(), uuid);
        List<BasicQosTest> basicQosTests = basicQosTestRepository.findByBasicTestUuid(uuid);
        log.debug("BasicQosTestServiceImpl:getBasicQosTestsByBasicTestUuid finished with tenant = {}, basicQosTests = {}", multiTenantManager.getCurrentTenant(), basicQosTests);
        return basicQosTests;
    }

    @Override
    public BasicQosTest getBasicQosTestByOpenTestUuid(String uuid) {
        log.debug("BasicQosTestServiceImpl:getBasicQosTestByOpenTestUuid started with tenant = {}, uuid = {}", multiTenantManager.getCurrentTenant(), uuid);
        BasicQosTest basicQosTest = basicQosTestRepository.findByBasicTestUuid(uuid)
                .orElse(null);
        log.debug("BasicQosTestServiceImpl:getBasicQosTestByOpenTestUuid finished with tenant = {}, basicQosTest = {}", multiTenantManager.getCurrentTenant(), basicQosTest);
        return basicQosTest;
    }

    @Override
    public void saveMeasurementQosToElastic(MeasurementQos measurementQos) {
        Float tcpRate = tcpGetRate(measurementQos.getTcpTestResults());
        Float dnsRate = dnsGetRate(measurementQos.getDnsTestResults());
        Float httpProxyRate = httpProxyRate(measurementQos.getHttpProxyTestResults());
        Float nonTransparentRate = nonTransparentRate(measurementQos.getNonTransparentProxyTestResults());
        Float udpRate = udpRate(measurementQos.getUdpTestResults());
        Float voipRate = voipRate(measurementQos.getVoipTestResults());
        Float websiteRate = websiteRate(measurementQos.getWebsiteTestResults());
        Float tracerouteRate = tracerouteRate(measurementQos.getTracerouteTestResults());
        BasicQosTest.BasicQosTestBuilder basicQosTestBuilder = BasicQosTest.builder()
                .tcp(tcpRate)
                .dns(dnsRate)
                .httpProxy(httpProxyRate)
                .nonTransparentProxy(nonTransparentRate)
                .udp(udpRate)
                .voip(voipRate)
                .website(websiteRate)
                .traceroute(tracerouteRate);
        float overall = (tcpRate + dnsRate
                + httpProxyRate + nonTransparentRate + udpRate
                + voipRate) / 6.0f;
        saveBasicQosMeasurement(measurementQos, basicQosTestBuilder, overall, Collections.emptyList());
        log.debug("BasicQosTestServiceImpl:saveMeasurementQosToElastic finished with tenant = {}", multiTenantManager.getCurrentTenant());
    }

    @Override
    public BasicQosTest saveMeasurementQosMobileToElastic(MeasurementQos measurementQos) {
        log.info("BasicQosTestServiceImpl:saveMeasurementQosMobileToElastic started with tenant = {}, measurementQos = {}", multiTenantManager.getCurrentTenant(), measurementQos);
        List<TestResultCounter> qosResultCounters = getTestCountersList(measurementQos);
        BasicQosTest.BasicQosTestBuilder basicQosTestBuilder = BasicQosTest.builder()
                .tcp(getRateByTestType(TestType.TCP, qosResultCounters))
                .dns(getRateByTestType(TestType.DNS, qosResultCounters))
                .httpProxy(getRateByTestType(TestType.HTTP_PROXY, qosResultCounters))
                .nonTransparentProxy(getRateByTestType(TestType.NON_TRANSPARENT_PROXY, qosResultCounters))
                .udp(getRateByTestType(TestType.UDP, qosResultCounters))
                .voip(getRateByTestType(TestType.VOIP, qosResultCounters))
                .website(getRateByTestType(TestType.WEBSITE, qosResultCounters))
                .traceroute(getRateByTestType(TestType.TRACEROUTE, qosResultCounters))
                .typeOfProbePort(PortType.MOBILE.name())
                .clientType(ClientType.MOBILE.name());
        float overall = getOverallQosMobile(qosResultCounters);
        return saveBasicQosMeasurement(measurementQos, basicQosTestBuilder, overall, qosResultCounters);
    }

    private BasicQosTest saveBasicQosMeasurement(MeasurementQos measurementQos, BasicQosTest.BasicQosTestBuilder basicQosTestBuilder, float overall, List<TestResultCounter> qosResultCounters) {
        log.debug("BasicQosTestServiceImpl:saveBasicQosMeasurement started with tenant = {}, measurementQos = {}, basicQosTestBuilder = {}, overallQos = {}, qosResultCounters = {}",
                multiTenantManager.getCurrentTenant(), measurementQos, basicQosTestBuilder, overall, qosResultCounters);
        BasicTest basicTest = basicTestService.getBasicTestByUUID(measurementQos.getOpenTestUuid());
        log.trace("BasicQosTestServiceImpl:saveBasicQosMeasurement tenant = {}, basicTest = {}", multiTenantManager.getCurrentTenant(), basicTest);
        basicQosTestBuilder
                .openTestUuid(measurementQos.getOpenTestUuid())
                .clientUuid(measurementQos.getClientUuid())
                .clientLanguage(measurementQos.getClientLanguage())
                .clientName(measurementQos.getClientName())
                .clientVersion(measurementQos.getClientVersion())
                .overallQos(overall)
                .qosTestResultCounters(qosResultCounters)
                .operator(basicTest.getOperator())
                .country(basicTest.getCountry());


        if (basicTest.getMeasurementServerId() != 0L) {
            MeasurementServer measurementServer = this.basicMeasurementServerService.getMeasurementServerById(basicTest.getMeasurementServerId());
            basicQosTestBuilder.measurementServerId(measurementServer.getId());
            basicQosTestBuilder.measurementServerName(measurementServer.getName());
        }

        if (basicTest.getTimestamp() != null) {
            log.trace("Time QOS _____ " + (basicTest.getTimestamp().getTime()));
            basicQosTestBuilder.timestamp(String.valueOf(basicTest.getTimestamp().getTime()));
            basicQosTestBuilder.measurementDate(basicTest.getMeasurementDate());
            basicQosTestBuilder.graphHour(basicTest.getGraphHour());
        }

        if (basicTest.getServerType() != null) {
            basicQosTestBuilder.serverType(basicTest.getServerType());
        }

        if (!Objects.isNull(measurementQos.getAdHocCampaign())) {
            basicQosTestBuilder.adHocCampaign(measurementQos.getAdHocCampaign().getId());
        }

        if (!Objects.isNull(basicTest.getAdHocCampaign())) {
            basicQosTestBuilder.adHocCampaign(basicTest.getAdHocCampaign());
        }

        BasicQosTest basicQosTest = basicQosTestBuilder.build();
        updateBasicQosTestWithProbe(basicTest.getTag(), basicTest.getDevice(), basicQosTest);
        log.info("BasicQosTestServiceImpl:saveBasicQosMeasurement started with tenant = {}, measurementQos = {}", multiTenantManager.getCurrentTenant(), basicQosTest);


        BasicTestHistoryMobileResponse basicTestHistoryMobileResponse = basicTestMobileMapper.basicTestResponseToBasicTestHistoryMobileResponse(basicTest, basicQosTest);
        basicTestHistoryCacheService.save(basicTestHistoryMobileResponse);

        basicQosTestRepository.save(basicQosTest);
        log.debug("BasicQosTestServiceImpl:saveBasicQosMeasurement finished with tenant = {}, basicQosTest = {}", multiTenantManager.getCurrentTenant(), basicQosTest);
        return basicQosTest;
    }


    private List<TestResultCounter> getTestCountersList(MeasurementQos measurementQos) {
        log.trace("BasicQosTestServiceImpl:getTestCountersList started with tenant = {}, measurementQos = {}", multiTenantManager.getCurrentTenant(), measurementQos);
        List<TestResultCounter> qosResultCounters = new ArrayList<>();
        qosResultCounters.add(getSucceededAndFailedPair(measurementQos.getTcpTestResults(), TestResultCounter.builder().testType(TestType.TCP)));
        qosResultCounters.add(getSucceededAndFailedPair(measurementQos.getDnsTestResults(), TestResultCounter.builder().testType(TestType.DNS)));
        qosResultCounters.add(getSucceededAndFailedPair(measurementQos.getHttpProxyTestResults(), TestResultCounter.builder().testType(TestType.HTTP_PROXY)));
        qosResultCounters.add(getSucceededAndFailedPair(measurementQos.getVoipTestResults(), TestResultCounter.builder().testType(TestType.VOIP)));
        qosResultCounters.add(getSucceededAndFailedPair(measurementQos.getNonTransparentProxyTestResults(), TestResultCounter.builder().testType(TestType.NON_TRANSPARENT_PROXY)));
        qosResultCounters.add(getSucceededAndFailedPair(measurementQos.getUdpTestResults(), TestResultCounter.builder().testType(TestType.UDP)));
        qosResultCounters.add(getSucceededAndFailedPair(measurementQos.getWebsiteTestResults(), TestResultCounter.builder().testType(TestType.WEBSITE)));
        qosResultCounters.add(getSucceededAndFailedPair(measurementQos.getTracerouteTestResults(), TestResultCounter.builder().testType(TestType.TRACEROUTE)));
        log.trace("BasicQosTestServiceImpl:getTestCountersList finished with tenant = {}, qosResultCounters = {}", multiTenantManager.getCurrentTenant(), qosResultCounters);
        return qosResultCounters;
    }

    private Float getRateByTestType(TestType testType, List<TestResultCounter> qosResultCounters) {
        return qosResultCounters.stream()
                .filter(t -> testType.equals(t.getTestType()))
                .filter(t -> t.getTotalCount() + t.getSuccessCount() > 0)
                .findFirst()
                .map(t -> (float) t.getSuccessCount() / t.getTotalCount())
                .orElse(NumberUtils.FLOAT_ZERO);
    }

    private TestResultCounter getSucceededAndFailedPair(List<? extends QosResult> results, TestResultCounter.TestResultCounterBuilder testResultCounterDtoBuilder) {
        if (!isValidList(results)) {
            return testResultCounterDtoBuilder.successCount(0).totalCount(0).build();
        }
        Integer size = results.size();
        Integer passed = (int) results.stream().filter(e -> e.getSuccessCount() > 0).count();
        return testResultCounterDtoBuilder.successCount(passed).totalCount(size).build();
    }

    private float getOverallQosMobile(List<TestResultCounter> qosResultCounters) {
        float succeeded = 0f;
        float total = 0f;
        for (TestResultCounter testResultCounter : qosResultCounters) {
            succeeded += testResultCounter.getSuccessCount().floatValue();
            total += testResultCounter.getTotalCount().floatValue();
        }
        return succeeded / total;
    }

    private float tcpGetRate(List<TcpTestResult> results) {
        if (!isValidList(results)) {
            return 0.0f;
        }
        float size = results.size();
        float passed = results.stream().filter(e -> "OK".equals(e.getTcpResultOut())).count();
        return passed / size;
    }

    private float dnsGetRate(List<DnsTestResult> results) {
        if (!isValidList(results)) {
            return 0.0f;
        }
        float size = results.size();
        float passed = results.stream().filter(e -> "OK".equals(e.getDnsResultInfo())).count();
        return passed / size;
    }

    private float nonTransparentRate(List<NonTransparentProxyTestResult> results) {
        if (!isValidList(results)) {
            return 0.0f;
        }
        float size = results.size();
        float passed = results.stream().filter(e -> "OK".equals(e.getNonTransparentProxyResult())).count();
        return passed / size;
    }

    private float httpProxyRate(List<HttpProxyTestResult> results) {
        if (!isValidList(results)) {
            return 0.0f;
        }
        float size = results.size();
        float passed = results.stream().filter(e -> (e.getHttpResultStatus() == 200)).count();
        return passed / size;
    }

    private float udpRate(List<UdpTestResult> results) {
        if (!isValidList(results)) {
            return 0.0f;
        }
        float size = results.size();
        float passed = results.stream().filter(e -> ("0".equals(e.getUdpResultOutPacketLossRate()))).count();
        return passed / size;
    }

    private float voipRate(List<VoipTestResult> results) {
        if (!isValidList(results)) {
            return 0.0f;
        }
        float size = results.size();
        float passed = results.stream().filter(e -> ("OK".equals(e.getVoipResultStatus()))).count();
        return passed / size;
    }

    private float websiteRate(List<WebsiteTestResult> results) {
        if (!isValidList(results)) {
            return 0.0f;
        }
        float size = results.size();
        float passed = results.stream().filter(e -> ("OK".equals(e.getWebsiteResultStatus()))).count();
        return passed / size;
    }

    private float tracerouteRate(List<TracerouteTestResult> results) {
        if (!isValidList(results)) {
            return 0.0f;
        }
        float size = results.size();
        float passed = results.stream().filter(e -> ("OK".equals(e.getTracerouteResultStatus()))).count();
        return passed / size;
    }

    private <T> boolean isValidList(List<T> results) {
        if (results == null) {
            return false;
        }
        return (results.size()) != 0;
    }

    protected void updateBasicQosTestWithProbe(String probePortName, String probeId, BasicQosTest basicQosTest) {
        //do nothing
    }
}
