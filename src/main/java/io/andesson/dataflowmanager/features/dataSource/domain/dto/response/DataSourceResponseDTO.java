package io.andesson.dataflowmanager.features.dataSource.domain.dto.response;

import java.util.UUID;

import io.andesson.dataflowmanager.features.dataSource.domain.enums.DataSourceType;
import io.andesson.dataflowmanager.features.dataSource.domain.model.DataSource;

public record DataSourceResponseDTO(
        UUID id,
        String name,
        DataSourceType type,
        String connectionUri
) {
    // Construtor de conveniência para mapear da Entidade para o DTO
    public DataSourceResponseDTO(DataSource dataSource) {
        this(
                dataSource.getId(),
                dataSource.getName(),
                dataSource.getType(),
                dataSource.getConnectionUri()
        );
    }
}