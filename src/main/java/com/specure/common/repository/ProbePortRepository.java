package com.specure.common.repository;


import com.specure.common.model.jpa.ProbePort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProbePortRepository extends JpaRepository<ProbePort, Long> {

    @Query(
            value = "SELECT * FROM probe_port WHERE package_id is not null ORDER BY ?#{#pageable}",
            nativeQuery = true
    )
    Page<ProbePort> findAllByAPackageIsNotNull(Pageable pageable);

    Optional<ProbePort> findByNameAndProbeId(String portName, String probeId);

    List<ProbePort> findAllByProbeId(String probeId);

    @Query(value = "DELETE FROM probe_port where probe_id=?1", nativeQuery = true)
    void deleteByProbeId(String id);

}
