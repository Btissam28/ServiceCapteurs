package org.example.servicecapteurs.service.impl;

import org.example.servicecapteurs.domain.dto.SensorDto;
import org.example.servicecapteurs.domain.entity.Container;
import org.example.servicecapteurs.domain.entity.Sensor;
import org.example.servicecapteurs.domain.enums.SensorStatus;
import org.example.servicecapteurs.domain.enums.SensorType;
import org.example.servicecapteurs.repository.ContainerRepository;
import org.example.servicecapteurs.repository.SensorRepository;
import org.example.servicecapteurs.service.SensorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SensorServiceImpl implements SensorService {

    private static final Logger logger = LoggerFactory.getLogger(SensorServiceImpl.class);

    private final SensorRepository sensorRepository;
    private final ContainerRepository containerRepository;

    public SensorServiceImpl(SensorRepository sensorRepository, ContainerRepository containerRepository) {
        this.sensorRepository = sensorRepository;
        this.containerRepository = containerRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<SensorDto> getAllSensors() {
        logger.info("Récupération de tous les capteurs");
        return sensorRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public SensorDto getSensorById(Long id) {
        logger.info("Récupération du capteur avec ID: {}", id);
        Sensor sensor = sensorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Capteur non trouvé avec l'ID: " + id));
        return convertToDto(sensor);
    }

    @Override
    @Transactional(readOnly = true)
    public SensorDto getSensorBySensorId(String sensorId) {
        logger.info("Récupération du capteur avec SensorID: {}", sensorId);
        Sensor sensor = sensorRepository.findBySensorId(sensorId)
                .orElseThrow(() -> new RuntimeException("Capteur non trouvé avec le SensorID: " + sensorId));
        return convertToDto(sensor);
    }

    @Override
    public SensorDto createSensor(SensorDto sensorDto) {
        logger.info("Création d'un nouveau capteur: {}", sensorDto.getSensorId());

        // Vérifier si le sensorId existe déjà
        sensorRepository.findBySensorId(sensorDto.getSensorId())
                .ifPresent(s -> {
                    throw new RuntimeException("Un capteur avec le SensorID " + sensorDto.getSensorId() + " existe déjà");
                });

        Sensor sensor = new Sensor(
                sensorDto.getSensorId(),
                sensorDto.getModel(),
                SensorType.valueOf(sensorDto.getType())
        );

        sensor.setBatteryLevel(sensorDto.getBatteryLevel());
        sensor.setLastMaintenance(sensorDto.getLastMaintenance());

        // Assigner au conteneur si spécifié
        if (sensorDto.getContainerId() != null) {
            Container container = containerRepository.findById(sensorDto.getContainerId())
                    .orElseThrow(() -> new RuntimeException("Conteneur non trouvé avec l'ID: " + sensorDto.getContainerId()));
            sensor.setContainer(container);
        }

        Sensor savedSensor = sensorRepository.save(sensor);
        logger.info("Capteur créé avec succès: {}", savedSensor.getId());

        return convertToDto(savedSensor);
    }

    @Override
    public SensorDto updateSensor(Long id, SensorDto sensorDto) {
        logger.info("Mise à jour du capteur avec ID: {}", id);

        Sensor sensor = sensorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Capteur non trouvé avec l'ID: " + id));

        sensor.setModel(sensorDto.getModel());
        sensor.setBatteryLevel(sensorDto.getBatteryLevel());

        if (sensorDto.getStatus() != null) {
            sensor.setStatus(SensorStatus.valueOf(sensorDto.getStatus()));
        }

        sensor.setLastMaintenance(sensorDto.getLastMaintenance());

        Sensor updatedSensor = sensorRepository.save(sensor);
        logger.info("Capteur mis à jour avec succès: {}", updatedSensor.getId());

        return convertToDto(updatedSensor);
    }

    @Override
    public void deleteSensor(Long id) {
        logger.info("Suppression du capteur avec ID: {}", id);

        Sensor sensor = sensorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Capteur non trouvé avec l'ID: " + id));

        sensorRepository.delete(sensor);
        logger.info("Capteur supprimé avec succès: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SensorDto> getSensorsByStatus(String status) {
        logger.info("Récupération des capteurs avec statut: {}", status);
        SensorStatus sensorStatus = SensorStatus.valueOf(status.toUpperCase());
        return sensorRepository.findByStatus(sensorStatus).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SensorDto> getSensorsByContainer(Long containerId) {
        logger.info("Récupération des capteurs pour le conteneur: {}", containerId);
        return sensorRepository.findByContainerId(containerId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public SensorDto assignToContainer(Long sensorId, Long containerId) {
        logger.info("Assignation du capteur {} au conteneur {}", sensorId, containerId);

        Sensor sensor = sensorRepository.findById(sensorId)
                .orElseThrow(() -> new RuntimeException("Capteur non trouvé avec l'ID: " + sensorId));
        Container container = containerRepository.findById(containerId)
                .orElseThrow(() -> new RuntimeException("Conteneur non trouvé avec l'ID: " + containerId));

        sensor.setContainer(container);
        Sensor updatedSensor = sensorRepository.save(sensor);

        logger.info("Capteur {} assigné au conteneur {} avec succès", sensorId, containerId);
        return convertToDto(updatedSensor);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SensorDto> getSensorsWithLowBattery(Double batteryThreshold) {
        logger.info("Récupération des capteurs avec batterie faible (< {})", batteryThreshold);
        return sensorRepository.findSensorsWithLowBattery(batteryThreshold).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public SensorDto updateBatteryLevel(Long sensorId, Double batteryLevel) {
        logger.info("Mise à jour du niveau de batterie du capteur {}: {}", sensorId, batteryLevel);

        Sensor sensor = sensorRepository.findById(sensorId)
                .orElseThrow(() -> new RuntimeException("Capteur non trouvé avec l'ID: " + sensorId));

        sensor.setBatteryLevel(batteryLevel);

        // Mettre à jour le statut si la batterie est faible
        if (batteryLevel < 20.0) {
            sensor.setStatus(SensorStatus.MAINTENANCE);
        }

        Sensor updatedSensor = sensorRepository.save(sensor);
        logger.info("Niveau de batterie mis à jour pour le capteur {}", sensorId);

        return convertToDto(updatedSensor);
    }

    private SensorDto convertToDto(Sensor sensor) {
        SensorDto dto = new SensorDto();
        dto.setId(sensor.getId());
        dto.setSensorId(sensor.getSensorId());
        dto.setModel(sensor.getModel());
        dto.setType(sensor.getType().name());
        dto.setStatus(sensor.getStatus().name());
        dto.setBatteryLevel(sensor.getBatteryLevel());
        dto.setInstalledAt(sensor.getInstalledAt());
        dto.setLastMaintenance(sensor.getLastMaintenance());

        if (sensor.getContainer() != null) {
            dto.setContainerId(sensor.getContainer().getId());
            dto.setContainerIdentifier(sensor.getContainer().getContainerId());
        }

        return dto;
    }
}
