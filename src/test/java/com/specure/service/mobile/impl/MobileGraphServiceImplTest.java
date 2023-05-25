package com.specure.service.mobile.impl;

import com.specure.common.model.elastic.BasicTest;
import com.specure.common.model.elastic.SimpleSpeedDetail;
import com.specure.mapper.mobile.MobileGraphMapper;
import com.specure.multitenant.MultiTenantManager;
import com.specure.response.mobile.MobileGraphResponse;
import com.specure.sah.TestConstants;
import com.specure.service.mobile.MobileGraphService;
import com.specure.service.sah.BasicTestService;
import com.specure.service.sah.SpeedDetailService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class MobileGraphServiceImplTest {
    private MobileGraphService mobileGraphService;

    @MockBean
    private BasicTestService basicTestService;
    @MockBean
    private MobileGraphMapper mobileGraphMapper;
    @MockBean
    private MultiTenantManager multiTenantManager;
    @MockBean
    private SpeedDetailService speedDetailService;

    @Mock
    private BasicTest basicTest;
    @Mock
    private MobileGraphResponse mobileGraphResponse;
    @Mock
    private List<SimpleSpeedDetail> speedDetails;

    @Before
    public void setUp() {
        mobileGraphService = new MobileGraphServiceImpl(basicTestService, mobileGraphMapper, multiTenantManager, speedDetailService);
    }

    @Test
    public void getMobileGraph_whenCommonData_expectMobileGraphResponse() {
        when(basicTestService.getBasicTestByUUID(TestConstants.DEFAULT_UUID)).thenReturn(basicTest);
        when(basicTest.getSpeedDetail()).thenReturn(speedDetails);
        when(mobileGraphMapper.basicTestToGraphResponse(speedDetails)).thenReturn(mobileGraphResponse);

        var response = mobileGraphService.getMobileGraph(TestConstants.DEFAULT_UUID);

        assertEquals(mobileGraphResponse, response);
    }

    @Test
    public void getMobileGraph_whenBasicTestSpeedDetailsIsNull_expectMobileGraphResponse() {
        when(basicTestService.getBasicTestByUUID(TestConstants.DEFAULT_UUID)).thenReturn(basicTest);
        when(basicTest.getSpeedDetail()).thenReturn(null);
        when(speedDetailService.getSpeedDetailsBy(TestConstants.DEFAULT_UUID)).thenReturn(speedDetails);
        when(mobileGraphMapper.basicTestToGraphResponse(speedDetails)).thenReturn(mobileGraphResponse);

        var response = mobileGraphService.getMobileGraph(TestConstants.DEFAULT_UUID);

        assertEquals(mobileGraphResponse, response);
    }
}
