package com.specure.repository.sah.impl;

import com.specure.common.repository.impl.GeoLocationRepositoryImpl;
import com.specure.model.elastic.GeoShape;
import com.specure.multitenant.MultiTenantManager;
import com.specure.common.repository.GeoLocationRepository;
import com.specure.sah.TestConstants;
import com.specure.sah.TestUtils;
import org.elasticsearch.search.aggregations.Aggregations;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHitsImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static com.specure.sah.TestConstants.DEFAULT_TEXT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.data.elasticsearch.core.TotalHitsRelation.EQUAL_TO;

@RunWith(SpringRunner.class)
public class GeoLocationRepositoryImplTest {

    private GeoLocationRepository geoLocationRepository;

    @MockBean
    private MultiTenantManager multiTenantManager;

    @Mock
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Captor
    private ArgumentCaptor<NativeSearchQuery> nativeSearchQueryBuilderArgumentCaptor;

    @Before
    public void setUp() {
        geoLocationRepository = new GeoLocationRepositoryImpl(multiTenantManager);
    }

    @Test
    public void getLocationDescription_whenCommonData_expectGeoLocationDescriptionResponse() throws IOException {
        var expectedResponse = GeoShape.builder()
                .countySq(TestConstants.DEFAULT_COUNTY)
                .municipalitySq(TestConstants.DEFAULT_MUNICIPALITY)
                .build();
        final String queryJson = TestUtils.getJsonFromFilePath("elasticsearch/geo-location-description-filter.json");
        when(multiTenantManager.getCurrentTenantElastic()).thenReturn(elasticsearchRestTemplate);
        when(elasticsearchRestTemplate.search(any(NativeSearchQuery.class), eq(GeoShape.class))).thenReturn(getSearchHits());

        var result = geoLocationRepository.getGeoShapeByCoordinates(TestConstants.DEFAULT_LONGITUDE, TestConstants.DEFAULT_LATITUDE);

        verify(elasticsearchRestTemplate).search(nativeSearchQueryBuilderArgumentCaptor.capture(), eq(GeoShape.class));
        assertEquals(TestUtils.removeLineEndings(queryJson), TestUtils.removeLineEndings(nativeSearchQueryBuilderArgumentCaptor.getValue().getFilter().toString()));
        assertNull(nativeSearchQueryBuilderArgumentCaptor.getValue().getQuery());
        assertEquals(expectedResponse, result.get());
    }

    private SearchHitsImpl<GeoShape> getSearchHits() {
        SearchHit<GeoShape> rightHit = getGeoShapeSearchHit(
                GeoShape.builder()
                        .countySq(TestConstants.DEFAULT_COUNTY)
                        .municipalitySq(TestConstants.DEFAULT_MUNICIPALITY)
                        .build()
        );
        SearchHit<GeoShape> emptyHit = getGeoShapeSearchHit(
                GeoShape.builder()
                        .build()
        );
        return new SearchHitsImpl<>(
                0,
                EQUAL_TO,
                1.0f,
                DEFAULT_TEXT,
                List.of(rightHit, emptyHit),
                new Aggregations(Collections.emptyList())
        );
    }

    private SearchHit<GeoShape> getGeoShapeSearchHit(GeoShape geoShape) {
        return new SearchHit<>(null,
                null, null, 1.0f, null,
                null, geoShape
        );
    }
}
