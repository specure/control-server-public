package com.specure.common.model.elastic;

import com.specure.common.model.dto.TestResultCounter;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;

@Builder
@AllArgsConstructor
@Getter
@Setter
@Document(indexName = "basic_qos_test_bh", createIndex = false)
@ToString
public class BasicQosTest {

    @Id
    private String openTestUuid;
    private String clientUuid;
    private String clientType;

    @Field(type = FieldType.Date_Nanos)
    private String timestamp;
    private String measurementDate;
    private String operator;

    private long siteId;
    private String siteAdvertisedId;

    private long measurementServerId;
    private String measurementServerName;

    private String adHocCampaign;

    private Float dns;
    private Float httpProxy;
    private Float nonTransparentProxy;
    private Float tcp;
    private Float udp;
    private Float voip;
    private Float website;
    private Float traceroute;
    private Float overallQos;

    private String probeId;
    private String probePort;
    private String packageAdvertisedName;
    private String packageId;
    private String packageType;

    private String clientVersion;
    private String clientName;
    private String clientLanguage;

    private String typeOfProbePort;
    private String serverType;
    private int graphHour;

    private List<TestResultCounter> qosTestResultCounters;

    private String country;
}
