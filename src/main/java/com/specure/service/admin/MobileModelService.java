package com.specure.service.admin;

import com.specure.common.model.jpa.MobileModel;

import java.util.Optional;

public interface MobileModelService {

    Optional<MobileModel> getAndSaveNewIfNotExistMobileModelByModel(String model);
}
