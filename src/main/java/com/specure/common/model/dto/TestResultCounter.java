package com.specure.common.model.dto;

import com.specure.common.enums.TestType;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@EqualsAndHashCode
@ToString
public class TestResultCounter {

    private final Integer successCount;

    private final Integer totalCount;

    private final TestType testType;
}
