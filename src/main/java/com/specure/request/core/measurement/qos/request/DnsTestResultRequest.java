package com.specure.request.core.measurement.qos.request;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonTypeName("dns")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class DnsTestResultRequest extends TestResult {
    private List<DnsResultEntriesRequest> dnsResultEntries;
    private long dnsObjectiveTimeout;
    private String dnsObjectiveDnsRecord;
    private String dnsObjectiveHost;
    private String dnsObjectiveResolver;
    private String dnsResultInfo;
    private String dnsResultStatus;
    private long dnsResultDuration;
    private int dnsResultEntriesFound;
}
