package io.andesson.dataflowmanager.features.jobRun.domain.dto.response;

import io.andesson.dataflowmanager.features.jobRun.domain.enums.JobStatus;
import io.andesson.dataflowmanager.features.jobRun.domain.model.JobRun;

import java.time.LocalDateTime;
import java.util.UUID;

public record JobRunResponseDTO(
        UUID id,
        UUID pipelineId,
        JobStatus status,
        LocalDateTime startTime,
        LocalDateTime endTime,
        Boolean success,
        String logs,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    // Construtor de conveniência
    public JobRunResponseDTO(JobRun jobRun) {
        this(
                jobRun.getId(),
                jobRun.getPipelineId(), // Usando seu método de domínio!
                jobRun.getStatus(),
                jobRun.getStartTime(),
                jobRun.getEndTime(),
                jobRun.getSuccess(),
                jobRun.getLogs(),
                jobRun.getCreatedAt(),
                jobRun.getUpdatedAt()
        );
    }
}