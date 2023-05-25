package com.specure.common.repository;

import com.specure.common.enums.MeasurementServerType;
import com.specure.common.model.jpa.MeasurementServer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface MeasurementServerRepository extends JpaRepository<MeasurementServer, Long>, JpaSpecificationExecutor<MeasurementServer> {
    Optional<MeasurementServer> findById(Long id);

    List<MeasurementServer> findByProviderId(Long providerId);

    List<MeasurementServer> findAllByIsOnNetIsFalseOrderByDedicatedDesc();

    List<MeasurementServer> findAllByDedicatedIsTrue();

    @Query(
            value = "SELECT * FROM measurement_server ms " +
                    "WHERE ms.id = (SELECT m.measurement_server_id FROM measurement m WHERE m.client_uuid = ?1 ORDER BY m.id DESC LIMIT 1)",
            nativeQuery = true
    )
    Optional<MeasurementServer> findByClientUUID(String client_uuid);

    List<MeasurementServer> getByServerTypeDetails_ServerTypeIn(Collection<MeasurementServerType> serverTypes);
}
