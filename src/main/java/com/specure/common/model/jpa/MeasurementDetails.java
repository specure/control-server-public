package com.specure.common.model.jpa;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "measurement_details")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Builder
public class MeasurementDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "city")
    private String city;

    @Column(name = "country")
    private String country;

    @Column(name = "county")
    private String county;

    @Column(name = "postal_code")
    private String postalCode;

    @Column(name = "sim_mcc_mnc")
    private String simMccMnc;

    @Column(name = "sim_operator_name")
    private String simOperatorName;

    @Column(name = "network_is_roaming")
    private Boolean networkIsRoaming;

    @Column(name = "network_operator_name")
    private String networkOperatorName;

    @Column(name = "network_country")
    private String networkCountry;

    @Column(name = "sim_country")
    private String simCountry;

    @Column(name = "telephony_permission_granted")
    private Boolean telephonyPermissionGranted;

    @Column(name = "location_permission_granted")
    private Boolean locationPermissionGranted;

    @Column(name = "uuid_permission_granted")
    private Boolean uuidPermissionGranted;

    @Column(name = "mobile_model_manufacturer")
    private String mobileModelManufacturer;

    @Column(name = "mobile_model_category")
    private String mobileModelCategory;

    @Column(name = "app_version")
    private String appVersion;

    @OneToOne
    @JoinColumn(name = "id")
    @MapsId
    @JsonBackReference
    @ToString.Exclude
    private Measurement measurement;
}
