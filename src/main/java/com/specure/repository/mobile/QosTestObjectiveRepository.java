package com.specure.repository.mobile;

import com.specure.dto.sah.qos.QosTestObjective;

import java.util.Collection;

public interface QosTestObjectiveRepository {

    QosTestObjective getOne(long qosTestUid);

    Collection<QosTestObjective> findAll();
}
