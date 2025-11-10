package io.andesson.dataflowmanager.features.pipeline.domain.model;

import java.util.UUID;

import io.andesson.dataflowmanager.features.dataSource.domain.model.DataSource;
import io.andesson.dataflowmanager.features.pipeline.domain.enums.StatusPipeline;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "pipelines")
public class Pipeline {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /** Human-readable name for the pipeline */
    private String name;

    /** Cron-like schedule string defining pipeline execution frequency */
    private String schedule;

    /** Current status of the pipeline */
    @Enumerated(EnumType.STRING)
    private StatusPipeline status;

    /** Data source associated with this pipeline */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "data_source_id")
    private DataSource dataSource;
}
