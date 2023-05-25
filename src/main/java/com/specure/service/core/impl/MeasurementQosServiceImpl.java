package com.specure.service.core.impl;

import com.specure.common.enums.ServerNetworkType;
import com.specure.common.enums.ServerTechForMeasurement;
import com.specure.common.exception.MeasurementNotFoundByUuidException;
import com.specure.common.model.jpa.MeasurementQos;
import com.specure.common.model.jpa.MeasurementServer;
import com.specure.common.repository.MeasurementServerRepository;
import com.specure.exception.QoSMeasurementServerNotFoundByUuidException;
import com.specure.exception.QosMeasurementFromOnNetServerException;
import com.specure.mapper.core.MeasurementQosMapper;
import com.specure.multitenant.MultiTenantManager;
import com.specure.repository.core.MeasurementQosRepository;
import com.specure.request.core.MeasurementQosParametersRequest;
import com.specure.request.core.MeasurementQosRequest;
import com.specure.response.core.measurement.qos.response.*;
import com.specure.service.core.MeasurementQosService;
import com.specure.service.core.MeasurementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service("basicMeasurementQosService")
public class MeasurementQosServiceImpl implements MeasurementQosService {
    private final MeasurementServerRepository measurementServerRepository;
    private final MeasurementQosRepository measurementQosRepository;
    private final MeasurementQosMapper measurementQosMapper;
    private final MeasurementService measurementService;
    private final MultiTenantManager multiTenantManager;

    @Override
    public void saveMeasurementQos(MeasurementQosRequest measurementQosRequest) {
        log.info("MeasurementQosServiceImpl:saveMeasurementQos started with tenant = {}, measurementQosRequest = {}", multiTenantManager.getCurrentTenant(), measurementQosRequest);
        MeasurementQos measurementQos = measurementQosMapper.measurementQosRequestToMeasurementQos(measurementQosRequest);
        String token = measurementQos.getTestToken();
        var measurement = measurementService
                .getMeasurementByToken(token)
                .orElseThrow(() -> new MeasurementNotFoundByUuidException(token));
        if (!Objects.isNull(measurement.getAdHocCampaign())) {
            log.trace("MeasurementQosServiceImpl:saveMeasurementQos tenant = {}, set AdHocCampaign = {}", multiTenantManager.getCurrentTenant(), measurement.getAdHocCampaign());
            measurementQos.setAdHocCampaign(measurement.getAdHocCampaign());
        }
        if (ServerNetworkType.ON_NET.toString().equals(measurement.getServerType())) {
            throw new QosMeasurementFromOnNetServerException(measurement.getOpenTestUuid(), measurement.getMeasurementServerId());
        }
        MeasurementQos savedMeasurementQos = measurementQosRepository.save(measurementQos);
        log.debug("MeasurementQosServiceImpl:saveMeasurementQos finished, tenant = {}, saved MeasurementQos = {}", multiTenantManager.getCurrentTenant(), savedMeasurementQos);
    }

    @Override
    public MeasurementQosParametersResponse getQosParameters(MeasurementQosParametersRequest measurementQosParametersRequest) {
        log.debug("MeasurementQosServiceImpl:getQosParameters started with tenant = {}, request = {}", multiTenantManager.getCurrentTenant(), measurementQosParametersRequest);
        Optional<MeasurementServer> measurementServer = measurementServerRepository.findByClientUUID(measurementQosParametersRequest.getUuid());
        if (measurementServer.isEmpty()) {
            throw new QoSMeasurementServerNotFoundByUuidException(measurementQosParametersRequest.getUuid());
        }

        String serverAddress = measurementServer.get().getWebAddress();
        String serverPort = String.valueOf(ServerTechForMeasurement.QOS_TECH.getDefaultSslPort());

        var voip = Voip.builder()
                .concurrencyGroup("10")
                .inPort("5061")
                .outPort("5060")
                .serverPort(serverPort)
                .qosTestUid("76")
                .timeout("10000000000")
                .callDuration("2000000000")
                .serverAddr(serverAddress)
                .build();

        var jitter = Jitter.builder()
                .concurrency_group("10")
                .in_port("7061")
                .out_port("7060")
                .server_port(serverPort)
                .qos_test_uid("169")
                .timeout("10000000000")
                .call_duration("2000000000")
                .server_addr(serverAddress)
                .build();
        var tcp = List.of(
                TcpSettingsResponse.builder().concurrencyGroup("200").serverPort(serverPort).qosTestUid("116").serverAddr(serverAddress).timeout("5000000000").outPort("143").build(),
                TcpSettingsResponse.builder().concurrencyGroup("200").serverPort(serverPort).qosTestUid("100").serverAddr(serverAddress).timeout("5000000000").outPort("53").build(),
                TcpSettingsResponse.builder().concurrencyGroup("200").serverPort(serverPort).qosTestUid("91").serverAddr(serverAddress).timeout("5000000000").outPort("587").build(),
                TcpSettingsResponse.builder().concurrencyGroup("200").serverPort(serverPort).qosTestUid("92").serverAddr(serverAddress).timeout("5000000000").outPort("993").build(),
                TcpSettingsResponse.builder().concurrencyGroup("200").serverPort(serverPort).qosTestUid("93").serverAddr(serverAddress).timeout("5000000000").outPort("995").build(),
                TcpSettingsResponse.builder().concurrencyGroup("200").serverPort(serverPort).qosTestUid("94").serverAddr(serverAddress).timeout("5000000000").outPort("5060").build(),
                TcpSettingsResponse.builder().concurrencyGroup("200").serverPort(serverPort).qosTestUid("95").serverAddr(serverAddress).timeout("5000000000").outPort("9001").build(),
                TcpSettingsResponse.builder().concurrencyGroup("200").serverPort(serverPort).qosTestUid("96").serverAddr(serverAddress).timeout("5000000000").outPort("554").build(),
//                TcpSettingsResponse.builder().concurrencyGroup("200").serverPort(serverPort).qosTestUid("97").serverAddr(serverAddress).timeout("5000000000").outPort("80").build(),
                TcpSettingsResponse.builder().concurrencyGroup("200").serverPort(serverPort).qosTestUid("114").serverAddr(serverAddress).timeout("5000000000").outPort("6881").build(),
                TcpSettingsResponse.builder().concurrencyGroup("200").serverPort(serverPort).qosTestUid("117").serverAddr(serverAddress).timeout("5000000000").outPort("465").build(),
                TcpSettingsResponse.builder().concurrencyGroup("200").serverPort(serverPort).qosTestUid("118").serverAddr(serverAddress).timeout("5000000000").outPort("585").build(),
                TcpSettingsResponse.builder().concurrencyGroup("200").serverPort(serverPort).qosTestUid("115").serverAddr(serverAddress).timeout("5000000000").outPort("110").build()

//              this is ports return errors on measurements; fast solution is delete ones
//                TcpSettingsResponse.builder().concurrencyGroup("200").serverPort(serverPort).qosTestUid("98").serverAddr(serverAddress).timeout("5000000000").outPort("21").build(),
//                TcpSettingsResponse.builder().concurrencyGroup("200").serverPort(serverPort).qosTestUid("99").serverAddr(serverAddress).timeout("5000000000").outPort("22").build(),
//                TcpSettingsResponse.builder().concurrencyGroup("200").serverPort(serverPort).qosTestUid("90").serverAddr(serverAddress).timeout("5000000000").outPort("25").build(),
        );
        var udp = List.of(
                UdpSettingsResponse.builder().concurrencyGroup("200").outNumPackets("1").serverPort(serverPort).qosTestUid("102").serverAddr(serverAddress).timeout("5000000000").outPort("123").build(),
                UdpSettingsResponse.builder().concurrencyGroup("200").outNumPackets("1").serverPort(serverPort).qosTestUid("103").serverAddr(serverAddress).timeout("5000000000").outPort("27015").build(),
                UdpSettingsResponse.builder().concurrencyGroup("200").outNumPackets("1").serverPort(serverPort).qosTestUid("119").serverAddr(serverAddress).timeout("5000000000").outPort("500").build(),
                UdpSettingsResponse.builder().concurrencyGroup("200").outNumPackets("1").serverPort(serverPort).qosTestUid("120").serverAddr(serverAddress).timeout("5000000000").outPort("5060").build(),
                UdpSettingsResponse.builder().concurrencyGroup("200").outNumPackets("1").serverPort(serverPort).qosTestUid("122").serverAddr(serverAddress).timeout("5000000000").outPort("5005").build(),
                UdpSettingsResponse.builder().concurrencyGroup("200").outNumPackets("1").serverPort(serverPort).qosTestUid("124").serverAddr(serverAddress).timeout("5000000000").outPort("7078").build(),
                UdpSettingsResponse.builder().concurrencyGroup("200").outNumPackets("1").serverPort(serverPort).qosTestUid("157").serverAddr(serverAddress).timeout("5000000000").outPort("7082").build()

//              this is ports return errors on measurements; fast solution is delete ones
//                UdpSettingsResponse.builder().concurrencyGroup("200").outNumPackets("1").serverPort(serverPort).qosTestUid("101").serverAddr(serverAddress).timeout("5000000000").outPort("5004").build(),
//                UdpSettingsResponse.builder().concurrencyGroup("200").outNumPackets("1").serverPort(serverPort).qosTestUid("104").serverAddr(serverAddress).timeout("5000000000").outPort("53").build(),
//                UdpSettingsResponse.builder().concurrencyGroup("200").outNumPackets("1").serverPort(serverPort).qosTestUid("121").serverAddr(serverAddress).timeout("5000000000").outPort("27005").build(),
//                UdpSettingsResponse.builder().concurrencyGroup("200").outNumPackets("1").serverPort(serverPort).qosTestUid("123").serverAddr(serverAddress).timeout("5000000000").outPort("5004").build(),
        );
        var httpProxy = List.of(
                HttpProxySettingsResponse.builder().concurrencyGroup("400").downloadTimeout("10000000000").serverPort(serverPort).qosTestUid("160").serverAddr(serverAddress).url("http://nettest.org/qostest/reference05.jpg").connTimeout("5000000000").build(),
                HttpProxySettingsResponse.builder().concurrencyGroup("400").downloadTimeout("10000000000").serverPort(serverPort).qosTestUid("163").serverAddr(serverAddress).url("http://nettest.org/qostest/reference01.jpg").connTimeout("5000000000").build()
        );
        var website = List.of(
                WebsiteSettingsResponse.builder().concurrencyGroup("500").serverPort(serverPort).qosTestUid("159").serverAddr(serverAddress).url("http://nettest.org/kepler/").timeout("10000000000").build()
        );
        var nonTransparentProxy = List.of(
//                NonTransparentProxySettingsResponse.builder().request("GET ").port("80").concurrencyGroup("300").serverPort(serverPort).qosTestUid("84").serverAddr(serverAddress).timeout("5000000000").build(),
                NonTransparentProxySettingsResponse.builder().request("GET / HTTR/7.9").port("22222").concurrencyGroup("300").serverPort(serverPort).qosTestUid("85").serverAddr(serverAddress).timeout("5000000000").build(),
                NonTransparentProxySettingsResponse.builder().request("GET ").port("44444").concurrencyGroup("300").serverPort(serverPort).qosTestUid("140").serverAddr(serverAddress).timeout("5000000000").build()
//                NonTransparentProxySettingsResponse.builder().request("GET / HTTR/7.9").port("80").concurrencyGroup("300").serverPort(serverPort).qosTestUid("141").serverAddr(serverAddress).timeout("5000000000").build(),
//                NonTransparentProxySettingsResponse.builder().request("SMTP Transparent").port("25").concurrencyGroup("300").serverPort(serverPort).qosTestUid("158").serverAddr(serverAddress).timeout("5000000000").build()
        );
        var dns = List.of(
                DnsSettingsResponse.builder().concurrencyGroup("600").record("A").host("www.google.rs").serverPort(serverPort).qosTestUid("87").serverAddr(serverAddress).timeout("5000000000").build(),
                DnsSettingsResponse.builder().concurrencyGroup("600").record("A").host("limundo.com").serverPort(serverPort).qosTestUid("164").serverAddr(serverAddress).timeout("5000000000").build(),
                DnsSettingsResponse.builder().concurrencyGroup("640").record("A").host("avto.net").serverPort(serverPort).qosTestUid("150").serverAddr(serverAddress).timeout("5000000000").build(),
                DnsSettingsResponse.builder().resolver("8.8.8.8").concurrencyGroup("630").record("A").host("www.f9f520858f.darknet.akostest.net").serverPort(serverPort).qosTestUid("156").serverAddr(serverAddress).timeout("5000000000").build(),
                DnsSettingsResponse.builder().concurrencyGroup("600").record("A").host("24ur.com").serverPort(serverPort).qosTestUid("105").serverAddr(serverAddress).timeout("5000000000").build(),
                DnsSettingsResponse.builder().concurrencyGroup("610").record("A").host("touch.darkspace.akostest.net").serverPort(serverPort).qosTestUid("110").serverAddr(serverAddress).timeout("5000000000").build(),
                DnsSettingsResponse.builder().concurrencyGroup("640").record("A").host("najdi.si").serverPort(serverPort).qosTestUid("128").serverAddr(serverAddress).timeout("5000000000").build(),
                DnsSettingsResponse.builder().concurrencyGroup("600").record("A").host("slovenskenovice.si").serverPort(serverPort).qosTestUid("162").serverAddr(serverAddress).timeout("5000000000").build(),
                DnsSettingsResponse.builder().concurrencyGroup("600").record("A").host("apple.com").serverPort(serverPort).qosTestUid("83").serverAddr(serverAddress).timeout("5000000000").build(),
                DnsSettingsResponse.builder().resolver("8.8.8.8").concurrencyGroup("620").record("A").host("bolha.com").serverPort(serverPort).qosTestUid("142").serverAddr(serverAddress).timeout("5000000000").build(),
                DnsSettingsResponse.builder().concurrencyGroup("630").record("A").host("rtvslo.si").serverPort(serverPort).qosTestUid("143").serverAddr(serverAddress).timeout("5000000000").build(),
                DnsSettingsResponse.builder().concurrencyGroup("630").record("A").host("www.e79321c2d3.darknet.akostest.net").serverPort(serverPort).qosTestUid("145").serverAddr(serverAddress).timeout("5000000000").build(),
                DnsSettingsResponse.builder().concurrencyGroup("620").record("A").host("ftp.9d88ec3ff2.com").serverPort(serverPort).qosTestUid("89").serverAddr(serverAddress).timeout("5000000000").build(),
                DnsSettingsResponse.builder().concurrencyGroup("600").record("A").host("facebook.com").serverPort(serverPort).qosTestUid("87").serverAddr(serverAddress).timeout("5000000000").build(),
                DnsSettingsResponse.builder().concurrencyGroup("610").record("A").host("wikipedia.org").serverPort(serverPort).qosTestUid("108").serverAddr(serverAddress).timeout("5000000000").build(),
                DnsSettingsResponse.builder().concurrencyGroup("610").record("A").host("yahoo.com").serverPort(serverPort).qosTestUid("109").serverAddr(serverAddress).timeout("5000000000").build(),
                DnsSettingsResponse.builder().concurrencyGroup("610").record("A").host("simobil.si").serverPort(serverPort).qosTestUid("113").serverAddr(serverAddress).timeout("5000000000").build(),
                DnsSettingsResponse.builder().concurrencyGroup("610").record("A").host("telekom.si").serverPort(serverPort).qosTestUid("112").serverAddr(serverAddress).timeout("5000000000").build(),
                DnsSettingsResponse.builder().concurrencyGroup("640").record("A").host("www.google.com").serverPort(serverPort).qosTestUid("129").serverAddr(serverAddress).timeout("5000000000").build(),
                DnsSettingsResponse.builder().concurrencyGroup("620").record("A").host("microsoft.com").serverPort(serverPort).qosTestUid("138").serverAddr(serverAddress).timeout("5000000000").build(),
                DnsSettingsResponse.builder().concurrencyGroup("630").record("A").host("youtube.com").serverPort(serverPort).qosTestUid("144").serverAddr(serverAddress).timeout("5000000000").build(),
                DnsSettingsResponse.builder().concurrencyGroup("640").record("A").host("t-2.net").serverPort(serverPort).qosTestUid("130").serverAddr(serverAddress).timeout("5000000000").build(),
                DnsSettingsResponse.builder().concurrencyGroup("620").record("A").host("amis.net").serverPort(serverPort).qosTestUid("136").serverAddr(serverAddress).timeout("5000000000").build(),
                DnsSettingsResponse.builder().concurrencyGroup("620").record("A").host("telemach.si").serverPort(serverPort).qosTestUid("137").serverAddr(serverAddress).timeout("5000000000").build(),
                DnsSettingsResponse.builder().concurrencyGroup("640").record("MX").host("gov.si").serverPort(serverPort).qosTestUid("151").serverAddr(serverAddress).timeout("5000000000").build(),
                DnsSettingsResponse.builder().concurrencyGroup("630").record("A").host("akos-rs.si").serverPort(serverPort).qosTestUid("154").serverAddr(serverAddress).timeout("5000000000").build(),
                DnsSettingsResponse.builder().concurrencyGroup("620").record("A").host("finance.si").serverPort(serverPort).qosTestUid("155").serverAddr(serverAddress).timeout("5000000000").build(),
                DnsSettingsResponse.builder().concurrencyGroup("600").record("A").host("www.c144f9db70.net").serverPort(serverPort).qosTestUid("82").serverAddr(serverAddress).timeout("5000000000").build(),
                DnsSettingsResponse.builder().concurrencyGroup("610").record("A").host("twitter.com").serverPort(serverPort).qosTestUid("107").serverAddr(serverAddress).timeout("5000000000").build(),
                DnsSettingsResponse.builder().concurrencyGroup("640").record("AAAA").host("facebook.com").serverPort(serverPort).qosTestUid("126").serverAddr(serverAddress).timeout("5000000000").build(),
                DnsSettingsResponse.builder().concurrencyGroup("640").record("AAAA").host("google.com").serverPort(serverPort).qosTestUid("127").serverAddr(serverAddress).timeout("5000000000").build(),
                DnsSettingsResponse.builder().concurrencyGroup("630").record("A").host("invalidname.3c0b3194ba.com").serverPort(serverPort).qosTestUid("87").serverAddr(serverAddress).timeout("5000000000").build(),
                DnsSettingsResponse.builder().concurrencyGroup("630").record("AAAA").host("youtube.com").serverPort(serverPort).qosTestUid("149").serverAddr(serverAddress).timeout("5000000000").build(),
                DnsSettingsResponse.builder().concurrencyGroup("640").record("AAAA").host("wikipedia.org").serverPort(serverPort).qosTestUid("152").serverAddr(serverAddress).timeout("5000000000").build(),
                DnsSettingsResponse.builder().concurrencyGroup("610").record("A").host("www.61ccffce9f009a398b06.com").serverPort(serverPort).qosTestUid("153").serverAddr(serverAddress).timeout("5000000000").build(),
                DnsSettingsResponse.builder().concurrencyGroup("630").record("A").host("siol.net").serverPort(serverPort).qosTestUid("148").serverAddr(serverAddress).timeout("5000000000").build()
        );

        var objectives = Objectives.builder()
                .voip(List.of(voip))
                .jitter(List.of(jitter, jitter, jitter))
                .tcp(tcp)
                .udp(udp)
                .website(website)
                .httpProxy(httpProxy)
                .nonTransparentProxy(nonTransparentProxy)
                .dns(dns)
                .build();

        MeasurementQosParametersResponse measurementQosParametersResponse = MeasurementQosParametersResponse.builder()
                .clientRemoteIp("127.0.0.1")
                .testDuration("5")
                .objectives(objectives)
                .error(Collections.emptyList())
                .build();
        log.debug("MeasurementQosServiceImpl:getQosParameters finished with tenant = {}, response = {}", multiTenantManager.getCurrentTenant(), measurementQosParametersResponse);
        return measurementQosParametersResponse;
    }

    @Override
    public void deleteByOpenUUID(String uuid) {
        log.debug("MeasurementQosServiceImpl:deleteByOpenUUID started with tenant = {}, uuid = {}", multiTenantManager.getCurrentTenant(), uuid);
        measurementQosRepository.deleteByOpenTestUuid(uuid);
        log.debug("MeasurementQosServiceImpl:deleteByOpenUUID finished successfully tenant = {}", multiTenantManager.getCurrentTenant());
    }
}
