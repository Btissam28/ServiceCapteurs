package org.example.servicecapteurs.service;

import org.example.servicecapteurs.domain.dto.SensorDto;

import java.util.List;

public interface SensorService {
    List<SensorDto> getAllSensors();
    SensorDto getSensorById(Long id);
    SensorDto getSensorBySensorId(String sensorId);
    SensorDto createSensor(SensorDto sensorDto);
    SensorDto updateSensor(Long id, SensorDto sensorDto);
    void deleteSensor(Long id);
    List<SensorDto> getSensorsByStatus(String status);
    List<SensorDto> getSensorsByContainer(Long containerId);
    SensorDto assignToContainer(Long sensorId, Long containerId);
    List<SensorDto> getSensorsWithLowBattery(Double batteryThreshold);
    SensorDto updateBatteryLevel(Long sensorId, Double batteryLevel);
}