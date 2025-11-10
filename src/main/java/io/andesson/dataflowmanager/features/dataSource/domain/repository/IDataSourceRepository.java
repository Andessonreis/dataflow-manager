package io.andesson.dataflowmanager.features.dataSource.domain.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.andesson.dataflowmanager.features.dataSource.domain.enums.DataSourceType;
import io.andesson.dataflowmanager.features.dataSource.domain.model.DataSource;

/**
 * Repository interface for managing DataSource entities.
 * Provides CRUD operations for DataSource objects identified by UUID.
 *
 * @author Andesson Reis
 */
@Repository
public interface IDataSourceRepository extends JpaRepository<DataSource, UUID>  {
   List<DataSource> findByType(DataSourceType type);
}
