package com.specure.service.sah.impl;

import com.specure.mapper.sah.NationalTableMapper;
import com.specure.model.dto.NationalTable;
import com.specure.model.dto.StatsByOperator;
import com.specure.multitenant.MultiTenantManager;
import com.specure.repository.sah.StatsElasticsearchRepository;
import com.specure.response.sah.stats.NationalTableResponse;
import com.specure.service.sah.StatsService;
import com.specure.utils.sah.region.RegionConverter;
import com.specure.utils.sah.region.impl.NorwayRegionConverter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class StatsServiceImplTest {

    @MockBean
    private StatsElasticsearchRepository statsElasticsearchRepository;
    @MockBean
    private NationalTableMapper nationalTableMapper;
    @MockBean
    private MultiTenantManager multiTenantManager;

    @Captor
    private ArgumentCaptor<NationalTable> nationalTableArgumentCaptor;

    private final String tenantValue = "no";

    private StatsService statsService;

    @Before
    public void setUp() {

        RegionConverter regionConverter = new NorwayRegionConverter();
        ReflectionTestUtils.setField(regionConverter, "tenant", tenantValue);

        statsService = new StatsServiceImpl(
                statsElasticsearchRepository,
                nationalTableMapper,
                multiTenantManager
        );
    }

    @Test
    public void getNationalTable_whenCalled_expectCorrectResponse() {
        NationalTable empty = NationalTable.builder().statsByOperator(Collections.emptyList()).build();

        when(statsElasticsearchRepository.getNationalTable(any(), eq(null)))
                .thenReturn(empty);
        when(multiTenantManager.getCurrentTenant())
                .thenReturn(tenantValue);

        statsService.getNationalTable("all", null);
    }

    @Test
    public void getNationalTable_whenCalled_expectCorrectSortResponse() {
        NationalTable unsorted = NationalTable.builder()
                .statsByOperator(List.of(
                                StatsByOperator.builder().operatorName("Kb").build(),
                                StatsByOperator.builder().operatorName("Ka").build(),
                                StatsByOperator.builder().operatorName("la").build(),
                                StatsByOperator.builder().operatorName("ice").build(),
                                StatsByOperator.builder().operatorName("Lb").build()
                        )
                )
                .build();

        when(statsElasticsearchRepository.getNationalTable(any(), eq(null)))
                .thenReturn(unsorted);
        when(multiTenantManager.getCurrentTenant())
                .thenReturn("no");

        NationalTableResponse result = statsService.getNationalTable("all", null);

        verify(nationalTableMapper).nationalTableToNationalTableResponse(nationalTableArgumentCaptor.capture());

        NationalTable table = nationalTableArgumentCaptor.getValue();

        Assert.assertEquals("ice", table.getStatsByOperator().get(0).getOperatorName());
        Assert.assertEquals("Ka", table.getStatsByOperator().get(1).getOperatorName());
        Assert.assertEquals("Kb", table.getStatsByOperator().get(2).getOperatorName());
        Assert.assertEquals("la", table.getStatsByOperator().get(3).getOperatorName());
        Assert.assertEquals("Lb", table.getStatsByOperator().get(4).getOperatorName());
    }
}
