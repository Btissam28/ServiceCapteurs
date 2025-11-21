package org.example.servicecapteurs.domain.dto;

import java.time.LocalDateTime;

public class ContainerDto {
    private Long id;
    private String containerId;
    private Double capacity;
    private Double currentFillLevel;
    private String type;
    private String status;
    private LocalDateTime installedAt;
    private LocationDto location;
    private Boolean isFull;

    public ContainerDto() {}

    public ContainerDto(Long id, String containerId, Double capacity, String type) {
        this.id = id;
        this.containerId = containerId;
        this.capacity = capacity;
        this.type = type;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getContainerId() { return containerId; }
    public void setContainerId(String containerId) { this.containerId = containerId; }

    public Double getCapacity() { return capacity; }
    public void setCapacity(Double capacity) { this.capacity = capacity; }

    public Double getCurrentFillLevel() { return currentFillLevel; }
    public void setCurrentFillLevel(Double currentFillLevel) { this.currentFillLevel = currentFillLevel; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getInstalledAt() { return installedAt; }
    public void setInstalledAt(LocalDateTime installedAt) { this.installedAt = installedAt; }

    public LocationDto getLocation() { return location; }
    public void setLocation(LocationDto location) { this.location = location; }

    public Boolean getIsFull() { return isFull; }
    public void setIsFull(Boolean isFull) { this.isFull = isFull; }
}