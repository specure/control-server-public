package com.specure.mapper.mobile.impl;

import com.specure.common.model.elastic.SimpleSpeedDetail;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.specure.mapper.mobile.MobileGraphMapper;
import com.specure.sah.TestConstants;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
public class MobileGraphMapperImplTest {
    private MobileGraphMapper mobileGraphMapper;

    @Before
    public void setUp() {
        mobileGraphMapper = new MobileGraphMapperImpl();
    }

    @Test
    public void basicTestToGraphResponse_whenCommonData_expectMobileGraphResponse() throws JsonProcessingException {
        var request = getSpeedDetails();

        var response = mobileGraphMapper.basicTestToGraphResponse(request);
        assertEquals(TestConstants.DEFAULT_MOBILE_GRAPH_RESPONSE, response.toString());
    }

    private List<SimpleSpeedDetail> getSpeedDetails() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleSpeedDetail[] speedDetails = objectMapper.readValue(TestConstants.DEFAULT_SPEED_DETAILS_STRING, SimpleSpeedDetail[].class);
        return Arrays.asList(speedDetails);
    }
}
