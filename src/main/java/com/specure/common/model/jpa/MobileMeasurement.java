package com.specure.common.model.jpa;


import com.specure.common.enums.MeasurementStatus;
import com.specure.common.enums.NetworkGroupName;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@ToString
@Entity
@Table(name = "mobile_measurement")
public class MobileMeasurement implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uuid")
    private String uuid;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private MeasurementStatus status;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client client;

    @Column(name = "open_test_uuid")
    private String openTestUuid;

    @Column(name = "client_public_ip")
    private String clientPublicIp;

    @Column(name = "client_public_ip_anonymized")
    private String clientPublicIpAnonymized;

    @Column(name = "timezone")
    private String timezone;

    @Column(name = "client_time")
    private LocalDateTime clientTime;

    @Column(name = "public_ip_asn")
    private Long publicIpAsn;

    @Column(name = "public_ip_as_name")
    private String publicIpAsName;

    @Column(name = "country_asn")
    private String countryAsn;

    @Column(name = "public_ip_rdns")
    private String publicIpRdns;

    @Column(name = "last_sequence_number")
    private Integer lastSequenceNumber;

    @Column(name = "network_group_name")
    @Enumerated(EnumType.STRING)
    private NetworkGroupName networkGroupName;

//    @OneToOne(fetch = FetchType.LAZY)//todo on testRequest
//    @JoinColumn(
//            name = "uuid",
//            referencedColumnName = "test_uuid",
//            insertable = false, nullable = false, updatable = false
//    )
//    private LoopModeSettings loopModeSettings;

    private Integer duration;

    @Column(updatable = false, nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date time;

    @PrePersist
    protected void preInsert() {
        this.time = new Date();
    }

}
