package io.andesson.dataflowmanager.features.jobRun.domain.service;

import java.util.List;
import java.util.UUID;

import io.andesson.dataflowmanager.features.jobRun.domain.dto.response.JobRunResponseDTO;
import io.andesson.dataflowmanager.features.jobRun.domain.enums.JobStatus;

public interface IJobRunService {

    /**
     * Lista JobRuns com filtros opcionais da API.
     */
    List<JobRunResponseDTO> findAll(UUID pipelineId, JobStatus status);

    /**
     * Busca um JobRun específico pelo ID.
     */
    JobRunResponseDTO findById(UUID id);

    /**
     * Orquestra a simulação de uma execução de pipeline.
     * Cria um JobRun, o executa e retorna o resultado.
     */
    JobRunResponseDTO runPipeline(UUID pipelineId);
}