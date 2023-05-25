package com.specure.common.enums;

import lombok.Getter;

@Getter
public enum ProbePurpose {
    CAMPAIGN("Campaign"),
    SITE("Site");

    private final String name;

    ProbePurpose(String name) {
        this.name = name;
    }
    public static ProbePurpose fromString(String purpose) {
        for (ProbePurpose b : ProbePurpose.values()) {
            if (b.getName().equals(purpose)) {
                return b;
            }
        }
        return ProbePurpose.SITE; // default
    }
}
