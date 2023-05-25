package com.specure.utils.sah.region.impl;

import com.specure.utils.sah.region.NameCodeRelation;
import com.specure.utils.sah.region.RegionConverter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
public class NorwayRegionConverterTest {

    private RegionConverter regionConverter;

    @Before
    public void setUp() {
        Resource municipalityResource = new ClassPathResource("municipalities.json");
        regionConverter = new NorwayRegionConverter();
        ReflectionTestUtils.setField(regionConverter, "municipalityResource", municipalityResource);
    }

    @Test
    public void norwayRegionConverter_WhenGetTenantPurpose_ExpectReturnTenant() {
        String tenantValue = "no";
        ReflectionTestUtils.setField(regionConverter, "tenant", tenantValue);
        String tenant = regionConverter.getTenantPurpose();
        assertEquals(tenantValue, tenant);
    }

    @Test
    public void norwayRegionConverter_WhenGetNameByCode_ExpectReturnProperNameByCode() {

        // as municipality code and name are permanent

        var oslo = NameCodeRelation.builder().name("Oslo").code("0301").build();
        var bergen = NameCodeRelation.builder().name("Bergen").code("4601").build();

        Assert.assertEquals(oslo.getName(), regionConverter.getNameByCode(oslo.getCode()));
        Assert.assertEquals(bergen.getName(), regionConverter.getNameByCode(bergen.getCode()));
    }

    @Test
    public void norwayRegionConverter_WhenGetAllNameCodeRelations_ExpectReturnList() {
        var data = regionConverter.getAllNameCodeRelations();
        Assert.assertTrue(data.size()>0);
    }
}
