package com.specure.response.core.measurement.qos.response;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Builder
@Data
@Getter
@Setter
public class Jitter {
    private String concurrency_group;
    private String in_port;
    private String server_port;
    private String qos_test_uid;
    private String server_addr;
    private String timeout;
    private String call_duration;
    private String out_port;
}
