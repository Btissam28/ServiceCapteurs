package org.example.servicecapteurs.controller;

import org.example.servicecapteurs.domain.dto.LocationDto;
import org.example.servicecapteurs.service.LocationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/capteurs/locations")
public class LocationController {

    private static final Logger logger = LoggerFactory.getLogger(LocationController.class);

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping
    public ResponseEntity<List<LocationDto>> getAllLocations() {
        logger.info("GET /api/capteurs/locations - Récupération de tous les emplacements");
        List<LocationDto> locations = locationService.getAllLocations();
        return ResponseEntity.ok(locations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LocationDto> getLocationById(@PathVariable Long id) {
        logger.info("GET /api/capteurs/locations/{} - Récupération de l'emplacement par ID", id);
        LocationDto location = locationService.getLocationById(id);
        return ResponseEntity.ok(location);
    }

    @PostMapping
    public ResponseEntity<LocationDto> createLocation(@RequestBody LocationDto locationDto) {
        logger.info("POST /api/capteurs/locations - Création d'un nouvel emplacement");
        LocationDto createdLocation = locationService.createLocation(locationDto);
        return ResponseEntity.ok(createdLocation);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LocationDto> updateLocation(@PathVariable Long id, @RequestBody LocationDto locationDto) {
        logger.info("PUT /api/capteurs/locations/{} - Mise à jour de l'emplacement", id);
        LocationDto updatedLocation = locationService.updateLocation(id, locationDto);
        return ResponseEntity.ok(updatedLocation);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocation(@PathVariable Long id) {
        logger.info("DELETE /api/capteurs/locations/{} - Suppression de l'emplacement", id);
        locationService.deleteLocation(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/city/{city}")
    public ResponseEntity<List<LocationDto>> getLocationsByCity(@PathVariable String city) {
        logger.info("GET /api/capteurs/locations/city/{} - Récupération des emplacements par ville", city);
        List<LocationDto> locations = locationService.getLocationsByCity(city);
        return ResponseEntity.ok(locations);
    }

    @GetMapping("/bounding-box")
    public ResponseEntity<List<LocationDto>> getLocationsInBoundingBox(
            @RequestParam Double minLat,
            @RequestParam Double maxLat,
            @RequestParam Double minLng,
            @RequestParam Double maxLng) {
        logger.info("GET /api/capteurs/locations/bounding-box - Récupération des emplacements dans la zone [{}, {}] x [{}, {}]",
                minLat, maxLat, minLng, maxLng);
        List<LocationDto> locations = locationService.getLocationsInBoundingBox(minLat, maxLat, minLng, maxLng);
        return ResponseEntity.ok(locations);
    }

    @GetMapping("/postal-code/{postalCode}")
    public ResponseEntity<List<LocationDto>> getLocationsByPostalCode(@PathVariable String postalCode) {
        logger.info("GET /api/capteurs/locations/postal-code/{} - Récupération des emplacements par code postal", postalCode);
        List<LocationDto> locations = locationService.getLocationsByPostalCode(postalCode);
        return ResponseEntity.ok(locations);
    }
}