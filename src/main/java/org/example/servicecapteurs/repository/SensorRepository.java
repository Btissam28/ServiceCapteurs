package org.example.servicecapteurs.repository;

import org.example.servicecapteurs.domain.entity.Sensor;
import org.example.servicecapteurs.domain.enums.SensorStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SensorRepository extends JpaRepository<Sensor, Long> {
    Optional<Sensor> findBySensorId(String sensorId);
    List<Sensor> findByStatus(SensorStatus status);
    List<Sensor> findByContainerId(Long containerId);

    @Query("SELECT s FROM Sensor s WHERE s.type = :type")
    List<Sensor> findByType(String type);

    @Query("SELECT s FROM Sensor s WHERE s.batteryLevel < :batteryThreshold")
    List<Sensor> findSensorsWithLowBattery(Double batteryThreshold);
}