package io.andesson.dataflowmanager.features.jobRun.domain.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.andesson.dataflowmanager.features.jobRun.domain.enums.JobStatus;
import io.andesson.dataflowmanager.features.jobRun.domain.model.JobRun;

@Repository
public interface IJobRunRepository extends JpaRepository<JobRun, UUID> {

    List<JobRun> findByPipeline_Id(UUID pipelineId);

    // Para o filtro `?status=COMPLETED`
    List<JobRun> findByStatus(JobStatus status);

    List<JobRun> findByPipeline_IdAndStatus(UUID pipelineId, JobStatus status);
}
