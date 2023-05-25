package com.specure.service.sah.impl;

import com.specure.dto.sah.NationalTableParams;
import com.specure.mapper.sah.NationalTableMapper;
import com.specure.model.dto.NationalTable;
import com.specure.model.dto.StatsByOperator;
import com.specure.multitenant.MultiTenantManager;
import com.specure.repository.sah.StatsElasticsearchRepository;
import com.specure.response.sah.stats.NationalTableResponse;
import com.specure.service.sah.StatsService;
import com.specure.utils.sah.MobileTechnologyConverter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
public class StatsServiceImpl implements StatsService {

    private final StatsElasticsearchRepository statsElasticsearchRepository;
    private final NationalTableMapper nationalTableMapper;
    private final MultiTenantManager multiTenantManager;

    @Override
    public NationalTableResponse getNationalTable(String mobileTechnology, String municipalityCode) {
        log.debug("StatsServiceImpl:getNationalTable started with tenant = {}, mobileTechnology = {}, municipalityCode = {}", multiTenantManager.getCurrentTenant(), mobileTechnology, municipalityCode);
        NationalTableParams nationalTableParams = MobileTechnologyConverter
                .getNetworkTypesByMobileTechName(mobileTechnology);

        NationalTable nationalTable = statsElasticsearchRepository
                .getNationalTable(nationalTableParams, municipalityCode);
        log.trace("StatsServiceImpl:getNationalTable tenant = {}, nationalTable = {}");
        List<StatsByOperator> sorted = nationalTable
                .getStatsByOperator()
                .stream()
                .sorted(Comparator.comparing(statsByOperator -> statsByOperator.getOperatorName().toLowerCase()))
                .collect(Collectors.toList());

        nationalTable.setStatsByOperator(sorted);
        NationalTableResponse nationalTableResponse = nationalTableMapper.nationalTableToNationalTableResponse(nationalTable);
        log.debug("StatsServiceImpl:getNationalTable finished with tenant = {}, nationalTableResponse = {}", multiTenantManager.getCurrentTenant(), nationalTableResponse);
        return nationalTableResponse;
    }
}
