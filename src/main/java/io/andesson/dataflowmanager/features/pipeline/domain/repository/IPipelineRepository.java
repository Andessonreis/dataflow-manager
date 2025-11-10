package io.andesson.dataflowmanager.features.pipeline.domain.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.andesson.dataflowmanager.features.pipeline.domain.model.Pipeline;

@Repository
public interface IPipelineRepository extends JpaRepository<Pipeline, UUID> {
  
}
