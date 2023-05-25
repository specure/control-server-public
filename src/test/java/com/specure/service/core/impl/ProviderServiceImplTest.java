package com.specure.service.core.impl;

import com.specure.common.exception.ProviderNotFoundException;
import com.specure.common.model.jpa.Provider;
import com.specure.common.repository.ProviderRepository;
import com.specure.multitenant.MultiTenantManager;
import com.specure.sah.TestConstants;
import com.specure.service.admin.ProviderService;
import com.specure.service.admin.impl.ProviderServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class ProviderServiceImplTest {

    @MockBean
    private ProviderRepository providerRepository;
    @MockBean
    private MultiTenantManager multiTenantManager;
    private ProviderService providerService;

    @Mock
    private Provider provider;


    @BeforeEach
    void setUp() {
        providerService = new ProviderServiceImpl(providerRepository,
                multiTenantManager);
    }

    @Test
    void getProviderById_whenCorrectInvocation_expectedProvider() {
        when(providerRepository.findById(TestConstants.DEFAULT_PROVIDER_ID)).thenReturn(Optional.of(provider));

        var result = providerService.getProviderById(TestConstants.DEFAULT_PROVIDER_ID);

        Assertions.assertEquals(provider, result);
    }

    @Test
    void getProviderById_whenProviderNotFound_expectedException() {
        when(providerRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ProviderNotFoundException.class, () -> providerService.getProviderById(TestConstants.DEFAULT_PROVIDER_ID));
    }
}
