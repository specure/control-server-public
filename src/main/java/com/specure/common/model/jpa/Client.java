package com.specure.common.model.jpa;

import com.specure.common.enums.ClientType;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "uuid")
    private String uuid;

    @Enumerated(EnumType.STRING)
    @Column(name = "client_type")
    private ClientType clientType;

    @Column(name = "terms_and_conditions_accepted")
    private Boolean termsAndConditionsAccepted;

    @Column(name = "terms_and_conditions_accepted_version")
    private Long termsAndConditionsAcceptedVersion;

    @Column(name = "terms_and_conditions_accepted_timestamp")
    private LocalDateTime termsAndConditionsAcceptedTimestamp;

    @Column(name = "last_seen")
    private LocalDateTime lastSeen;
}
