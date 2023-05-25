package com.specure.common.model.jpa;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Builder
@Data
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
//todo rename to state/condition
public class MeasurementIsActive {

    private boolean active;

}
