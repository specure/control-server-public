package com.specure.repository.sah.impl;

import com.specure.common.enums.NetworkType;
import com.specure.common.exception.MeasurementNotFoundByUuidException;
import com.specure.common.model.elastic.BasicTest;
import com.specure.common.page.EmptyPage;
import com.google.common.collect.Lists;
import com.specure.multitenant.MultiTenantManager;
import com.specure.repository.BasicTestRepository;
import com.specure.request.mobile.MeasurementHistoryMobileRequest;
import com.specure.response.core.settings.HistorySettingsResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHitSupport;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;
import static org.elasticsearch.search.aggregations.AggregationBuilders.terms;

@Repository
@RequiredArgsConstructor
@Slf4j
public class BasicTestRepositoryImpl implements BasicTestRepository {

    private static final String TERM_QUERY_FILTER_FIELD = "clientUuid.keyword";
    private static final String TERM_QUERY_FILTER_FIELD_PROBE_PORT_TYPE = "typeOfProbePort.keyword";
    private static final String PROBE_ID_FIELD = "probeId";
    private static final String DATE_FIELD = "measurementDate";
    private static final String AD_HOC_CAMPAIGN_FIELD = "adHocCampaign";


    private static final String TERMS_PACKAGE_FIELD = "packageId.keyword";
    private static final String TERMS_PROBE_FIELD = "probeId.keyword";
    private static final String AD_HOC_CAMPAIGN_KEYWORD = "adHocCampaign.keyword";

    private static final String PACKAGE_AGG = "package";
    private static final String PROBE_AGG = "probe";

    private static final String AGGREGATION_NAME_DEVICES = "devices";
    private static final String AGGREGATION_NAME_NETWORK_TYPES = "networkTypes";
    private static final String TERM_DEVICE_KEYWORD = "device.keyword";
    private static final String TERM_NETWORK_TYPE_KEYWORD = "networkType.keyword";

    private static final String DOWNLOAD_FIELD = "download";
    private static final String UPLOAD_FIELD = "upload";
    private static final String DOWNLOAD_AGGREGATION = "aggDownload";
    private static final String UPLOAD_AGGREGATION = "aggUpload";
    private static final Double LOWEST_SPEED_CAP = 0.20;

    private static final String HISTOGRAM_AGGREGATION = "histogram";
    private static final String HOUR_FIELD = "graphHour";
    private static final int MIN_HOUR_BOUND = 0;
    private static final int MAX_HOUR_BOUND = 24;
    private static final int HOUR_INTERVAL = 1;
    private static final String AVG_AGGREGATION = "avg";
    public static final String COUNTRY_TERM_FIELD = "country";
    @Value("${elastic-index.scrollTimeInMillis}")
    private Long SCROLL_TIME_IN_MILLIS;

    private final MultiTenantManager multiTenantManager;

    @Override
    public BasicTest findByUUID(String uuid) {

        final var searchQuery = new NativeSearchQueryBuilder()
                .withQuery(termQuery("openTestUuid.keyword", uuid))
                .withSearchType(SearchType.DEFAULT)
                .build();

        return multiTenantManager
                .getCurrentTenantElastic()
                .search(searchQuery, BasicTest.class, IndexCoordinates.of(multiTenantManager.getCurrentTenantBasicIndex())).get()
                .map(SearchHit::getContent)
                .findAny()
                .orElseThrow(() -> new MeasurementNotFoundByUuidException(uuid));
    }

    @Override
    public String save(BasicTest basicTest) {
        IndexQuery indexQuery = new IndexQueryBuilder()
                .withObject(basicTest)
                .build();
        return multiTenantManager.getCurrentTenantElastic().index(indexQuery, IndexCoordinates.of(multiTenantManager.getCurrentTenantBasicIndex()));
    }

    @Override
    public Page<BasicTest> findByMeasurementHistoryMobileRequest(MeasurementHistoryMobileRequest request, Pageable pageable) {
        BoolQueryBuilder filteredQuery = boolQuery();
        filteredQuery.must(QueryBuilders.termQuery(TERM_QUERY_FILTER_FIELD, request.getClientUuid()));
        filteredQuery.must(QueryBuilders.rangeQuery(DATE_FIELD).from(new Date(1L)));

        if (!CollectionUtils.isEmpty(request.getNetworkTypes())) {
            filteredQuery.must(QueryBuilders.termsQuery(TERM_NETWORK_TYPE_KEYWORD, Arrays.stream(request.getNetworkTypes().toArray(String[]::new))
                    .map(NetworkType::fromCategoryValue).flatMap(Collection::stream).toArray()));
        }
        if (!CollectionUtils.isEmpty(request.getDevices())) {
            filteredQuery.must(QueryBuilders.termsQuery(TERM_DEVICE_KEYWORD, request.getDevices().toArray(String[]::new)));
        }
        final Query searchQuery = new NativeSearchQueryBuilder()
                .withQuery(filteredQuery)
                .withSearchType(SearchType.DEFAULT)
                .withPageable(pageable)
                .build();

        return SearchHitSupport.searchPageFor(multiTenantManager.getCurrentTenantElastic().search(searchQuery, BasicTest.class, IndexCoordinates.of(multiTenantManager.getCurrentTenantBasicIndex())), searchQuery.getPageable()).map(SearchHit::getContent);
    }

    @Override
    public Page<List<BasicTest>> findByMeasurementLoopUuidAggregatedHistoryMobileRequest(MeasurementHistoryMobileRequest request, Pageable pageable) {
        BoolQueryBuilder filteredQuery = boolQuery();
        filteredQuery.must(QueryBuilders.termQuery(TERM_QUERY_FILTER_FIELD, request.getClientUuid()));
        filteredQuery.must(QueryBuilders.rangeQuery(DATE_FIELD).from(new Date(1L)));

        if (!CollectionUtils.isEmpty(request.getNetworkTypes())) {
            filteredQuery.must(QueryBuilders.termsQuery(TERM_NETWORK_TYPE_KEYWORD, Arrays.stream(request.getNetworkTypes().toArray(String[]::new))
                    .map(NetworkType::fromCategoryValue).flatMap(Collection::stream).toArray()));
        }
        if (!CollectionUtils.isEmpty(request.getDevices())) {
            filteredQuery.must(QueryBuilders.termsQuery(TERM_DEVICE_KEYWORD, request.getDevices().toArray(String[]::new)));
        }

        final Query searchQuery = new NativeSearchQueryBuilder()
                .withQuery(filteredQuery)
                .withSearchType(SearchType.DEFAULT)
                .build();
        IndexCoordinates indexCoordinates = IndexCoordinates.of(multiTenantManager.getCurrentTenantBasicIndex());
        List<SearchHit<BasicTest>> basicTests = multiTenantManager
                .getCurrentTenantElastic()
                .search(searchQuery, BasicTest.class, indexCoordinates)
                .getSearchHits();
        return collectToLoopUuidPage(basicTests, pageable);
    }

    @Override
    public HistorySettingsResponse getDistinctDevicesAndNetworkTypesByClientUuid(String clientUuid) {
        BoolQueryBuilder filteredQuery = boolQuery();
        filteredQuery.must(QueryBuilders.termQuery(TERM_QUERY_FILTER_FIELD, clientUuid));

        final NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(filteredQuery)
                .withSearchType(SearchType.DEFAULT)
                .withPageable(EmptyPage.INSTANCE)
                .addAggregation(terms(AGGREGATION_NAME_DEVICES).field(TERM_DEVICE_KEYWORD))
                .addAggregation(terms(AGGREGATION_NAME_NETWORK_TYPES).field(TERM_NETWORK_TYPE_KEYWORD))
                .build();

        Aggregations aggregations = multiTenantManager.getCurrentTenantElastic().search(searchQuery, BasicTest.class, IndexCoordinates.of(multiTenantManager.getCurrentTenantBasicIndex())).getAggregations();

        List<String> networks = getKeyAsStringListFromAggregationsByAggregationName(aggregations, AGGREGATION_NAME_NETWORK_TYPES);
        List<String> devices = getKeyAsStringListFromAggregationsByAggregationName(aggregations, AGGREGATION_NAME_DEVICES);


        return HistorySettingsResponse.builder()
                .networks(networks.stream().map(NetworkType::valueOfSafely).map(NetworkType::getCategory)
                        .filter(l -> !l.equalsIgnoreCase(NetworkType.UNKNOWN.getCategory()))
                        .collect(Collectors.toList()))
                .devices(devices)
                .build();
    }

    private List<String> getKeyAsStringListFromAggregationsByAggregationName(Aggregations aggregations, String aggregationName) {
        Aggregation aggregation = aggregations.get(aggregationName);
        if (Objects.nonNull(aggregation)) {
            List<? extends Terms.Bucket> buckets = ((Terms) aggregation).getBuckets();
            if (buckets != null) {
                return buckets.stream()
                        .map(Terms.Bucket::getKeyAsString)
                        .collect(Collectors.toList());
            }
        }
        return Collections.emptyList();
    }

    private Page<List<BasicTest>> collectToLoopUuidPage(List<SearchHit<BasicTest>> basicTests, Pageable pageable) {
        Map<String, List<BasicTest>> groupedByLoopMode = basicTests.stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toMap(x -> Optional.ofNullable(x.getLoopModeUuid()).orElse(x.getOpenTestUuid()), Lists::newArrayList, (x1, x2) -> {
                    x1.addAll(x2);
                    x1.sort(Comparator.comparing(BasicTest::getTimestamp).reversed());
                    return x1;
                }));

        Comparator<List<BasicTest>> comparator = Comparator.comparing(x -> x.get(0).getTimestamp());
        List<List<BasicTest>> sortedLists = new ArrayList<>(groupedByLoopMode.values());
        sortedLists.sort(comparator.reversed());

        var listPartition = Lists.partition(sortedLists, pageable.getPageSize());
        if (pageable.getPageNumber() > listPartition.size() - 1) {
            return new PageImpl<>(Collections.emptyList(), pageable, sortedLists.size());
        }
        return new PageImpl<>(listPartition.get(pageable.getPageNumber()), pageable, sortedLists.size());
    }
}
