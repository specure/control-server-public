package com.specure.service.admin.impl;

import com.specure.common.model.jpa.MobileModel;
import com.specure.common.repository.MobileModelRepository;
import com.specure.service.admin.MobileModelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MobileModelServiceImpl implements MobileModelService {

    private final MobileModelRepository mobileModelRepository;


    @Override
    public Optional<MobileModel> getAndSaveNewIfNotExistMobileModelByModel(String model) {
        if (Objects.nonNull(model)) {
            MobileModel mobileModel = mobileModelRepository.findMobileModelByModel(model)
                    .orElseGet(() -> {
                        MobileModel newMobileModel = new MobileModel(null, null, model, null, null);
                        return mobileModelRepository.save(newMobileModel);
                    });
            return Optional.of(mobileModel);
        } else {
            return Optional.empty();
        }
    }
}
