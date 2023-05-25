package com.specure.common.utils;

import com.specure.common.constant.PropertyName;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.math.RoundingMode;

@UtilityClass
public class UnitForMeasurementParams {
    public static final String MBP = "Mbps";
    public static final String MS = "ms";

    public String getUnitByAggregationName(String aggregationName) {
        switch (aggregationName) {
            case PropertyName.DOWNLOAD_METRIC_PARAMETER:
            case PropertyName.UPLOAD_METRIC_PARAMETER:
                return MBP;
        }
        return MS;
    }

    public Double convertUnitValueOfMetric(Double value, String metricName) {
        if (PropertyName.PING_METRIC_PARAMETER.equals(metricName)) {
            return value;
        }
        return BigDecimal
                .valueOf(value / 1000)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }
}
