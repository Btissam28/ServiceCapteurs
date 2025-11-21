package org.example.servicecapteurs.service;


import org.example.servicecapteurs.domain.dto.ContainerDto;

import java.util.List;

public interface ContainerService {
    List<ContainerDto> getAllContainers();
    ContainerDto getContainerById(Long id);
    ContainerDto getContainerByContainerId(String containerId);
    ContainerDto createContainer(ContainerDto containerDto);
    ContainerDto updateContainer(Long id, ContainerDto containerDto);
    void deleteContainer(Long id);
    List<ContainerDto> getContainersByStatus(String status);
    List<ContainerDto> getContainersByType(String type);
    List<ContainerDto> getFullContainers();
    List<ContainerDto> getContainersByCity(String city);
    ContainerDto updateFillLevel(Long containerId, Double fillLevel);
    List<ContainerDto> getContainersByFillLevelRange(Double minLevel, Double maxLevel);
    ContainerDto resetContainerAfterCollection(Long containerId);
}