package com.specure.common.repository.impl;

import com.specure.common.repository.GeoLocationRepository;
import com.specure.model.elastic.GeoShape;
import com.specure.multitenant.MultiTenantManager;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.geometry.Point;
import org.elasticsearch.index.query.GeoShapeQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GeoLocationRepositoryImpl implements GeoLocationRepository {

    private final MultiTenantManager multiTenantManager;

    @Override
    public Optional<GeoShape> getGeoShapeByCoordinates(Double longitude, Double latitude) {
        try {
            GeoShapeQueryBuilder geoFilter = QueryBuilders.geoShapeQuery("location", new Point(longitude, latitude));
            Query searchQuery = new NativeSearchQueryBuilder()
                    .withFilter(geoFilter)
                    .build();

            return multiTenantManager.getCurrentTenantElastic().search(searchQuery, GeoShape.class).stream()
                    .map(SearchHit::getContent)
                    .filter(x -> StringUtils.isNotBlank(x.getCountySq()) && StringUtils.isNotBlank(x.getMunicipalitySq()))
                    .findFirst();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
