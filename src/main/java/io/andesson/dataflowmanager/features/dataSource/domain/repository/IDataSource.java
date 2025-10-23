package io.andesson.dataflowmanager.features.dataSource.domain.repository;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import io.andesson.dataflowmanager.features.dataSource.domain.model.DataSource;

/**
 * Repository interface for managing DataSource entities.
 * Provides CRUD operations for DataSource objects identified by UUID.
 *
 * @author Andesson Reis
 */
public interface IDataSource extends CrudRepository<DataSource, UUID>  {
  
}
