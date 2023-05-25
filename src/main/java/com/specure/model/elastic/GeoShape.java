package com.specure.model.elastic;

import lombok.*;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.GeoShapeField;
import org.springframework.data.elasticsearch.core.geo.GeoJsonMultiPolygon;

@Document(indexName = "shape", createIndex = false)
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class GeoShape {

    @Field(name = "ADM0_PCODE")
    private String countryPCode;

    @Field(name = "ADM0_EN")
    private String countryEn;

    @Field(name = "ADM0_SQ")
    private String countrySq;

    @Field(name = "ADM1_PCODE")
    private String countyPCode;

    @Field(name = "ADM1_EN")
    private String countyEn;

    @Field(name = "ADM1_SQ")
    private String countySq;

    @Field(name = "ADM2_EN")
    private String municipalityEn;

    @Field(name = "ADM2_SQ")
    private String municipalitySq;

    @Field(name = "ADM2_PCODE")
    private String municipalityPCode;

    @GeoShapeField
    @Field(name = "location")
    private GeoJsonMultiPolygon location;
}
