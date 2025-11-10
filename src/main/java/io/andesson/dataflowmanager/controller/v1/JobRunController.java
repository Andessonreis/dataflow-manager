package io.andesson.dataflowmanager.controller.v1;


import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.andesson.dataflowmanager.features.jobRun.domain.dto.response.JobRunResponseDTO;
import io.andesson.dataflowmanager.features.jobRun.domain.enums.JobStatus;
import io.andesson.dataflowmanager.features.jobRun.domain.service.IJobRunService;

@RestController
@RequestMapping("/api/job-runs")
public class JobRunController {

    @Autowired
    private IJobRunService jobRunService;

    /**
     * Lista execuções com filtros opcionais.
     * ?pipelineId=uuid
     * ?status=COMPLETED
     */
    @GetMapping
    public ResponseEntity<List<JobRunResponseDTO>> getAllJobRuns(
            @RequestParam(required = false) UUID pipelineId,
            @RequestParam(required = false) JobStatus status
    ) {
        List<JobRunResponseDTO> jobRuns = jobRunService.findAll(pipelineId, status);
        return ResponseEntity.ok(jobRuns);
    }

    /**
     * Busca um JobRun específico.
     */
    @GetMapping("/{id}")
    public ResponseEntity<JobRunResponseDTO> getJobRunById(@PathVariable UUID id) {
        JobRunResponseDTO jobRun = jobRunService.findById(id);
        return ResponseEntity.ok(jobRun);
    }
}