package com.specure.mapper.mobile;

import com.specure.dto.sah.qos.QosTestObjective;
import com.specure.response.mobile.MobileQosParamsResponse;

public interface MobileQosTestObjectiveMapper {

    MobileQosParamsResponse qosTestObjectiveToQosParamsResponse(QosTestObjective qosTestObjective);
}
