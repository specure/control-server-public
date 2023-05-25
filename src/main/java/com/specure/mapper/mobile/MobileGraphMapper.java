package com.specure.mapper.mobile;

import com.specure.common.model.elastic.SimpleSpeedDetail;
import com.specure.response.mobile.MobileGraphResponse;

import java.util.List;

public interface MobileGraphMapper {

    MobileGraphResponse basicTestToGraphResponse(List<SimpleSpeedDetail> speedDetails);
}
