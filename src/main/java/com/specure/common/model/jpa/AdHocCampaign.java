package com.specure.common.model.jpa;

import com.specure.common.enums.AdHocCampaignStatus;
import com.specure.common.enums.ProbePortStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.sql.Timestamp;


@Builder
@Entity
@Component
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(callSuper = false)
@ToString
public class AdHocCampaign extends BaseEntity {
    @Id
    @Column(name = "id")
    private String id;

    private String campaignName;

    @Enumerated(EnumType.STRING)
    private AdHocCampaignStatus status;

    private String location;

    @Embedded
    private Coordinates coordinates;

    private Timestamp start;
    private Timestamp finish;

    @JsonBackReference
    @ToString.Exclude
    @ManyToOne()
    @JoinColumn(name = "probe_id")
    private Probe probe;

    @Column(name = "provider_id")
    private Long providerId;

    private String description;

    @Column(name = "downtime_status")
    @Enumerated(EnumType.STRING)
    private ProbePortStatus downtimeStatus;

    @Column(name = "downtime_status_changed")
    private Timestamp downtimeStatusChanged;
}
