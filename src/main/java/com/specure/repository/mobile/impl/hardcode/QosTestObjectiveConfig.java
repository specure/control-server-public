package com.specure.repository.mobile.impl.hardcode;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.specure.common.model.dto.qos.QosParams;
import com.specure.dto.sah.qos.QosTestObjective;
import com.specure.common.enums.TestType;
import com.specure.common.model.jpa.MeasurementServer;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Getter
public class QosTestObjectiveConfig {

    private final Map<Integer, QosTestObjective> qosTestObjectiveMap = new HashMap<>();
    private final MeasurementServer DEFAULT_MEASUREMENT_SERVER = MeasurementServer.builder()
            .name("RTR QoS 10G AT")
            .port(443)
            .webAddress("10g-102v4.netztest.at")
            .secretKey("OGUBzOFFFO2Ok2JFMVLoP09Io9rIOYE99oj1DaFIIWbFqOzmDPXE2wRmeZLKMrLcCrasdAqoXHo3vqrcCkdOMXOOEYUKK2Xmta1L")
            .build();

    public QosTestObjectiveConfig() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        qosTestObjectiveMap.put(76,
                QosTestObjective.builder().
                        id(76)
                        .testType(TestType.VOIP)
                        .concurrencyGroup(10)
                        .testDescription("voip.testinfo")
                        .testSummary("test.desc.voip")
                        .param(objectMapper.readValue("{\"in_port\": \"5061\", \"call_duration\": \"2000000000\", \"timeout\": \"10000000000\", \"out_port\": \"5060\"}", QosParams.class))
                        .results("[\n" +
                                "  {\n" +
                                "    \"evaluate\": \"%EVAL if (nn.coalesce(voip_result_out_mean_jitter, 50000000) < 50000000) result=true; else result=false;%\",\n" +
                                "    \"on_failure\": \"voip.jitter.outgoing.failure\",\n" +
                                "    \"on_success\": \"voip.jitter.outgoing.success\"\n" +
                                "  },\n" +
                                "  {\n" +
                                "    \"evaluate\": \"%EVAL if (nn.coalesce(voip_result_in_mean_jitter, 50000000) < 50000000) result=true; else result=false;%\",\n" +
                                "    \"on_failure\": \"voip.jitter.incoming.failure\",\n" +
                                "    \"on_success\": \"voip.jitter.incoming.success\"\n" +
                                "  },\n" +
                                "  {\n" +
                                "  \"evaluate\": \"%EVAL if (nn.coalesce(voip_result_out_num_packets, 0) > 0) result=true; else result=false;%\",\n" +
                                "  \"on_failure\": \"voip.outgoing.packet.failure\",\n" +
                                "    \"on_success\": \"voip.outgoing.packet.success\"\n" +
                                "  },\n" +
                                "  {\n" +
                                "    \"evaluate\": \"%EVAL if (nn.coalesce(voip_result_in_num_packets, 0) > 0) result=true; else result=false;%\",\n" +
                                "    \"on_failure\": \"voip.incoming.packet.failure\",\n" +
                                "    \"on_success\": \"voip.incoming.packet.success\"\n" +
                                "  },\n" +
                                "  {\n" +
                                "    \"evaluate\": \"%EVAL var _sent= parseInt(voip_objective_call_duration/voip_objective_delay); var _plr=parseInt(100 * ((_sent - voip_result_in_num_packets) / _sent)); if (_plr > 5) result=false; else result=true;%\",\n" +
                                "    \"on_failure\": \"voip.incoming.packet_loss.failure\"\n" +
                                "  },\n" +
                                "  {\n" +
                                "    \"evaluate\": \"%EVAL var _sent= parseInt(voip_objective_call_duration/voip_objective_delay); var _plr=parseInt(100 * ((_sent - voip_result_out_num_packets) / _sent)); if (_plr > 5) result=false; else result=true;%\",\n" +
                                "    \"on_failure\": \"voip.outgoing.packet_loss.failure\"\n" +
                                "  }, \n" +
                                "  {\n" +
                                "    \"evaluate\": \"%EVAL if(voip_result_status=='TIMEOUT') result={type: 'failure', key: 'voip.timeout'}%\"\n" +
                                "  }\n" +
                                "]\n")
                        .build()
        );
        qosTestObjectiveMap.put(169,
                QosTestObjective.builder().
                        id(169)
                        .testType(TestType.JITTER)
                        .concurrencyGroup(10)
                        .testDescription("jitter.testinfo")
                        .testSummary("test.desc.jitter")
                        .param(objectMapper.readValue("{\"in_port\": \"7061\", \"call_duration\": \"2000000000\", \"timeout\": \"10000000000\", \"out_port\": \"7060\"}", QosParams.class))
                        .results("[\n" +
                                "  {\n" +
                                "    \"evaluate\": \"%EVAL if (nn.coalesce(voip_result_out_mean_jitter, 50000000) < 50000000) result=true; else result=false;%\",\n" +
                                "    \"on_failure\": \"voip.jitter.outgoing.failure\",\n" +
                                "    \"on_success\": \"voip.jitter.outgoing.success\"\n" +
                                "  },\n" +
                                "  {\n" +
                                "    \"evaluate\": \"%EVAL if (nn.coalesce(voip_result_in_mean_jitter, 50000000) < 50000000) result=true; else result=false;%\",\n" +
                                "    \"on_failure\": \"voip.jitter.incoming.failure\",\n" +
                                "    \"on_success\": \"voip.jitter.incoming.success\"\n" +
                                "  },\n" +
                                "  {\n" +
                                "\t\"evaluate\": \"%EVAL if (nn.coalesce(voip_result_out_num_packets, 0) > 0) result=true; else result=false;%\",\n" +
                                "\t\"on_failure\": \"voip.outgoing.packet.failure\",\n" +
                                "    \"on_success\": \"voip.outgoing.packet.success\"\n" +
                                "  },\n" +
                                "  {\n" +
                                "    \"evaluate\": \"%EVAL if (nn.coalesce(voip_result_in_num_packets, 0) > 0) result=true; else result=false;%\",\n" +
                                "    \"on_failure\": \"voip.incoming.packet.failure\",\n" +
                                "    \"on_success\": \"voip.incoming.packet.success\"\n" +
                                "  }\n" +
                                "]")
                        .build()
        );
        qosTestObjectiveMap.put(9,
                QosTestObjective.builder().
                        id(9)
                        .testType(TestType.TCP)
                        .concurrencyGroup(200)
                        .testDescription("tcp.out.testinfo")
                        .testSummary("test.desc.tcp.out.80")
                        .param(objectMapper.readValue("{\"timeout\": \"5000000000\", \"out_port\": \"80\"}", QosParams.class))
                        .results("[{\"operator\": \"eq\", \"on_failure\": \"tcp.failure\", \"on_success\": \"tcp.success\", \"tcp_result_out\": \"OK\"}]")
                        .build()
        );
        qosTestObjectiveMap.put(36,
                QosTestObjective.builder().
                        id(36)
                        .testType(TestType.TCP)
                        .concurrencyGroup(200)
                        .testDescription("tcp.out.testinfo")
                        .testSummary("test.desc.tcp.out.21")
                        .param(objectMapper.readValue("{\"timeout\": \"5000000000\", \"out_port\": \"21\"}", QosParams.class))
                        .results("[{\"operator\": \"eq\", \"on_failure\": \"tcp.failure\", \"on_success\": \"tcp.success\", \"tcp_result_out\": \"OK\"}]")
                        .build()
        );
        qosTestObjectiveMap.put(37,
                QosTestObjective.builder().
                        id(37)
                        .testType(TestType.TCP)
                        .concurrencyGroup(37)
                        .testDescription("tcp.out.testinfo")
                        .testSummary("test.desc.tcp.out.22")
                        .param(objectMapper.readValue("{\"timeout\": \"5000000000\", \"out_port\": \"22\"}", QosParams.class))
                        .results("[{\"operator\": \"eq\", \"on_failure\": \"tcp.failure\", \"on_success\": \"tcp.success\", \"tcp_result_out\": \"OK\"}]")
                        .build()
        );
        qosTestObjectiveMap.put(38,
                QosTestObjective.builder().
                        id(38)
                        .testType(TestType.TCP)
                        .concurrencyGroup(200)
                        .testDescription("tcp.out.testinfo")
                        .testSummary("test.desc.tcp.out.25")
                        .param(objectMapper.readValue("{\"timeout\": \"5000000000\", \"out_port\": \"25\"}", QosParams.class))
                        .results("[{\"operator\": \"eq\", \"on_failure\": \"tcp.failure\", \"on_success\": \"tcp.success\", \"tcp_result_out\": \"OK\"}]")
                        .build()
        );
        qosTestObjectiveMap.put(39,
                QosTestObjective.builder().
                        id(39)
                        .testType(TestType.TCP)
                        .concurrencyGroup(200)
                        .testDescription("tcp.out.testinfo")
                        .testSummary("test.desc.tcp.out.53")
                        .param(objectMapper.readValue("{\"timeout\": \"5000000000\", \"out_port\": \"53\"}", QosParams.class))
                        .results("[{\"operator\": \"eq\", \"on_failure\": \"tcp.failure\", \"on_success\": \"tcp.success\", \"tcp_result_out\": \"OK\"}]")
                        .build()
        );
        qosTestObjectiveMap.put(215,
                QosTestObjective.builder().
                        id(215)
                        .testType(TestType.TCP)
                        .concurrencyGroup(200)
                        .testDescription("tcp.out.testinfo")
                        .testSummary("test.desc.tcp.out.110")
                        .param(objectMapper.readValue("{\"timeout\": \"5000000000\", \"out_port\": \"110\"}", QosParams.class))
                        .results("[{\"operator\": \"eq\", \"on_failure\": \"tcp.failure\", \"on_success\": \"tcp.success\", \"tcp_result_out\": \"OK\"}]")
                        .build()
        );
        qosTestObjectiveMap.put(116,
                QosTestObjective.builder().
                        id(116)
                        .testType(TestType.TCP)
                        .concurrencyGroup(200)
                        .testDescription("tcp.out.testinfo")
                        .testSummary("test.desc.tcp.out.143")
                        .param(objectMapper.readValue("{\"timeout\": \"5000000000\", \"out_port\": \"143\"}", QosParams.class))
                        .results("[{\"operator\": \"eq\", \"on_failure\": \"tcp.failure\", \"on_success\": \"tcp.success\", \"tcp_result_out\": \"OK\"}]")
                        .build()
        );
        qosTestObjectiveMap.put(117,
                QosTestObjective.builder().
                        id(117)
                        .testType(TestType.TCP)
                        .concurrencyGroup(200)
                        .testDescription("tcp.out.testinfo")
                        .testSummary("test.desc.tcp.out.465")
                        .param(objectMapper.readValue("{\"timeout\": \"5000000000\", \"out_port\": \"465\"}", QosParams.class))
                        .results("[{\"operator\": \"eq\", \"on_failure\": \"tcp.failure\", \"on_success\": \"tcp.success\", \"tcp_result_out\": \"OK\"}]")
                        .build()
        );
        qosTestObjectiveMap.put(118,
                QosTestObjective.builder().
                        id(118)
                        .testType(TestType.TCP)
                        .concurrencyGroup(200)
                        .testDescription("tcp.out.testinfo")
                        .testSummary("test.desc.tcp.out.585")
                        .param(objectMapper.readValue("{\"timeout\": \"5000000000\", \"out_port\": \"585\"}", QosParams.class))
                        .results("[{\"operator\": \"eq\", \"on_failure\": \"tcp.failure\", \"on_success\": \"tcp.success\", \"tcp_result_out\": \"OK\"}]")
                        .build()
        );
        qosTestObjectiveMap.put(91,
                QosTestObjective.builder().
                        id(91)
                        .testType(TestType.TCP)
                        .concurrencyGroup(200)
                        .testDescription("tcp.out.testinfo")
                        .testSummary("test.desc.tcp.out.587")
                        .param(objectMapper.readValue("{\"timeout\": \"5000000000\", \"out_port\": \"587\"}", QosParams.class))
                        .results("[{\"operator\": \"eq\", \"on_failure\": \"tcp.failure\", \"on_success\": \"tcp.success\", \"tcp_result_out\": \"OK\"}]")
                        .build()
        );
        qosTestObjectiveMap.put(192,
                QosTestObjective.builder().
                        id(192)
                        .testType(TestType.TCP)
                        .concurrencyGroup(200)
                        .testDescription("tcp.out.testinfo")
                        .testSummary("test.desc.tcp.out.993")
                        .param(objectMapper.readValue("{\"timeout\": \"5000000000\", \"out_port\": \"993\"}", QosParams.class))
                        .results("[{\"operator\": \"eq\", \"on_failure\": \"tcp.failure\", \"on_success\": \"tcp.success\", \"tcp_result_out\": \"OK\"}]")
                        .build()
        );
        qosTestObjectiveMap.put(193,
                QosTestObjective.builder().
                        id(193)
                        .testType(TestType.TCP)
                        .concurrencyGroup(200)
                        .testDescription("tcp.out.testinfo")
                        .testSummary("test.desc.tcp.out.995")
                        .param(objectMapper.readValue("{\"timeout\": \"5000000000\", \"out_port\": \"995\"}", QosParams.class))
                        .results("[{\"operator\": \"eq\", \"on_failure\": \"tcp.failure\", \"on_success\": \"tcp.success\", \"tcp_result_out\": \"OK\"}]")
                        .build()
        );
        qosTestObjectiveMap.put(94,
                QosTestObjective.builder().
                        id(94)
                        .testType(TestType.TCP)
                        .concurrencyGroup(200)
                        .testDescription("tcp.out.testinfo")
                        .testSummary("test.desc.tcp.out.5060")
                        .param(objectMapper.readValue("{\"timeout\": \"5000000000\", \"out_port\": \"5060\"}", QosParams.class))
                        .results("[{\"operator\": \"eq\", \"on_failure\": \"tcp.failure\", \"on_success\": \"tcp.success\", \"tcp_result_out\": \"OK\"}]")
                        .build()
        );
        qosTestObjectiveMap.put(114,
                QosTestObjective.builder().
                        id(114)
                        .testType(TestType.TCP)
                        .concurrencyGroup(200)
                        .testDescription("tcp.out.testinfo")
                        .testSummary("test.desc.tcp.out.6881")
                        .param(objectMapper.readValue("{\"timeout\": \"5000000000\", \"out_port\": \"6881\"}", QosParams.class))
                        .results("[{\"operator\": \"eq\", \"on_failure\": \"tcp.failure\", \"on_success\": \"tcp.success\", \"tcp_result_out\": \"OK\"}]")
                        .build()
        );
        qosTestObjectiveMap.put(95,
                QosTestObjective.builder().
                        id(95)
                        .testType(TestType.TCP)
                        .concurrencyGroup(200)
                        .testDescription("tcp.out.testinfo")
                        .testSummary("test.desc.tcp.out.9001")
                        .param(objectMapper.readValue("{\"timeout\": \"5000000000\", \"out_port\": \"9001\"}", QosParams.class))
                        .results("[{\"operator\": \"eq\", \"on_failure\": \"tcp.failure\", \"on_success\": \"tcp.success\", \"tcp_result_out\": \"OK\"}]")
                        .build()
        );
        qosTestObjectiveMap.put(196,
                QosTestObjective.builder().
                        id(196)
                        .testType(TestType.TCP)
                        .concurrencyGroup(200)
                        .testDescription("tcp.out.testinfo")
                        .testSummary("test.desc.tcp.out.554")
                        .param(objectMapper.readValue("{\"timeout\": \"5000000000\", \"out_port\": \"554\"}", QosParams.class))
                        .results("[{\"operator\": \"eq\", \"on_failure\": \"tcp.failure\", \"on_success\": \"tcp.success\", \"tcp_result_out\": \"OK\"}]")
                        .build()
        );
        qosTestObjectiveMap.put(202,
                QosTestObjective.builder().
                        id(202)
                        .testType(TestType.UDP)
                        .concurrencyGroup(200)
                        .testDescription("udp.out.testinfo")
                        .testSummary("test.desc.udp.out.123")
                        .param(objectMapper.readValue("{\"timeout\": \"5000000000\", \"out_port\": \"123\", \"out_num_packets\": \"1\"}", QosParams.class))
                        .results("[{\"operator\": \"eq\", \"on_failure\": \"udp.failure\", \"on_success\": \"udp.success\", \"udp_result_out_response_num_packets\": \"%PARAM udp_objective_out_num_packets%\"}]")
                        .build()
        );
        qosTestObjectiveMap.put(103,
                QosTestObjective.builder().
                        id(103)
                        .testType(TestType.UDP)
                        .concurrencyGroup(200)
                        .testDescription("udp.out.testinfo")
                        .testSummary("test.desc.udp.out.27015")
                        .param(objectMapper.readValue("{\"timeout\": \"5000000000\", \"out_port\": \"27015\", \"out_num_packets\": \"1\"}", QosParams.class))
                        .results("[{\"operator\": \"eq\", \"on_failure\": \"udp.failure\", \"on_success\": \"udp.success\", \"udp_result_out_response_num_packets\": \"%PARAM udp_objective_out_num_packets%\"}]")
                        .build()
        );
        qosTestObjectiveMap.put(119,
                QosTestObjective.builder().
                        id(119)
                        .testType(TestType.UDP)
                        .concurrencyGroup(200)
                        .testDescription("udp.out.testinfo")
                        .testSummary("test.desc.udp.out.500")
                        .param(objectMapper.readValue("{\"timeout\": \"5000000000\", \"out_port\": \"500\", \"out_num_packets\": \"1\"}", QosParams.class))
                        .results("[{\"operator\": \"eq\", \"on_failure\": \"udp.failure\", \"on_success\": \"udp.success\", \"udp_result_out_response_num_packets\": \"%PARAM udp_objective_out_num_packets%\"}]")
                        .build()
        );
        qosTestObjectiveMap.put(120,
                QosTestObjective.builder().
                        id(120)
                        .testType(TestType.UDP)
                        .concurrencyGroup(200)
                        .testDescription("udp.out.testinfo")
                        .testSummary("test.desc.udp.out.5060")
                        .param(objectMapper.readValue("{\"timeout\": \"5000000000\", \"out_port\": \"5060\", \"out_num_packets\": \"1\"}", QosParams.class))
                        .results("[{\"operator\": \"eq\", \"on_failure\": \"udp.failure\", \"on_success\": \"udp.success\", \"udp_result_out_response_num_packets\": \"%PARAM udp_objective_out_num_packets%\"}]")
                        .build()
        );
        qosTestObjectiveMap.put(222,
                QosTestObjective.builder().
                        id(222)
                        .testType(TestType.UDP)
                        .concurrencyGroup(200)
                        .testDescription("udp.out.testinfo")
                        .testSummary("test.desc.udp.out.5005")
                        .param(objectMapper.readValue("{\"timeout\": \"5000000000\", \"out_port\": \"5005\", \"out_num_packets\": \"1\"}", QosParams.class))
                        .results("[{\"operator\": \"eq\", \"on_failure\": \"udp.failure\", \"on_success\": \"udp.success\", \"udp_result_out_response_num_packets\": \"%PARAM udp_objective_out_num_packets%\"}]")
                        .build()
        );

        qosTestObjectiveMap.put(124,
                QosTestObjective.builder().
                        id(124)
                        .testType(TestType.UDP)
                        .concurrencyGroup(200)
                        .testDescription("udp.out.testinfo")
                        .testSummary("test.desc.udp.out.7078")
                        .param(objectMapper.readValue("{\"timeout\": \"5000000000\", \"out_port\": \"7078\", \"out_num_packets\": \"1\"}", QosParams.class))
                        .results("[{\"operator\": \"eq\", \"on_failure\": \"udp.failure\", \"on_success\": \"udp.success\", \"udp_result_out_response_num_packets\": \"%PARAM udp_objective_out_num_packets%\"}]")
                        .build()
        );
        qosTestObjectiveMap.put(157,
                QosTestObjective.builder().
                        id(157)
                        .testType(TestType.UDP)
                        .concurrencyGroup(200)
                        .testDescription("udp.out.testinfo")
                        .testSummary("test.desc.udp.out.7082")
                        .param(objectMapper.readValue("{\"timeout\": \"5000000000\", \"out_port\": \"7082\", \"out_num_packets\": \"1\"}", QosParams.class))
                        .results("[{\"operator\": \"eq\", \"on_failure\": \"udp.failure\", \"on_success\": \"udp.success\", \"udp_result_out_response_num_packets\": \"%PARAM udp_objective_out_num_packets%\"}]")
                        .build()
        );
        qosTestObjectiveMap.put(160,
                QosTestObjective.builder().
                        id(160)
                        .testType(TestType.HTTP_PROXY)
                        .concurrencyGroup(400)
                        .testDescription("http.testinfo")
                        .testSummary("test.desc.http")
                        .param(objectMapper.readValue("{\"url\": \"http://nettest.org/qostest/reference05.jpg\", \"conn_timeout\": \"5000000000\", \"download_timeout\": \"10000000000\"}", QosParams.class))
                        .results("[{\"operator\": \"eq\", \"on_failure\": \"http.failure\", \"on_success\": \"http.success\", \"http_result_hash\": \"ae9592475c364fa01909dab663417ab5\"}]")
                        .build()
        );
        qosTestObjectiveMap.put(163,
                QosTestObjective.builder().
                        id(163)
                        .testType(TestType.HTTP_PROXY)
                        .concurrencyGroup(400)
                        .testDescription("http.testinfo")
                        .testSummary("test.desc.http")
                        .param(objectMapper.readValue("{\"url\": \"http://nettest.org/qostest/reference01.jpg\", \"conn_timeout\": \"5000000000\", \"download_timeout\": \"10000000000\"}", QosParams.class))
                        .results("[{\"operator\": \"eq\", \"on_failure\": \"http.failure\", \"on_success\": \"http.success\", \"http_result_hash\": \"fc563e1e80b8cb964d712982fa2143c8\"}]")
                        .build()
        );
        qosTestObjectiveMap.put(159,
                QosTestObjective.builder().
                        id(159)
                        .testType(TestType.WEBSITE)
                        .concurrencyGroup(500)
                        .testDescription("website.testinfo")
                        .testSummary("test.desc.website")
                        .param(objectMapper.readValue("{\"url\": \"http://nettest.org/kepler/\", \"timeout\": \"10000000000\"}", QosParams.class))
                        .results("[{\"operator\": \"eq\", \"on_failure\": \"website.error\", \"on_success\": \"website.200\", \"website_result_status\": \"200\"}]")
                        .build()
        );
        qosTestObjectiveMap.put(85,
                QosTestObjective.builder().
                        id(85)
                        .testType(TestType.NON_TRANSPARENT_PROXY)
                        .concurrencyGroup(300)
                        .testDescription("ntp.testinfo")
                        .testSummary("test.desc.ntp")
                        .param(objectMapper.readValue("{\"port\": \"22222\", \"request\": \"GET / HTTR/7.9\", \"timeout\": \"5000000000\"}", QosParams.class))
                        .results("[{\"operator\": \"eq\", \"on_failure\": \"ntp.failure\", \"on_success\": \"ntp.success\", \"nontransproxy_result_response\": \"%PARAM nontransproxy_objective_request%\"}]")
                        .build()
        );
        qosTestObjectiveMap.put(4,
                QosTestObjective.builder().
                        id(4)
                        .testType(TestType.NON_TRANSPARENT_PROXY)
                        .concurrencyGroup(300)
                        .testDescription("ntp.testinfo")
                        .testSummary("test.desc.ntp")
                        .param(objectMapper.readValue("{\"port\": \"44444\", \"request\": \"GET \", \"timeout\": \"5000000000\"}", QosParams.class))
                        .results("[{\"operator\": \"eq\", \"on_failure\": \"ntp.failure\", \"on_success\": \"ntp.success\", \"nontransproxy_result_response\": \"%PARAM nontransproxy_objective_request%\"}]")
                        .build()
        );
        qosTestObjectiveMap.put(187,
                QosTestObjective.builder().
                        id(187)
                        .testType(TestType.DNS)
                        .concurrencyGroup(600)
                        .testDescription("dns.testinfo")
                        .testSummary("test.desc.dns")
                        .param(objectMapper.readValue("{\"host\": \"www.google.rs\", \"record\": \"A\", \"timeout\": \"5000000000\"}", QosParams.class))
                        .results("[{\"operator\": \"ge\", \"on_failure\": \"dns.failure\", \"on_success\": \"dns.success\", \"dns_result_entries_found\": \"1\"},{\"operator\": \"ne\", \"on_failure\": \"test.timeout.exceeded\", \"dns_result_info\": \"TIMEOUT\"}]")
                        .build()
        );
        qosTestObjectiveMap.put(164,
                QosTestObjective.builder().
                        id(164)
                        .testType(TestType.DNS)
                        .concurrencyGroup(600)
                        .testDescription("dns.testinfo")
                        .testSummary("test.desc.dns")
                        .param(objectMapper.readValue("{\"host\": \"limundo.com\", \"record\": \"A\", \"timeout\": \"5000000000\"}", QosParams.class))
                        .results("[{\"operator\": \"ge\", \"on_failure\": \"dns.failure\", \"on_success\": \"dns.success\", \"dns_result_entries_found\": \"1\"},{\"operator\": \"ne\", \"on_failure\": \"test.timeout.exceeded\", \"dns_result_info\": \"TIMEOUT\"}]")
                        .build()
        );

        qosTestObjectiveMap.put(150,
                QosTestObjective.builder().
                        id(150)
                        .testType(TestType.DNS)
                        .concurrencyGroup(640)
                        .testDescription("dns.testinfo")
                        .testSummary("test.desc.dns")
                        .param(objectMapper.readValue("{\"host\": \"avto.net\", \"record\": \"A\", \"timeout\": \"5000000000\"}", QosParams.class))
                        .results("[{\"operator\": \"ge\", \"on_failure\": \"dns.failure\", \"on_success\": \"dns.success\", \"dns_result_entries_found\": \"1\"},{\"operator\": \"ne\", \"on_failure\": \"test.timeout.exceeded\", \"dns_result_info\": \"TIMEOUT\"}]")
                        .build()
        );
        qosTestObjectiveMap.put(176,
                QosTestObjective.builder().
                        id(176)
                        .testType(TestType.DNS)
                        .concurrencyGroup(600)
                        .testDescription("dns.testinfo")
                        .testSummary("test.desc.dns")
                        .param(objectMapper.readValue("{\"host\": \"24ur.com\", \"record\": \"A\", \"timeout\": \"5000000000\"}", QosParams.class))
                        .results("[{\"operator\": \"ge\", \"on_failure\": \"dns.failure\", \"on_success\": \"dns.success\", \"dns_result_entries_found\": \"1\"},{\"operator\": \"ne\", \"on_failure\": \"test.timeout.exceeded\", \"dns_result_info\": \"TIMEOUT\"}]")
                        .build()
        );
        qosTestObjectiveMap.put(287,
                QosTestObjective.builder().
                        id(287)
                        .testType(TestType.DNS)
                        .concurrencyGroup(610)
                        .testDescription("dns.testinfo")
                        .testSummary("test.desc.dns")
                        .param(objectMapper.readValue("{\"host\": \"touch.darkspace.akostest.net\", \"record\": \"A\", \"timeout\": \"5000000000\"}", QosParams.class))
                        .results("[{\"operator\": \"ge\", \"on_failure\": \"dns.failure\", \"on_success\": \"dns.success\", \"dns_result_entries_found\": \"1\"},{\"operator\": \"ne\", \"on_failure\": \"test.timeout.exceeded\", \"dns_result_info\": \"TIMEOUT\"}]")
                        .build()
        );
        qosTestObjectiveMap.put(79,
                QosTestObjective.builder().
                        id(79)
                        .testType(TestType.DNS)
                        .concurrencyGroup(640)
                        .testDescription("dns.testinfo")
                        .testSummary("test.desc.dns")
                        .param(objectMapper.readValue("{\"host\": \"najdi.si\", \"record\": \"A\", \"timeout\": \"5000000000\"}", QosParams.class))
                        .results("[{\"operator\": \"ge\", \"on_failure\": \"dns.failure\", \"on_success\": \"dns.success\", \"dns_result_entries_found\": \"1\"},{\"operator\": \"ne\", \"on_failure\": \"test.timeout.exceeded\", \"dns_result_info\": \"TIMEOUT\"}]")
                        .build()
        );
        qosTestObjectiveMap.put(80,
                QosTestObjective.builder().
                        id(80)
                        .testType(TestType.DNS)
                        .concurrencyGroup(600)
                        .testDescription("dns.testinfo")
                        .testSummary("test.desc.dns")
                        .param(objectMapper.readValue("{\"host\": \"slovenskenovice.si\", \"record\": \"A\", \"timeout\": \"5000000000\"}", QosParams.class))
                        .results("[{\"operator\": \"ge\", \"on_failure\": \"dns.failure\", \"on_success\": \"dns.success\", \"dns_result_entries_found\": \"1\"},{\"operator\": \"ne\", \"on_failure\": \"test.timeout.exceeded\", \"dns_result_info\": \"TIMEOUT\"}]")
                        .build()
        );
        qosTestObjectiveMap.put(82,
                QosTestObjective.builder().
                        id(82)
                        .testType(TestType.DNS)
                        .concurrencyGroup(600)
                        .testDescription("dns.testinfo")
                        .testSummary("test.desc.dns")
                        .param(objectMapper.readValue("{\"host\": \"apple.com\", \"record\": \"A\", \"timeout\": \"5000000000\"}", QosParams.class))
                        .results("[{\"operator\": \"ge\", \"on_failure\": \"dns.failure\", \"on_success\": \"dns.success\", \"dns_result_entries_found\": \"1\"},{\"operator\": \"ne\", \"on_failure\": \"test.timeout.exceeded\", \"dns_result_info\": \"TIMEOUT\"}]")
                        .build()
        );
        qosTestObjectiveMap.put(86,
                QosTestObjective.builder().
                        id(86)
                        .testType(TestType.DNS)
                        .concurrencyGroup(620)
                        .testDescription("dns.testinfo")
                        .testSummary("test.desc.dns")
                        .param(objectMapper.readValue("{\"host\": \"bolha.com\", \"record\": \"A\", \"resolver\": \"8.8.8.8\", \"timeout\": \"5000000000\"}", QosParams.class))
                        .results("[{\"operator\": \"ge\", \"on_failure\": \"dns.failure\", \"on_success\": \"dns.success\", \"dns_result_entries_found\": \"1\"},{\"operator\": \"ne\", \"on_failure\": \"test.timeout.exceeded\", \"dns_result_info\": \"TIMEOUT\"}]")
                        .build()
        );

        qosTestObjectiveMap.put(87,
                QosTestObjective.builder().
                        id(87)
                        .testType(TestType.DNS)
                        .concurrencyGroup(630)
                        .testDescription("dns.testinfo")
                        .testSummary("test.desc.dns")
                        .param(objectMapper.readValue("{\"host\": \"rtvslo.si\", \"record\": \"A\", \"timeout\": \"5000000000\"}", QosParams.class))
                        .results("[{\"operator\": \"ge\", \"on_failure\": \"dns.failure\", \"on_success\": \"dns.success\", \"dns_result_entries_found\": \"1\"},{\"operator\": \"ne\", \"on_failure\": \"test.timeout.exceeded\", \"dns_result_info\": \"TIMEOUT\"}]")
                        .build()
        );
        qosTestObjectiveMap.put(126,
                QosTestObjective.builder().
                        id(126)
                        .testType(TestType.DNS)
                        .concurrencyGroup(630)
                        .testDescription("dns.testinfo")
                        .testSummary("test.desc.dns")
                        .param(objectMapper.readValue("{\"host\": \"www.e79321c2d3.darknet.akostest.net\", \"record\": \"A\", \"timeout\": \"5000000000\"}", QosParams.class))
                        .results("[{\"operator\": \"ge\", \"on_failure\": \"dns.failure\", \"on_success\": \"dns.success\", \"dns_result_entries_found\": \"1\"},{\"operator\": \"ne\", \"on_failure\": \"test.timeout.exceeded\", \"dns_result_info\": \"TIMEOUT\"}]")
                        .build()
        );
        qosTestObjectiveMap.put(127,
                QosTestObjective.builder().
                        id(127)
                        .testType(TestType.DNS)
                        .concurrencyGroup(620)
                        .testDescription("dns.testinfo")
                        .testSummary("test.desc.dns")
                        .param(objectMapper.readValue("{\"host\": \"ftp.9d88ec3ff2.com\", \"record\": \"A\", \"timeout\": \"5000000000\"}", QosParams.class))
                        .results("[{\"operator\": \"ge\", \"on_failure\": \"dns.failure\", \"on_success\": \"dns.success\", \"dns_result_entries_found\": \"1\"},{\"operator\": \"ne\", \"on_failure\": \"test.timeout.exceeded\", \"dns_result_info\": \"TIMEOUT\"}]")
                        .build()
        );


        qosTestObjectiveMap.put(92,
                QosTestObjective.builder().
                        id(92)
                        .testType(TestType.DNS)
                        .concurrencyGroup(600)
                        .testDescription("dns.testinfo")
                        .testSummary("test.desc.dns")
                        .param(objectMapper.readValue("{\"host\": \"facebook.com\", \"record\": \"A\", \"timeout\": \"5000000000\"}", QosParams.class))
                        .results("[{\"operator\": \"ge\", \"on_failure\": \"dns.failure\", \"on_success\": \"dns.success\", \"dns_result_entries_found\": \"1\"},{\"operator\": \"ne\", \"on_failure\": \"test.timeout.exceeded\", \"dns_result_info\": \"TIMEOUT\"}]")
                        .build()
        );
        qosTestObjectiveMap.put(93,
                QosTestObjective.builder().
                        id(93)
                        .testType(TestType.DNS)
                        .concurrencyGroup(610)
                        .testDescription("dns.testinfo")
                        .testSummary("test.desc.dns")
                        .param(objectMapper.readValue("{\"host\": \"wikipedia.org\", \"record\": \"A\", \"timeout\": \"5000000000\"}", QosParams.class))
                        .results("[{\"operator\": \"ge\", \"on_failure\": \"dns.failure\", \"on_success\": \"dns.success\", \"dns_result_entries_found\": \"1\"},{\"operator\": \"ne\", \"on_failure\": \"test.timeout.exceeded\", \"dns_result_info\": \"TIMEOUT\"}]")
                        .build()
        );
        qosTestObjectiveMap.put(96,
                QosTestObjective.builder().
                        id(96)
                        .testType(TestType.DNS)
                        .concurrencyGroup(610)
                        .testDescription("dns.testinfo")
                        .testSummary("test.desc.dns")
                        .param(objectMapper.readValue("{\"host\": \"yahoo.com\", \"record\": \"A\", \"timeout\": \"5000000000\"}", QosParams.class))
                        .results("[{\"operator\": \"ge\", \"on_failure\": \"dns.failure\", \"on_success\": \"dns.success\", \"dns_result_entries_found\": \"1\"},{\"operator\": \"ne\", \"on_failure\": \"test.timeout.exceeded\", \"dns_result_info\": \"TIMEOUT\"}]")
                        .build()
        );
        qosTestObjectiveMap.put(97,
                QosTestObjective.builder().
                        id(97)
                        .testType(TestType.DNS)
                        .concurrencyGroup(610)
                        .testDescription("dns.testinfo")
                        .testSummary("test.desc.dns")
                        .param(objectMapper.readValue("{\"host\": \"simobil.si\", \"record\": \"A\", \"timeout\": \"5000000000\"}", QosParams.class))
                        .results("[{\"operator\": \"ge\", \"on_failure\": \"dns.failure\", \"on_success\": \"dns.success\", \"dns_result_entries_found\": \"1\"},{\"operator\": \"ne\", \"on_failure\": \"test.timeout.exceeded\", \"dns_result_info\": \"TIMEOUT\"}]")
                        .build()
        );
        qosTestObjectiveMap.put(98,
                QosTestObjective.builder().
                        id(98)
                        .testType(TestType.DNS)
                        .concurrencyGroup(610)
                        .testDescription("dns.testinfo")
                        .testSummary("test.desc.dns")
                        .param(objectMapper.readValue("{\"host\": \"telekom.si\", \"record\": \"A\", \"timeout\": \"5000000000\"}", QosParams.class))
                        .results("[{\"operator\": \"ge\", \"on_failure\": \"dns.failure\", \"on_success\": \"dns.success\", \"dns_result_entries_found\": \"1\"},{\"operator\": \"ne\", \"on_failure\": \"test.timeout.exceeded\", \"dns_result_info\": \"TIMEOUT\"}]")
                        .build()
        );
        qosTestObjectiveMap.put(101,
                QosTestObjective.builder().
                        id(101)
                        .testType(TestType.DNS)
                        .concurrencyGroup(640)
                        .testDescription("dns.testinfo")
                        .testSummary("test.desc.dns")
                        .param(objectMapper.readValue("{\"host\": \"www.google.com\", \"record\": \"A\", \"timeout\": \"5000000000\"}", QosParams.class))
                        .results("[{\"operator\": \"ge\", \"on_failure\": \"dns.failure\", \"on_success\": \"dns.success\", \"dns_result_entries_found\": \"1\"},{\"operator\": \"ne\", \"on_failure\": \"test.timeout.exceeded\", \"dns_result_info\": \"TIMEOUT\"}]")
                        .build()
        );
        qosTestObjectiveMap.put(102,
                QosTestObjective.builder().
                        id(102)
                        .testType(TestType.DNS)
                        .concurrencyGroup(620)
                        .testDescription("dns.testinfo")
                        .testSummary("test.desc.dns")
                        .param(objectMapper.readValue("{\"host\": \"microsoft.com\", \"record\": \"A\", \"timeout\": \"5000000000\"}", QosParams.class))
                        .results("[{\"operator\": \"ge\", \"on_failure\": \"dns.failure\", \"on_success\": \"dns.success\", \"dns_result_entries_found\": \"1\"},{\"operator\": \"ne\", \"on_failure\": \"test.timeout.exceeded\", \"dns_result_info\": \"TIMEOUT\"}]")
                        .build()
        );
        qosTestObjectiveMap.put(104,
                QosTestObjective.builder().
                        id(104)
                        .testType(TestType.DNS)
                        .concurrencyGroup(630)
                        .testDescription("dns.testinfo")
                        .testSummary("test.desc.dns")
                        .param(objectMapper.readValue("{\"host\": \"youtube.com\", \"record\": \"AAAA\", \"timeout\": \"5000000000\"}", QosParams.class))
                        .results("[{\"operator\": \"ge\", \"on_failure\": \"dns.failure\", \"on_success\": \"dns.success\", \"dns_result_entries_found\": \"1\"},{\"operator\": \"ne\", \"on_failure\": \"test.timeout.exceeded\", \"dns_result_info\": \"TIMEOUT\"}]")
                        .build()
        );
        qosTestObjectiveMap.put(105,
                QosTestObjective.builder().
                        id(105)
                        .testType(TestType.DNS)
                        .concurrencyGroup(640)
                        .testDescription("dns.testinfo")
                        .testSummary("test.desc.dns")
                        .param(objectMapper.readValue("{\"host\": \"t-2.net\", \"record\": \"A\", \"timeout\": \"5000000000\", \"resolver\": \"8.8.8.8\"}", QosParams.class))
                        .results("[{\"operator\": \"ge\", \"on_failure\": \"dns.failure\", \"on_success\": \"dns.success\", \"dns_result_entries_found\": \"1\"},{\"operator\": \"ne\", \"on_failure\": \"test.timeout.exceeded\", \"dns_result_info\": \"TIMEOUT\"}]")
                        .build()
        );
        qosTestObjectiveMap.put(108,
                QosTestObjective.builder().
                        id(108)
                        .testType(TestType.DNS)
                        .concurrencyGroup(620)
                        .testDescription("dns.testinfo")
                        .testSummary("test.desc.dns")
                        .param(objectMapper.readValue("{\"host\": \"amis.net\", \"record\": \"A\", \"timeout\": \"5000000000\"}", QosParams.class))
                        .results("[{\"operator\": \"ge\", \"on_failure\": \"dns.failure\", \"on_success\": \"dns.success\", \"dns_result_entries_found\": \"1\"},{\"operator\": \"ne\", \"on_failure\": \"test.timeout.exceeded\", \"dns_result_info\": \"TIMEOUT\"}]")
                        .build()
        );
        qosTestObjectiveMap.put(109,
                QosTestObjective.builder().
                        id(109)
                        .testType(TestType.DNS)
                        .concurrencyGroup(620)
                        .testDescription("dns.testinfo")
                        .testSummary("test.desc.dns")
                        .param(objectMapper.readValue("{\"host\": \"telemach.si\", \"record\": \"A\", \"timeout\": \"5000000000\"}", QosParams.class))
                        .results("[{\"operator\": \"ge\", \"on_failure\": \"dns.failure\", \"on_success\": \"dns.success\", \"dns_result_entries_found\": \"1\"},{\"operator\": \"ne\", \"on_failure\": \"test.timeout.exceeded\", \"dns_result_info\": \"TIMEOUT\"}]")
                        .build()
        );
        qosTestObjectiveMap.put(152,
                QosTestObjective.builder().
                        id(152)
                        .testType(TestType.DNS)
                        .concurrencyGroup(640)
                        .testDescription("dns.testinfo")
                        .testSummary("test.desc.dns")
                        .param(objectMapper.readValue("{\"host\": \"gov.si\", \"record\": \"MX\", \"timeout\": \"5000000000\"}", QosParams.class))
                        .results("[{\"operator\": \"ge\", \"on_failure\": \"dns.failure\", \"on_success\": \"dns.success\", \"dns_result_entries_found\": \"1\"},{\"operator\": \"ne\", \"on_failure\": \"test.timeout.exceeded\", \"dns_result_info\": \"TIMEOUT\"}]")
                        .build()
        );
        qosTestObjectiveMap.put(111,
                QosTestObjective.builder().
                        id(111)
                        .testType(TestType.DNS)
                        .concurrencyGroup(630)
                        .testDescription("dns.testinfo")
                        .testSummary("test.desc.dns")
                        .param(objectMapper.readValue("{\"host\": \"akos-rs.si\", \"record\": \"A\", \"timeout\": \"5000000000\"}", QosParams.class))
                        .results("[{\"operator\": \"ge\", \"on_failure\": \"dns.failure\", \"on_success\": \"dns.success\", \"dns_result_entries_found\": \"1\"},{\"operator\": \"ne\", \"on_failure\": \"test.timeout.exceeded\", \"dns_result_info\": \"TIMEOUT\"}]")
                        .build()
        );
        qosTestObjectiveMap.put(149,
                QosTestObjective.builder().
                        id(149)
                        .testType(TestType.DNS)
                        .concurrencyGroup(620)
                        .testDescription("dns.testinfo")
                        .testSummary("test.desc.dns")
                        .param(objectMapper.readValue("{\"host\": \"finance.si\", \"record\": \"A\", \"timeout\": \"5000000000\"}", QosParams.class))
                        .results("[{\"operator\": \"ge\", \"on_failure\": \"dns.failure\", \"on_success\": \"dns.success\", \"dns_result_entries_found\": \"1\"},{\"operator\": \"ne\", \"on_failure\": \"test.timeout.exceeded\", \"dns_result_info\": \"TIMEOUT\"}]")
                        .build()
        );
        qosTestObjectiveMap.put(115,
                QosTestObjective.builder().
                        id(115)
                        .testType(TestType.DNS)
                        .concurrencyGroup(600)
                        .testDescription("dns.testinfo")
                        .testSummary("test.desc.dns")
                        .param(objectMapper.readValue("{\"host\": \"www.c144f9db70.net\", \"record\": \"A\", \"timeout\": \"5000000000\"}", QosParams.class))
                        .results("[{\"operator\": \"ge\", \"on_failure\": \"dns.failure\", \"on_success\": \"dns.success\", \"dns_result_entries_found\": \"1\"},{\"operator\": \"ne\", \"on_failure\": \"test.timeout.exceeded\", \"dns_result_info\": \"TIMEOUT\"}]")
                        .build()
        );
        qosTestObjectiveMap.put(121,
                QosTestObjective.builder().
                        id(121)
                        .testType(TestType.DNS)
                        .concurrencyGroup(610)
                        .testDescription("dns.testinfo")
                        .testSummary("test.desc.dns")
                        .param(objectMapper.readValue("{\"host\": \"twitter.com\", \"record\": \"A\", \"timeout\": \"5000000000\", \"resolver\": \"8.8.8.8\"}", QosParams.class))
                        .results("[{\"operator\": \"ge\", \"on_failure\": \"dns.failure\", \"on_success\": \"dns.success\", \"dns_result_entries_found\": \"1\"},{\"operator\": \"ne\", \"on_failure\": \"test.timeout.exceeded\", \"dns_result_info\": \"TIMEOUT\"}]")
                        .build()
        );
        qosTestObjectiveMap.put(122,
                QosTestObjective.builder().
                        id(122)
                        .testType(TestType.DNS)
                        .concurrencyGroup(640)
                        .testDescription("dns.testinfo")
                        .testSummary("test.desc.dns")
                        .param(objectMapper.readValue("{\"host\": \"facebook.com\", \"record\": \"AAAA\", \"timeout\": \"5000000000\"}", QosParams.class))
                        .results("[{\"operator\": \"ge\", \"on_failure\": \"dns.failure\", \"on_success\": \"dns.success\", \"dns_result_entries_found\": \"1\"},{\"operator\": \"ne\", \"on_failure\": \"test.timeout.exceeded\", \"dns_result_info\": \"TIMEOUT\"}]")
                        .build()
        );
        qosTestObjectiveMap.put(171,
                QosTestObjective.builder().
                        id(171)
                        .testType(TestType.DNS)
                        .concurrencyGroup(630)
                        .testDescription("dns.testinfo")
                        .testSummary("test.desc.dns")
                        .param(objectMapper.readValue("{\"host\": \"invalidname.3c0b3194ba.com\", \"record\": \"A\", \"timeout\": \"5000000000\"}", QosParams.class))
                        .results("[{\"operator\": \"ge\", \"on_failure\": \"dns.failure\", \"on_success\": \"dns.success\", \"dns_result_entries_found\": \"1\"},{\"operator\": \"ne\", \"on_failure\": \"test.timeout.exceeded\", \"dns_result_info\": \"TIMEOUT\"}]")
                        .build()
        );
        qosTestObjectiveMap.put(172,
                QosTestObjective.builder().
                        id(172)
                        .testType(TestType.DNS)
                        .concurrencyGroup(630)
                        .testDescription("dns.testinfo")
                        .testSummary("test.desc.dns")
                        .param(objectMapper.readValue("{\"host\": \"youtube.com\", \"record\": \"A\", \"timeout\": \"5000000000\", \"resolver\": \"8.8.4.4\"}", QosParams.class))
                        .results("[{\"operator\": \"ge\", \"on_failure\": \"dns.failure\", \"on_success\": \"dns.success\", \"dns_result_entries_found\": \"1\"},{\"operator\": \"ne\", \"on_failure\": \"test.timeout.exceeded\", \"dns_result_info\": \"TIMEOUT\"}]")
                        .build()
        );
        qosTestObjectiveMap.put(173,
                QosTestObjective.builder().
                        id(173)
                        .testType(TestType.DNS)
                        .concurrencyGroup(640)
                        .testDescription("dns.testinfo")
                        .testSummary("test.desc.dns")
                        .param(objectMapper.readValue("{\"host\": \"wikipedia.org\", \"record\": \"A\", \"timeout\": \"5000000000\", \"resolver\": \"81.16.157.19\"}", QosParams.class))
                        .results("[{\"operator\": \"ge\", \"on_failure\": \"dns.failure\", \"on_success\": \"dns.success\", \"dns_result_entries_found\": \"1\"},{\"operator\": \"ne\", \"on_failure\": \"test.timeout.exceeded\", \"dns_result_info\": \"TIMEOUT\"}]")
                        .build()
        );
        qosTestObjectiveMap.put(174,
                QosTestObjective.builder().
                        id(174)
                        .testType(TestType.DNS)
                        .concurrencyGroup(610)
                        .testDescription("dns.testinfo")
                        .testSummary("test.desc.dns")
                        .param(objectMapper.readValue("{\"host\": \"www.61ccffce9f009a398b06.com\", \"record\": \"A\", \"timeout\": \"5000000000\", \"resolver\": \"1.1.1.1\"}", QosParams.class))
                        .results("[{\"operator\": \"ge\", \"on_failure\": \"dns.failure\", \"on_success\": \"dns.success\", \"dns_result_entries_found\": \"1\"},{\"operator\": \"ne\", \"on_failure\": \"test.timeout.exceeded\", \"dns_result_info\": \"TIMEOUT\"}]")
                        .build()
        );
        qosTestObjectiveMap.put(175,
                QosTestObjective.builder().
                        id(175)
                        .testType(TestType.DNS)
                        .concurrencyGroup(630)
                        .testDescription("dns.testinfo")
                        .testSummary("test.desc.dns")
                        .param(objectMapper.readValue("{\"host\": \"siol.net\", \"record\": \"A\", \"timeout\": \"5000000000\", \"resolver\": \"9.9.9.9\"}", QosParams.class))
                        .results("[{\"operator\": \"ge\", \"on_failure\": \"dns.failure\", \"on_success\": \"dns.success\", \"dns_result_entries_found\": \"1\"},{\"operator\": \"ne\", \"on_failure\": \"test.timeout.exceeded\", \"dns_result_info\": \"TIMEOUT\"}]")
                        .build()
        );
    }
}
