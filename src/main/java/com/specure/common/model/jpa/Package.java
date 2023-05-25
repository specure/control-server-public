package com.specure.common.model.jpa;

import com.specure.common.enums.PackageType;
import com.specure.common.enums.PortType;
import com.specure.common.enums.Technology;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.specure.enums.PackageDescription;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@ToString
public class Package extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String advertisedName;

    private String nativeName;

    private Long groupId;

    private boolean groupActive;

    @Column(name = "provider")
    private Long providerId;

    @Enumerated(EnumType.STRING)
    private PackageDescription packageDescription;

    @Enumerated(EnumType.STRING)
    private PackageType packageType;

    @Enumerated(EnumType.STRING)
    private PortType readyForPort;

    @Enumerated(EnumType.STRING)
    private Technology technology;

    private Long threshold;
    private Long download;
    private Long upload;
    private Long throttleSpeedDownload;
    private Long throttleSpeedUpload;

    @ToString.Exclude
    @OneToMany(mappedBy = "aPackage")
    @JsonManagedReference
    private List<ProbePort> probePorts;

    private boolean visibleOnPublicPortal;
}
