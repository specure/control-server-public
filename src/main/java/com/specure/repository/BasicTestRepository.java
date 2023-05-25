package com.specure.repository;

import com.specure.common.model.elastic.BasicTest;
import com.specure.request.mobile.MeasurementHistoryMobileRequest;
import com.specure.response.core.settings.HistorySettingsResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BasicTestRepository {

    BasicTest findByUUID(String uuid);

    String save(BasicTest basicTest);

    Page<BasicTest> findByMeasurementHistoryMobileRequest(MeasurementHistoryMobileRequest request, Pageable pageable);

    Page<List<BasicTest>> findByMeasurementLoopUuidAggregatedHistoryMobileRequest(MeasurementHistoryMobileRequest request, Pageable pageable);

    HistorySettingsResponse getDistinctDevicesAndNetworkTypesByClientUuid(String toString);
}
