package com.specure.common.model.jpa;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.specure.common.enums.MeasurementServerType;
import lombok.*;
import org.hibernate.annotations.SQLDeleteAll;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString
@Table(name = "measurement_server_types")
@Entity
@SQLDeleteAll(sql = "UPDATE measurement_server_types SET deleted = true, modified_date = NOW() where id = ?")
@Where(clause = "deleted = false")
public class MeasurementServerTypeDetail extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "server_type")
    @Enumerated(EnumType.STRING)
    private MeasurementServerType serverType;

    @Column(name = "port")
    private Integer port;

    @Column(name = "port_ssl")
    private Integer portSsl;

    @Column(name = "encrypted")
    private boolean encrypted;

    @ManyToOne
    @JoinColumn(name = "measurement_server_id")
    @JsonBackReference
    @ToString.Exclude
    private MeasurementServer measurementServer;
}
