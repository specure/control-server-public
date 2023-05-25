package com.specure.dto.sah.qos;

import com.specure.common.model.dto.qos.QosParams;
import com.specure.common.enums.TestType;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class QosTestObjective {

    private Integer id;
    private TestType testType;
    private QosParams param;
    private String results;
    private Integer concurrencyGroup;
    private String testDescription;
    private String testSummary;
    private String testServerAddress;
    private String testServerPort;
}
