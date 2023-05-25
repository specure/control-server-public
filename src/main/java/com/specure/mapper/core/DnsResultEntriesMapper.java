package com.specure.mapper.core;

import com.specure.common.model.jpa.qos.DnsResultEntries;
import com.specure.request.core.measurement.qos.request.DnsResultEntriesRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DnsResultEntriesMapper {
    DnsResultEntries dnsResultEntriesRequestToDnsResultEntries(DnsResultEntriesRequest dnsResultEntriesRequest);
}
