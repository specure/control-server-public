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
@EqualsAndHashCode(callSuper = false)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class DnsTestResult extends QosResult {

    private long dnsObjectiveTimeout;
    private String dnsObjectiveDnsRecord;
    private String dnsObjectiveHost;
    private String dnsObjectiveResolver;
    private String dnsResultInfo;
    private String dnsResultStatus;
    private long dnsResultDuration;
    private int dnsResultEntriesFound;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "dnsTestResult")
    @JsonManagedReference
    private List<DnsResultEntries> dnsResultEntries;
}
