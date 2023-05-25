package com.specure.common.model.dto;

import com.specure.common.enums.UserExperienceQuality;

import java.util.Comparator;

public class QualityComparator implements Comparator<UserExperienceQuality> {
    public int compare(UserExperienceQuality first, UserExperienceQuality second) {
        return first.getScore() - second.getScore();
    }
}
