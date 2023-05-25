package com.specure.core.utils;

import com.specure.request.core.measurement.request.PingRequest;
import com.specure.core.TestConstants;
import com.specure.utils.core.MeasurementCalculatorUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;

@RunWith(SpringRunner.class)
public class MeasurementCalculatorUtilTest {

    @Test
    public void median_WhenCallSorted_ExpectFindCorrectMedian() {
        Long shortest = 1L;
        ArrayList<PingRequest> pingRequests = new ArrayList<>();
        pingRequests.add(getPing(1L));
        pingRequests.add(getPing(2L));
        pingRequests.add(getPing(3L));
        pingRequests.add(getPing(4L));
        pingRequests.add(getPing(5L));

        Long median = MeasurementCalculatorUtil.median(pingRequests, shortest);
        assert (median).equals(3L);
    }

    @Test
    public void median_WhenCallUnSorted_ExpectFindCorrectMedian() {
        Long shortest = 1L;
        ArrayList<PingRequest> pingRequests = new ArrayList<>();
        pingRequests.add(getPing(3L));
        pingRequests.add(getPing(2L));
        pingRequests.add(getPing(1L));
        pingRequests.add(getPing(5L));
        pingRequests.add(getPing(4L));

        Long median = MeasurementCalculatorUtil.median(pingRequests, shortest);
        assert (median).equals(3L);
    }

    @Test
    public void median_WhenCallUnSortedAndMultiple_ExpectFindCorrectMedian() {
        Long shortest = 1L;
        ArrayList<PingRequest> pingRequests = new ArrayList<>();
        pingRequests.add(getPing(1L));
        pingRequests.add(getPing(2L));
        pingRequests.add(getPing(3L));
        pingRequests.add(getPing(5L));
        pingRequests.add(getPing(6L));
        pingRequests.add(getPing(7L));

        Long median = MeasurementCalculatorUtil.median(pingRequests, shortest);
        assert (median).equals(4L);
    }

    @Test
    public void median_WhenCallWithZeroLengthArray_ExpectReturnSeconParameter() {
        Long shortest = 1L;
        ArrayList<PingRequest> pingRequests = new ArrayList<>();
        Long median = MeasurementCalculatorUtil.median(pingRequests, shortest);
        assert (median).equals(1L);
    }

    private PingRequest getPing(Long value) {
        return PingRequest.builder()
            .value(value)
            .valueServer(value)
            .timeNs(TestConstants.DEFAULT_PING.longValue())
            .build();
    }
}
