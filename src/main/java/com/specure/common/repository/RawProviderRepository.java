package com.specure.common.repository;

import com.specure.common.model.jpa.RawProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface RawProviderRepository extends JpaRepository<RawProvider, Long>, JpaSpecificationExecutor<RawProvider> {

    boolean existsByCountryAndRawName(String country, String rawName);

    List<RawProvider> findAllByCountryAndRawNameAndAsnAndMccMnc(String country, String rawName, String asn, String mccMnc);

    Optional<RawProvider> findTopByCountryAndRawName(String country, String rawName);

    List<RawProvider> findAllByProviderId(Long providerId);
}
