package com.specure.utils.sah.region;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NameCodeRelation {
    private String name;
    private String code;
}
