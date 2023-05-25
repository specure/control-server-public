package com.specure.common.model.jpa;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "loop_mode_settings")
public class LoopModeSettings implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "test_uuid")
    protected String testUuid;

    @Column(name = "client_uuid")
    protected String clientUuid;

    @Column(name = "max_delay")
    protected Integer maxDelay;

    @Column(name = "max_movement")
    protected Integer maxMovement;

    @Column(name = "max_tests")
    protected Integer maxTests;

    @Column(name = "test_counter")
    protected Integer testCounter;

    @Column(name = "loop_uuid")
    private String loopUuid;
}
