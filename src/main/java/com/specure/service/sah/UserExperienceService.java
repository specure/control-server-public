package com.specure.service.sah;

import com.specure.common.model.elastic.BasicTest;
import com.specure.common.response.userexperience.UserExperienceMetric;

import java.util.List;

public interface UserExperienceService {

    List<UserExperienceMetric> getBasicTestUserExperience(BasicTest basicTest);
}
