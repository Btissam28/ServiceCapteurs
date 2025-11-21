package org.example.servicecapteurs.domain.entity;


import jakarta.persistence.*;
import org.example.servicecapteurs.domain.enums.SensorStatus;
import org.example.servicecapteurs.domain.enums.SensorType;

import java.time.LocalDateTime;

@Entity
@Table(name = "sensor")
public class Sensor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String sensorId;

    @Column(nullable = false)
    private String model;

    @Enumerated(EnumType.STRING)
    private SensorType type;

    @Enumerated(EnumType.STRING)
    private SensorStatus status;

    private Double batteryLevel;

    @Column(nullable = false)
    private LocalDateTime installedAt;

    private LocalDateTime lastMaintenance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "container_id")
    private Container container;

    public Sensor() {}

    public Sensor(String sensorId, String model, SensorType type) {
        this.sensorId = sensorId;
        this.model = model;
        this.type = type;
        this.installedAt = LocalDateTime.now();
        this.status = SensorStatus.ACTIVE;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSensorId() { return sensorId; }
    public void setSensorId(String sensorId) { this.sensorId = sensorId; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public SensorType getType() { return type; }
    public void setType(SensorType type) { this.type = type; }

    public SensorStatus getStatus() { return status; }
    public void setStatus(SensorStatus status) { this.status = status; }

    public Double getBatteryLevel() { return batteryLevel; }
    public void setBatteryLevel(Double batteryLevel) { this.batteryLevel = batteryLevel; }

    public LocalDateTime getInstalledAt() { return installedAt; }
    public void setInstalledAt(LocalDateTime installedAt) { this.installedAt = installedAt; }

    public LocalDateTime getLastMaintenance() { return lastMaintenance; }
    public void setLastMaintenance(LocalDateTime lastMaintenance) { this.lastMaintenance = lastMaintenance; }

    public Container getContainer() { return container; }
    public void setContainer(Container container) { this.container = container; }
}

