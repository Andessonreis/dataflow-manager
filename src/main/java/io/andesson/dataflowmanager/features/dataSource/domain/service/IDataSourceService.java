package io.andesson.dataflowmanager.features.dataSource.domain.service;

import java.util.List;
import java.util.UUID;

import io.andesson.dataflowmanager.features.dataSource.domain.dto.request.DataSourceRequestDTO;
import io.andesson.dataflowmanager.features.dataSource.domain.dto.response.DataSourceResponseDTO;
import io.andesson.dataflowmanager.features.dataSource.domain.enums.DataSourceType;

/**
 * Service interface for managing DataSource entities.
 * Defines operations related to DataSource business logic.
 * 
 * @author Andesson Reis
 */
public interface IDataSourceService {
  // GET (com filtro opcional)
    List<DataSourceResponseDTO> findAll(DataSourceType type);
    
    // GET (por ID)
    DataSourceResponseDTO findById(UUID id);
    
    // POST
    DataSourceResponseDTO create(DataSourceRequestDTO requestDTO);
    
    // PUT
    DataSourceResponseDTO update(UUID id, DataSourceRequestDTO requestDTO);
    
    // DELETE
    void delete(UUID id);
}
