package com.specure.service.admin.impl;

import com.specure.common.model.jpa.MobileModel;
import com.specure.common.repository.MobileModelRepository;
import com.specure.sah.TestConstants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class MobileModelServiceImplTest {

    @Mock
    private MobileModelRepository mobileModelRepository;
    @InjectMocks
    private MobileModelServiceImpl mobileModelService;

    @Mock
    private MobileModel mobileModel;
    @Captor
    private ArgumentCaptor<MobileModel> mobileModelCaptor;

    @Test
    void getMarketingNameByModel_whenMobileModelExists_expectMarketingName() {
        when(mobileModelRepository.findMobileModelByModel(TestConstants.MobileModel.MODEL)).thenReturn(Optional.of(mobileModel));

        var result = mobileModelService.getAndSaveNewIfNotExistMobileModelByModel(TestConstants.MobileModel.MODEL);

        assertEquals(Optional.of(mobileModel), result);
    }

    @Test
    void getMarketingNameByModel_whenMobileModelNotExists_expectNullAndSavedModel() {
        var expectedSavedMobileModel = new MobileModel(null, null, TestConstants.MobileModel.MODEL, null, null);
        when(mobileModelRepository.save(any())).thenReturn(mobileModel);

        var result = mobileModelService.getAndSaveNewIfNotExistMobileModelByModel(TestConstants.MobileModel.MODEL);

        assertEquals(Optional.of(mobileModel), result);
        verify(mobileModelRepository).save(mobileModelCaptor.capture());
        assertEquals(expectedSavedMobileModel, mobileModelCaptor.getValue());
    }

    @Test
    void getMarketingNameByModel_whenMobileModelIsNull_expectOptionalEmpty() {
        var result = mobileModelService.getAndSaveNewIfNotExistMobileModelByModel(null);

        assertEquals(Optional.empty(), result);
        verifyNoInteractions(mobileModelRepository);
    }
}
