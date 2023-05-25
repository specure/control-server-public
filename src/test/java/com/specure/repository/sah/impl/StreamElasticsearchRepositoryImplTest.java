//package com.specure.repository.sah.impl;
//
//import com.specure.core.enums.DigitalSeparator;
//import com.specure.core.enums.MeasurementStatus;
//import com.specure.core.multitenant.MultiTenantManager;
//import com.specure.sah.TestUtils;
//import com.specure.mapper.sah.BasicTestMapper;
//import com.specure.model.sah.elastic.BasicTest;
//import com.specure.sah.repository.StreamElasticsearchRepository;
//import org.elasticsearch.action.search.SearchResponse;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.ArgumentCaptor;
//import org.mockito.Captor;
//import org.mockito.Mock;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
//import org.springframework.data.elasticsearch.core.ResultsExtractor;
//import org.springframework.data.elasticsearch.core.ScrolledPage;
//import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
//import org.springframework.data.elasticsearch.core.query.SearchQuery;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.io.IOException;
//import java.sql.Timestamp;
//import java.util.Collections;
//import java.util.List;
//
//import static org.mockito.Mockito.*;
//
//@RunWith(SpringRunner.class)
//public class StreamElasticsearchRepositoryImplTest {
//
//    @MockBean
//    private MultiTenantManager multiTenantManager;
//    @MockBean
//    private BasicTestMapper basicTestMapper;
//    @MockBean
//    private ElasticsearchOperations elasticsearchOperations;
//
//    @Captor
//    private ArgumentCaptor<ResultsExtractor<SearchResponse>> resultsExtractorCaptor;
//    @Captor
//    private ArgumentCaptor<SearchQuery> searchQueryCaptor;
//
//    @Mock
//    private ScrolledPage<BasicTest> scrolledPage;
//
//    private StreamElasticsearchRepository streamElasticsearchRepository;
//
//    @Before
//    public void setUp(){
//        streamElasticsearchRepository = new StreamElasticsearchRepositoryImpl(
//                multiTenantManager, basicTestMapper
//        );
//        when(multiTenantManager.getCurrentTenantElastic()).thenReturn(elasticsearchOperations);
//    }
//
//    @Test
//    public void findAllByTimeBetweenAndStatus_whenInvoke_expectCorrectQuery() throws IOException {
//        when(multiTenantManager.getCurrentTenant()).thenReturn("no");
//        final String queryJson = TestUtils.getJson("elasticsearch/open-mobile-data-by-month.json");
//        Timestamp start = Timestamp.valueOf("2021-01-11 05:00:00");
//        Timestamp finish = Timestamp.valueOf("2021-02-11 05:00:00");
//
//        doReturn(new AggregatedPageImpl(List.of(BasicTest.builder().build())))
//                .when(elasticsearchOperations).startScroll(eq(3000L), (SearchQuery) any(),any());
//        doReturn(new AggregatedPageImpl(Collections.emptyList()))
//                .when(elasticsearchOperations).startScroll(eq(3000L), (SearchQuery) any(),any());
//
//        streamElasticsearchRepository.findAllByTimeBetweenAndStatus(start, finish, MeasurementStatus.FINISHED, DigitalSeparator.COMMA);
//
//        verify(elasticsearchOperations).startScroll( eq(3000L), searchQueryCaptor.capture(), any());
//        SearchQuery searchQuery = searchQueryCaptor.getValue();
//        Assert.assertEquals(queryJson, searchQuery.getQuery().toString()+"\n");
//    }
//
//    @Test
//    public void findAllByStatus_whenInvoke_expectCorrectQuery() throws IOException {
//        final String queryJson = TestUtils.getJson("elasticsearch/open-mobile-data.json");
//        ScrolledPage<BasicTest> scrollPage = new AggregatedPageImpl(Collections.emptyList());
//        doReturn(scrollPage).when(elasticsearchOperations).startScroll(eq(3000L), (SearchQuery) any(),any());
//        streamElasticsearchRepository.findAllByStatus(MeasurementStatus.FINISHED, DigitalSeparator.COMMA);
//        verify(elasticsearchOperations).startScroll( eq(3000L), searchQueryCaptor.capture(), any());
//        SearchQuery searchQuery = searchQueryCaptor.getValue();
//        Assert.assertEquals(queryJson, searchQuery.getQuery().toString()+"\n");
//    }
//}
