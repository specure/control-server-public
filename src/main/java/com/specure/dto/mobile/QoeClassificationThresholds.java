package com.specure.dto.mobile;


import com.specure.enums.QoeCategory;
import com.specure.enums.QoeCriteria;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Builder
@Getter
public class QoeClassificationThresholds {

    private final QoeCategory qoeCategory;

    private final Map<QoeCriteria, Long[]> thresholds;
}
