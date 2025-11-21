package org.example.servicecapteurs.service.impl;

import org.example.servicecapteurs.domain.dto.ContainerDto;
import org.example.servicecapteurs.domain.dto.LocationDto;
import org.example.servicecapteurs.domain.entity.Container;
import org.example.servicecapteurs.domain.entity.Location;
import org.example.servicecapteurs.domain.enums.ContainerStatus;
import org.example.servicecapteurs.domain.enums.ContainerType;
import org.example.servicecapteurs.repository.ContainerRepository;
import org.example.servicecapteurs.service.ContainerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ContainerServiceImpl implements ContainerService {

    private static final Logger logger = LoggerFactory.getLogger(ContainerServiceImpl.class);

    private final ContainerRepository containerRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public ContainerServiceImpl(ContainerRepository containerRepository,
                                KafkaTemplate<String, Object> kafkaTemplate) {
        this.containerRepository = containerRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContainerDto> getAllContainers() {
        logger.info("Récupération de tous les conteneurs");
        return containerRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ContainerDto getContainerById(Long id) {
        logger.info("Récupération du conteneur avec ID: {}", id);
        Container container = containerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conteneur non trouvé avec l'ID: " + id));
        return convertToDto(container);
    }

    @Override
    @Transactional(readOnly = true)
    public ContainerDto getContainerByContainerId(String containerId) {
        logger.info("Récupération du conteneur avec ContainerID: {}", containerId);
        Container container = containerRepository.findByContainerId(containerId)
                .orElseThrow(() -> new RuntimeException("Conteneur non trouvé avec le ContainerID: " + containerId));
        return convertToDto(container);
    }

    @Override
    public ContainerDto createContainer(ContainerDto containerDto) {
        logger.info("Création d'un nouveau conteneur: {}", containerDto.getContainerId());

        // Vérifier si le containerId existe déjà
        containerRepository.findByContainerId(containerDto.getContainerId())
                .ifPresent(c -> {
                    throw new RuntimeException("Un conteneur avec le ContainerID " + containerDto.getContainerId() + " existe déjà");
                });

        Container container = new Container(
                containerDto.getContainerId(),
                containerDto.getCapacity(),
                ContainerType.valueOf(containerDto.getType())
        );

        container.setCurrentFillLevel(containerDto.getCurrentFillLevel());

        // Créer et assigner l'emplacement si spécifié
        if (containerDto.getLocation() != null) {
            Location location = convertToEntity(containerDto.getLocation());
            container.setLocation(location);
        }

        Container savedContainer = containerRepository.save(container);
        logger.info("Conteneur créé avec succès: {}", savedContainer.getId());

        return convertToDto(savedContainer);
    }

    @Override
    public ContainerDto updateContainer(Long id, ContainerDto containerDto) {
        logger.info("Mise à jour du conteneur avec ID: {}", id);

        Container container = containerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conteneur non trouvé avec l'ID: " + id));

        container.setCapacity(containerDto.getCapacity());
        container.setCurrentFillLevel(containerDto.getCurrentFillLevel());

        if (containerDto.getStatus() != null) {
            container.setStatus(ContainerStatus.valueOf(containerDto.getStatus()));
        }

        Container updatedContainer = containerRepository.save(container);
        logger.info("Conteneur mis à jour avec succès: {}", updatedContainer.getId());

        return convertToDto(updatedContainer);
    }

    @Override
    public void deleteContainer(Long id) {
        logger.info("Suppression du conteneur avec ID: {}", id);

        Container container = containerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conteneur non trouvé avec l'ID: " + id));

        containerRepository.delete(container);
        logger.info("Conteneur supprimé avec succès: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContainerDto> getContainersByStatus(String status) {
        logger.info("Récupération des conteneurs avec statut: {}", status);
        ContainerStatus containerStatus = ContainerStatus.valueOf(status.toUpperCase());
        return containerRepository.findByStatus(containerStatus).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContainerDto> getContainersByType(String type) {
        logger.info("Récupération des conteneurs de type: {}", type);
        ContainerType containerType = ContainerType.valueOf(type.toUpperCase());
        return containerRepository.findByType(containerType).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContainerDto> getFullContainers() {
        logger.info("Récupération des conteneurs pleins");
        return containerRepository.findFullContainers().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContainerDto> getContainersByCity(String city) {
        logger.info("Récupération des conteneurs dans la ville: {}", city);
        return containerRepository.findByCity(city).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public ContainerDto updateFillLevel(Long containerId, Double fillLevel) {
        logger.info("Mise à jour du niveau de remplissage du conteneur {}: {}%", containerId, fillLevel);

        Container container = containerRepository.findById(containerId)
                .orElseThrow(() -> new RuntimeException("Conteneur non trouvé avec l'ID: " + containerId));

        Double oldFillLevel = container.getCurrentFillLevel();
        container.setCurrentFillLevel(fillLevel);

        // Mettre à jour le statut si nécessaire
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

        Container updatedContainer = containerRepository.save(container);
        logger.info("Niveau de remplissage mis à jour pour le conteneur {}: {}% -> {}%",
                containerId, oldFillLevel, fillLevel);

        return convertToDto(updatedContainer);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContainerDto> getContainersByFillLevelRange(Double minLevel, Double maxLevel) {
        logger.info("Récupération des conteneurs avec niveau de remplissage entre {}% et {}%", minLevel, maxLevel);
        return containerRepository.findByFillLevelRange(minLevel, maxLevel).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public ContainerDto resetContainerAfterCollection(Long containerId) {
        logger.info("Réinitialisation du conteneur après collecte: {}", containerId);

        Container container = containerRepository.findById(containerId)
                .orElseThrow(() -> new RuntimeException("Conteneur non trouvé avec l'ID: " + containerId));

        container.setCurrentFillLevel(0.0);
        container.setStatus(ContainerStatus.ACTIVE);

        Container updatedContainer = containerRepository.save(container);
        logger.info("Conteneur réinitialisé après collecte: {}", containerId);

        return convertToDto(updatedContainer);
    }

    private void sendContainerFullAlert(Container container) {
        try {
            ContainerFullAlert alert = new ContainerFullAlert(
                    container.getContainerId(),
                    container.getCurrentFillLevel(),
                    container.getLocation() != null ? container.getLocation().getLatitude() : null,
                    container.getLocation() != null ? container.getLocation().getLongitude() : null,
                    java.time.LocalDateTime.now()
            );

            kafkaTemplate.send("container-full-alert", alert);
            logger.debug("Alerte Kafka envoyée pour le conteneur plein: {}", container.getContainerId());

        } catch (Exception e) {
            logger.error("Erreur lors de l'envoi de l'alerte Kafka pour le conteneur: {}", container.getContainerId(), e);
        }
    }

    private ContainerDto convertToDto(Container container) {
        ContainerDto dto = new ContainerDto();
        dto.setId(container.getId());
        dto.setContainerId(container.getContainerId());
        dto.setCapacity(container.getCapacity());
        dto.setCurrentFillLevel(container.getCurrentFillLevel());
        dto.setType(container.getType().name());
        dto.setStatus(container.getStatus().name());
        dto.setInstalledAt(container.getInstalledAt());
        dto.setIsFull(container.isFull());

        if (container.getLocation() != null) {
            dto.setLocation(convertToDto(container.getLocation()));
        }

        return dto;
    }

    private LocationDto convertToDto(Location location) {
        LocationDto dto = new LocationDto();
        dto.setId(location.getId());
        dto.setLatitude(location.getLatitude());
        dto.setLongitude(location.getLongitude());
        dto.setAddress(location.getAddress());
        dto.setCity(location.getCity());
        dto.setPostalCode(location.getPostalCode());
        dto.setDescription(location.getDescription());
        return dto;
    }

    private Location convertToEntity(LocationDto locationDto) {
        Location location = new Location();
        location.setLatitude(locationDto.getLatitude());
        location.setLongitude(locationDto.getLongitude());
        location.setAddress(locationDto.getAddress());
        location.setCity(locationDto.getCity());
        location.setPostalCode(locationDto.getPostalCode());
        location.setDescription(locationDto.getDescription());
        return location;
    }

    // Classe interne pour l'alerte Kafka
    public static class ContainerFullAlert {
        private String containerId;
        private Double fillLevel;
        private Double latitude;
        private Double longitude;
        private java.time.LocalDateTime timestamp;

        public ContainerFullAlert(String containerId, Double fillLevel, Double latitude, Double longitude, java.time.LocalDateTime timestamp) {
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
        public java.time.LocalDateTime getTimestamp() { return timestamp; }
        public void setTimestamp(java.time.LocalDateTime timestamp) { this.timestamp = timestamp; }
    }
}
