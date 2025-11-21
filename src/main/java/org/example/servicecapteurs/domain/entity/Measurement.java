package org.example.servicecapteurs.domain.entity;


import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "measurement")
public class Measurement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sensor_id", nullable = false)
    private Sensor sensor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "container_id", nullable = false)
    private Container container;

    @Column(nullable = false)
    private Double fillLevel;

    private Double temperature;
    private Double humidity;

    @Column(nullable = false)
    private LocalDateTime measuredAt;

    public Measurement() {}

    public Measurement(Sensor sensor, Container container, Double fillLevel) {
        this.sensor = sensor;
        this.container = container;
        this.fillLevel = fillLevel;
        this.measuredAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Sensor getSensor() { return sensor; }
    public void setSensor(Sensor sensor) { this.sensor = sensor; }

    public Container getContainer() { return container; }
    public void setContainer(Container container) { this.container = container; }

    public Double getFillLevel() { return fillLevel; }
    public void setFillLevel(Double fillLevel) { this.fillLevel = fillLevel; }

    public Double getTemperature() { return temperature; }
    public void setTemperature(Double temperature) { this.temperature = temperature; }

    public Double getHumidity() { return humidity; }
    public void setHumidity(Double humidity) { this.humidity = humidity; }

    public LocalDateTime getMeasuredAt() { return measuredAt; }
    public void setMeasuredAt(LocalDateTime measuredAt) { this.measuredAt = measuredAt; }
}