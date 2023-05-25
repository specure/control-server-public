package com.specure.repository.sah.impl;

import com.specure.common.model.elastic.BasicQosTest;
import com.specure.multitenant.MultiTenantManager;
import com.specure.repository.sah.BasicQosTestRepository;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.boolQuery;

@Repository
@RequiredArgsConstructor
public class BasicQosTestRepositoryImpl implements BasicQosTestRepository {

    private final MultiTenantManager multiTenantManager;

    @Override
    public List<BasicQosTest> findByBasicTestUuid(Collection<String> uuid) {
        BoolQueryBuilder filteredQuery = boolQuery();
        filteredQuery.must(QueryBuilders.termsQuery("openTestUuid.keyword", uuid.toArray(String[]::new))); //

        final Query searchQuery = new NativeSearchQueryBuilder()
                .withQuery(filteredQuery)
                .withSearchType(SearchType.DEFAULT)
                .build();

        return multiTenantManager.getCurrentTenantElastic().search(searchQuery, BasicQosTest.class, IndexCoordinates.of(multiTenantManager.getCurrentTenantQosIndex())).get().map(SearchHit::getContent).collect(Collectors.toList());
    }

    @Override
    public Optional<BasicQosTest> findByBasicTestUuid(String uuid) {
        BoolQueryBuilder filteredQuery = boolQuery();
        filteredQuery.must(QueryBuilders.termQuery("openTestUuid.keyword", uuid));
        final Query searchQuery = new NativeSearchQueryBuilder()
                .withQuery(filteredQuery)
                .withSearchType(SearchType.DEFAULT)
                .build();
        return Optional.ofNullable(multiTenantManager.getCurrentTenantElastic().searchOne(searchQuery, BasicQosTest.class, IndexCoordinates.of(multiTenantManager.getCurrentTenantQosIndex())))
                .map(SearchHit::getContent);
    }

    @Override
    public String save(BasicQosTest basicTest) {
        IndexQuery indexQuery = new IndexQueryBuilder()
                .withObject(basicTest)
                .build();
        return multiTenantManager.getCurrentTenantElastic().index(indexQuery, IndexCoordinates.of(multiTenantManager.getCurrentTenantQosIndex()));
    }
}
