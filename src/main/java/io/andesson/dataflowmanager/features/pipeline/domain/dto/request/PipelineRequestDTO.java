package io.andesson.dataflowmanager.features.pipeline.domain.dto.request;

import io.andesson.dataflowmanager.features.pipeline.domain.enums.StatusPipeline;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record PipelineRequestDTO(
        @NotBlank(message = "O nome não pode estar em branco")
        String name,

        @NotBlank(message = "O schedule (cron) não pode estar em branco")
        String schedule,

        @NotNull(message = "O status não pode ser nulo")
        StatusPipeline status,

        @NotNull(message = "O sourceId (DataSource) não pode ser nulo")
        UUID sourceId
) {
}