package com.specure.common.model.jpa;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Builder
@Data
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class Coordinates implements Serializable {

    @Column(name = "latitude")
    private Float lat;

    @Column(name = "longitude")
    private Float lng;

}
