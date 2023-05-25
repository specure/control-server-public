package com.specure.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Condition {
    MORE(">"),
    LESS("<"),
    MORE_OR_EQUAL(">="),
    LESS_OR_EQUAL("<=");

    private final String value;
}
