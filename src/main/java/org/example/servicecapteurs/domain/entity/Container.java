package org.example.servicecapteurs.domain.entity;

import jakarta.persistence.*;
import org.example.servicecapteurs.domain.enums.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "container")
public class Container {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String containerId;

    @Column(nullable = false)
    private Double capacity;

    private Double currentFillLevel;

    @Enumerated(EnumType.STRING)
    private ContainerType type;

    @Enumerated(EnumType.STRING)
    private ContainerStatus status;

    @Column(nullable = false)
    private LocalDateTime installedAt;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Location location;

    @OneToMany(mappedBy = "container", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Sensor> sensors = new ArrayList<>();

    @OneToMany(mappedBy = "container", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Measurement> measurements = new ArrayList<>();

    public Container() {}

    public Container(String containerId, Double capacity, ContainerType type) {
        this.containerId = containerId;
        this.capacity = capacity;
        this.type = type;
        this.installedAt = LocalDateTime.now();
        this.status = ContainerStatus.ACTIVE;
        this.currentFillLevel = 0.0;
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

    public ContainerType getType() { return type; }
    public void setType(ContainerType type) { this.type = type; }

    public ContainerStatus getStatus() { return status; }
    public void setStatus(ContainerStatus status) { this.status = status; }

    public LocalDateTime getInstalledAt() { return installedAt; }
    public void setInstalledAt(LocalDateTime installedAt) { this.installedAt = installedAt; }

    public Location getLocation() { return location; }
    public void setLocation(Location location) { this.location = location; }

    public List<Sensor> getSensors() { return sensors; }
    public void setSensors(List<Sensor> sensors) { this.sensors = sensors; }

    public List<Measurement> getMeasurements() { return measurements; }
    public void setMeasurements(List<Measurement> measurements) { this.measurements = measurements; }

    public boolean isFull() {
        return currentFillLevel != null && currentFillLevel >= 90.0;
    }
}



