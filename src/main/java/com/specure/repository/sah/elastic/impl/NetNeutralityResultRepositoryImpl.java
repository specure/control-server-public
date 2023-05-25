package com.specure.repository.sah.elastic.impl;

import com.specure.model.elastic.neutrality.NetNeutralityResult;
import com.specure.multitenant.MultiTenantManager;
import com.specure.repository.sah.elastic.NetNeutralityResultRepository;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHitSupport;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.boolQuery;

@Component
@RequiredArgsConstructor
public class NetNeutralityResultRepositoryImpl implements NetNeutralityResultRepository {

    private final MultiTenantManager multiTenantManager;

    @Override
    public List<NetNeutralityResult> saveAll(List<NetNeutralityResult> elasticNeutralityQosTest) {
        return elasticNeutralityQosTest.stream()
                .map(this::saveOne)
                .collect(Collectors.toList());
    }

    @Override
    public Page<NetNeutralityResult> findAllByClientUuid(String clientUuid, String openTestUuid, Pageable pageable) {
        BoolQueryBuilder filteredQuery = boolQuery();
        filteredQuery.must(QueryBuilders.termQuery("clientUuid.keyword", clientUuid));
        if (Objects.nonNull(openTestUuid)) {
            filteredQuery.must(QueryBuilders.termQuery("openTestUuid.keyword", openTestUuid));
        }
        final Query searchQuery = new NativeSearchQueryBuilder()
                .withQuery(filteredQuery)
                .withSearchType(SearchType.DEFAULT)
                .withPageable(pageable)
                .build();
        return SearchHitSupport.searchPageFor(multiTenantManager.getCurrentTenantElastic().search(searchQuery, NetNeutralityResult.class, IndexCoordinates.of(multiTenantManager.getCurrentTenantNetNeutralityIndex())), searchQuery.getPageable()).map(SearchHit::getContent);

    }

    private NetNeutralityResult saveOne(NetNeutralityResult neutralityQosTest) {
        IndexQuery indexQuery = new IndexQueryBuilder()
                .withObject(neutralityQosTest)
                .build();
        String id = multiTenantManager.getCurrentTenantElastic().index(indexQuery, IndexCoordinates.of(multiTenantManager.getCurrentTenantNetNeutralityIndex()));

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        Query query = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.idsQuery().addIds(id))
                .build();
        return multiTenantManager.getCurrentTenantElastic().searchOne(query, NetNeutralityResult.class, IndexCoordinates.of(multiTenantManager.getCurrentTenantNetNeutralityIndex()))
                .getContent();
    }
}
