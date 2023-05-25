package com.specure.common.enums;

import lombok.Getter;

@Getter
public enum PortType {
    FIXED("port", 3),
    MOBILE("sim", 1);

    private final String name;
    private final int initialNumber;

    PortType(String name, int initialNumber) {
        this.name = name;
        this.initialNumber = initialNumber;
    }
}
