package com.specure.service.sah.impl;

import com.specure.common.model.jpa.Provider;
import com.specure.common.model.jpa.RawProvider;
import com.specure.common.repository.ProviderRepository;
import com.specure.common.repository.RawProviderRepository;
import com.specure.common.specification.ProviderSpecification;
import com.specure.common.specification.RawProviderSpecification;
import com.specure.config.TenantCountryMapping;
import com.specure.service.sah.NationalOperatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NationalOperatorServiceImpl implements NationalOperatorService {

    private final ProviderRepository providerRepository;
    private final RawProviderRepository rawProviderRepository;
    private final TenantCountryMapping tenantCountryMapping;

    @Override
    public List<String> getNationalOperatorNames(String currentTenant, boolean isMno) {
        ProviderSpecification.ProviderSpecificationBuilder specificationBuilder = ProviderSpecification.builder()
                .countries(tenantCountryMapping.getMapping().getOrDefault(currentTenant, Collections.emptyList()));
        if (isMno) {
            specificationBuilder.mnoActive(true);
        } else {
            specificationBuilder.ispActive(true);
        }
        ProviderSpecification providerSpecification = specificationBuilder.build();
        return providerRepository.findAll(providerSpecification).stream()
                .map(Provider::getName)
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, List<String>> getNationalOperatorsAliases() {
        RawProviderSpecification rawProviderSpecification = RawProviderSpecification.builder()
                .isMappedToActiveProvider(true)
                .build();

        return rawProviderRepository.findAll(rawProviderSpecification)
                .stream()
                .collect(Collectors.groupingBy(x -> x.getProvider().getName(),
                        Collectors.mapping(RawProvider::getRawName, Collectors.toList())));
    }
}
