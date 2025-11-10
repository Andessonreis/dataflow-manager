package io.andesson.dataflowmanager.features.pipeline.domain.dto.response;

import io.andesson.dataflowmanager.features.pipeline.domain.enums.StatusPipeline;
import io.andesson.dataflowmanager.features.pipeline.domain.model.Pipeline;

import java.util.UUID;

public record PipelineResponseDTO(
        UUID id,
        String name,
        String schedule,
        StatusPipeline status,
        UUID sourceId // Conforme sua especificação, apenas o ID da fonte
) {
    // Construtor de conveniência para mapear da Entidade para o DTO
    public PipelineResponseDTO(Pipeline pipeline) {
        this(
                pipeline.getId(),
                pipeline.getName(),
                pipeline.getSchedule(),
                pipeline.getStatus(),
                // Extrai o ID do objeto DataSource associado
                pipeline.getDataSource() != null ? pipeline.getDataSource().getId() : null
        );
    }
}