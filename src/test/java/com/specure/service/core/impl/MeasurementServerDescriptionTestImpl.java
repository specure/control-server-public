package com.specure.service.core.impl;

import com.specure.common.model.jpa.MeasurementServerDescription;
import com.specure.multitenant.MultiTenantManager;
import com.specure.common.repository.MeasurementServerDescriptionRepository;
import com.specure.service.core.MeasurementServerDescriptionService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static com.specure.core.TestConstants.DEFAULT_ID;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
public class MeasurementServerDescriptionTestImpl {

    @MockBean
    private MeasurementServerDescriptionRepository measurementServerDescriptionRepository;
    @MockBean
    private MultiTenantManager multiTenantManager;
    @Mock
    private MeasurementServerDescription measurementServerDescription;

    private MeasurementServerDescriptionService measurementServerDescriptionService;

    @Before
    public void setUp() {
        measurementServerDescriptionService =
                new MeasurementServerDescriptionServiceImpl(measurementServerDescriptionRepository, multiTenantManager);
    }

    @Test
    public void save_correctInvocation_saved() {
        measurementServerDescriptionService.save(measurementServerDescription);
        verify(measurementServerDescriptionRepository).save(measurementServerDescription);
    }

    @Test
    public void deleteById_correctInvocation_removed() {
        measurementServerDescriptionService.deleteById(DEFAULT_ID);
        verify(measurementServerDescriptionRepository).deleteById(DEFAULT_ID);
    }

    @Test
    public void existById_correctInvocation_repositoryCall() {
        measurementServerDescriptionService.existById(DEFAULT_ID);
        verify(measurementServerDescriptionRepository).existsById(DEFAULT_ID);
    }

}
