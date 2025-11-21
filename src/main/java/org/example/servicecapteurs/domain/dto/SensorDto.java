package org.example.servicecapteurs.domain.dto;


import java.time.LocalDateTime;

public class SensorDto {
    private Long id;
    private String sensorId;
    private String model;
    private String type;
    private String status;
    private Double batteryLevel;
    private LocalDateTime installedAt;
    private LocalDateTime lastMaintenance;
    private Long containerId;
    private String containerIdentifier;

    public SensorDto() {}

    public SensorDto(Long id, String sensorId, String model, String type) {
        this.id = id;
        this.sensorId = sensorId;
        this.model = model;
        this.type = type;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSensorId() { return sensorId; }
    public void setSensorId(String sensorId) { this.sensorId = sensorId; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Double getBatteryLevel() { return batteryLevel; }
    public void setBatteryLevel(Double batteryLevel) { this.batteryLevel = batteryLevel; }

    public LocalDateTime getInstalledAt() { return installedAt; }
    public void setInstalledAt(LocalDateTime installedAt) { this.installedAt = installedAt; }

    public LocalDateTime getLastMaintenance() { return lastMaintenance; }
    public void setLastMaintenance(LocalDateTime lastMaintenance) { this.lastMaintenance = lastMaintenance; }

    public Long getContainerId() { return containerId; }
    public void setContainerId(Long containerId) { this.containerId = containerId; }

    public String getContainerIdentifier() { return containerIdentifier; }
    public void setContainerIdentifier(String containerIdentifier) { this.containerIdentifier = containerIdentifier; }
}