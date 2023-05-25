package com.specure.response.core.settings;

import com.specure.enums.QosMeasurement;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class QosMeasurementTypeDescription {
    private String name;
    private QosMeasurement testType;

    public static QosMeasurementTypeDescription fromQosMeasurement(QosMeasurement qosMeasurement) {
        return new QosMeasurementTypeDescription(qosMeasurement.getDescription(), qosMeasurement);
    }
}
