package io.andesson.dataflowmanager.features.dataSource.domain.service;
/**
 * Service implementation for managing DataSource entities.
 * Implements operations defined in the IDataSourceService interface.
 * 
 * @author Andesson Reis
 */

import io.andesson.dataflowmanager.config.exceptions.ResourceNotFoundException;
import io.andesson.dataflowmanager.features.dataSource.domain.dto.request.DataSourceRequestDTO;
import io.andesson.dataflowmanager.features.dataSource.domain.dto.response.DataSourceResponseDTO;
import io.andesson.dataflowmanager.features.dataSource.domain.enums.DataSourceType;
import io.andesson.dataflowmanager.features.dataSource.domain.model.DataSource;
import io.andesson.dataflowmanager.features.dataSource.domain.repository.IDataSourceRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service // Marca como um componente de Serviço do Spring
public class DataSourceService implements IDataSourceService {

    @Autowired // Injeta o Repositório
    private IDataSourceRepository dataSourceRepository;

    @Override
    @Transactional(readOnly = true) // Boa prática para métodos de leitura
    public List<DataSourceResponseDTO> findAll(DataSourceType type) {
        List<DataSource> dataSources;
        if (type != null) {
            // Precisamos adicionar este método no IDataSourceRepository!
            dataSources = dataSourceRepository.findByType(type);
        } else {
            dataSources = dataSourceRepository.findAll();
        }
        
        // Mapeia a Lista de Entidades para uma Lista de DTOs
        return dataSources.stream()
                .map(DataSourceResponseDTO::new) // (dataSource -> new DataSourceResponseDTO(dataSource))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public DataSourceResponseDTO findById(UUID id) {
        DataSource dataSource = findDataSourceById(id);
        return new DataSourceResponseDTO(dataSource);
    }

    @Override
    @Transactional // Transação de escrita
    public DataSourceResponseDTO create(DataSourceRequestDTO requestDTO) {
        // Mapeia DTO de Request para Entidade
        DataSource newDataSource = new DataSource();
        newDataSource.setName(requestDTO.name());
        newDataSource.setType(requestDTO.type());
        newDataSource.setConnectionUri(requestDTO.connectionUri());
        
        DataSource savedDataSource = dataSourceRepository.save(newDataSource);
        
        return new DataSourceResponseDTO(savedDataSource);
    }

    @Override
    @Transactional
    public DataSourceResponseDTO update(UUID id, DataSourceRequestDTO requestDTO) {
        // Primeiro, busca a entidade existente
        DataSource existingDataSource = findDataSourceById(id);
        
        // Atualiza os campos
        existingDataSource.setName(requestDTO.name());
        existingDataSource.setType(requestDTO.type());
        existingDataSource.setConnectionUri(requestDTO.connectionUri());
        
        DataSource updatedDataSource = dataSourceRepository.save(existingDataSource);
        
        return new DataSourceResponseDTO(updatedDataSource);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        DataSource dataSource = findDataSourceById(id);
        dataSourceRepository.delete(dataSource);
    }
    
    // --- Método utilitário privado ---
    
    /**
     * Busca um DataSource pelo ID ou lança ResourceNotFoundException.
     * Evita repetição de código.
     */
    private DataSource findDataSourceById(UUID id) {
        return dataSourceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DataSource não encontrado com o ID: " + id));
    }
}