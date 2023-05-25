package com.specure.repository.mobile.impl;

import com.specure.repository.mobile.impl.hardcode.QosTestObjectiveConfig;
import com.specure.repository.mobile.QosTestObjectiveRepository;
import com.specure.dto.sah.qos.QosTestObjective;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class QosTestObjectiveRepositoryImpl implements QosTestObjectiveRepository {

    private final QosTestObjectiveConfig qosTestObjectiveConfig;

    @Override
    public QosTestObjective getOne(long qosTestUid) {
        return qosTestObjectiveConfig.getQosTestObjectiveMap().get(Long.valueOf(qosTestUid).intValue());
    }

    @Override
    public Collection<QosTestObjective> findAll() {
        return qosTestObjectiveConfig.getQosTestObjectiveMap().values();
    }
}
