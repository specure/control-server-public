package com.specure.service.sah.impl;

import com.specure.common.constant.PropertyName;
import com.specure.common.enums.UserExperienceCategory;
import com.specure.common.model.elastic.BasicTest;
import com.specure.common.response.userexperience.UserExperienceParameter;
import com.specure.common.utils.calculator.QualityCalculator;
import com.specure.multitenant.MultiTenantManager;
import com.specure.sah.TestConstants;
import com.specure.service.sah.UserExperienceService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class UserExperienceServiceImplTest {


    @MockBean
    private QualityCalculator qualityCalculator;
    @MockBean
    private MultiTenantManager multiTenantManager;
    private UserExperienceService userExperienceService;

    @Before
    public void setUp() {
        userExperienceService = new UserExperienceServiceImpl(List.of(qualityCalculator), multiTenantManager);
    }

    @Test
    public void getBasicTestUserExperience_WhenCalled_ExpectCorrectMetrics() {
        List<UserExperienceParameter> parameters = List.of(
                UserExperienceParameter.builder()
                        .field(PropertyName.DOWNLOAD_METRIC_PARAMETER)
                        .value(TestConstants.DEFAULT_SPEED_DOWNLOAD.doubleValue())
                        .build(),
                UserExperienceParameter.builder()
                        .field(PropertyName.UPLOAD_METRIC_PARAMETER)
                        .value(TestConstants.DEFAULT_SPEED_UPLOAD.doubleValue())
                        .build(),
                UserExperienceParameter.builder()
                        .field(PropertyName.PING_METRIC_PARAMETER)
                        .value(TestConstants.DEFAULT_PING.doubleValue())
                        .build()
        );
        BasicTest basicTest = BasicTest.builder()
                .download(TestConstants.DEFAULT_SPEED_DOWNLOAD)
                .upload(TestConstants.DEFAULT_SPEED_UPLOAD)
                .ping(TestConstants.DEFAULT_PING)
                .build();
        when(qualityCalculator.calculate(parameters)).thenReturn(TestConstants.DEFAULT_USER_EXPERIENCE_QUALITY);
        when(qualityCalculator.getCategory()).thenReturn(UserExperienceCategory.EMAIL_MESSAGING);

        var response = userExperienceService.getBasicTestUserExperience(basicTest);
        assertEquals(1, response.size());
        assertEquals(UserExperienceCategory.EMAIL_MESSAGING, response.get(0).getCategory());
        assertEquals(TestConstants.DEFAULT_USER_EXPERIENCE_QUALITY, response.get(0).getQuality());
    }
}
