package com.specure.repository.mobile.impl;

import com.specure.repository.mobile.impl.hardcode.QosTestDescConfig;
import com.specure.repository.mobile.QosTestDescRepository;
import com.specure.dto.sah.qos.QosTestDesc;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QosTestDescRepositoryImpl implements QosTestDescRepository {

    private final QosTestDescConfig qosTestDescConfig;

    @Override

    public List<QosTestDesc> findByKeysAndLocales(String lang, Collection<String> langs, Collection<String> keys) {
        return qosTestDescConfig.getQosTestDescList().stream()
                .filter(x->langs.contains(x.getLang()))
                .filter(x->keys.contains(x.getDescKey()))
                .collect(Collectors.toList());
    }
}
