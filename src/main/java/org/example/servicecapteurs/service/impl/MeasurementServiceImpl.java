package org.example.servicecapteurs.service.impl;


import org.example.servicecapteurs.domain.dto.MeasurementDto;
import org.example.servicecapteurs.domain.entity.Container;
import org.example.servicecapteurs.domain.entity.Measurement;
import org.example.servicecapteurs.domain.entity.Sensor;
import org.example.servicecapteurs.domain.enums.ContainerStatus;
import org.example.servicecapteurs.repository.ContainerRepository;
import org.example.servicecapteurs.repository.MeasurementRepository;
import org.example.servicecapteurs.repository.SensorRepository;
import org.example.servicecapteurs.service.MeasurementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MeasurementServiceImpl implements MeasurementService {

    private static final Logger logger = LoggerFactory.getLogger(MeasurementServiceImpl.class);

    private final MeasurementRepository measurementRepository;
    private final SensorRepository sensorRepository;
    private final ContainerRepository containerRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public MeasurementServiceImpl(MeasurementRepository measurementRepository,
                                  SensorRepository sensorRepository,
                                  ContainerRepository containerRepository,
                                  KafkaTemplate<String, Object> kafkaTemplate) {
        this.measurementRepository = measurementRepository;
        this.sensorRepository = sensorRepository;
        this.containerRepository = containerRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    @Transactional(readOnly = true)
    public List<MeasurementDto> getAllMeasurements() {
        logger.info("Récupération de toutes les mesures");
        return measurementRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public MeasurementDto getMeasurementById(Long id) {
        logger.info("Récupération de la mesure avec ID: {}", id);
        Measurement measurement = measurementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mesure non trouvée avec l'ID: " + id));
        return convertToDto(measurement);
    }

    @Override
    public MeasurementDto createMeasurement(MeasurementDto measurementDto) {
        logger.info("Création d'une nouvelle mesure pour le capteur: {}", measurementDto.getSensorId());

        Sensor sensor = sensorRepository.findById(measurementDto.getSensorId())
                .orElseThrow(() -> new RuntimeException("Capteur non trouvé avec l'ID: " + measurementDto.getSensorId()));
        Container container = containerRepository.findById(measurementDto.getContainerId())
                .orElseThrow(() -> new RuntimeException("Conteneur non trouvé avec l'ID: " + measurementDto.getContainerId()));

        Measurement measurement = new Measurement(sensor, container, measurementDto.getFillLevel());
        measurement.setTemperature(measurementDto.getTemperature());
        measurement.setHumidity(measurementDto.getHumidity());

        Measurement savedMeasurement = measurementRepository.save(measurement);

        // Mettre à jour le niveau de remplissage du conteneur
        updateContainerFillLevel(container, measurementDto.getFillLevel());

        logger.info("Mesure créée avec succès: {}", savedMeasurement.getId());
        return convertToDto(savedMeasurement);
    }

    @Override
    public void deleteMeasurement(Long id) {
        logger.info("Suppression de la mesure avec ID: {}", id);

        Measurement measurement = measurementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mesure non trouvée avec l'ID: " + id));

        measurementRepository.delete(measurement);
        logger.info("Mesure supprimée avec succès: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MeasurementDto> getMeasurementsByContainer(Long containerId) {
        logger.info("Récupération des mesures pour le conteneur: {}", containerId);
        return measurementRepository.findByContainerId(containerId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MeasurementDto> getMeasurementsBySensor(Long sensorId) {
        logger.info("Récupération des mesures pour le capteur: {}", sensorId);
        return measurementRepository.findBySensorId(sensorId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MeasurementDto> getMeasurementsByDateRange(LocalDateTime start, LocalDateTime end) {
        logger.info("Récupération des mesures entre {} et {}", start, end);
        return measurementRepository.findByMeasuredAtBetween(start, end).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public MeasurementDto recordMeasurement(String sensorId, Double fillLevel, Double temperature, Double humidity) {
        logger.info("Enregistrement d'une mesure pour le capteur: {}, niveau: {}%", sensorId, fillLevel);

        Sensor sensor = sensorRepository.findBySensorId(sensorId)
                .orElseThrow(() -> new RuntimeException("Capteur non trouvé avec le SensorID: " + sensorId));

        if (sensor.getContainer() == null) {
            throw new RuntimeException("Le capteur n'est assigné à aucun conteneur");
        }

        Container container = sensor.getContainer();

        Measurement measurement = new Measurement(sensor, container, fillLevel);
        measurement.setTemperature(temperature);
        measurement.setHumidity(humidity);

        Measurement savedMeasurement = measurementRepository.save(measurement);

        // Mettre à jour le niveau de remplissage du conteneur
        updateContainerFillLevel(container, fillLevel);

        logger.info("Mesure enregistrée avec succès pour le capteur: {}", sensorId);
        return convertToDto(savedMeasurement);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MeasurementDto> getLatestMeasurementsBySensor(Long sensorId, int limit) {
        logger.info("Récupération des {} dernières mesures pour le capteur: {}", limit, sensorId);
        return measurementRepository.findLatestMeasurementsBySensor(sensorId, limit).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MeasurementDto> getContainerMeasurementsHistory(Long containerId, int hours) {
        logger.info("Récupération de l'historique des mesures du conteneur {} sur {} heures", containerId, hours);
        LocalDateTime endTime = LocalDateTime.now();
        LocalDateTime startTime = endTime.minusHours(hours);

        return measurementRepository.findRecentMeasurementsForContainer(containerId, startTime, endTime).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private void updateContainerFillLevel(Container container, Double fillLevel) {
        Double oldFillLevel = container.getCurrentFillLevel();
        container.setCurrentFillLevel(fillLevel);

        // Vérifier si le statut doit être mis à jour
        boolean wasFull = container.isFull();
        boolean isNowFull = fillLevel >= 90.0;

        if (isNowFull && !wasFull) {
            container.setStatus(ContainerStatus.FULL);
            // Publier une alerte Kafka
            sendContainerFullAlert(container);
            logger.info("Alerte conteneur plein envoyée pour: {}", container.getContainerId());
        } else if (!isNowFull && container.getStatus() == ContainerStatus.FULL) {
            container.setStatus(ContainerStatus.ACTIVE);
        }

        containerRepository.save(container);
        logger.debug("Niveau de remplissage mis à jour pour le conteneur {}: {}% -> {}%",
                container.getContainerId(), oldFillLevel, fillLevel);
    }

    private void sendContainerFullAlert(Container container) {
        try {
            ContainerFullAlert alert = new ContainerFullAlert(
                    container.getContainerId(),
                    container.getCurrentFillLevel(),
                    container.getLocation() != null ? container.getLocation().getLatitude() : null,
                    container.getLocation() != null ? container.getLocation().getLongitude() : null,
                    LocalDateTime.now()
            );

            kafkaTemplate.send("container-full-alert", alert);
            logger.debug("Alerte Kafka envoyée pour le conteneur plein: {}", container.getContainerId());

        } catch (Exception e) {
            logger.error("Erreur lors de l'envoi de l'alerte Kafka pour le conteneur: {}", container.getContainerId(), e);
        }
    }

    private MeasurementDto convertToDto(Measurement measurement) {
        MeasurementDto dto = new MeasurementDto();
        dto.setId(measurement.getId());
        dto.setSensorId(measurement.getSensor().getId());
        dto.setSensorIdentifier(measurement.getSensor().getSensorId());
        dto.setContainerId(measurement.getContainer().getId());
        dto.setContainerIdentifier(measurement.getContainer().getContainerId());
        dto.setFillLevel(measurement.getFillLevel());
        dto.setTemperature(measurement.getTemperature());
        dto.setHumidity(measurement.getHumidity());
        dto.setMeasuredAt(measurement.getMeasuredAt());
        return dto;
    }

    // Classe interne pour l'alerte Kafka
    public static class ContainerFullAlert {
        private String containerId;
        private Double fillLevel;
        private Double latitude;
        private Double longitude;
        private LocalDateTime timestamp;

        public ContainerFullAlert(String containerId, Double fillLevel, Double latitude, Double longitude, LocalDateTime timestamp) {
            this.containerId = containerId;
            this.fillLevel = fillLevel;
            this.latitude = latitude;
            this.longitude = longitude;
            this.timestamp = timestamp;
        }

        // Getters et setters
        public String getContainerId() { return containerId; }
        public void setContainerId(String containerId) { this.containerId = containerId; }
        public Double getFillLevel() { return fillLevel; }
        public void setFillLevel(Double fillLevel) { this.fillLevel = fillLevel; }
        public Double getLatitude() { return latitude; }
        public void setLatitude(Double latitude) { this.latitude = latitude; }
        public Double getLongitude() { return longitude; }
        public void setLongitude(Double longitude) { this.longitude = longitude; }
        public LocalDateTime getTimestamp() { return timestamp; }
        public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    }
}