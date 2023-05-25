package com.specure.aspect;

import com.specure.constant.Constants;
import com.specure.multitenant.MultiTenantManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor//TODO: move to annotation mechanism with tenant param
public class AdminTenantAspect {

    private final MultiTenantManager multiTenantManager;

    @Pointcut("execution(* com.specure.service.admin.impl.RawProviderServiceImpl.*(..)) " +
            "|| execution(* com.specure.service.admin.impl.MobileModelServiceImpl.*(..)) " +
            "|| execution(* com.specure.service.sah.impl.NationalOperatorServiceImpl.*(..))")
    public void providerClassMethods() {
    }

    @Around("providerClassMethods()")
    public Object changeTenantToAdminAndBack(ProceedingJoinPoint pjp) throws Throwable {
        String tenantBefore = multiTenantManager.getCurrentTenant();
        List<String> countries = multiTenantManager.getCurrentCountries();
        return CompletableFuture.supplyAsync(() -> executeMethodInNewThread(pjp, tenantBefore, countries)).get();//TODO: update with separate
    }

    private Object executeMethodInNewThread(ProceedingJoinPoint pjp, String tenantBefore, List<String> countries) {
        multiTenantManager.setCurrentTenant(Constants.ADMIN_TENANT_NAME);
        multiTenantManager.setCurrentCountry(Collections.enumeration(countries));
        try {
            try {
                return pjp.proceed();
            } catch (Throwable throwable) {
                throw new RuntimeException(throwable);
            }
        } finally {
            multiTenantManager.setCurrentTenant(tenantBefore);
        }
    }
}
