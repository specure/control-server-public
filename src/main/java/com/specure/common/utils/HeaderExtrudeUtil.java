package com.specure.common.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@UtilityClass
@Slf4j
public class HeaderExtrudeUtil {

    public final String HEADER_NGINX_X_REAL_IP = "x-real-ip";
    public final String HEADER_NGINX_X_FORWARDED_FOR = "x-forwarded-for";
    private final String USER_AGENT = "User-Agent";
    private final String USER_AGENT_LOW_CASE = "user-agent";
    private final String IP_UNKNOWN = "ip unknown";
    private final String UNKNOWN_DEVICE = "unknown device";

    public String getIpFromNgNixHeader(Map<String, String> headers) {
        if (headers.containsKey(HEADER_NGINX_X_REAL_IP)) {
            log.info(HEADER_NGINX_X_REAL_IP + "___" + headers.get(HEADER_NGINX_X_REAL_IP));
            return headers.get(HEADER_NGINX_X_REAL_IP);
        }
        if (headers.containsKey(HEADER_NGINX_X_FORWARDED_FOR)) {
            log.info(HEADER_NGINX_X_FORWARDED_FOR + "___" + headers.get(HEADER_NGINX_X_FORWARDED_FOR));
            return headers.get(HEADER_NGINX_X_FORWARDED_FOR);
        }
        return IP_UNKNOWN;
    }

    public String getUserAgent(Map<String, String> headers) {
        if (headers.containsKey(USER_AGENT)) {
            return headers.get(USER_AGENT);
        }
        if (headers.containsKey(USER_AGENT_LOW_CASE)) {
            return headers.get(USER_AGENT_LOW_CASE);
        }
        return UNKNOWN_DEVICE;
    }

    public String getIpFromNgNixHeader(HttpServletRequest request, Map<String, String> headers) {
        return Optional.ofNullable(headers).orElse(Collections.emptyMap()).entrySet().stream()
                .filter(x -> StringUtils.equalsAnyIgnoreCase(x.getKey(), HEADER_NGINX_X_REAL_IP))
                .findFirst()
                .map(Map.Entry::getValue)
                .orElse(request.getLocalAddr());
    }
}
