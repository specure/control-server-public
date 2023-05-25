package com.specure.service.sah.impl;

import com.specure.common.enums.NetNeutralityTestType;
import com.specure.common.model.jpa.neutrality.NetNeutralitySetting;
import com.specure.common.repository.NetNeutralitySettingRepository;
import com.specure.mapper.sah.neutrality.NetNeutralityProcessorFactoryImpl;
import com.specure.model.elastic.neutrality.NetNeutralityResult;
import com.specure.repository.sah.elastic.NetNeutralityResultRepository;
import com.specure.request.sah.neutrality.result.NetNeutralityMeasurementResultRequest;
import com.specure.response.sah.neutrality.parameters.NetNeutralityTestParameterResponse;
import com.specure.response.sah.neutrality.result.NetNeutralityOverallTestResultResponse;
import com.specure.response.sah.neutrality.result.NetNeutralityResultResponse;
import com.specure.service.sah.NetNeutralityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class NetNeutralityServiceImpl implements NetNeutralityService {

    private final NetNeutralityResultRepository netNeutralityResultRepository;
    private final NetNeutralitySettingRepository netNeutralitySettingRepository;
    private final NetNeutralityProcessorFactoryImpl netNeutralityProcessorFactory;

    @Override
    public Map<NetNeutralityTestType, List<NetNeutralityTestParameterResponse>> getQosParameters() {
        return netNeutralitySettingRepository.findAll().stream()
                .filter(NetNeutralitySetting::isActive)
                .map(netNeutralityProcessorFactory::toNetNeutralityTestParameterResponse)
                .collect(Collectors.groupingBy(NetNeutralityTestParameterResponse::getType));
    }

    @Override
    public NetNeutralityOverallTestResultResponse saveNetNeutralityMeasurementResult(NetNeutralityMeasurementResultRequest netNeutralityMeasurementResultRequest) {
        List<NetNeutralityResult> elasticNetNeutralityResult = netNeutralityMeasurementResultRequest.getTestResults().stream()
                .map(x -> netNeutralityProcessorFactory.toElasticModel(x, netNeutralityMeasurementResultRequest))
                .collect(Collectors.toList());

        List<NetNeutralityResultResponse> netNeutralityResultResponses = netNeutralityResultRepository.saveAll(elasticNetNeutralityResult).stream()
                .map(netNeutralityProcessorFactory::toElasticResponse)
                .collect(Collectors.toList());

        return NetNeutralityOverallTestResultResponse.builder()
                .netNeutralityResultResponse(netNeutralityResultResponses)
                .build();
    }

    @Override
    public Page<NetNeutralityResultResponse> getNetNeutralityResultByClientUuid(String uuid, String openTestUuid, Pageable pageable) {
        return netNeutralityResultRepository.findAllByClientUuid(uuid, openTestUuid, pageable)
                .map(netNeutralityProcessorFactory::toElasticResponse);
    }
}
