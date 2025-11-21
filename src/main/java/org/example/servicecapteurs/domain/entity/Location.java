package org.example.servicecapteurs.domain.entity;
import jakarta.persistence.*;

@Entity
@Table(name = "location")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    private String address;
    private String city;
    private String postalCode;
    private String description;

    @OneToOne(mappedBy = "location", fetch = FetchType.LAZY)
    private Container container;

    public Location() {}

    public Location(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Location(Double latitude, Double longitude, String address, String city) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.city = city;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getPostalCode() { return postalCode; }
    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Container getContainer() { return container; }
    public void setContainer(Container container) { this.container = container; }
}