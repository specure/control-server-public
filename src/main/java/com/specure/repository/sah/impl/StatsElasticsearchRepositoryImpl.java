package com.specure.repository.sah.impl;

import com.specure.common.enums.NetworkType;
import com.specure.common.enums.Platform;
import com.specure.common.model.elastic.BasicTest;
import com.specure.common.page.EmptyPage;
import com.specure.constant.ErrorMessage;
import com.specure.dto.sah.NationalTableParams;
import com.specure.exception.GeoShapeNotFoundException;
import com.specure.model.dto.NationalTable;
import com.specure.model.dto.StatsByOperator;
import com.specure.model.elastic.GeoShape;
import com.specure.multitenant.MultiTenantManager;
import com.specure.repository.sah.StatsElasticsearchRepository;
import com.specure.service.sah.NationalOperatorService;
import lombok.AllArgsConstructor;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.ParsedAvg;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.time.Clock;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.search.aggregations.AggregationBuilders.terms;

@AllArgsConstructor
@Repository
public class StatsElasticsearchRepositoryImpl implements StatsElasticsearchRepository {
    private static final String OPERATOR_AGGREGATION_NAME = "provider";
    private static final String ISP_RAW_ID_FIELD_KEYWORD = "ispRawId.keyword";
    private static final String ISP_NAME_FIELD_KEYWORD = "ispName.keyword";

    private static final String AVG_DOWNLOAD = "download";
    private static final String AVG_UPLOAD = "upload";
    private static final String AVG_PING = "ping";

    private static final String DOWNLOAD_FIELD = "download";
    private static final String UPLOAD_FIELD = "upload";
    private static final String PING_FIELD = "ping";
    private static final String PLATFORM_FIELD = "platform.keyword";
    private static final String IMPLAUSIBLE_FIELD = "implausible.keyword";
    private static final String NETWORK_TYPE_FIELD = "networkType.keyword";
    private static final int MAX_BUCKET_SIZE = 50;
    private static final String GEO_SHAPE_INDEX = "shape";
    private static final String GEO_SHAPE_MUNICIPALITY_CODE_KEYWORD = "ADM2_PCODE.keyword";

    private final MultiTenantManager multiTenantManager;
    private final Clock clock;
    private final NationalOperatorService nationalOperatorService;

    @Override
    public NationalTable getNationalTable(NationalTableParams nationalTableParams, String municipalityCode) {
        BoolQueryBuilder filteredQuery = boolQuery();
        LocalDate startPoint = LocalDate.now(clock).minus(6, ChronoUnit.MONTHS);

        filteredQuery.must(QueryBuilders.rangeQuery("timestamp").from(startPoint));
        filteredQuery.must(QueryBuilders.termsQuery(PLATFORM_FIELD, List.of(Platform.ANDROID.name(), Platform.IOS.name())));
        filteredQuery.must(QueryBuilders.existsQuery(ISP_NAME_FIELD_KEYWORD));
        List<String> nationalOperators = getNationalOperators(nationalTableParams.isMno());
        if (!CollectionUtils.isEmpty(nationalOperators)) {
            filteredQuery.must(QueryBuilders.termsQuery(ISP_NAME_FIELD_KEYWORD, nationalOperators));
        }

        filteredQuery.mustNot(QueryBuilders.termQuery(IMPLAUSIBLE_FIELD, "true"));
        filteredQuery.mustNot(QueryBuilders.termQuery(ISP_RAW_ID_FIELD_KEYWORD, "unknown"));

        if (!nationalTableParams.getNetworkTypes().isEmpty()) {
            var terms = QueryBuilders.termsQuery(NETWORK_TYPE_FIELD, nationalTableParams.getNetworkTypes().stream().map(NetworkType::name).collect(Collectors.toList()));
            filteredQuery.must(terms);
        }
        if (Objects.nonNull(municipalityCode)) {
            String municipalityName = getMunicipalityNameByCode(municipalityCode);
            var municipalityTerm = QueryBuilders.termsQuery("municipality.keyword", municipalityName);
            filteredQuery.must(municipalityTerm);
        }
        final Query searchQuery = new NativeSearchQueryBuilder()
                .withQuery(filteredQuery)
                .withSearchType(SearchType.DEFAULT)
                .addAggregation(buildAggregationStatsByProvider())
                .addAggregation(AggregationBuilders.avg(AVG_UPLOAD).field(UPLOAD_FIELD))
                .addAggregation(AggregationBuilders.avg(AVG_DOWNLOAD).field(DOWNLOAD_FIELD))
                .addAggregation(AggregationBuilders.avg(AVG_PING).field(PING_FIELD))
                .withPageable(EmptyPage.INSTANCE)
                .build();

        return getNationalTableFromQuery(multiTenantManager.getCurrentTenantElastic().search(searchQuery, BasicTest.class, IndexCoordinates.of(multiTenantManager.getCurrentTenantBasicIndex())));
    }

    private List<String> getNationalOperators(boolean isMno) {
        final String currentTenant = multiTenantManager.getCurrentTenant();
        return nationalOperatorService.getNationalOperatorNames(currentTenant, isMno).stream()
                .distinct()
                .collect(Collectors.toList());
    }

    private String getMunicipalityNameByCode(String code) {
        BoolQueryBuilder filteredQuery = boolQuery();
        filteredQuery.must(QueryBuilders.termsQuery(GEO_SHAPE_MUNICIPALITY_CODE_KEYWORD, code));
        Query searchQuery = new NativeSearchQueryBuilder()
                .withQuery(filteredQuery)
                .withSearchType(SearchType.DEFAULT)
                .build();

        return Optional.ofNullable(multiTenantManager.getCurrentTenantElastic().searchOne(searchQuery, GeoShape.class, IndexCoordinates.of(GEO_SHAPE_INDEX)))
                .map(SearchHit::getContent)
                .map(GeoShape::getMunicipalitySq)
                .orElseThrow(() -> new GeoShapeNotFoundException(String.format(ErrorMessage.GEO_SHAPE_NOT_FOUND_BY_CODE, code)));
    }

    private NationalTable getNationalTableFromQuery(SearchHits<BasicTest> q) {
        ParsedAvg download = q.getAggregations().get(AVG_DOWNLOAD);
        ParsedAvg upload = q.getAggregations().get(AVG_UPLOAD);
        ParsedAvg ping = q.getAggregations().get(AVG_PING);

        ParsedTerms providers = q.getAggregations().get(OPERATOR_AGGREGATION_NAME);
        List<StatsByOperator> statsByOperators = providers.getBuckets()
                .stream()
                .map(this::extrudeProviderDataFromBucket)
                .collect(Collectors.toList());

        Long total = statsByOperators
                .stream()
                .map(StatsByOperator::getMeasurements)
                .reduce(0L, Long::sum);

        return NationalTable.builder()
                .averageLatency(ping.getValue())
                .averageDownload(download.getValue())
                .averageUpload(upload.getValue())
                .statsByOperator(statsByOperators)
                .allMeasurements(total)
                .build();
    }

    private TermsAggregationBuilder buildAggregationStatsByProvider() {
        return terms(OPERATOR_AGGREGATION_NAME)
                .field(ISP_NAME_FIELD_KEYWORD)
                .size(MAX_BUCKET_SIZE)
                .order(List.of(BucketOrder.key(true)))
                .subAggregation(AggregationBuilders.avg(AVG_UPLOAD).field(UPLOAD_FIELD))
                .subAggregation(AggregationBuilders.avg(AVG_DOWNLOAD).field(DOWNLOAD_FIELD))
                .subAggregation(AggregationBuilders.avg(AVG_PING).field(PING_FIELD));

    }

    private StatsByOperator extrudeProviderDataFromBucket(Terms.Bucket bucket) {
        var name = bucket.getKey().toString();
        double avgDownload = ((ParsedAvg) bucket.getAggregations().asMap().get(AVG_DOWNLOAD)).getValue();
        double avgUpload = ((ParsedAvg) bucket.getAggregations().asMap().get(AVG_UPLOAD)).getValue();
        double avgPing = ((ParsedAvg) bucket.getAggregations().asMap().get(AVG_PING)).getValue();
        return StatsByOperator.builder()
                .operatorName(name)
                .download(avgDownload)
                .upload(avgUpload)
                .latency(avgPing)
                .measurements(bucket.getDocCount())
                .build();
    }
}
