package com.specure.service.core.impl;

import com.blueconic.browscap.Capabilities;
import com.blueconic.browscap.UserAgentParser;
import com.specure.multitenant.MultiTenantManager;
import com.specure.service.core.UserAgentExtractService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class UserAgentExtractServiceImpl implements UserAgentExtractService {

    private final UserAgentParser userAgentParser;
    private final MultiTenantManager multiTenantManager;

    @Override
    public String getBrowser(String userAgentHeader) {
        log.debug("UserAgentExtractServiceImpl:getBrowser started with tenant = {}, userAgentHeader = {}", multiTenantManager.getCurrentTenant(), userAgentHeader);
        final Capabilities capabilities = userAgentParser.parse(userAgentHeader);
        String browser = capabilities.getBrowser();
        log.debug("UserAgentExtractServiceImpl:getBrowser finished with tenant = {}, browser = {}", multiTenantManager.getCurrentTenant(), browser);
        return browser;
    }
}
