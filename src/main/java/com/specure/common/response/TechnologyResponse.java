package com.specure.common.response;

import com.specure.common.enums.Technology;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TechnologyResponse {
    private final Technology technology;
    private final String name;
    private final String type;
}
