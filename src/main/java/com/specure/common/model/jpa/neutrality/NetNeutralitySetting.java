package com.specure.common.model.jpa.neutrality;

import com.specure.common.enums.NetNeutralityTestType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "neutrality_qos_test")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
@Getter
@Setter
@ToString
@EqualsAndHashCode
public abstract class NetNeutralitySetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    protected Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", insertable = false, updatable = false)
    protected NetNeutralityTestType type;

    @Column(name = "active")
    protected boolean isActive;
}
