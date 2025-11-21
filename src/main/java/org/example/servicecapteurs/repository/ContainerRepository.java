package org.example.servicecapteurs.repository;

import org.example.servicecapteurs.domain.entity.Container;
import org.example.servicecapteurs.domain.enums.ContainerStatus;
import org.example.servicecapteurs.domain.enums.ContainerType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ContainerRepository extends JpaRepository<Container, Long> {
    Optional<Container> findByContainerId(String containerId);
    List<Container> findByStatus(ContainerStatus status);
    List<Container> findByType(ContainerType type);
    List<Container> findByCurrentFillLevelGreaterThanEqual(Double fillLevel);

    @Query("SELECT c FROM Container c WHERE c.currentFillLevel >= 90.0")
    List<Container> findFullContainers();

    @Query("SELECT c FROM Container c WHERE c.location.city = :city")
    List<Container> findByCity(String city);

    @Query("SELECT c FROM Container c WHERE c.currentFillLevel BETWEEN :minLevel AND :maxLevel")
    List<Container> findByFillLevelRange(Double minLevel, Double maxLevel);
}