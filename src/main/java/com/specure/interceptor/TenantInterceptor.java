package com.specure.interceptor;

import com.specure.constant.ErrorMessage;
import com.specure.common.constant.HeaderConstants;
import com.specure.constant.URIConstants;
import com.specure.exception.WrongTenantException;
import com.specure.multitenant.MultiTenantManager;
import com.specure.security.Role;
import com.specure.security.SahAccount;
import com.specure.security.resolver.SahAccountHandlerMethodArgumentResolver;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

import static com.specure.security.constants.SecurityConstants.*;

@Component
@AllArgsConstructor
@Slf4j
public class TenantInterceptor extends HandlerInterceptorAdapter {

    private final static List<String> EQUALS_EXCLUDES = List.of("/",
            "/swagger-ui.html",
            "/error",
            "/ip",
            "/mobile/ip");
    private final static List<String> START_WITH_EXCLUDES = List.of("/webjars",
            "/swagger-resources");

    private final MultiTenantManager multiTenantManager;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Optional.ofNullable(request.getHeader(CLIENT_NAME))
                .filter(multiTenantManager::isTenantExist)
                .ifPresentOrElse(multiTenantManager::setCurrentTenant,
                        () -> {
                            if (isMustContainTenant(request.getRequestURI())) {
                                throw new WrongTenantException(String.format(ErrorMessage.WRONG_TENANT, request.getHeader(CLIENT_NAME), request.getRequestURI()));
                            }
                        }
                );
        multiTenantManager.setCurrentCountry(request.getHeaders(HeaderConstants.COUNTRY));

        if (request.getHeader(AUTHORIZATION) == null) {
            return true;
        }
        if (request.getServletPath().equals(URIConstants.ERROR)) {
            return true;
        }
        SahAccount sahAccount = new SahAccountHandlerMethodArgumentResolver()
                .convert(request.getUserPrincipal(), StringUtils.removeStart(request.getHeader(AUTHORIZATION), BEARER));
        String tenantPermissionHeader = String.join(":", CLIENT, request.getHeader(CLIENT_NAME));
        if (sahAccount.getRoles().contains(Role.SPECURE.getName())) {
            return true;
        }
        if (!sahAccount.getRoles().contains(tenantPermissionHeader)) throw new SahAccount.NotPermitted();
        return true;
    }

    private boolean isMustContainTenant(String uri) {
        return !EQUALS_EXCLUDES.contains(uri) && !isExcludedByPrefix(uri);
    }

    private boolean isExcludedByPrefix(String uri) {
        return START_WITH_EXCLUDES.stream()
                .anyMatch(uri::startsWith);
    }
}
