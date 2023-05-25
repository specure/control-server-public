package com.specure.common.model.jpa.neutrality;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("UDP")
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class NetNeutralityUdpSetting extends NetNeutralitySetting {

    @Column(name = "timeout")
    private Long timeout;

    @Column(name = "port_number")
    private Long portNumber;

    @Column(name = "num_packets_sent")
    private Long numberOfPacketsSent;

    @Column(name = "min_number_packets_received")
    private Long minNumberOfPacketsReceived;
}
