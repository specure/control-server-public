package com.specure.common.repository;

import com.specure.common.model.jpa.MeasurementServerDescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeasurementServerDescriptionRepository extends JpaRepository<MeasurementServerDescription, Long> { }
