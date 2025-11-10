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
import org.springframework.web.bind.annotation.RestController;

import io.andesson.dataflowmanager.features.jobRun.domain.dto.response.JobRunResponseDTO;
import io.andesson.dataflowmanager.features.jobRun.domain.service.IJobRunService;
import io.andesson.dataflowmanager.features.pipeline.domain.dto.request.PipelineRequestDTO;
import io.andesson.dataflowmanager.features.pipeline.domain.dto.response.PipelineResponseDTO;
import io.andesson.dataflowmanager.features.pipeline.domain.service.IPipelineService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/pipelines")
public class PipelineController {

    @Autowired
    private IPipelineService pipelineService;
    
    @Autowired
    private IJobRunService jobRunService;

    // GET /api/pipelines
    @GetMapping
    public ResponseEntity<List<PipelineResponseDTO>> getAllPipelines() {
        return ResponseEntity.ok(pipelineService.findAll());
    }

    // GET /api/pipelines/{id}
    @GetMapping("/{id}")
    public ResponseEntity<PipelineResponseDTO> getPipelineById(@PathVariable UUID id) {
        return ResponseEntity.ok(pipelineService.findById(id));
    }

    // POST /api/pipelines
    @PostMapping
    public ResponseEntity<PipelineResponseDTO> createPipeline(
            @Valid @RequestBody PipelineRequestDTO requestDTO
    ) {
        PipelineResponseDTO createdPipeline = pipelineService.create(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPipeline);
    }

    // PUT /api/pipelines/{id}
    @PutMapping("/{id}")
    public ResponseEntity<PipelineResponseDTO> updatePipeline(
            @PathVariable UUID id,
            @Valid @RequestBody PipelineRequestDTO requestDTO
    ) {
        PipelineResponseDTO updatedPipeline = pipelineService.update(id, requestDTO);
        return ResponseEntity.ok(updatedPipeline);
    }

    // DELETE /api/pipelines/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePipeline(@PathVariable UUID id) {
        pipelineService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // POST /api/pipelines/{id}/run
    @PostMapping("/{id}/run")
    public ResponseEntity<JobRunResponseDTO> runPipeline(@PathVariable UUID id) {
        // O JobRunService contém toda a lógica de simulação
        JobRunResponseDTO jobRun = jobRunService.runPipeline(id);
        return ResponseEntity.ok(jobRun);
    }
}