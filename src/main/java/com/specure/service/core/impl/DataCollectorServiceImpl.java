package com.specure.service.core.impl;

import com.google.common.net.InetAddresses;
import com.specure.constant.Constants;
import com.specure.multitenant.MultiTenantManager;
import com.specure.response.core.DataCollectorResponse;
import com.specure.response.core.IpResponse;
import com.specure.service.core.DataCollectorService;
import com.specure.common.utils.HeaderExtrudeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class DataCollectorServiceImpl implements DataCollectorService {

    private final MultiTenantManager multiTenantManager;

    @Override
    public DataCollectorResponse extrudeData(HttpServletRequest request, Map<String, String> headers) {
        log.debug("DataCollectorServiceImpl:extrudeData started with tenant = {}, headers = {}", multiTenantManager.getCurrentTenant(), StringUtils.join(headers));
        DataCollectorResponse dataCollectorResponse = DataCollectorResponse.builder()
                .agent(request.getHeader("User-Agent"))
                .headers(headers)
                .ip(HeaderExtrudeUtil.getIpFromNgNixHeader(headers))
                .port(request.getRemotePort())
                .build();
        log.debug("DataCollectorServiceImpl:extrudeData finished with tenant = {}, response = {}", multiTenantManager.getCurrentTenant(), dataCollectorResponse);
        return dataCollectorResponse;
    }

    @Override
    public IpResponse getIpVersion(HttpServletRequest request, Map<String, String> headers) {
        log.info("DataCollectorServiceImpl:getIpVersion started with tenant ={}, headers = {}", multiTenantManager.getCurrentTenant(), StringUtils.join(headers));
        String clientIpRaw = HeaderExtrudeUtil.getIpFromNgNixHeader(headers);
        InetAddress clientAddress = InetAddresses.forString(clientIpRaw);
        IpResponse ipResponse = IpResponse.builder()
                .ip(clientIpRaw)
                .version(getVersion(clientAddress))
                .build();
        log.debug("DataCollectorServiceImpl:getIpVersion finished with tenant = {}, response = {}", multiTenantManager.getCurrentTenant(), ipResponse);
        return ipResponse;
    }

    private String getVersion(InetAddress clientAddress) {
        log.trace("DataCollectorServiceImpl:getVersion started with tenant = {}, clientAddress = {}", multiTenantManager.getCurrentTenant(), clientAddress);
        String addressVersion;
        if (clientAddress instanceof Inet4Address) {
            addressVersion = Constants.INET_4_IP_VERSION;
        } else {
            addressVersion = Constants.INET_6_IP_VERSION;
        }
        log.trace("DataCollectorServiceImpl:getVersion tenant = {}, inet address version = {}", multiTenantManager.getCurrentTenant(), addressVersion);
        return addressVersion;
    }
}
