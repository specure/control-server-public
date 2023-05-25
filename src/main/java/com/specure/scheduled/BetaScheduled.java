package com.specure.scheduled;

import com.specure.common.model.elastic.BasicQosTest;
import com.specure.common.model.elastic.BasicTest;
import com.specure.common.model.elastic.ProbeKeepAliveElasticsearch;
import com.specure.multitenant.MultiTenantManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import static org.elasticsearch.index.query.QueryBuilders.boolQuery;

@Profile(value = "stage")
@Component
@RequiredArgsConstructor
@Slf4j
public class BetaScheduled {

    private final MultiTenantManager multiTenantManager;
    private final Clock clock;

    @Value("${elastic-index.keepAlive}")
    private String KEEP_ALIVE_INDEX;

    @Scheduled(cron = "0 0 0 * * *")
    public void cleanOldDocumentsInElasticsearch() {
        log.info("Clean up documents elasticsearch older 3 month started");
        Query deleteQuery = deleteQuery();
        multiTenantManager.getElasticsearchTenants()
                .forEach(x -> deleteOldDocument(x, deleteQuery));
        log.info("Clean up documents elasticsearch older 3 month finished");
    }

    private Query deleteQuery() {
        BoolQueryBuilder filteredQuery = boolQuery();
        LocalDate startPoint = LocalDate.now(clock).minus(3, ChronoUnit.MONTHS);
        filteredQuery.must(QueryBuilders.rangeQuery("timestamp").to(startPoint));
        return new NativeSearchQueryBuilder()
                .withQuery(filteredQuery)
                .build();
    }

    private void deleteOldDocument(String tenant, Query deleteQuery) {
        multiTenantManager.setCurrentTenant(tenant);
        IndexCoordinates basicTestIndexCoordinates = IndexCoordinates.of(multiTenantManager.getCurrentTenantBasicIndex());
        IndexCoordinates basicQosTestIndexCoordinates = IndexCoordinates.of(multiTenantManager.getCurrentTenantQosIndex());
        IndexCoordinates keepAliveIndexCoordinates = IndexCoordinates.of(KEEP_ALIVE_INDEX);

        runTryCatch(deleteQuery, BasicTest.class, basicTestIndexCoordinates);
        runTryCatch(deleteQuery, BasicQosTest.class, basicQosTestIndexCoordinates);
        runTryCatch(deleteQuery, ProbeKeepAliveElasticsearch.class, keepAliveIndexCoordinates);
    }

    private void runTryCatch(Query query, Class<?> clazz, IndexCoordinates index) {
        try {
            multiTenantManager.getCurrentTenantElastic().delete(query, clazz, index);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
