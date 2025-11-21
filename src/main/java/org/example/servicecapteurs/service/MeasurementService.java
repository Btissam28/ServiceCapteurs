package org.example.servicecapteurs.service;

import org.example.servicecapteurs.domain.dto.MeasurementDto;

import java.time.LocalDateTime;
import java.util.List;

public interface MeasurementService {
    List<MeasurementDto> getAllMeasurements();
    MeasurementDto getMeasurementById(Long id);
    MeasurementDto createMeasurement(MeasurementDto measurementDto);
    void deleteMeasurement(Long id);
    List<MeasurementDto> getMeasurementsByContainer(Long containerId);
    List<MeasurementDto> getMeasurementsBySensor(Long sensorId);
    List<MeasurementDto> getMeasurementsByDateRange(LocalDateTime start, LocalDateTime end);
    MeasurementDto recordMeasurement(String sensorId, Double fillLevel, Double temperature, Double humidity);
    List<MeasurementDto> getLatestMeasurementsBySensor(Long sensorId, int limit);
    List<MeasurementDto> getContainerMeasurementsHistory(Long containerId, int hours);
}
