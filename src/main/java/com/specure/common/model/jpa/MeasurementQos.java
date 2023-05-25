package com.specure.common.model.jpa;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.specure.common.model.jpa.qos.*;
import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class MeasurementQos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String testToken;
    private String clientUuid;
    @Column(name = "open_test_uuid")
    private String openTestUuid;

    @ManyToOne
    @JoinColumn(name = "open_test_uuid", insertable = false, updatable = false)
    @JsonBackReference
    @ToString.Exclude
    private Measurement measurement;
    private Timestamp time;
    private String clientVersion;

    private String clientName;
    private String clientLanguage;

    @ManyToOne()
    @JoinColumn(name = "ad_hoc_campaign_id")
    private AdHocCampaign adHocCampaign;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "measurementQos")
    @JsonManagedReference
    @ToString.Exclude
    private List<VoipTestResult> voipTestResults;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "measurementQos")
    @JsonManagedReference
    @ToString.Exclude
    private List<TcpTestResult> tcpTestResults;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "measurementQos")
    @JsonManagedReference
    @ToString.Exclude
    private List<UdpTestResult> udpTestResults;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "measurementQos")
    @JsonManagedReference
    @ToString.Exclude
    private List<HttpProxyTestResult> httpProxyTestResults;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "measurementQos")
    @JsonManagedReference
    @ToString.Exclude
    private List<NonTransparentProxyTestResult> nonTransparentProxyTestResults;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "measurementQos")
    @JsonManagedReference
    @ToString.Exclude
    private List<DnsTestResult> dnsTestResults;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "measurementQos")
    @JsonManagedReference
    @ToString.Exclude
    private List<TracerouteTestResult> tracerouteTestResults;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "measurementQos")
    @JsonManagedReference
    @ToString.Exclude
    private List<WebsiteTestResult> websiteTestResults;
}
