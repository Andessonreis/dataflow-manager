package io.andesson.dataflowmanager.features.pipeline.domain.service;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.andesson.dataflowmanager.config.exceptions.ResourceNotFoundException;
import io.andesson.dataflowmanager.features.dataSource.domain.model.DataSource;
import io.andesson.dataflowmanager.features.dataSource.domain.repository.IDataSourceRepository;
import io.andesson.dataflowmanager.features.pipeline.domain.dto.request.PipelineRequestDTO;
import io.andesson.dataflowmanager.features.pipeline.domain.dto.response.PipelineResponseDTO;
import io.andesson.dataflowmanager.features.pipeline.domain.model.Pipeline;
import io.andesson.dataflowmanager.features.pipeline.domain.repository.IPipelineRepository;

@Service
public class PipelineService implements IPipelineService {

    @Autowired
    private IPipelineRepository pipelineRepository;

    @Autowired
    private IDataSourceRepository dataSourceRepository; // Para validar o sourceId

    @Override
    @Transactional(readOnly = true)
    public List<PipelineResponseDTO> findAll() {
        return pipelineRepository.findAll().stream()
                .map(PipelineResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PipelineResponseDTO findById(UUID id) {
        Pipeline pipeline = findPipelineById(id);
        return new PipelineResponseDTO(pipeline);
    }

    @Override
    @Transactional
    public PipelineResponseDTO create(PipelineRequestDTO requestDTO) {
        // Valida se o DataSource referenciado existe
        DataSource dataSource = findDataSourceById(requestDTO.sourceId());

        Pipeline newPipeline = new Pipeline();
        newPipeline.setName(requestDTO.name());
        newPipeline.setSchedule(requestDTO.schedule());
        newPipeline.setStatus(requestDTO.status());
        newPipeline.setDataSource(dataSource); // Associa a entidade

        Pipeline savedPipeline = pipelineRepository.save(newPipeline);
        return new PipelineResponseDTO(savedPipeline);
    }

    @Override
    @Transactional
    public PipelineResponseDTO update(UUID id, PipelineRequestDTO requestDTO) {
        // Busca a pipeline existente
        Pipeline existingPipeline = findPipelineById(id);
        
        // Valida se o novo DataSource (ou o mesmo) existe
        DataSource dataSource = findDataSourceById(requestDTO.sourceId());

        // Atualiza os campos
        existingPipeline.setName(requestDTO.name());
        existingPipeline.setSchedule(requestDTO.schedule());
        existingPipeline.setStatus(requestDTO.status());
        existingPipeline.setDataSource(dataSource);

        Pipeline updatedPipeline = pipelineRepository.save(existingPipeline);
        return new PipelineResponseDTO(updatedPipeline);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        Pipeline pipeline = findPipelineById(id);
        // TODO: Adicionar lógica futura - o que acontece com os JobRuns?
        // Por enquanto, apenas deletamos a pipeline.
        pipelineRepository.delete(pipeline);
    }
    
    // --- Métodos utilitários privados ---

    private Pipeline findPipelineById(UUID id) {
        return pipelineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pipeline não encontrada com o ID: " + id));
    }

    private DataSource findDataSourceById(UUID id) {
        return dataSourceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DataSource não encontrado com o ID: " + id));
    }
}