package com.specure.scheduled;

import com.specure.common.model.elastic.BasicQosTest;
import com.specure.common.model.elastic.BasicTest;
import com.specure.common.model.elastic.ProbeKeepAliveElasticsearch;
import com.specure.multitenant.MultiTenantManager;
import com.specure.sah.TestConstants;
import com.specure.sah.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class BetaScheduledTest {

    @MockBean
    private MultiTenantManager multiTenantManager;
    @MockBean
    private Clock clock;

    private BetaScheduled betaScheduled;

    @Mock
    private ElasticsearchOperations elasticsearchOperations;
    @Captor
    private ArgumentCaptor<IndexCoordinates> indexCoordinatesArgumentCaptor;
    @Captor
    private ArgumentCaptor<NativeSearchQuery> nativeSearchQueryArgumentCaptor;

    @BeforeEach
    void setUp() {
        when(clock.instant()).thenReturn(Instant.ofEpochMilli(TestConstants.DEFAULT_TIME_INSTANT));
        when(clock.getZone()).thenReturn(ZoneId.of("UTC"));
        betaScheduled = new BetaScheduled(multiTenantManager, clock);
        ReflectionTestUtils.setField(betaScheduled, "KEEP_ALIVE_INDEX", TestConstants.DEFAULT_KEEP_ALIVE_INDEX_NAME);
    }

    @Test
    void cleanOldDocumentsInElasticsearch_correctInvocation_expectDeleted() throws IOException {
        final String queryJson = TestUtils.getJsonFromFilePath("elasticsearch/scheduled.json");
        when(multiTenantManager.getElasticsearchTenants()).thenReturn(Set.of(TestConstants.DEFAULT_TENANT));
        when(multiTenantManager.getCurrentTenantElastic()).thenReturn(elasticsearchOperations);
        when(multiTenantManager.getCurrentTenantBasicIndex()).thenReturn(TestConstants.DEFAULT_INDEX_NAME);
        when(multiTenantManager.getCurrentTenantQosIndex()).thenReturn(TestConstants.DEFAULT_TENANT_QOS_INDEX);

        betaScheduled.cleanOldDocumentsInElasticsearch();

        verify(elasticsearchOperations).delete(nativeSearchQueryArgumentCaptor.capture(), eq(BasicTest.class), indexCoordinatesArgumentCaptor.capture());
        verify(elasticsearchOperations).delete(any(), eq(BasicQosTest.class), indexCoordinatesArgumentCaptor.capture());
        verify(elasticsearchOperations).delete(any(), eq(ProbeKeepAliveElasticsearch.class), indexCoordinatesArgumentCaptor.capture());
        assertEquals(TestConstants.DEFAULT_INDEX_NAME, indexCoordinatesArgumentCaptor.getAllValues().get(0).getIndexName());
        assertEquals(TestConstants.DEFAULT_TENANT_QOS_INDEX, indexCoordinatesArgumentCaptor.getAllValues().get(1).getIndexName());
        assertEquals(TestConstants.DEFAULT_KEEP_ALIVE_INDEX_NAME, indexCoordinatesArgumentCaptor.getAllValues().get(2).getIndexName());
        assertNotNull(nativeSearchQueryArgumentCaptor.getValue().getQuery());
        assertEquals(TestUtils.removeLineEndings(queryJson), TestUtils.removeLineEndings(nativeSearchQueryArgumentCaptor.getValue().getQuery().toString()));
    }
}
