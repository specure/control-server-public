package com.specure.mapper.core;

import com.specure.common.model.jpa.qos.PathElementEntriesResult;
import com.specure.common.model.jpa.qos.TracerouteTestResult;
import com.specure.request.core.measurement.qos.request.PathElementEntriesRequest;
import com.specure.request.core.measurement.qos.request.TracerouteTestResultRequest;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class TracerouteTestResultMapperTest {

    @InjectMocks
    private TracerouteTestResultMapper mapper = new TracerouteTestResultMapperImpl();

    @Test
    void testTracerouteTestResultRequestToWebsiteTestResult() {
        // Given
        TracerouteTestResultRequest request = new TracerouteTestResultRequest();
        request.setTracerouteObjectiveHost("example.com");
        request.setTracerouteResultStatus("success");
        request.setTracerouteResultDuration(1000L);
        request.setTracerouteObjectiveTimeout(5000L);
        request.setTracerouteObjectiveMaxHops(30);
        request.setTracerouteResultHops(10);
        request.setQosTestUid(123L);
        request.setDurationNs(1000000000L);
        request.setStartTimeNs(11L);

        PathElementEntriesRequest pathElement = new PathElementEntriesRequest();
        pathElement.setTime(1);
        pathElement.setHost("192.0.2.1");
        List<PathElementEntriesRequest> pathElementList = new ArrayList<>();
        pathElementList.add(pathElement);
        request.setTracerouteResultDetails(pathElementList);

        // When
        TracerouteTestResult result = mapper.tracerouteTestResultRequestToWebsiteTestResult(request);

        // Then
        assertThat(result.getQosTestUid()).isEqualTo(123L);
        assertThat(result.getDurationNs()).isEqualTo(1000000000L);
        assertThat(result.getStartTimeNs()).isEqualTo(11L);
        assertThat(result.getTracerouteObjectiveHost()).isEqualTo("example.com");
        assertThat(result.getTracerouteResultStatus()).isEqualTo("success");
        assertThat(result.getTracerouteResultDuration()).isEqualTo(1000L);
        assertThat(result.getTracerouteObjectiveTimeout()).isEqualTo(5000L);
        assertThat(result.getTracerouteObjectiveMaxHops()).isEqualTo(30);
        assertThat(result.getTracerouteResultHops()).isEqualTo(10);

        assertThat(result.getTracerouteResultDetails()).containsExactly(new PathElementEntriesResult(null, new Timestamp(1), "192.0.2.1", null));
    }
}
