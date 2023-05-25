package com.specure.common.model.jpa;

import lombok.*;

import javax.persistence.*;

@Builder(toBuilder = true)
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString
public class Provider extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "country")
    private String country;

    @Column(name = "mno_active")
    private boolean mnoActive;

    @Column(name = "isp_active")
    private boolean ispActive;
}
