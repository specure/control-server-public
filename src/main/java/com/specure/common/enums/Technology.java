package com.specure.common.enums;

import lombok.Getter;

@Getter
public enum Technology {
    ADSL("ADSL", TechnologyType.FIXED),
    FIBER("Fiber", TechnologyType.FIXED),
    FOUR_G_LTE("4G LTE", TechnologyType.MOBILE),
    FIVE_G("5G", TechnologyType.MOBILE);

    private final String name;
    private final TechnologyType type;

    Technology(String name, TechnologyType type) {
        this.name = name;
        this.type = type;
    }
}
