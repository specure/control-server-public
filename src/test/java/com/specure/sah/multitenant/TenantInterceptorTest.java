package com.specure.sah.multitenant;

import com.specure.common.constant.HeaderConstants;
import com.specure.constant.URIConstants;
import com.specure.exception.WrongTenantException;
import com.specure.interceptor.TenantInterceptor;
import com.specure.multitenant.MultiTenantManager;
import com.specure.sah.TestConstants;
import com.specure.security.constants.SecurityConstants;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;

import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class TenantInterceptorTest {

    @MockBean
    private MultiTenantManager multiTenantManager;
    private TenantInterceptor tenantInterceptor;

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private Object handler;
    @Mock
    private Enumeration<String> enumerationCountries;

    @Before
    public void setUp() throws Exception {
        tenantInterceptor = new TenantInterceptor(multiTenantManager);
    }

    @Test
    public void preHandle_countriesNotNull_multiTenantUpdated() {
        when(request.getHeaders(HeaderConstants.COUNTRY)).thenReturn(enumerationCountries);
        when(request.getHeader(SecurityConstants.CLIENT_NAME)).thenReturn(TestConstants.DEFAULT_TENANT);
        when(multiTenantManager.isTenantExist(TestConstants.DEFAULT_TENANT)).thenReturn(true);

        tenantInterceptor.preHandle(request, response, handler);

        verify(multiTenantManager).setCurrentCountry(enumerationCountries);
    }

    @Test
    public void preHandle_clientNotNull_multiTenantUpdated() {
        when(multiTenantManager.isTenantExist(TestConstants.DEFAULT_TENANT)).thenReturn(true);
        when(request.getHeader(SecurityConstants.CLIENT_NAME)).thenReturn(TestConstants.DEFAULT_TENANT);

        tenantInterceptor.preHandle(request, response, handler);

        verify(multiTenantManager).setCurrentTenant(TestConstants.DEFAULT_TENANT);
    }

    @Test(expected = WrongTenantException.class)
    public void preHandle_clientIsNullAndNotAdmin_multiTenantNotUpdated() {
        when(multiTenantManager.isTenantExist(null)).thenReturn(false);
        when(request.getHeader(SecurityConstants.CLIENT_NAME)).thenReturn(null);
        when(request.getRequestURI()).thenReturn(URIConstants.MEASUREMENT_SERVER);

        tenantInterceptor.preHandle(request, response, handler);

        verify(multiTenantManager, times(0)).setCurrentTenant(anyString());
    }
}
