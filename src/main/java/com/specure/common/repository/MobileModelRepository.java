package com.specure.common.repository;

import com.specure.common.model.jpa.MobileModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MobileModelRepository extends JpaRepository<MobileModel, Long> {

    Optional<MobileModel> findMobileModelByModel(String model);

    boolean existsByModel(String model);
}
