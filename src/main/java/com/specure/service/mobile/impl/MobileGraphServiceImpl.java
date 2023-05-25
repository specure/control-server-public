package com.specure.service.mobile.impl;

import com.specure.common.model.elastic.BasicTest;
import com.specure.common.model.elastic.SimpleSpeedDetail;
import com.specure.mapper.mobile.MobileGraphMapper;
import com.specure.multitenant.MultiTenantManager;
import com.specure.response.mobile.MobileGraphResponse;
import com.specure.service.mobile.MobileGraphService;
import com.specure.service.sah.BasicTestService;
import com.specure.service.sah.SpeedDetailService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class MobileGraphServiceImpl implements MobileGraphService {

    private final BasicTestService basicTestService;
    private final MobileGraphMapper mobileGraphMapper;
    private final MultiTenantManager multiTenantManager;
    private final SpeedDetailService speedDetailService;

    @Override
    public MobileGraphResponse getMobileGraph(String uuid) {
        log.debug("MobileGraphServiceImpl:getMobileGraph started with tenant = {}, uuid = {}", multiTenantManager.getCurrentTenant(), uuid);
        BasicTest basicTest;
        basicTest = basicTestService.getBasicTestByUUID(uuid);
        List<SimpleSpeedDetail> speedDetails = Optional.ofNullable(basicTest.getSpeedDetail())
                .orElseGet(() -> speedDetailService.getSpeedDetailsBy(uuid));
        MobileGraphResponse mobileGraphResponse = mobileGraphMapper.basicTestToGraphResponse(speedDetails);
        log.debug("MobileGraphServiceImpl:getMobileGraph finished with tenant = {}, mobileGraphResponse = {}", multiTenantManager, mobileGraphResponse);
        return mobileGraphResponse;
    }
}
