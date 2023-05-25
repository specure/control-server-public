package com.specure.repository.sah;

import com.specure.common.model.elastic.BasicQosTest;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface BasicQosTestRepository {
    String save(BasicQosTest basicTest);

    List<BasicQosTest> findByBasicTestUuid(Collection<String> uuid);

    Optional<BasicQosTest> findByBasicTestUuid(String uuid);
}
