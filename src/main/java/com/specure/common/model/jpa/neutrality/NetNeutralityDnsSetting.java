package com.specure.common.model.jpa.neutrality;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("DNS")
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class NetNeutralityDnsSetting extends NetNeutralitySetting {

    @Column(name = "target")
    private String target;

    @Column(name = "timeout")
    private Long timeout;

    @Column(name = "entry_type")
    private String entryType;

    @Column(name = "resolver")
    private String resolver;

    @Column(name = "dns_status")
    private String expectedDnsStatus;

    @Column(name = "dns_entries")
    private String expectedDnsEntries;
}
