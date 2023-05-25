package com.specure.repository.mobile;

import com.specure.dto.sah.qos.QosTestDesc;

import java.util.Collection;
import java.util.List;

public interface QosTestDescRepository {

    List<QosTestDesc> findByKeysAndLocales(String lang, Collection<String> langs, Collection<String> keys);
}
