package com.specure.common.enums;

public enum UserExperienceQuality {

    EXCELLENT("excellent", 5),
    GOOD("good", 4),
    MODERATE("moderate", 3),
    POOR("poor", 2),
    BAD("bad", 1);

    private final String name;
    private final int score;

    UserExperienceQuality(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public String getName() {
        return name;
    }
}
