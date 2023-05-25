package com.specure.utils.mobile;

import com.specure.common.model.jpa.MobileModel;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Optional;

@UtilityClass
public class MobileModelUtils {

    public String extractDevice(Optional<MobileModel> mobileModel, String model) {
        if (mobileModel.isPresent()) {
            return ObjectUtils.firstNonNull(mobileModel.get().getMarketingName(), mobileModel.get().getModel());
        } else {
            return model;
        }
    }

    public String extractCategory(Optional<MobileModel> mobileModel) {
        return mobileModel
                .map(MobileModel::getCategory)
                .orElse(null);
    }

    public String extractManufacturer(Optional<MobileModel> mobileModel) {
        return mobileModel
                .map(MobileModel::getManufacturer)
                .orElse(null);
    }
}
