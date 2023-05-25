package com.specure.repository.sah.impl;

import com.specure.common.model.elastic.BasicQosTest;
import com.specure.multitenant.MultiTenantManager;
import com.specure.repository.sah.BasicQosTestRepository;
import com.specure.sah.TestConstants;
import com.specure.sah.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class BasicQosTestRepositoryImplTest {
    private BasicQosTestRepository basicQosTestRepository;

    @MockBean
    private MultiTenantManager multiTenantManager;

    @Mock
    private ElasticsearchRestTemplate elasticsearchRestTemplate;
    @Mock
    private BasicQosTest basicQosTest;
    @Captor
    private ArgumentCaptor<IndexCoordinates> indexCoordinatesArgumentCaptor;
    @Captor
    private ArgumentCaptor<NativeSearchQuery> nativeSearchQueryArgumentCaptor;

    @Before
    public void setUp() {
        basicQosTestRepository = new BasicQosTestRepositoryImpl(multiTenantManager);
    }

    @Test
    public void findByBasicTestUuid_whenCommonData_expect() throws IOException {
        final String queryJson = TestUtils.getJsonFromFilePath("elasticsearch/get-basic-test-by-uuid.json");
        when(multiTenantManager.getCurrentTenantElastic()).thenReturn(elasticsearchRestTemplate);
        when(multiTenantManager.getCurrentTenantQosIndex()).thenReturn(TestConstants.DEFAULT_TENANT_QOS_INDEX);
        when(elasticsearchRestTemplate.searchOne(any(), eq(BasicQosTest.class), any())).thenReturn(getDefaultHit());

        var response = basicQosTestRepository.findByBasicTestUuid(TestConstants.DEFAULT_UUID);

        verify(elasticsearchRestTemplate).searchOne(nativeSearchQueryArgumentCaptor.capture(), eq(BasicQosTest.class), indexCoordinatesArgumentCaptor.capture());
        assertEquals(TestConstants.DEFAULT_TENANT_QOS_INDEX, indexCoordinatesArgumentCaptor.getValue().getIndexName());
        assertEquals(TestUtils.removeLineEndings(queryJson), TestUtils.removeLineEndings(nativeSearchQueryArgumentCaptor.getValue().getQuery().toString()));
        assertEquals(Optional.of(basicQosTest), response);
    }

    private SearchHit<BasicQosTest> getDefaultHit() {
        return new SearchHit<>(null,
                null, null, 1.0f, null,
                null, basicQosTest
        );
    }
}
