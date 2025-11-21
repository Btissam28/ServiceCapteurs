package org.example.servicecapteurs.controller;



import org.example.servicecapteurs.domain.dto.MeasurementDto;
import org.example.servicecapteurs.service.MeasurementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/capteurs/measurements")
public class MeasurementController {

    private static final Logger logger = LoggerFactory.getLogger(MeasurementController.class);

    private final MeasurementService measurementService;

    public MeasurementController(MeasurementService measurementService) {
        this.measurementService = measurementService;
    }

    @GetMapping
    public ResponseEntity<List<MeasurementDto>> getAllMeasurements() {
        logger.info("GET /api/capteurs/measurements - Récupération de toutes les mesures");
        List<MeasurementDto> measurements = measurementService.getAllMeasurements();
        return ResponseEntity.ok(measurements);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MeasurementDto> getMeasurementById(@PathVariable Long id) {
        logger.info("GET /api/capteurs/measurements/{} - Récupération de la mesure par ID", id);
        MeasurementDto measurement = measurementService.getMeasurementById(id);
        return ResponseEntity.ok(measurement);
    }

    @PostMapping
    public ResponseEntity<MeasurementDto> createMeasurement(@RequestBody MeasurementDto measurementDto) {
        logger.info("POST /api/capteurs/measurements - Création d'une nouvelle mesure");
        MeasurementDto createdMeasurement = measurementService.createMeasurement(measurementDto);
        return ResponseEntity.ok(createdMeasurement);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMeasurement(@PathVariable Long id) {
        logger.info("DELETE /api/capteurs/measurements/{} - Suppression de la mesure", id);
        measurementService.deleteMeasurement(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/container/{containerId}")
    public ResponseEntity<List<MeasurementDto>> getMeasurementsByContainer(@PathVariable Long containerId) {
        logger.info("GET /api/capteurs/measurements/container/{} - Récupération des mesures par conteneur", containerId);
        List<MeasurementDto> measurements = measurementService.getMeasurementsByContainer(containerId);
        return ResponseEntity.ok(measurements);
    }

    @GetMapping("/sensor/{sensorId}")
    public ResponseEntity<List<MeasurementDto>> getMeasurementsBySensor(@PathVariable Long sensorId) {
        logger.info("GET /api/capteurs/measurements/sensor/{} - Récupération des mesures par capteur", sensorId);
        List<MeasurementDto> measurements = measurementService.getMeasurementsBySensor(sensorId);
        return ResponseEntity.ok(measurements);
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<MeasurementDto>> getMeasurementsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        logger.info("GET /api/capteurs/measurements/date-range - Récupération des mesures entre {} et {}", start, end);
        List<MeasurementDto> measurements = measurementService.getMeasurementsByDateRange(start, end);
        return ResponseEntity.ok(measurements);
    }

    @PostMapping("/record")
    public ResponseEntity<MeasurementDto> recordMeasurement(
            @RequestParam String sensorId,
            @RequestParam Double fillLevel,
            @RequestParam(required = false) Double temperature,
            @RequestParam(required = false) Double humidity) {
        logger.info("POST /api/capteurs/measurements/record - Enregistrement d'une mesure pour le capteur: {}, niveau: {}%",
                sensorId, fillLevel);
        MeasurementDto recordedMeasurement = measurementService.recordMeasurement(sensorId, fillLevel, temperature, humidity);
        return ResponseEntity.ok(recordedMeasurement);
    }

    @GetMapping("/sensor/{sensorId}/latest")
    public ResponseEntity<List<MeasurementDto>> getLatestMeasurementsBySensor(
            @PathVariable Long sensorId,
            @RequestParam(defaultValue = "10") int limit) {
        logger.info("GET /api/capteurs/measurements/sensor/{}/latest - Récupération des {} dernières mesures", sensorId, limit);
        List<MeasurementDto> measurements = measurementService.getLatestMeasurementsBySensor(sensorId, limit);
        return ResponseEntity.ok(measurements);
    }

    @GetMapping("/container/{containerId}/history")
    public ResponseEntity<List<MeasurementDto>> getContainerMeasurementsHistory(
            @PathVariable Long containerId,
            @RequestParam(defaultValue = "24") int hours) {
        logger.info("GET /api/capteurs/measurements/container/{}/history - Récupération de l'historique sur {} heures",
                containerId, hours);
        List<MeasurementDto> measurements = measurementService.getContainerMeasurementsHistory(containerId, hours);
        return ResponseEntity.ok(measurements);
    }
}