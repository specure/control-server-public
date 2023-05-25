package com.specure.common.model.jpa;

import com.specure.common.enums.PortType;
import com.specure.common.enums.ProbePurpose;
import com.specure.common.enums.ProbeType;
import com.specure.common.enums.Status;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Builder(toBuilder = true)
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@ToString
public class Probe extends BaseEntity {

    @Id
    private String id;

    @Enumerated(EnumType.STRING)
    private ProbeType type;

    @Enumerated(EnumType.STRING)
    private Status status;

    private String operator;

    private Integer modemCount;

    private String comment;

    private String currentAdHocCampaignMark;

    @Formula("site_id is not null OR current_ad_hoc_campaign_mark is not null")
    private boolean assigned;

    @Enumerated(EnumType.STRING)
    private ProbePurpose probePurpose;

    @JsonBackReference
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "site_id")
    private Site site;

    @ToString.Exclude
    @JsonManagedReference
    @OneToMany(mappedBy = "probe")
    List<AdHocCampaign> adHocCampaigns = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "probe", cascade = {CascadeType.ALL})
    @JsonManagedReference
    private List<ProbePort> probePorts;

    public Integer getFixedPortNumber() {
        return this.getPortNumber(PortType.FIXED);
    }

    public Integer mobilePortNumber() {
        return this.getPortNumber(PortType.MOBILE);
    }

    private Integer getPortNumber(PortType portType) {
        if (Objects.isNull(this.probePorts) || this.probePorts.size() == 0) {
            return 0;
        }
        return Math.toIntExact(this.probePorts.stream()
                .filter(port -> port.getType() == portType)
                .count());
    }

    public Package getPackageByPortName(String port) {
        if (Objects.isNull(this.probePorts)) {
            return null;
        }
        return this.probePorts.stream()
                .filter(e -> e.getName().equals(port))
                .findFirst()
                .map(ProbePort::getAPackage)
                .orElse(null);
    }
}
