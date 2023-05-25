package com.specure.common.model.jpa.qos;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;

@Entity
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class VoipTestResult extends QosResult {

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
