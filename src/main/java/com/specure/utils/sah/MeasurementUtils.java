package com.specure.utils.sah;

import lombok.experimental.UtilityClass;

import java.util.Objects;
import java.util.Optional;

@UtilityClass
public class MeasurementUtils {

    public boolean isUseSignal(Integer simCount, boolean dualSim) {
        if (dualSim && Objects.nonNull(simCount)) {
            return true;
        }
        return !dualSim;
    }

    public boolean isDualSim(Integer networkType, Boolean dualSim) {
        if (Objects.nonNull(networkType) && networkType > 90) {
            return false;
        } else {
            return Optional.ofNullable(dualSim)
                    .orElse(false);
        }
    }
}
