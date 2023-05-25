package com.specure.common.model.jpa.neutrality;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("WEB")
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class NetNeutralityWebSetting extends NetNeutralitySetting {

    @Column(name = "target")
    private String target;

    @Column(name = "timeout")
    private Long timeout;

    @Column(name = "status_code")
    private Long expectedStatusCode;
}
