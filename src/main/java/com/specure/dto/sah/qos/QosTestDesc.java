package com.specure.dto.sah.qos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QosTestDesc {
    private Long id;
    private String descKey;
    private String value;
    private String lang;
}
