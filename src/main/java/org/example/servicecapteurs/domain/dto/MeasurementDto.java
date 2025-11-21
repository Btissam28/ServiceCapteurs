package org.example.servicecapteurs.domain.dto;

import java.time.LocalDateTime;

public class MeasurementDto {
    private Long id;
    private Long sensorId;
    private String sensorIdentifier;
    private Long containerId;
    private String containerIdentifier;
    private Double fillLevel;
    private Double temperature;
    private Double humidity;
    private LocalDateTime measuredAt;

    public MeasurementDto() {}

    public MeasurementDto(Long sensorId, Long containerId, Double fillLevel) {
        this.sensorId = sensorId;
        this.containerId = containerId;
        this.fillLevel = fillLevel;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getSensorId() { return sensorId; }
    public void setSensorId(Long sensorId) { this.sensorId = sensorId; }

    public String getSensorIdentifier() { return sensorIdentifier; }
    public void setSensorIdentifier(String sensorIdentifier) { this.sensorIdentifier = sensorIdentifier; }

    public Long getContainerId() { return containerId; }
    public void setContainerId(Long containerId) { this.containerId = containerId; }

    public String getContainerIdentifier() { return containerIdentifier; }
    public void setContainerIdentifier(String containerIdentifier) { this.containerIdentifier = containerIdentifier; }

    public Double getFillLevel() { return fillLevel; }
    public void setFillLevel(Double fillLevel) { this.fillLevel = fillLevel; }

    public Double getTemperature() { return temperature; }
    public void setTemperature(Double temperature) { this.temperature = temperature; }

    public Double getHumidity() { return humidity; }
    public void setHumidity(Double humidity) { this.humidity = humidity; }

    public LocalDateTime getMeasuredAt() { return measuredAt; }
    public void setMeasuredAt(LocalDateTime measuredAt) { this.measuredAt = measuredAt; }
}