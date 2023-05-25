package com.specure.request.core.measurement.qos.request;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@JsonTypeName("voip")
@EqualsAndHashCode(callSuper = false)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class VoipTestResultRequest extends TestResult {

    private int voipResultOutShortSeq;
    private int voipResultInSequenceError;
    private int voipObjectiveSampleRate;
    private int voipObjectiveBitsPerSample;
    private int voipResultOutLongSeq;
    private long voipResultOutMaxJitter;
    private String voipResultStatus;
    private int voipResultInNumPackets;
    private int voipResultOutSequenceError;
    private String voipResultJitter;
    private long voipResultInMaxJitter;
    private long voipResultInSkew;
    private long voipResultOutMaxDelta;
    private int voipObjectiveInPort;
    private int voipResultInShortSeq;
    private int voipObjectivePayload;
    private long voipResultInMaxDelta;
    private int voipObjectiveOutPort;
    private long voipResultOutSkew;
    private int voipResultInLongSeq;
    private String voipResultPacketLoss;

    private long voipResultOutMeanJitter;
    private long voipResultInMeanJitter;
    private long voipObjectiveDelay;
    private long voipObjectiveCallDuration;
    private long voipResultOutNumPackets;
}
