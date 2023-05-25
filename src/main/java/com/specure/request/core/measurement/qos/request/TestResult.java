package com.specure.request.core.measurement.qos.request;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "test_type", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = VoipTestResultRequest.class, name = "voip"),
        @JsonSubTypes.Type(value = TcpTestResultRequest.class, name = "tcp"),
        @JsonSubTypes.Type(value = UdpTestResultRequest.class, name = "udp"),
        @JsonSubTypes.Type(value = NonTransparentProxyTestResultRequest.class, name = "non_transparent_proxy"),
        @JsonSubTypes.Type(value = HttpProxyTestResultRequest.class, name = "http_proxy"),
        @JsonSubTypes.Type(value = DnsTestResultRequest.class, name = "dns"),
        @JsonSubTypes.Type(value = WebsiteTestResultRequest.class, name = "website"),
        @JsonSubTypes.Type(value = TracerouteTestResultRequest.class, names = {"traceroute", "traceroute_masked"})
})
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class TestResult {
    private String test_type;
    private long qosTestUid;
    private long durationNs;
    private long startTimeNs;
}
