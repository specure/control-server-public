package com.specure.repository.sah.elastic;

import com.specure.model.elastic.neutrality.NetNeutralityResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NetNeutralityResultRepository {

    List<NetNeutralityResult> saveAll(List<NetNeutralityResult> elasticNeutralityQosTest);

    Page<NetNeutralityResult> findAllByClientUuid(String uuid, String openTestUuid, Pageable pageable);
}
