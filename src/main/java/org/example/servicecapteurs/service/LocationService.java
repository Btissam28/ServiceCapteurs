package org.example.servicecapteurs.service;




import org.example.servicecapteurs.domain.dto.LocationDto;

import java.util.List;

public interface LocationService {
    List<LocationDto> getAllLocations();
    LocationDto getLocationById(Long id);
    LocationDto createLocation(LocationDto locationDto);
    LocationDto updateLocation(Long id, LocationDto locationDto);
    void deleteLocation(Long id);
    List<LocationDto> getLocationsByCity(String city);
    List<LocationDto> getLocationsInBoundingBox(Double minLat, Double maxLat, Double minLng, Double maxLng);
    List<LocationDto> getLocationsByPostalCode(String postalCode);
}