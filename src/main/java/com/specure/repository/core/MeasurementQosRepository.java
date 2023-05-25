package com.specure.repository.core;

import com.specure.common.model.jpa.MeasurementQos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MeasurementQosRepository extends JpaRepository<MeasurementQos, Long> {
    void deleteByOpenTestUuid(String uuid);

    Optional<MeasurementQos> findByOpenTestUuid(String testUuid);
}
