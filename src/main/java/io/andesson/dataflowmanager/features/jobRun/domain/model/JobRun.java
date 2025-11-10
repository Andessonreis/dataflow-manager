package io.andesson.dataflowmanager.features.jobRun.domain.model;

import io.andesson.dataflowmanager.features.jobRun.domain.enums.JobStatus;
import io.andesson.dataflowmanager.features.pipeline.domain.model.Pipeline;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Representa uma execução (run) de um Pipeline.
 * Cada JobRun pertence a um Pipeline e controla o ciclo de vida da execução:
 * início, conclusão, falha e armazenamento de logs.
 *
 * @author Andesson
 * @since 2025-11-01
 */
@Entity
@Table(name = "job_runs")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(of = "id")
@ToString(onlyExplicitlyIncluded = true)
public class JobRun {

    // ------------------------
    // Atributos Persistentes
    // ------------------------

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false)
    @ToString.Include
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @ToString.Include
    private JobStatus status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pipeline_id", nullable = false)
    private Pipeline pipeline;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    /**
     * True se o job foi concluído com sucesso,
     * False se falhou,
     * Null se ainda não foi finalizado.
     */
    private Boolean success;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String logs;

    // ------------------------
    // Construtor de Domínio
    // ------------------------

    /**
     * Cria uma nova execução de job associada a um Pipeline.
     * Ela inicia no estado PENDING e define os timestamps iniciais.
     *
     * @param pipeline O Pipeline associado a esta execução.
     */
    public JobRun(Pipeline pipeline) {
        if (pipeline == null) {
            throw new IllegalArgumentException("O Pipeline não pode ser nulo.");
        }
        this.pipeline = pipeline;
        this.status = JobStatus.PENDING;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // ------------------------
    // Lógica de Domínio
    // ------------------------

    /**
     * Marca o job como iniciado.
     */
    public void start() {
        if (this.status != JobStatus.PENDING) {
            throw new IllegalStateException(
                    "O job só pode ser iniciado se estiver no estado PENDING. Estado atual: " + this.status);
        }
        this.status = JobStatus.RUNNING;
        this.startTime = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Marca o job como concluído com sucesso.
     *
     * @param finalLogs Logs finais da execução.
     */
    public void complete(String finalLogs) {
        if (this.status != JobStatus.RUNNING) {
            throw new IllegalStateException(
                    "O job só pode ser concluído se estiver no estado RUNNING. Estado atual: " + this.status);
        }
        this.status = JobStatus.COMPLETED;
        this.success = true;
        this.endTime = LocalDateTime.now();
        this.logs = finalLogs;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Marca o job como falho.
     *
     * @param errorLogs Logs de erro da execução.
     */
    public void fail(String errorLogs) {
        if (this.status != JobStatus.RUNNING) {
            throw new IllegalStateException(
                    "O job só pode ser marcado como falho se estiver no estado RUNNING. Estado atual: " + this.status);
        }
        this.status = JobStatus.FAILED;
        this.success = false;
        this.endTime = LocalDateTime.now();
        this.logs = errorLogs;
        this.updatedAt = LocalDateTime.now();
    }

    // ------------------------
    // Métodos Auxiliares
    // ------------------------

    @ToString.Include(name = "pipelineId")
    public UUID getPipelineId() {
        return this.pipeline != null ? this.pipeline.getId() : null;
    }
}
