package io.andesson.dataflowmanager.features.dataSource.domain.dto.request;
import io.andesson.dataflowmanager.features.dataSource.domain.enums.DataSourceType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record DataSourceRequestDTO(
        @NotBlank(message = "O nome não pode estar em branco") 
        @Size(min = 3, message = "O nome deve ter pelo menos 3 caracteres") 
        String name,

        @NotNull(message = "O tipo não pode ser nulo") 
        DataSourceType type,

        @NotBlank(message = "A URI de conexão não pode estar em branco") 
        String connectionUri
) {
}