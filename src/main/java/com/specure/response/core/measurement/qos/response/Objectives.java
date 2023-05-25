package com.specure.response.core.measurement.qos.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Data
@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Objectives {
    private List<Voip> voip;
    private List<Jitter> jitter;
    private List<TcpSettingsResponse> tcp;
    private List<UdpSettingsResponse> udp;
    private List<HttpProxySettingsResponse> httpProxy;
    private List<WebsiteSettingsResponse> website;
    private List<NonTransparentProxySettingsResponse> nonTransparentProxy;
    private List<DnsSettingsResponse> dns;
}
