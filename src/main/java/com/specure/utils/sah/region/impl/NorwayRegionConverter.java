package com.specure.utils.sah.region.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.specure.utils.sah.region.NameCodeRelation;
import com.specure.utils.sah.region.RegionConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
@Component
public class NorwayRegionConverter implements RegionConverter {

    @Value("${country.norway}")
    private String tenant;

    @Value("classpath:municipalities.json")
    private Resource municipalityResource;

    private List<NameCodeRelation> norwayMunicipalities;

    @Override
    public String getTenantPurpose() {
        return tenant;
    }

    @Override
    public String getNameByCode(String code) {
        if(Objects.isNull(code)) return null;

        return getAllNameCodeRelations().stream()
                .filter(nameCodeRelation -> nameCodeRelation.getCode().equals(code))
                .findFirst()
                .orElseGet(()-> NameCodeRelation.builder().name(null).build())
                .getName();

    }

    @Override
    public List<NameCodeRelation> getAllNameCodeRelations() {
        if (Objects.isNull(norwayMunicipalities)) {
            try (Reader reader = new InputStreamReader(municipalityResource.getInputStream(), UTF_8)) {
                String str =  FileCopyUtils.copyToString(reader);
                ObjectMapper objectMapper = new ObjectMapper();
                norwayMunicipalities = Arrays.asList(objectMapper.readValue(str, NameCodeRelation[].class).clone());
            } catch (IOException e) {
                log.error(e.getMessage());
                return Collections.emptyList();
            }
        }
        return norwayMunicipalities;
    }
}
