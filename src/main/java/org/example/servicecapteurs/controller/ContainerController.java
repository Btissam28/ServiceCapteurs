package org.example.servicecapteurs.controller;

import org.example.servicecapteurs.domain.dto.ContainerDto;
import org.example.servicecapteurs.service.ContainerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/capteurs/containers")
public class ContainerController {

    private static final Logger logger = LoggerFactory.getLogger(ContainerController.class);

    private final ContainerService containerService;

    public ContainerController(ContainerService containerService) {
        this.containerService = containerService;
    }

    @GetMapping
    public ResponseEntity<List<ContainerDto>> getAllContainers() {
        logger.info("GET /api/capteurs/containers - Récupération de tous les conteneurs");
        List<ContainerDto> containers = containerService.getAllContainers();
        return ResponseEntity.ok(containers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContainerDto> getContainerById(@PathVariable Long id) {
        logger.info("GET /api/capteurs/containers/{} - Récupération du conteneur par ID", id);
        ContainerDto container = containerService.getContainerById(id);
        return ResponseEntity.ok(container);
    }

    @GetMapping("/container-id/{containerId}")
    public ResponseEntity<ContainerDto> getContainerByContainerId(@PathVariable String containerId) {
        logger.info("GET /api/capteurs/containers/container-id/{} - Récupération du conteneur par ContainerID", containerId);
        ContainerDto container = containerService.getContainerByContainerId(containerId);
        return ResponseEntity.ok(container);
    }

    @PostMapping
    public ResponseEntity<ContainerDto> createContainer(@RequestBody ContainerDto containerDto) {
        logger.info("POST /api/capteurs/containers - Création d'un nouveau conteneur");
        ContainerDto createdContainer = containerService.createContainer(containerDto);
        return ResponseEntity.ok(createdContainer);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContainerDto> updateContainer(@PathVariable Long id, @RequestBody ContainerDto containerDto) {
        logger.info("PUT /api/capteurs/containers/{} - Mise à jour du conteneur", id);
        ContainerDto updatedContainer = containerService.updateContainer(id, containerDto);
        return ResponseEntity.ok(updatedContainer);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContainer(@PathVariable Long id) {
        logger.info("DELETE /api/capteurs/containers/{} - Suppression du conteneur", id);
        containerService.deleteContainer(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<ContainerDto>> getContainersByStatus(@PathVariable String status) {
        logger.info("GET /api/capteurs/containers/status/{} - Récupération des conteneurs par statut", status);
        List<ContainerDto> containers = containerService.getContainersByStatus(status);
        return ResponseEntity.ok(containers);
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<ContainerDto>> getContainersByType(@PathVariable String type) {
        logger.info("GET /api/capteurs/containers/type/{} - Récupération des conteneurs par type", type);
        List<ContainerDto> containers = containerService.getContainersByType(type);
        return ResponseEntity.ok(containers);
    }

    @GetMapping("/full")
    public ResponseEntity<List<ContainerDto>> getFullContainers() {
        logger.info("GET /api/capteurs/containers/full - Récupération des conteneurs pleins");
        List<ContainerDto> containers = containerService.getFullContainers();
        return ResponseEntity.ok(containers);
    }

    @GetMapping("/city/{city}")
    public ResponseEntity<List<ContainerDto>> getContainersByCity(@PathVariable String city) {
        logger.info("GET /api/capteurs/containers/city/{} - Récupération des conteneurs par ville", city);
        List<ContainerDto> containers = containerService.getContainersByCity(city);
        return ResponseEntity.ok(containers);
    }

    @PutMapping("/{containerId}/fill-level")
    public ResponseEntity<ContainerDto> updateFillLevel(@PathVariable Long containerId, @RequestParam Double fillLevel) {
        logger.info("PUT /api/capteurs/containers/{}/fill-level - Mise à jour du niveau de remplissage: {}%", containerId, fillLevel);
        ContainerDto updatedContainer = containerService.updateFillLevel(containerId, fillLevel);
        return ResponseEntity.ok(updatedContainer);
    }

    @GetMapping("/fill-level-range")
    public ResponseEntity<List<ContainerDto>> getContainersByFillLevelRange(
            @RequestParam Double minLevel,
            @RequestParam Double maxLevel) {
        logger.info("GET /api/capteurs/containers/fill-level-range - Récupération des conteneurs avec niveau entre {}% et {}%", minLevel, maxLevel);
        List<ContainerDto> containers = containerService.getContainersByFillLevelRange(minLevel, maxLevel);
        return ResponseEntity.ok(containers);
    }

    @PostMapping("/{containerId}/reset-after-collection")
    public ResponseEntity<ContainerDto> resetContainerAfterCollection(@PathVariable Long containerId) {
        logger.info("POST /api/capteurs/containers/{}/reset-after-collection - Réinitialisation après collecte", containerId);
        ContainerDto resetContainer = containerService.resetContainerAfterCollection(containerId);
        return ResponseEntity.ok(resetContainer);
    }
}