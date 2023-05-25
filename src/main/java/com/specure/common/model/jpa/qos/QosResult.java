package com.specure.common.model.jpa.qos;

import com.specure.common.model.jpa.MeasurementQos;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@MappedSuperclass
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public abstract class QosResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @JsonIgnore
    private long qosTestUid;

    @ManyToOne
    @JsonBackReference
    @ToString.Exclude
    @JoinColumn(name = "measurement_qos_id")
    private MeasurementQos measurementQos;

    private long startTimeNs;

    private long durationNs;

    @JsonIgnore
    private int successCount;

    @JsonIgnore
    private int failureCount;
}
