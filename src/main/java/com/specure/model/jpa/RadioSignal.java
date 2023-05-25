package com.specure.model.jpa;


import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "radio_signal")
public class RadioSignal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "open_test_uuid")
    private String openTestUuid;

    @Column(name = "time")
    private Timestamp time;

    @Column(name = "bit_error_rate")
    private Integer bitErrorRate;

    @Column(name = "cell_uuid")
    private String cellUUID;

    @Column(name = "network_type_id")
    private Integer networkTypeId;

    @Column(name = "signal_strength")
    private Integer signalStrength;

    @Column(name = "time_ns_last")
    private Long timeNsLast;

    @Column(name = "time_ns")
    private Long timeNs;

    @Column(name = "wifi_link_speed")
    private Integer wifiLinkSpeed;

    @Column(name = "lte_rsrp")
    private Integer lteRSRP;

    @Column(name = "lte_rsrq")
    private Integer lteRSRQ;

    @Column(name = "lte_rssnr")
    private Integer lteRSSNR;

    @Column(name = "lte_cqi")
    private Integer lteCQI;

    @Column(name = "timing_advance")
    private Integer timingAdvance;
}
