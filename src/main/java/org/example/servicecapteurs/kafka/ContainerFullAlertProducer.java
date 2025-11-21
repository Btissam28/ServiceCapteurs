package org.example.servicecapteurs.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class ContainerFullAlertProducer {

    private static final Logger logger = LoggerFactory.getLogger(ContainerFullAlertProducer.class);

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public ContainerFullAlertProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendContainerFullAlert(String containerId, Double fillLevel, Double latitude, Double longitude) {
        try {
            ContainerFullAlert alert = new ContainerFullAlert(containerId, fillLevel, latitude, longitude, java.time.LocalDateTime.now());
            kafkaTemplate.send("container-full-alert", alert);
            logger.info("Alerte conteneur plein envoyée: {} ({}%)", containerId, fillLevel);
        } catch (Exception e) {
            logger.error("Erreur lors de l'envoi de l'alerte Kafka pour le conteneur: {}", containerId, e);
        }
    }

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