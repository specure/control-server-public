package com.specure.common.model.jpa;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Data
@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GeoLocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "measurement_id")
    private Measurement measurement;

    private Double geoLat;
    private Double geoLong;
    private Double accuracy;
    private Double altitude;
    private Double bearing;
    private Double speed;
    private Date timestamp;
    private String provider;
}
