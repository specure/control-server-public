package com.specure.repository.sah;

import com.specure.common.model.jpa.SpeedDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpeedDetailRepository extends JpaRepository<SpeedDetail, Long> {

    List<SpeedDetail> findAllByMeasurement_OpenTestUuid(String openTestUuid);
}
