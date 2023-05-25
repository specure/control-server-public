package com.specure.model.jpa;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sim_operator")
public class SimOperator {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "mccmnc")
    private String mccMnc;

    @Column(name = "country")
    private String country;

    @Column(name = "name")
    private String name;

    @Column(name = "shortname")
    private String shortName;

    @Column(name = "valid_from")
    private String validFrom;
}
