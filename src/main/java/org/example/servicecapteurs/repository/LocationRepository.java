package org.example.servicecapteurs.repository;

import org.example.servicecapteurs.domain.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    @Query("SELECT l FROM Location l WHERE l.latitude BETWEEN :minLat AND :maxLat AND l.longitude BETWEEN :minLng AND :maxLng")
    List<Location> findByBoundingBox(Double minLat, Double maxLat, Double minLng, Double maxLng);

    List<Location> findByCity(String city);

    @Query("SELECT l FROM Location l WHERE l.postalCode = :postalCode")
    List<Location> findByPostalCode(String postalCode);
}