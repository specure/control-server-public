package com.specure.common.model.jpa;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.specure.common.enums.Direction;
import lombok.*;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@EqualsAndHashCode
public class SpeedDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "measurement_id")
    @JsonBackReference
    @ToString.Exclude
    private Measurement measurement;

    @Enumerated(EnumType.STRING)
    private Direction direction;

    private Integer thread;
    private Long time;
    private Integer bytes;
}
