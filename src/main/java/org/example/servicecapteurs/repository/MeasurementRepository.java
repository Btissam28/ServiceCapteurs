package org.example.servicecapteurs.repository;


import org.example.servicecapteurs.domain.entity.Measurement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MeasurementRepository extends JpaRepository<Measurement, Long> {
    List<Measurement> findByContainerId(Long containerId);
    List<Measurement> findBySensorId(Long sensorId);

    @Query("SELECT m FROM Measurement m WHERE m.measuredAt BETWEEN :start AND :end")
    List<Measurement> findByMeasuredAtBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT m FROM Measurement m WHERE m.container.id = :containerId AND m.measuredAt BETWEEN :start AND :end ORDER BY m.measuredAt DESC")
    List<Measurement> findRecentMeasurementsForContainer(Long containerId, LocalDateTime start, LocalDateTime end);

    @Query("SELECT m FROM Measurement m WHERE m.sensor.id = :sensorId ORDER BY m.measuredAt DESC LIMIT :limit")
    List<Measurement> findLatestMeasurementsBySensor(Long sensorId, int limit);
}
