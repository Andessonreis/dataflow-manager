package io.andesson.dataflowmanager.features.pipeline.domain.service;

import java.util.List;
import java.util.UUID;

import io.andesson.dataflowmanager.features.pipeline.domain.dto.request.PipelineRequestDTO;
import io.andesson.dataflowmanager.features.pipeline.domain.dto.response.PipelineResponseDTO;

public interface IPipelineService {
  List<PipelineResponseDTO> findAll();

    PipelineResponseDTO findById(UUID id);

    PipelineResponseDTO create(PipelineRequestDTO requestDTO);

    PipelineResponseDTO update(UUID id, PipelineRequestDTO requestDTO);

    void delete(UUID id);
}
