package com.specure.sah.onstartup;

import com.specure.config.ClientTenantConfig;
import com.specure.multitenant.MultiTenantManager;
import com.specure.onstartup.OnStartUpRunner;
import com.specure.service.sah.BasicTestHistoryCacheService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class OnStartUpRunnerTest {

    @MockBean
    private ClientTenantConfig dataSourceConfig;
    @MockBean
    private MultiTenantManager multiTenantManager;
    @MockBean
    private BasicTestHistoryCacheService basicTestHistoryCacheService;

    OnStartUpRunner onStartUpRunner;

    @Before
    public void setUp() {
        onStartUpRunner = new OnStartUpRunner(
                dataSourceConfig,
                multiTenantManager,
                basicTestHistoryCacheService
        );
    }


    @Test
    public void run_whenInvoke_expectAddTenant() {
        when(dataSourceConfig.getClientTenantMapping())
                .thenReturn(Map.of("bh", getDatasourceClientCredential()));

        onStartUpRunner.run(null);

        verify(multiTenantManager).addTenant("bh");
        verify(multiTenantManager).addTenantElasticsearch("bh");
    }

    private ClientTenantConfig.DatasourceClientCredential getDatasourceClientCredential() {
        return new ClientTenantConfig.DatasourceClientCredential();
    }
}
