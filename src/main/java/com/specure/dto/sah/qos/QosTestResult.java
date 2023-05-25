package com.specure.dto.sah.qos;

import com.specure.common.enums.TestType;
import lombok.*;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class QosTestResult {
    private Long id;

    private Long testUid;

    private Long qosTestObjectiveId;

    private Integer successCount;

    private Integer failureCount;

    private boolean implausible;

    private String result;

    private String testDescription;

    private String testSummary;

    private final Map<String, String> resultKeyMap = new HashMap<>();

    private QosTestObjective qosTestObjective;

    private TestType testType;
}
