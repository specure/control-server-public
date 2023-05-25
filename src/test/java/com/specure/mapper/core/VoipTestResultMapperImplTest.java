package com.specure.mapper.core;

import com.specure.common.model.jpa.qos.VoipTestResult;
import com.specure.request.core.measurement.qos.request.VoipTestResultRequest;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class VoipTestResultMapperTest {

    private VoipTestResultMapper mapper = Mappers.getMapper(VoipTestResultMapper.class);

    @Test
    void voipTestResultRequestToVoipTestResult_shouldMapFields() {
        // Arrange
        VoipTestResultRequest request = new VoipTestResultRequest();
        request.setQosTestUid(123L);
        request.setDurationNs(1000000000L);
        request.setStartTimeNs(11L);
        request.setVoipResultOutShortSeq(1);
        request.setVoipResultInSequenceError(2);
        request.setVoipObjectiveSampleRate(3);
        request.setVoipObjectiveBitsPerSample(4);
        request.setVoipResultOutLongSeq(5);
        request.setVoipResultOutMaxJitter(6L);
        request.setVoipResultStatus("success");
        request.setVoipResultInNumPackets(7);
        request.setVoipResultOutSequenceError(8);
        request.setVoipResultJitter("9/10");
        request.setVoipResultInMaxJitter(11L);
        request.setVoipResultInSkew(12L);
        request.setVoipResultOutMaxDelta(13L);
        request.setVoipObjectiveInPort(14);
        request.setVoipResultInShortSeq(15);
        request.setVoipObjectivePayload(16);
        request.setVoipResultInMaxDelta(17L);
        request.setVoipObjectiveOutPort(18);
        request.setVoipResultOutSkew(19L);
        request.setVoipResultInLongSeq(20);
        request.setVoipResultPacketLoss("1.5%");
        request.setVoipResultOutMeanJitter(21L);
        request.setVoipResultInMeanJitter(22L);
        request.setVoipObjectiveDelay(23L);
        request.setVoipObjectiveCallDuration(24L);
        request.setVoipResultOutNumPackets(25L);

        // Act
        VoipTestResult result = mapper.voipTestResultRequestToVoipTestResult(request);

        // Assert
        assertNotNull(result);
        assertEquals(request.getVoipResultOutShortSeq(), result.getVoipResultOutShortSeq());
        assertEquals(request.getVoipResultInSequenceError(), result.getVoipResultInSequenceError());
        assertEquals(request.getVoipObjectiveSampleRate(), result.getVoipObjectiveSampleRate());
        assertEquals(request.getVoipObjectiveBitsPerSample(), result.getVoipObjectiveBitsPerSample());
        assertEquals(request.getVoipResultOutLongSeq(), result.getVoipResultOutLongSeq());
        assertEquals(request.getVoipResultOutMaxJitter(), result.getVoipResultOutMaxJitter());
        assertEquals(request.getVoipResultStatus(), result.getVoipResultStatus());
        assertEquals(request.getVoipResultInNumPackets(), result.getVoipResultInNumPackets());
        assertEquals(request.getVoipResultOutSequenceError(), result.getVoipResultOutSequenceError());
        assertEquals(request.getVoipResultJitter(), result.getVoipResultJitter());
        assertEquals(request.getVoipResultInMaxJitter(), result.getVoipResultInMaxJitter());
        assertEquals(request.getVoipResultInSkew(), result.getVoipResultInSkew());
        assertEquals(request.getVoipResultOutMaxDelta(), result.getVoipResultOutMaxDelta());
        assertEquals(request.getVoipObjectiveInPort(), result.getVoipObjectiveInPort());
        assertEquals(request.getVoipResultInShortSeq(), result.getVoipResultInShortSeq());
        assertEquals(request.getVoipObjectivePayload(), result.getVoipObjectivePayload());
        assertEquals(request.getVoipResultInMaxDelta(), result.getVoipResultInMaxDelta());
        assertEquals(request.getVoipObjectiveOutPort(), result.getVoipObjectiveOutPort());
        assertEquals(request.getVoipResultOutSkew(), result.getVoipResultOutSkew());
        assertEquals(request.getVoipResultInLongSeq(), result.getVoipResultInLongSeq());
        assertEquals(request.getVoipResultPacketLoss(), result.getVoipResultPacketLoss());
        assertEquals(request.getVoipResultOutMeanJitter(), result.getVoipResultOutMeanJitter());
        assertEquals(request.getVoipResultInMeanJitter(), result.getVoipResultInMeanJitter());
        assertEquals(request.getVoipObjectiveDelay(), result.getVoipObjectiveDelay());
        assertEquals(request.getVoipObjectiveCallDuration(), result.getVoipObjectiveCallDuration());
        assertEquals(request.getVoipResultOutNumPackets(), result.getVoipResultOutNumPackets());
        assertEquals(request.getQosTestUid(),result.getQosTestUid());
        assertEquals(request.getDurationNs(),result.getDurationNs());
        assertEquals(request.getStartTimeNs(),result.getStartTimeNs());
    }
}
