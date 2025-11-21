package org.example.servicecapteurs.controller;

import org.example.servicecapteurs.domain.dto.SensorDto;
import org.example.servicecapteurs.service.SensorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/capteurs/sensors")
public class SensorController {

    private static final Logger logger = LoggerFactory.getLogger(SensorController.class);

    private final SensorService sensorService;

    public SensorController(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    @GetMapping
    public ResponseEntity<List<SensorDto>> getAllSensors() {
        logger.info("GET /api/capteurs/sensors - Récupération de tous les capteurs");
        List<SensorDto> sensors = sensorService.getAllSensors();
        return ResponseEntity.ok(sensors);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SensorDto> getSensorById(@PathVariable Long id) {
        logger.info("GET /api/capteurs/sensors/{} - Récupération du capteur par ID", id);
        SensorDto sensor = sensorService.getSensorById(id);
        return ResponseEntity.ok(sensor);
    }

    @GetMapping("/sensor-id/{sensorId}")
    public ResponseEntity<SensorDto> getSensorBySensorId(@PathVariable String sensorId) {
        logger.info("GET /api/capteurs/sensors/sensor-id/{} - Récupération du capteur par SensorID", sensorId);
        SensorDto sensor = sensorService.getSensorBySensorId(sensorId);
        return ResponseEntity.ok(sensor);
    }

    @PostMapping
    public ResponseEntity<SensorDto> createSensor(@RequestBody SensorDto sensorDto) {
        logger.info("POST /api/capteurs/sensors - Création d'un nouveau capteur");
        SensorDto createdSensor = sensorService.createSensor(sensorDto);
        return ResponseEntity.ok(createdSensor);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SensorDto> updateSensor(@PathVariable Long id, @RequestBody SensorDto sensorDto) {
        logger.info("PUT /api/capteurs/sensors/{} - Mise à jour du capteur", id);
        SensorDto updatedSensor = sensorService.updateSensor(id, sensorDto);
        return ResponseEntity.ok(updatedSensor);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSensor(@PathVariable Long id) {
        logger.info("DELETE /api/capteurs/sensors/{} - Suppression du capteur", id);
        sensorService.deleteSensor(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<SensorDto>> getSensorsByStatus(@PathVariable String status) {
        logger.info("GET /api/capteurs/sensors/status/{} - Récupération des capteurs par statut", status);
        List<SensorDto> sensors = sensorService.getSensorsByStatus(status);
        return ResponseEntity.ok(sensors);
    }

    @GetMapping("/container/{containerId}")
    public ResponseEntity<List<SensorDto>> getSensorsByContainer(@PathVariable Long containerId) {
        logger.info("GET /api/capteurs/sensors/container/{} - Récupération des capteurs par conteneur", containerId);
        List<SensorDto> sensors = sensorService.getSensorsByContainer(containerId);
        return ResponseEntity.ok(sensors);
    }

    @PostMapping("/{sensorId}/assign-to-container/{containerId}")
    public ResponseEntity<SensorDto> assignToContainer(@PathVariable Long sensorId, @PathVariable Long containerId) {
        logger.info("POST /api/capteurs/sensors/{}/assign-to-container/{} - Assignation du capteur au conteneur", sensorId, containerId);
        SensorDto updatedSensor = sensorService.assignToContainer(sensorId, containerId);
        return ResponseEntity.ok(updatedSensor);
    }

    @GetMapping("/low-battery")
    public ResponseEntity<List<SensorDto>> getSensorsWithLowBattery(@RequestParam(defaultValue = "20.0") Double batteryThreshold) {
        logger.info("GET /api/capteurs/sensors/low-battery - Récupération des capteurs avec batterie faible (< {}%)", batteryThreshold);
        List<SensorDto> sensors = sensorService.getSensorsWithLowBattery(batteryThreshold);
        return ResponseEntity.ok(sensors);
    }

    @PutMapping("/{sensorId}/battery-level")
    public ResponseEntity<SensorDto> updateBatteryLevel(@PathVariable Long sensorId, @RequestParam Double batteryLevel) {
        logger.info("PUT /api/capteurs/sensors/{}/battery-level - Mise à jour du niveau de batterie: {}%", sensorId, batteryLevel);
        SensorDto updatedSensor = sensorService.updateBatteryLevel(sensorId, batteryLevel);
        return ResponseEntity.ok(updatedSensor);
    }
}