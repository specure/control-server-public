package com.specure.common.model.jpa.qos;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TracerouteTestResult extends QosResult {

    private String tracerouteObjectiveHost;

    private String tracerouteResultStatus;

    private Long tracerouteResultDuration;

    private Long tracerouteObjectiveTimeout;

    private Integer tracerouteObjectiveMaxHops;

    private Integer tracerouteResultHops;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tracerouteTestResult")
    @JsonManagedReference
    private List<PathElementEntriesResult> tracerouteResultDetails;
}
