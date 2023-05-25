package com.specure.common.model.dto.qos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.specure.common.utils.hstoreparser.annotation.HstoreKey;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class JitterResult extends AbstractResult<JitterResult> {

    @JsonProperty("jitter_objective_in_port")
    @HstoreKey("jitter_objective_in_port")
    Object inPort;

    @JsonProperty("jitter_objective_out_port")
    @HstoreKey("jitter_objective_out_port")
    Object outPort;

    @JsonProperty("jitter_objective_call_duration")
    @HstoreKey("jitter_objective_call_duration")
    Object callDuration;

    @JsonProperty("jitter_objective_bits_per_sample")
    @HstoreKey("jitter_objective_bits_per_sample")
    Object bitsPerSample;

    @JsonProperty("jitter_objective_sample_rate")
    @HstoreKey("jitter_objective_sample_rate")
    Object sampleRate;

    @JsonProperty("jitter_objective_delay")
    @HstoreKey("jitter_objective_delay")
    Object delay;

    @JsonProperty("jitter_objective_timeout")
    @HstoreKey("jitter_objective_timeout")
    Object timeout;

    @JsonProperty("jitter_objective_payload")
    @HstoreKey("jitter_objective_payload")
    Object payload;

    @JsonProperty("jitter_result_in_max_jitter")
    @HstoreKey("jitter_result_in_max_jitter")
    Object maxJitterIn;

    @JsonProperty("jitter_result_in_mean_jitter")
    @HstoreKey("jitter_result_in_mean_jitter")
    Object minJitterIn;

    @JsonProperty("jitter_result_in_max_delta")
    @HstoreKey("jitter_result_in_max_delta")
    Object maxDeltaIn;

    @JsonProperty("jitter_result_in_num_packets")
    @HstoreKey("jitter_result_in_num_packets")
    Object numPacketsIn;

    @JsonProperty("jitter_result_in_skew")
    @HstoreKey("jitter_result_in_skew")
    Object skewIn;

    @JsonProperty("jitter_result_out_max_jitter")
    @HstoreKey("jitter_result_out_max_jitter")
    Object maxJitterOut;

    @JsonProperty("jitter_result_out_mean_jitter")
    @HstoreKey("jitter_result_out_mean_jitter")
    Object minJitterOut;

    @JsonProperty("jitter_result_out_max_delta")
    @HstoreKey("jitter_result_out_max_delta")
    Object maxDeltaOut;

    @JsonProperty("jitter_result_out_num_packets")
    @HstoreKey("jitter_result_out_num_packets")
    Object numPacketsOut;

    @JsonProperty("jitter_result_out_skew")
    @HstoreKey("jitter_result_out_skew")
    Object skewOut;

    @JsonProperty("jitter_result_in_sequence_error")
    @HstoreKey("jitter_result_in_sequence_error")
    Object seqErrorsIn;

    @JsonProperty("jitter_result_out_sequence_error")
    @HstoreKey("jitter_result_out_sequence_error")
    Object seqErrorsOut;

    @JsonProperty("jitter_result_in_short_seq")
    @HstoreKey("jitter_result_in_short_seq")
    Object shortSequenceIn;

    @JsonProperty("jitter_result_out_short_seq")
    @HstoreKey("jitter_result_out_short_seq")
    Object shortSequenceOut;

    @JsonProperty("jitter_result_in_long_seq")
    @HstoreKey("jitter_result_in_long_seq")
    Object longSequenceIn;

    @JsonProperty("jitter_result_out_long_seq")
    @HstoreKey("jitter_result_out_long_seq")
    Object longSequenceOut;

    @JsonProperty("jitter_result_status")
    @HstoreKey("jitter_result_status")
    String status;

    @Override
    public String toString() {
        return "JitterResult [inPort=" + inPort + ", outPort=" + outPort
                + ", callDuration=" + callDuration + ", bitsPerSample="
                + bitsPerSample + ", sampleRate=" + sampleRate + ", delay="
                + delay + ", timeout=" + timeout + ", payload=" + payload
                + ", maxJitterIn=" + maxJitterIn + ", minJitterIn="
                + minJitterIn + ", maxDeltaIn=" + maxDeltaIn
                + ", numPacketsIn=" + numPacketsIn + ", skewIn=" + skewIn
                + ", maxJitterOut=" + maxJitterOut + ", minJitterOut="
                + minJitterOut + ", maxDeltaOut=" + maxDeltaOut
                + ", numPacketsOut=" + numPacketsOut + ", skewOut=" + skewOut
                + ", seqErrorsIn=" + seqErrorsIn + ", seqErrorsOut="
                + seqErrorsOut + ", shortSequenceIn=" + shortSequenceIn
                + ", shortSequenceOut=" + shortSequenceOut
                + ", longSequenceIn=" + longSequenceIn + ", longSequenceOut="
                + longSequenceOut + "]";
    }
}
