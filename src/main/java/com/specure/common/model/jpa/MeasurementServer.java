package com.specure.common.model.jpa;

import com.specure.common.constant.Constants;
import com.specure.common.enums.MeasurementServerType;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.List;
import java.util.Optional;


@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString
@SQLDelete(sql = "UPDATE measurement_server SET deleted = true, modified_date = NOW() where id = ?")
@Where(clause = "deleted = false")
public class MeasurementServer extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String webAddress;
    @ToString.Exclude
    private String secretKey;

    @Column(name = "provider")
    private Long providerId;

    @ToString.Exclude
    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "measurementServerDescription")
    private MeasurementServerDescription measurementServerDescription;

    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "measurementServer", orphanRemoval = true)
    @JsonManagedReference
    private List<MeasurementServerTypeDetail> serverTypeDetails;

    @Column(name = "server_type")
    @Enumerated(EnumType.STRING)
    @Deprecated(since = "Since moving to multiple server type for one server")
    private MeasurementServerType serverType;

    @ToString.Exclude
    @Column(name = "uuid")
    private String uuid;

    @Column(name = "port")
    private Integer port;

    @Column(name = "port_ssl")
    private Integer portSsl;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "on_net")
    private boolean isOnNet;

    @Column(name = "dedicated")
    private boolean dedicated;

    @Column(name = "ip_v4_support")
    private Boolean ipV4Support;

    @Column(name = "ip_v6_support")
    private Boolean ipV6Support;

    public Integer getPort() {
        return Optional.ofNullable(port)
                .orElse(Constants.DEFAULT_PORT);
    }

    public Integer getPortSsl() {
        return Optional.ofNullable(portSsl)
                .orElse(Constants.DEFAULT_PORT);
    }
}
