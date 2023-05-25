package com.specure.common.model.jpa.qos;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class PathElementEntriesResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date time;

    private String host;

    @ManyToOne
    @JsonBackReference
    @ToString.Exclude
    @JoinColumn(name = "traceroute_test_result_id")
    private TracerouteTestResult tracerouteTestResult;
}
