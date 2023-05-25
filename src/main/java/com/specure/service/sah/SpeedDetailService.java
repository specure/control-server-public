package com.specure.service.sah;

import com.specure.common.model.elastic.SimpleSpeedDetail;
import com.specure.common.model.jpa.SpeedDetail;

import java.util.List;

public interface SpeedDetailService {

    List<SimpleSpeedDetail> getSpeedDetailsBy(String openTestUuid);

    void saveSpeedDetailsToCache(String openTestUuid, List<SpeedDetail> speedDetails);
}
