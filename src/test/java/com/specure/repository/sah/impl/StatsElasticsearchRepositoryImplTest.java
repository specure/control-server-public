package com.specure.repository.sah.impl;

import com.specure.common.model.elastic.BasicTest;
import com.specure.dto.sah.NationalTableParams;
import com.specure.model.elastic.GeoShape;
import com.specure.multitenant.MultiTenantManager;
import com.specure.repository.sah.StatsElasticsearchRepository;
import com.specure.sah.TestConstants;
import com.specure.sah.TestUtils;
import com.specure.service.sah.NationalOperatorService;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedTerms;
import org.elasticsearch.search.aggregations.metrics.ParsedAvg;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;

import static com.specure.common.enums.NetworkType.NR;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class StatsElasticsearchRepositoryImplTest {

    final String NOW = "2020-11-03T00:00:00";

    @MockBean
    private ElasticsearchOperations elasticsearchOperations;
    @MockBean
    private MultiTenantManager multiTenantManager;
    @MockBean
    private Clock clock;
    @MockBean
    private NationalOperatorService nationalOperatorService;
    @Mock
    private SearchHits<BasicTest> basicTestSearchHits;
    @Mock
    private Aggregations aggregation;
    @Captor
    private ArgumentCaptor<NativeSearchQuery> searchQueryCaptor;
    @Captor
    private ArgumentCaptor<IndexCoordinates> indexCoordinatesArgumentCaptor;
    @Mock
    private ParsedAvg downloadParsedAvg;
    @Mock
    private ParsedAvg uploadParsedAvg;
    @Mock
    private ParsedAvg pingParsedAvg;
    @Mock
    private ParsedTerms providerParsedTerms;
    @Mock
    private SearchHit<GeoShape> geoShapeSearchHit;
    @Mock
    private GeoShape geoShape;
    private StatsElasticsearchRepository statsElasticsearchRepository;


    @Before
    public void setUp() {
        clock = Clock.fixed(LocalDateTime.parse(NOW).toInstant(ZoneOffset.UTC), ZoneId.of("UTC"));
        statsElasticsearchRepository = new StatsElasticsearchRepositoryImpl(
                multiTenantManager,
                clock,
                nationalOperatorService
        );
        when(multiTenantManager.getCurrentTenantElastic()).thenReturn(elasticsearchOperations);
        when(elasticsearchOperations.search(any(NativeSearchQuery.class), eq(BasicTest.class), any())).thenReturn(basicTestSearchHits);
        when(basicTestSearchHits.getAggregations()).thenReturn(aggregation);
        when(aggregation.get("download")).thenReturn(downloadParsedAvg);
        when(aggregation.get("upload")).thenReturn(uploadParsedAvg);
        when(aggregation.get("ping")).thenReturn(pingParsedAvg);
        when(aggregation.get("provider")).thenReturn(providerParsedTerms);
        when(providerParsedTerms.getBuckets()).thenReturn(Collections.emptyList());
        when(elasticsearchOperations.searchOne(any(NativeSearchQuery.class), eq(GeoShape.class), any())).thenReturn(geoShapeSearchHit);
        when(geoShapeSearchHit.getContent()).thenReturn(geoShape);
        when(geoShape.getMunicipalitySq()).thenReturn("Oslo");
    }

    @Test
    public void getNationalTable_whenInvoke_expectHalfYearPeriodInQuery() throws IOException {
        NationalTableParams nationalTableParams = new NationalTableParams(true, Collections.emptyList());
        final String queryJson = TestUtils.getJsonFromFilePath("elasticsearch/nationalTable/national-table-query.json");
        when(multiTenantManager.getCurrentTenant()).thenReturn("bh");

        statsElasticsearchRepository.getNationalTable(nationalTableParams, null);

        verify(elasticsearchOperations).search(searchQueryCaptor.capture(), eq(BasicTest.class), indexCoordinatesArgumentCaptor.capture());
        Assert.assertNotNull(searchQueryCaptor.getValue().getQuery());
        Assert.assertEquals(TestUtils.removeLineEndings(queryJson), TestUtils.removeLineEndings(searchQueryCaptor.getValue().getQuery().toString()));
    }

    @Test
    public void getNationalTable_whenOperatorNotNull_expectQueryWithOperator() throws IOException {
        NationalTableParams nationalTableParams = new NationalTableParams(true, Collections.emptyList());
        final String queryJson = TestUtils.getJsonFromFilePath("elasticsearch/nationalTable/national-table-query-with-operators.json");
        when(multiTenantManager.getCurrentTenant()).thenReturn("bh");
        when(nationalOperatorService.getNationalOperatorNames("bh", true)).thenReturn(List.of(TestConstants.DEFAULT_PROVIDER_NAME, TestConstants.DEFAULT_OPERATOR));

        statsElasticsearchRepository.getNationalTable(nationalTableParams, null);

        verify(elasticsearchOperations).search(searchQueryCaptor.capture(), eq(BasicTest.class), indexCoordinatesArgumentCaptor.capture());
        Assert.assertNotNull(searchQueryCaptor.getValue().getQuery());
        Assert.assertEquals(TestUtils.removeLineEndings(queryJson), TestUtils.removeLineEndings(searchQueryCaptor.getValue().getQuery().toString()));
    }

    @Test
    public void getNationalTable_whenInvokeWithAreaLabel_expectFilterByAreaInQuery() throws IOException {
        NationalTableParams nationalTableParams = new NationalTableParams(true, Collections.emptyList());
        final String queryJson = TestUtils.getJsonFromFilePath("elasticsearch/nationalTable/national-table-query-with-filter-by-municipality.json");
        when(multiTenantManager.getCurrentTenant()).thenReturn("no");

        statsElasticsearchRepository.getNationalTable(nationalTableParams, "Oslo");

        verify(elasticsearchOperations).search(searchQueryCaptor.capture(), eq(BasicTest.class), indexCoordinatesArgumentCaptor.capture());
        NativeSearchQuery searchQuery = searchQueryCaptor.getValue();
        Assert.assertNotNull(searchQuery.getQuery());
        Assert.assertEquals(TestUtils.removeLineEndings(queryJson), TestUtils.removeLineEndings(searchQuery.getQuery().toString()));
    }

    @Test
    public void getNationalTable_whenInvokeWithSpecificNetworks_expectFilterInQuery() throws IOException {
        NationalTableParams nationalTableParams = new NationalTableParams(true, List.of(NR));
        final String queryJson = TestUtils.getJsonFromFilePath("elasticsearch/nationalTable/national-table-query-with-filter-by-networks.json");
        when(multiTenantManager.getCurrentTenant()).thenReturn("bh");

        statsElasticsearchRepository.getNationalTable(nationalTableParams, null);

        verify(elasticsearchOperations).search(searchQueryCaptor.capture(), eq(BasicTest.class), indexCoordinatesArgumentCaptor.capture());
        NativeSearchQuery searchQuery = searchQueryCaptor.getValue();
        Assert.assertNotNull(searchQuery.getQuery());
        Assert.assertEquals(TestUtils.removeLineEndings(queryJson), TestUtils.removeLineEndings(searchQuery.getQuery().toString()));
    }

    @Test
    public void getMunicipalityNameByCode_whenInvokeCorrectly_expectGeoShapeQuery() throws IOException {
        NationalTableParams nationalTableParams = new NationalTableParams(true, Collections.emptyList());
        final String queryJson = TestUtils.getJsonFromFilePath("elasticsearch/nationalTable/national-table-mynicipality-code.json");
        when(multiTenantManager.getCurrentTenant()).thenReturn("bh");

        statsElasticsearchRepository.getNationalTable(nationalTableParams, TestConstants.DEFAULT_MUNICIPALITY_CODE);

        verify(elasticsearchOperations).searchOne(searchQueryCaptor.capture(), eq(GeoShape.class), indexCoordinatesArgumentCaptor.capture());
        NativeSearchQuery searchQuery = searchQueryCaptor.getValue();
        Assert.assertNotNull(searchQuery.getQuery());
        Assert.assertEquals(TestUtils.removeLineEndings(queryJson), TestUtils.removeLineEndings(searchQuery.getQuery().toString()));
        Assert.assertEquals("shape", indexCoordinatesArgumentCaptor.getValue().getIndexName());
    }
}
