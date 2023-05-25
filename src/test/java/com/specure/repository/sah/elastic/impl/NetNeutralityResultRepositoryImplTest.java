package com.specure.repository.sah.elastic.impl;

import com.specure.model.elastic.neutrality.NetNeutralityResult;
import com.specure.multitenant.MultiTenantManager;
import com.specure.sah.TestConstants;
import com.specure.sah.TestObjects;
import com.specure.sah.TestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class NetNeutralityResultRepositoryImplTest {

    @Mock
    private MultiTenantManager multiTenantManager;
    @InjectMocks
    private NetNeutralityResultRepositoryImpl netNeutralityResultRepository;

    @Mock
    private ElasticsearchOperations elasticsearchOperations;

    @Captor
    private ArgumentCaptor<IndexCoordinates> indexCoordinatesArgumentCaptor;
    @Captor
    private ArgumentCaptor<NativeSearchQuery> nativeSearchQueryArgumentCaptor;
    @Mock
    private SearchHits<NetNeutralityResult> searchHits;
    @Mock
    private SearchHit<NetNeutralityResult> searchHit;
    @Mock
    private NetNeutralityResult netNeutralityResult;

    @BeforeEach
    void setUp() {
        when(multiTenantManager.getCurrentTenantElastic()).thenReturn(elasticsearchOperations);
        when(multiTenantManager.getCurrentTenantNetNeutralityIndex()).thenReturn(TestConstants.DEFAULT_INDEX_NAME);
    }

    @Test
    void findAllByClientUuid_whenOpenTestUuidIsNotNull_expectedNetNeutralityResultPage() throws IOException {
        var total = 1L;
        Pageable pageable = TestObjects.getDefaultPageable();
        String queryJson = TestUtils.getJsonFromFilePath("elasticsearch/net-neutrality/open-test-uuid-is-not-null.json");
        when(elasticsearchOperations.search(nativeSearchQueryArgumentCaptor.capture(), eq(NetNeutralityResult.class), indexCoordinatesArgumentCaptor.capture())).thenReturn(searchHits);
        when(searchHits.getSearchHits()).thenReturn(List.of(searchHit));
        when(searchHits.getTotalHits()).thenReturn(total);
        when(searchHit.getContent()).thenReturn(netNeutralityResult);

        var expectedResult = new PageImpl<>(List.of(netNeutralityResult), pageable, total);

        var result = netNeutralityResultRepository.findAllByClientUuid(TestConstants.DEFAULT_CLIENT_UUID_STRING, TestConstants.DEFAULT_OPEN_TEST_UUID_STRING, pageable);

        Assertions.assertEquals(TestConstants.DEFAULT_INDEX_NAME, indexCoordinatesArgumentCaptor.getValue().getIndexName());
        Assertions.assertEquals(TestUtils.removeLineEndings(queryJson), TestUtils.removeLineEndings(nativeSearchQueryArgumentCaptor.getValue().getQuery().toString()));
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    void findAllByClientUuid_whenOpenTestUuidIsNull_expectedNetNeutralityResultPage() throws IOException {
        var total = 1L;
        Pageable pageable = TestObjects.getDefaultPageable();
        String queryJson = TestUtils.getJsonFromFilePath("elasticsearch/net-neutrality/open-test-uuid-is-null.json");
        when(elasticsearchOperations.search(nativeSearchQueryArgumentCaptor.capture(), eq(NetNeutralityResult.class), indexCoordinatesArgumentCaptor.capture())).thenReturn(searchHits);
        when(searchHits.getSearchHits()).thenReturn(List.of(searchHit));
        when(searchHits.getTotalHits()).thenReturn(total);
        when(searchHit.getContent()).thenReturn(netNeutralityResult);

        var expectedResult = new PageImpl<>(List.of(netNeutralityResult), pageable, total);

        var result = netNeutralityResultRepository.findAllByClientUuid(TestConstants.DEFAULT_CLIENT_UUID_STRING, null, pageable);

        Assertions.assertEquals(TestConstants.DEFAULT_INDEX_NAME, indexCoordinatesArgumentCaptor.getValue().getIndexName());
        Assertions.assertEquals(TestUtils.removeLineEndings(queryJson), TestUtils.removeLineEndings(nativeSearchQueryArgumentCaptor.getValue().getQuery().toString()));
        Assertions.assertEquals(expectedResult, result);
    }
}