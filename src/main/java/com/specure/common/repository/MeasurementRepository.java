package com.specure.common.repository;

import com.specure.common.enums.Platform;
import com.specure.common.model.jpa.AdHocCampaign;
import com.specure.common.model.jpa.Measurement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public interface MeasurementRepository extends JpaRepository<Measurement, Long> {

    Optional<Measurement> findByOpenTestUuidAndClientUuid(String uuid, String clientId);

    Optional<Measurement> findByToken(String token);

    Optional<Measurement> findByOpenTestUuid(String uuid);

    long countAllByTestSlot(int slot);

    @Query(
            value = "SELECT mesur.*\n" +
                    "FROM measurement mesur\n" +
                    "INNER JOIN (SELECT measurement_server_id, MAX(time) AS MaxDateTime\n" +
                    "    FROM measurement\n" +
                    "    WHERE  measurement_server_id in ?1\n" +
                    "    GROUP BY measurement_server_id\n" +
                    "    ) groupedMesuremnts\n" +
                    "ON mesur.measurement_server_id = groupedMesuremnts.measurement_server_id\n" +
                    "AND mesur.time = groupedMesuremnts.MaxDateTime",
            nativeQuery = true
    )
    List<Measurement> findLastMeasurementsForMeasurementServerIds(List<Long> ids);

    long countAllByTimeAfterAndTimeBeforeAndPlatform(Timestamp start, Timestamp finish, Platform platform);

    @Query(
            value = "SELECT mesur.*\n" +
                    "FROM measurement mesur\n" +
                    "INNER JOIN (SELECT measurement_server_id, MAX(time) AS MaxDateTime\n" +
                    "    FROM measurement\n" +
                    "    WHERE  measurement_server_id in ?1 AND status = 'FINISHED'\n" +
                    "    AND time > DATE_SUB(NOW(), INTERVAL 30 DAY)" + //todo fix that
                    "    GROUP BY measurement_server_id\n" +
                    "    ) groupedMesuremnts\n" +
                    "ON mesur.measurement_server_id = groupedMesuremnts.measurement_server_id\n" +
                    "AND mesur.time = groupedMesuremnts.MaxDateTime",
            nativeQuery = true
    )
    List<Measurement> findTopByOrderByStatusAndMeasurementServerIdOrderByTime(List<Long> ids);

    void deleteByOpenTestUuid(String uuid);

    void deleteByAdHocCampaign(AdHocCampaign campaignId);

    long deleteByDevice(String probeId);

    long deleteByMeasurementServerId(Long serverId);

    long countByAdHocCampaign(AdHocCampaign adHocCampaign);
}
