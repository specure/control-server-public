package com.specure.common.repository;

import com.specure.common.enums.NetNeutralityTestType;
import com.specure.common.model.jpa.neutrality.NetNeutralitySetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface NetNeutralitySettingRepository extends JpaRepository<NetNeutralitySetting, Long> {

    @Override
    @Query("from NetNeutralitySetting")
    List<NetNeutralitySetting> findAll();

    Optional<NetNeutralitySetting> findByIdAndType(Long id, NetNeutralityTestType type);
}
