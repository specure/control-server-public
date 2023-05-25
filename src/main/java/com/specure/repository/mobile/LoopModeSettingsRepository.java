package com.specure.repository.mobile;

import com.specure.common.model.jpa.LoopModeSettings;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LoopModeSettingsRepository extends JpaRepository<LoopModeSettings, Long> {

    Optional<LoopModeSettings> findByTestUuid(String testUuid);
}
