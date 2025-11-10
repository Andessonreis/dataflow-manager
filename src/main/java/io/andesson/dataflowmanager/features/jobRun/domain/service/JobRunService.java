package io.andesson.dataflowmanager.features.jobRun.domain.service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.andesson.dataflowmanager.config.exceptions.ResourceNotFoundException;
import io.andesson.dataflowmanager.features.jobRun.domain.dto.response.JobRunResponseDTO;
import io.andesson.dataflowmanager.features.jobRun.domain.enums.JobStatus;
import io.andesson.dataflowmanager.features.jobRun.domain.model.JobRun;
import io.andesson.dataflowmanager.features.jobRun.domain.repository.IJobRunRepository;
import io.andesson.dataflowmanager.features.pipeline.domain.model.Pipeline;
import io.andesson.dataflowmanager.features.pipeline.domain.repository.IPipelineRepository;

@Service
public class JobRunService implements IJobRunService {

    @Autowired
    private IJobRunRepository jobRunRepository;

    @Autowired
    private IPipelineRepository pipelineRepository;
    
    /**
     * Esta é a lógica principal da simulação.
     * Ela usa os métodos de domínio (start, complete, fail) da sua Entidade JobRun.
     */
    @Override
    @Transactional
    public JobRunResponseDTO runPipeline(UUID pipelineId) {
        // 1. Encontra a Pipeline-mãe
        Pipeline pipeline = pipelineRepository.findById(pipelineId)
                .orElseThrow(() -> new ResourceNotFoundException("Pipeline não encontrada com o ID: " + pipelineId));

        // 2. Cria o JobRun usando seu construtor de domínio
        // (Status: PENDING)
        JobRun jobRun = new JobRun(pipeline);
        jobRunRepository.save(jobRun);

        try {
            // 3. Inicia o Job
            // (Status: RUNNING)
            jobRun.start();
            jobRunRepository.save(jobRun);

            // 4. *** SIMULAÇÃO DE TRABALHO ***
            // (Aqui você faria o ETL real. Vamos simular com um sleep)
            simulateWork(pipeline);

            // 5. Conclui o Job
            // (Status: COMPLETED)
            String logs = String.format("Simulação concluída. Pipeline: %s. Fonte: %s (%s).",
                    pipeline.getName(),
                    pipeline.getDataSource().getName(),
                    pipeline.getDataSource().getType()
            );
            jobRun.complete(logs);

        } catch (Exception e) {
            // 6. Falha no Job
            // (Status: FAILED)
            String errorMsg = "Falha na simulação: " + e.getMessage();
            jobRun.fail(errorMsg);
        }

        // 7. Salva o estado final (COMPLETED ou FAILED)
        JobRun finalJobRun = jobRunRepository.save(jobRun);
        return new JobRunResponseDTO(finalJobRun);
    }
    
    // Método privado para simular o trabalho
    private void simulateWork(Pipeline pipeline) throws InterruptedException {
        // Simula um trabalho de 1 a 3 segundos
        long duration = (long) (Math.random() * 2000) + 1000;
        TimeUnit.MILLISECONDS.sleep(duration);

        // Simula uma falha aleatória (10% de chance)
        if (Math.random() < 0.1) {
            throw new RuntimeException("Simulação de falha: Erro de conexão com " + pipeline.getDataSource().getConnectionUri());
        }
    }
    
    
    @Override
    @Transactional(readOnly = true)
    public List<JobRunResponseDTO> findAll(UUID pipelineId, JobStatus status) {
        List<JobRun> jobRuns;

        // Lógica de filtro baseada nos parâmetros da API
        if (pipelineId != null && status != null) {
            jobRuns = jobRunRepository.findByPipeline_IdAndStatus(pipelineId, status);
        } else if (pipelineId != null) {
            jobRuns = jobRunRepository.findByPipeline_Id(pipelineId);
        } else if (status != null) {
            jobRuns = jobRunRepository.findByStatus(status);
        } else {
            jobRuns = jobRunRepository.findAll();
        }

        return jobRuns.stream()
                .map(JobRunResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public JobRunResponseDTO findById(UUID id) {
        JobRun jobRun = jobRunRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("JobRun não encontrado com o ID: " + id));
        return new JobRunResponseDTO(jobRun);
    }
}