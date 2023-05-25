package com.specure.service.sah;

import com.specure.common.model.elastic.BasicTest;
import com.specure.common.model.jpa.Measurement;
import com.specure.request.mobile.MeasurementHistoryMobileRequest;
import com.specure.response.core.MeasurementHistoryResponse;
import com.specure.response.core.settings.HistorySettingsResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface BasicTestService {
    BasicTest getBasicTestByUUID(String uuid);

    MeasurementHistoryResponse getMeasurementDetailByUuidFromElasticSearch(String uuid) throws InterruptedException;

    Page<BasicTest> getFilteredBasicTestsByMeasurementHistoryMobileRequest(Pageable pageable, MeasurementHistoryMobileRequest request);

    Page<List<BasicTest>> getFilteredLoopUuidAggregatedBasicTestsByMeasurementHistoryMobileRequest(Pageable pageable, MeasurementHistoryMobileRequest request);

    void saveMeasurementToElastic(Measurement measurement);

    void saveMeasurementMobileToElastic(Measurement measurement);

    HistorySettingsResponse getHistoryResponseByClientUuid(UUID uuid);
}
