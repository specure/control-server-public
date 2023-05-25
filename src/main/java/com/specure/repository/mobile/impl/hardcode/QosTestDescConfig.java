package com.specure.repository.mobile.impl.hardcode;

import com.specure.dto.sah.qos.QosTestDesc;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Getter
public class QosTestDescConfig {

    private final List<QosTestDesc> qosTestDescList = new ArrayList<>();

    public QosTestDescConfig() {
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("ntp.success")
                .value("The request to the test server was not modified.")
                .lang("en")
                .build()
        );
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("http.success")
                .value("The received content is exactly the same as the original one, hence has not been modified.")
                .lang("en")
                .build()
        );
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("http.failure")
                .value("The received content is not the same as the original one, hence looks like been modified.")
                .lang("en")
                .build()
        );
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("ntp.failure")
                .value("The request to the QoS test server has been manipulated.")
                .lang("en")
                .build()
        );
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("dns.success")
                .value("DNS request successful (resolver: %PARAM dns_objective_resolver%)")
                .lang("en")
                .build()
        );
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("dns.failure")
                .value("DNS request failed (resolver: %PARAM dns_objective_resolver%)")
                .lang("en")
                .build()
        );
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("tcp.success")
                .value("The test was successful. A connection could be established.")
                .lang("en")
                .build()
        );
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("website.long")
                .value("Transfer of %PARAM website_objective_url% took more than %PARAM website_objective_timeout 1000000000 0 f% s.\n" +
                        "Duration: %PARAM website_result_duration_ns 1000000000 1 f% s")
                .lang("en")
                .build()
        );
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("website.short")
                .value("Transfer of %PARAM website_objective_url% took less than %PARAM website_objective_timeout 1000000000 0 f% s.\n" +
                        "Duration: %PARAM website_result_duration_ns 1000000000 1 f% s")
                .lang("en")
                .build()
        );
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("tcp.success")
                .value("The test was successful. A connection could be established.")
                .lang("en")
                .build()
        );
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("tcp.failure")
                .value("The test was not successful. A connection could not be established.")
                .lang("en")
                .build()
        );
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("udp.success")
                .value("The UDP test was successful. All packets have been transferred successfully.")
                .lang("en")
                .build()
        );
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("udp.failure")
                .value("The UDP test failed. Some packets have been lost.")
                .lang("en")
                .build()
        );
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("test.http")
                .value("This test downloads a test web ressource (e.g. image) and checks if it was modified during transport.")
                .lang("en")
                .build()
        );
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("test.ntp")
                .value("This test checks if a request is modified by a proxy or other middlebox.")
                .lang("en")
                .build()
        );
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("test.udp")
                .value("UDP is an important connectionless Internet protocol. It is used for real-time communications, e.g. for VoIP and video.")
                .lang("en")
                .build()
        );
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("test.tcp")
                .value("TCP is an important connection oriented Internet protocol. It is used for example for web pages or e-mail.")
                .lang("en")
                .build()
        );
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("test.dns")
                .value("DNS is a fundamental Internet service. It is used to translate domain names to IP addresses. Depending on the test it is checked if the service is available, if the answers are correct and how fast the server responds.")
                .lang("en")
                .build()
        );
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("test.website")
                .value("The website test downloads a reference web page (mobile Kepler page by ETSI). It is verified, if the page can be transferred and how long the download of the page takes.")
                .lang("en")
                .build()
        );
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("http.testinfo")
                .value("Target: '%PARAM http_objective_url%'\n" +
                        "Range: %PARAM http_objective_range%\n" +
                        "Duration: %PARAM duration_ns 1000000000 1 f% s\n" +
                        "Length: %PARAM http_result_length%\n" +
                        "Status code: %PARAM http_result_status%\n" +
                        "Hash: %PARAM http_result_hash%\n" +
                        "Header: \n" +
                        "%PARAM http_result_header%")
                .lang("en")
                .build()
        );
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("dns.unknowndomain.info")
                .value("A DNS request for a non-existent domain (%PARAM dns_objective_host%) has been run to check the response for the request of the domain's DNS %PARAM dns_objective_dns_record% record.\n" +
                        "The correct answer would be 'NXDOMAIN' (non-existend domain).\n" +
                        "DNS status: '%PARAM dns_result_status%';\n" +
                        "Duration:%PARAM duration_ns 1000000 0 f% ms\n")
                .lang("en")
                .build()
        );
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("dns.unknowndomain.failure")
                .value("A DNS request for a not existing domain has returned an invalid result: %PARAM dns_result_entries%")
                .lang("en")
                .build()
        );
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("dns.unknowndomain.success")
                .value("A DNS request for a not existing domain: succeeded, no entries have been returned.")
                .lang("en")
                .build()
        );
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("udp.in.testinfo")
                .value("UDP Incoming:\n" +
                        "It has been attempted to receive packets from the QoS test server on port: %PARAM udp_objective_in_port% and send them back.\n" +
                        "Number of packets requested: %PARAM udp_objective_in_num_packets%, received by the client: %PARAM udp_result_in_num_packets%, came back to the server: %PARAM udp_result_in_response_num_packets%.\n" +
                        "Packet loss rate: %PARAM udp_result_in_packet_loss_rate%\\%")
                .lang("en")
                .build()
        );
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("udp.out.testinfo")
                .value("UDP Outgoing:\n" +
                        "It has been attempted to send packets to the QoS test server on port: %PARAM udp_objective_out_port% and receive them back.\n" +
                        "Number of sent packets: %PARAM udp_objective_out_num_packets%, received by the server: %PARAM udp_result_out_num_packets%, came back to the client: %PARAM udp_result_out_response_num_packets%.\n" +
                        "Packet loss rate: %PARAM udp_result_out_packet_loss_rate%\\%")
                .lang("en")
                .build()
        );
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("ntp.testinfo")
                .value("A request with the content: '%PARAM nontransproxy_objective_request%' has been sent to the test server.\n" +
                        "The answer was: '%PARAM nontransproxy_result_response%'")
                .lang("en")
                .build()
        );
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("tcp.in.testinfo")
                .value("TCP Incoming:\n" +
                        "It has been attempted to establish an incoming connection on port: %PARAM tcp_objective_in_port%.")
                .lang("en")
                .build()
        );
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("tcp.out.testinfo")
                .value("TCP outgoing:\n" +
                        "It has been attempted to establish an outgoing connection to the QoS test server on port: %PARAM tcp_objective_out_port%")
                .lang("en")
                .build()
        );
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("dns.testinfo")
                .value("DNS request for the domain: %PARAM dns_objective_host%\n" +
                        "Requested record: %PARAM dns_objective_dns_record%\n" +
                        "\n" +
                        "Test result:\n" +
                        "DNS status: %PARAM dns_result_status%\n" +
                        "DNS entries: %PARAM dns_result_entries%\n" +
                        "Test duration: %PARAM duration_ns 1000000 0 f% ms")
                .lang("en")
                .build()
        );
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("website.testinfo")
                .value("The transfer of %PARAM website_objective_url% took %PARAM duration_ns 1000000000 1 f% s.\n" +
                        "\n" +
                        "Transferred data downlink: %PARAM website_result_rx_bytes 1000 1 f% kB\n" +
                        "Transferred data uplink: %PARAM website_result_tx_bytes 1000 1 f% kB\n" +
                        "HTTP status code: %PARAM website_result_status%")
                .lang("en")
                .build()
        );
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("timeout")
                .value("Test could not be completed. Timeout exceeded!")
                .lang("en")
                .build()
        );
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("website.not_found")
                .value("The test web site could not be reached.")
                .lang("en")
                .build()
        );
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("test.description")
                .value("n/a")
                .lang("en")
                .build()
        );
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("test.desc.http")
                .value("Target: %PARAM http_objective_url%")
                .lang("en")
                .build()
        );
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("test.desc.dns")
                .value("Target: %PARAM dns_objective_host% \n" +
                        "Entry: %PARAM dns_objective_dns_record%\n" +
                        "Resolver: %PARAM dns_objective_resolver%")
                .lang("en")
                .build()
        );
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("test.desc.tcp.in")
                .value("TCP incoming, port: %PARAM tcp_objective_in_port%")
                .lang("en")
                .build()
        );
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("test.desc.tcp.out")
                .value("TCP outgoing, port: %PARAM tcp_objective_out_port%")
                .lang("en")
                .build()
        );
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("test.desc.udp.in")
                .value("UDP incoming, port: %PARAM udp_objective_in_port%, number of packets: %PARAM udp_objective_in_num_packets%")
                .lang("en")
                .build()
        );
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("test.desc.udp.out")
                .value("UDP outgoing, port: %PARAM udp_objective_out_port%, number of packets: %PARAM udp_objective_out_num_packets%")
                .lang("en")
                .build()
        );
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("test.desc.ntp")
                .value("Port: %PARAM nontransproxy_objective_port%\n" +
                        "Request: %PARAM nontransproxy_objective_request%")
                .lang("en")
                .build()
        );
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("test.desc.website")
                .value("Target: %PARAM website_objective_url%")
                .lang("en")
                .build()
        );
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("website.200")
                .value("The web page has been transferred successfully.")
                .lang("en")
                .build()
        );
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("website.error")
                .value("There has been an error during the test.")
                .lang("en")
                .build()
        );
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("name.website")
                .value("Web page")
                .lang("en")
                .build()
        );
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("name.http_proxy")
                .value("Unmodified content")
                .lang("en")
                .build()
        );
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("name.non_transparent_proxy")
                .value("Transparent connection")
                .lang("en")
                .build()
        );
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("name.dns")
                .value("DNS")
                .lang("en")
                .build()
        );
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("name.tcp")
                .value("TCP ports")
                .lang("en")
                .build()
        );
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("name.udp")
                .value("UDP ports")
                .lang("en")
                .build()
        );
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("test.timeout.exceeded")
                .value("Test timeout exceeded. The test could not be completed successfully.")
                .lang("en")
                .build()
        );
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("test.desc.tcp.out.21")
                .value("File transfer protocol (FTP, TCP port 21 outgoing)")
                .lang("en")
                .build()
        );
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("test.desc.tcp.out.22")
                .value("Secure logins and file transfers (SSH, TCP port 22 outgoing)")
                .lang("en")
                .build()
        );
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("test.desc.tcp.out.25")
                .value("E-mail transmission (SMTP, TCP port 25 outgoing)")
                .lang("en")
                .build()
        );
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("test.desc.tcp.out.53")
                .value("Name resolving for computers and services (DNS, TCP port 53 outgoing)")
                .lang("en")
                .build()
        );
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("test.desc.tcp.out.80")
                .value("Web site protocol (HTTP, TCP port 80 outgoing)")
                .lang("en")
                .build()
        );
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("test.desc.tcp.out.110")
                .value("E-mail retreival (POP3, TCP port 110 outgoing)")
                .lang("en")
                .build()
        );
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("test.desc.tcp.out.143")
                .value("E-mail retrieval and storage (IMAP, TCP port 143 outgoing)")
                .lang("en")
                .build()
        );
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("test.desc.tcp.out.465")
                .value("Secure e-mail transmission (SMTPS, TCP-Port 465 ausgehend)")
                .lang("en")
                .build()
        );
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("test.desc.tcp.out.554")
                .value("Control of streaming of audio and visual media (RTSP, TCP port 554 outgoing)")
                .lang("en")
                .build()
        );
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("test.desc.tcp.out.585")
                .value("Secure e-mail retrieval and storage (IMAPS, TCP port 585 outgoing)")
                .lang("en")
                .build()
        );
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("test.desc.tcp.out.587")
                .value("E-mail transmission (SMTP, TCP port 587 outgoing)")
                .lang("en")
                .build()
        );
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("test.desc.tcp.out.993")
                .value("Secure e-mail retrieval and storage (IMAPS, TCP port 993 outgoing)")
                .lang("en")
                .build()
        );
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("test.desc.tcp.out.995")
                .value("Secure e-mail retreival (POP3S, TCP port 995 outgoing)")
                .lang("en")
                .build()
        );
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("test.desc.tcp.out.5060")
                .value("Control of communication sessions (SIP, TCP port 5060 outgoing)")
                .lang("en")
                .build()
        );
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("test.desc.tcp.out.6881")
                .value("Peer to peer file sharing (BitTorrent, TCP port 6881 outgoing)")
                .lang("en")
                .build()
        );
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("test.desc.tcp.out.9001")
                .value("Online anonymity (TOR, TCP port 9001 outgoing)")
                .lang("en")
                .build()
        );
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("test.desc.udp.out.53")
                .value("Name resolving for computers and services (DNS, UDP port 53 outgoing)")
                .lang("en")
                .build()
        );
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("test.desc.udp.out.123")
                .value("Time synchronisation (NTP, UDP port 123 outgoing)")
                .lang("en")
                .build()
        );
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("test.desc.udp.out.500")
                .value("Establishment and usage of secure services (ISAKMP, UDP port 500 outgoing)")
                .lang("en")
                .build()
        );
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("test.desc.udp.out.554")
                .value("Control of streaming of audio and visual media (RTSP, UDP port 554 outgoing)")
                .lang("en")
                .build()
        );
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("test.desc.udp.out.5004")
                .value("Streaming of audio and visual media (RTP, UDP port 5004 outgoing)")
                .lang("en")
                .build()
        );
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("test.desc.udp.out.5005")
                .value("Quality of service for streaming of audio and visual media (RTCP, UDP port 5005 outgoing)")
                .lang("en")
                .build()
        );
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("test.desc.udp.out.5060")
                .value("Control of communication sessions (SIP, UDP port 5060 outgoing)")
                .lang("en")
                .build()
        );
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("test.desc.udp.out.7078")
                .value("Voice over Internet (VoIP, UDP port 7078 outgoing)")
                .lang("en")
                .build()
        );
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("test.desc.udp.out.7082")
                .value("Voice over Internet (VoIP, UDP port 7082 outgoing)")
                .lang("en")
                .build()
        );
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("test.desc.udp.out.27005")
                .value("Online gaming (Steam gaming, UDP port 27005 outgoing)")
                .lang("en")
                .build()
        );
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("test.desc.udp.out.27015")
                .value("Online gaming (Steam gaming, UDP port 27015 outgoing)")
                .lang("en")
                .build()
        );
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("traceroute.failure")
                .value("There has been an error during the traceroute test.")
                .lang("en")
                .build()
        );
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("traceroute.success")
                .value("There has been no error during the traceroute test.")
                .lang("en")
                .build()
        );

        qosTestDescList.add(QosTestDesc.builder()
                .descKey("trace.testinfo")
                .value("Traceroute test parameters:\n" +
                        "Host: %PARAM traceroute_objective_host%\n" +
                        "Max hops: %PARAM traceroute_objective_max_hops%\n" +
                        "\n" +
                        "Traceroute test results:\n" +
                        "Hops needed: %PARAM traceroute_result_hops%\n" +
                        "Traceroute result: %PARAM traceroute_result_status%\n" +
                        "\n" +
                        "Full route:\n" +
                        "%EVAL result=String(nn.parseTraceroute(traceroute_result_details))%")
                .lang("en")
                .build()
        );
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("test.desc.trace")
                .value("Traceroute target: %PARAM traceroute_objective_host%")
                .lang("en")
                .build()
        );
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("test.trace")
                .value("Traceroute is a tool for displaying the route across IP based networks.")
                .lang("en")
                .build()
        );
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("name.trace")
                .value("Traceroute")
                .lang("en")
                .build()
        );
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("name.voip")
                .value("Voice over IP")
                .lang("en")
                .build()
        );
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("test.voip")
                .value("VoIP (Voice over IP) is a technology for the delivery of voice across IP based networks.")
                .lang("en")
                .build()
        );

        qosTestDescList.add(QosTestDesc.builder()
                .descKey("voip.incoming.packet.failure")
                .value("Incoming voice is missing. \n" +
                        "All incoming voice packets have not arrived at the target location.")
                .lang("en")
                .build()
        );
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("voip.incoming.packet.success")
                .value("It is possible to receive voice packets.")
                .lang("en")
                .build()
        );
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("voip.outgoing.packet.failure")
                .value("Incoming voice is missing. \n" +
                        "All outgoing voice packets have not arrived at the target location.")
                .lang("en")
                .build()
        );
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("voip.outgoing.packet.success")
                .value("It is possible to send voice packets to port %PARAM voip_objective_out_port%.")
                .lang("en")
                .build()
        );
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("voip.jitter.incoming.failure")
                .value("The incoming mean jitter is too high or empty because of missing outgoing voice packets.")
                .lang("en")
                .build()
        );
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("voip.jitter.incoming.success")
                .value("The incoming mean jitter is acceptable for a VoIP connection.")
                .lang("en")
                .build()
        );
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("voip.jitter.outgoing.failure")
                .value("The outgoing mean jitter is too high or empty because of missing outgoing voice packets.")
                .lang("en")
                .build()
        );
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("voip.jitter.outgoing.success")
                .value("The outgoing mean jitter is acceptable for a VoIP connection.")
                .lang("en")
                .build()
        );
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("voip.timeout")
                .value("The test took too much time and ran into a timeout.")
                .lang("en")
                .build()
        );
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("voip.testinfo")
                .value("%$IF voip_result_status!='OK'%\n" +
                        "There has been an error during the VoIP test. No results available.\n" +
                        "%$ENDIF voip_result_status!='OK'%\n" +
                        "%$IF voip_result_status=='OK'%\n" +
                        "TEST PARAMETERS\n" +
                        "Sample rate: %PARAM voip_objective_sample_rate%, bits per sample: %PARAM voip_objective_bits_per_sample%\n" +
                        "Call duration: %PARAM voip_objective_call_duration 1000000 1 f% ms\n" +
                        "Packet interval: %PARAM voip_objective_delay 1000000 1 f% ms\n" +
                        "Payload type: %EVAL result=String(nn.getPayloadType(voip_objective_payload))%\n" +
                        "Target port: %PARAM voip_objective_out_port%\n" +
                        "\n" +
                        "TEST RESULTS\n" +
                        "\n" +
                        "Incoming voice stream:\n" +
                        "max. jitter: %PARAM voip_result_in_max_jitter 1000000 2 f% ms\n" +
                        "mean jitter: %PARAM voip_result_in_mean_jitter 1000000 2 f% ms\n" +
                        "max. delta: %PARAM voip_result_in_max_delta 1000000 2 f% ms\n" +
                        "packets sent: %EVAL result=String(parseInt(voip_objective_call_duration/voip_objective_delay));%\n" +
                        "packets received: %PARAM voip_result_in_num_packets%\n" +
                        "packet lost percentage: %EVAL var _sent= parseInt(voip_objective_call_duration/voip_objective_delay); result=(100 * ((_sent - voip_result_in_num_packets) / _sent)); %\\%\n" +
                        "sequence errors: %PARAM voip_result_in_sequence_error%\n" +
                        "shortest / longest sequence: %PARAM voip_result_in_short_seq% / %PARAM voip_result_in_long_seq%\n" +
                        "\n" +
                        "Outgoing voice stream:\n" +
                        "max. jitter: %PARAM voip_result_out_max_jitter 1000000 2 f% ms\n" +
                        "mean jitter: %PARAM voip_result_out_mean_jitter 1000000 2 f% ms\n" +
                        "max. delta: %PARAM voip_result_out_max_delta 1000000 2 f% ms\n" +
                        "packets sent: %EVAL result=String(parseInt(voip_objective_call_duration/voip_objective_delay));%\n" +
                        "packets received: %PARAM voip_result_out_num_packets%\n" +
                        "packet lost percentage: %EVAL var _sent= parseInt(voip_objective_call_duration/voip_objective_delay); result=(100 * ((_sent - voip_result_out_num_packets) / _sent)); %\\%\n" +
                        "sequence errors: %PARAM voip_result_out_sequence_error%\n" +
                        "shortest / longest sequence: %PARAM voip_result_out_short_seq% / %PARAM voip_result_out_long_seq%\n" +
                        "%$ENDIF voip_result_status=='OK'%")
                .lang("en")
                .build()
        );
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("jitter.testinfo")
                .value("%$IF voip_result_status!='OK'%\n" +
                        "There has been an error during the VoIP test. No results available.\n" +
                        "%$ENDIF voip_result_status!='OK'%\n" +
                        "%$IF voip_result_status=='OK'%\n" +
                        "TEST PARAMETERS\n" +
                        "Sample rate: %PARAM voip_objective_sample_rate%, bits per sample: %PARAM voip_objective_bits_per_sample%\n" +
                        "Call duration: %PARAM voip_objective_call_duration 1000000 1 f% ms\n" +
                        "Packet interval: %PARAM voip_objective_delay 1000000 1 f% ms\n" +
                        "Payload type: %EVAL result=String(nn.getPayloadType(voip_objective_payload))%\n" +
                        "Target port: %PARAM voip_objective_out_port%\n" +
                        "\n" +
                        "TEST RESULTS\n" +
                        "\n" +
                        "Incoming voice stream:\n" +
                        "max. jitter: %PARAM voip_result_in_max_jitter 1000000 2 f% ms\n" +
                        "mean jitter: %PARAM voip_result_in_mean_jitter 1000000 2 f% ms\n" +
                        "max. delta: %PARAM voip_result_in_max_delta 1000000 2 f% ms\n" +
                        "packets sent: %EVAL result=String(parseInt(voip_objective_call_duration/voip_objective_delay));%\n" +
                        "packets received: %PARAM voip_result_in_num_packets%\n" +
                        "packet lost percentage: %EVAL var _sent= parseInt(voip_objective_call_duration/voip_objective_delay); result=(100 * ((_sent - voip_result_in_num_packets) / _sent)); %\\%\n" +
                        "sequence errors: %PARAM voip_result_in_sequence_error%\n" +
                        "shortest / longest sequence: %PARAM voip_result_in_short_seq% / %PARAM voip_result_in_long_seq%\n" +
                        "\n" +
                        "Outgoing voice stream:\n" +
                        "max. jitter: %PARAM voip_result_out_max_jitter 1000000 2 f% ms\n" +
                        "mean jitter: %PARAM voip_result_out_mean_jitter 1000000 2 f% ms\n" +
                        "max. delta: %PARAM voip_result_out_max_delta 1000000 2 f% ms\n" +
                        "packets sent: %EVAL result=String(parseInt(voip_objective_call_duration/voip_objective_delay));%\n" +
                        "packets received: %PARAM voip_result_out_num_packets%\n" +
                        "packet lost percentage: %EVAL var _sent= parseInt(voip_objective_call_duration/voip_objective_delay); result=(100 * ((_sent - voip_result_out_num_packets) / _sent)); %\\%\n" +
                        "sequence errors: %PARAM voip_result_out_sequence_error%\n" +
                        "shortest / longest sequence: %PARAM voip_result_out_short_seq% / %PARAM voip_result_out_long_seq%\n" +
                        "%$ENDIF voip_result_status=='OK'%")
                .lang("en")
                .build()
        );
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("test.desc.voip")
                .value("Simulated VoIP call with a duration of %PARAM voip_objective_call_duration 1000000 1 f% ms.")
                .lang("en")
                .build()
        );
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("test.desc.jitter")
                .value("Simulated VoIP call with a duration of %PARAM voip_objective_call_duration 1000000 1 f% ms.")
                .lang("en")
                .build()
        );
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("voip.incoming.packet_loss.failure")
                .value("The incoming packet loss rate is greater than 5%!")
                .lang("en")
                .build()
        );
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("voip.outgoing.packet_loss.failure")
                .value("The outgoing packet loss rate is greater than 5%!")
                .lang("en")
                .build()
        );
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("voip.incoming.packet_loss.success")
                .value("The incoming packet loss rate is lower than 5%!")
                .lang("en")
                .build()
        );
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("voip.outgoing.packet_loss.success")
                .value("The outgoing packet loss rate is lower than 5%!")
                .lang("en")
                .build()
        );
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("test.desc.tcp.in.443")
                .value("Secure web site protocol (HTTPS, TCP port 443 incoming)")
                .lang("en")
                .build()
        );
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("test.desc.udp.in.5060")
                .value("Control of communication sessions (SIP, UDP port 5060 incoming)")
                .lang("en")
                .build()
        );
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("test.desc.udp.in.12345")
                .value("Dummy (TEST, UDP port 12345 incoming)")
                .lang("en")
                .build()
        );
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("test.desc.tcp.in.3389")
                .value("Microsoft Terminal Server (RDP, TCP port 3389 incoming)")
                .lang("en")
                .build()
        );
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("test.desc.udp.in.3389")
                .value("Microsoft Terminal Server (RDP, UDP port 3389 incoming)")
                .lang("en")
                .build()
        );
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("test.desc.tcp.in.8080")
                .value("Web site protocol (HTTP alternate, TCP port 8080 incoming)")
                .lang("en")
                .build()
        );
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("test.desc.tcp.in.5061")
                .value("Secure control of communication sessions (SIP over TLS, TCP port 5061 incoming)")
                .lang("en")
                .build()
        );
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("test.desc.tcp.in.5004")
                .value("Streaming of audio and visual media (RTP, TCP port 5004 incoming)")
                .lang("en")
                .build()
        );
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("test.desc.tcp.in.5005")
                .value("Quality of service for streaming of audio and visual media (RTCP, TCP port 5005 incoming)")
                .lang("en")
                .build()
        );
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("test.desc.udp.in.5004")
                .value("Streaming of audio and visual media (RTP, UDP port 5004 incoming)")
                .lang("en")
                .build()
        );
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("test.desc.udp.in.5005")
                .value("Quality of service for streaming of audio and visual media (RTCP, UDP port 5005 incoming)")
                .lang("en")
                .build()
        );
        qosTestDescList.add(QosTestDesc.builder()
                .descKey("dns.unknownrecordtype.info")
                .value("A DNS request for a non-existent record type %PARAM dns_objective_dns_record% of the domain %PARAM dns_objective_host% has been run. Correctly, no record should be found.\n" +
                        "DNS status: '%PARAM dns_result_status%'\n" +
                        "Duration:%PARAM duration_ns 1000000 0 f% ms\n")
                .lang("en")
                .build()
        );
    }
}
