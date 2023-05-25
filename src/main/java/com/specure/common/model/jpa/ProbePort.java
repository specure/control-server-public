package com.specure.common.model.jpa;

import com.specure.common.enums.PortType;
import com.specure.common.enums.ProbePortStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

@Builder(toBuilder = true)
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@ToString
public class ProbePort extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private PortType type;

    @Enumerated(EnumType.STRING)
    private ProbePortStatus status;

    private Timestamp timestamp;

    @ManyToOne
    @JoinColumn(name = "probe_id")
    @JsonBackReference
    @ToString.Exclude
    private Probe probe;

    @ManyToOne
    @JoinColumn(name = "package_id")
    @JsonBackReference
    @ToString.Exclude
    private Package aPackage;
}
