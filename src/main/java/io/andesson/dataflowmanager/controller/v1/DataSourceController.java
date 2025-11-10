package io.andesson.dataflowmanager.controller.v1;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.andesson.dataflowmanager.features.dataSource.domain.dto.request.DataSourceRequestDTO;
import io.andesson.dataflowmanager.features.dataSource.domain.dto.response.DataSourceResponseDTO;
import io.andesson.dataflowmanager.features.dataSource.domain.enums.DataSourceType;
import io.andesson.dataflowmanager.features.dataSource.domain.service.IDataSourceService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/data-sources")
public class DataSourceController {

    @Autowired // Injeta o Serviço
    private IDataSourceService dataSourceService;

    // GET /api/data-sources
    @GetMapping
    public ResponseEntity<List<DataSourceResponseDTO>> getAllDataSources(
            @RequestParam(required = false) DataSourceType type) {
        List<DataSourceResponseDTO> dataSources = dataSourceService.findAll(type);
        return ResponseEntity.ok(dataSources);
    }

    // GET /api/data-sources/{id}
    @GetMapping("/{id}")
    public ResponseEntity<DataSourceResponseDTO> getDataSourceById(@PathVariable UUID id) {
        DataSourceResponseDTO dataSource = dataSourceService.findById(id);
        return ResponseEntity.ok(dataSource);
    }

    // POST /api/data-sources
    @PostMapping
    public ResponseEntity<DataSourceResponseDTO> createDataSource(
            @Valid @RequestBody DataSourceRequestDTO requestDTO) {
        DataSourceResponseDTO createdDataSource = dataSourceService.create(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDataSource);
    }

    // PUT /api/data-sources/{id}
    @PutMapping("/{id}")
    public ResponseEntity<DataSourceResponseDTO> updateDataSource(
            @PathVariable UUID id,
            @Valid @RequestBody DataSourceRequestDTO requestDTO) {
        DataSourceResponseDTO updatedDataSource = dataSourceService.update(id, requestDTO);
        return ResponseEntity.ok(updatedDataSource);
    }

    // DELETE /api/data-sources/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDataSource(@PathVariable UUID id) {
        dataSourceService.delete(id);
        return ResponseEntity.noContent().build(); // Retorna 204 No Content
    }
}