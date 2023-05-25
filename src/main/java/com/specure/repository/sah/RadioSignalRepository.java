package com.specure.repository.sah;

import com.specure.model.jpa.RadioSignal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RadioSignalRepository extends JpaRepository<RadioSignal, Long> {

    List<RadioSignal> findAllByOpenTestUuid(String measurementUuid);
}
