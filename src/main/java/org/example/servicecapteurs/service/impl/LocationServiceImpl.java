package org.example.servicecapteurs.service.impl;


import org.example.servicecapteurs.domain.dto.LocationDto;
import org.example.servicecapteurs.domain.entity.Location;
import org.example.servicecapteurs.repository.LocationRepository;
import org.example.servicecapteurs.service.LocationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LocationServiceImpl implements LocationService {

    private static final Logger logger = LoggerFactory.getLogger(LocationServiceImpl.class);

    private final LocationRepository locationRepository;

    public LocationServiceImpl(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<LocationDto> getAllLocations() {
        logger.info("Récupération de tous les emplacements");
        return locationRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public LocationDto getLocationById(Long id) {
        logger.info("Récupération de l'emplacement avec ID: {}", id);
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Emplacement non trouvé avec l'ID: " + id));
        return convertToDto(location);
    }

    @Override
    public LocationDto createLocation(LocationDto locationDto) {
        logger.info("Création d'un nouvel emplacement: {}, {}", locationDto.getLatitude(), locationDto.getLongitude());

        Location location = new Location(locationDto.getLatitude(), locationDto.getLongitude());
        location.setAddress(locationDto.getAddress());
        location.setCity(locationDto.getCity());
        location.setPostalCode(locationDto.getPostalCode());
        location.setDescription(locationDto.getDescription());

        Location savedLocation = locationRepository.save(location);
        logger.info("Emplacement créé avec succès: {}", savedLocation.getId());

        return convertToDto(savedLocation);
    }

    @Override
    public LocationDto updateLocation(Long id, LocationDto locationDto) {
        logger.info("Mise à jour de l'emplacement avec ID: {}", id);

        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Emplacement non trouvé avec l'ID: " + id));

        location.setLatitude(locationDto.getLatitude());
        location.setLongitude(locationDto.getLongitude());
        location.setAddress(locationDto.getAddress());
        location.setCity(locationDto.getCity());
        location.setPostalCode(locationDto.getPostalCode());
        location.setDescription(locationDto.getDescription());

        Location updatedLocation = locationRepository.save(location);
        logger.info("Emplacement mis à jour avec succès: {}", updatedLocation.getId());

        return convertToDto(updatedLocation);
    }

    @Override
    public void deleteLocation(Long id) {
        logger.info("Suppression de l'emplacement avec ID: {}", id);

        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Emplacement non trouvé avec l'ID: " + id));

        locationRepository.delete(location);
        logger.info("Emplacement supprimé avec succès: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LocationDto> getLocationsByCity(String city) {
        logger.info("Récupération des emplacements dans la ville: {}", city);
        return locationRepository.findByCity(city).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<LocationDto> getLocationsInBoundingBox(Double minLat, Double maxLat, Double minLng, Double maxLng) {
        logger.info("Récupération des emplacements dans la zone: [{}, {}] x [{}, {}]", minLat, maxLat, minLng, maxLng);
        return locationRepository.findByBoundingBox(minLat, maxLat, minLng, maxLng).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<LocationDto> getLocationsByPostalCode(String postalCode) {
        logger.info("Récupération des emplacements avec code postal: {}", postalCode);
        return locationRepository.findByPostalCode(postalCode).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private LocationDto convertToDto(Location location) {
        LocationDto dto = new LocationDto();
        dto.setId(location.getId());
        dto.setLatitude(location.getLatitude());
        dto.setLongitude(location.getLongitude());
        dto.setAddress(location.getAddress());
        dto.setCity(location.getCity());
        dto.setPostalCode(location.getPostalCode());
        dto.setDescription(location.getDescription());
        return dto;
    }
}