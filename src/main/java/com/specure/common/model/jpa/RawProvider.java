package com.specure.common.model.jpa;

import lombok.*;

import javax.persistence.*;
import java.util.Optional;


@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString
public class RawProvider extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "raw_name")
    private String rawName;

    @Column(name = "country")
    private String country;

    @ManyToOne
    @JoinColumn(name = "provider_id")
    private Provider provider;

    @Column(name = "asn")
    private String asn;

    @Column(name = "mcc_mnc")
    private String mccMnc;

    public String getAlias() {
        return Optional.ofNullable(provider)
                .map(Provider::getName)
                .orElse(null);
    }
}
