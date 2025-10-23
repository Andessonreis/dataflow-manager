package io.andesson.dataflowmanager.features.dataSource.domain.model;

import java.util.UUID;

import io.andesson.dataflowmanager.features.dataSource.domain.enums.DataSourceType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;

import lombok.Data;

/**
 * Represents an external data source configuration.
 *
 * <p>This JPA entity stores metadata required to identify and connect to external
 * data sources such as APIs, databases, or file storage.
 *
 * <p>Fields:
 * <ul>
 *   <li><strong>id</strong> – auto-generated UUID uniquely identifying the data source.</li>
 *   <li><strong>name</strong> – human-readable name for display, selection, and management.</li>
 *   <li><strong>type</strong> – type of the data source, defined by {@link DataSourceType} enum.</li>
 *   <li><strong>connectionUri</strong> – protocol-specific URI used to connect to the source.</li>
 * </ul>
 *
 * <p><strong>Security:</strong> avoid embedding plaintext credentials in {@code connectionUri}.
 * Use environment variables, vaults, or encrypted configuration whenever possible.
 *
 * <p><strong>Usage:</strong> components reference this entity to establish connections
 * and select the appropriate driver or factory at runtime.
 *
 * @author Andesson Reis
 */
@Data
@Entity
public class DataSource {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * Human-readable name of the data source.
     *
     * <p>Used for display, selection, and management in the UI or API.
     */
    private String name;

    /**
     * Type of the data source.
     *
     * <p>Defined by the {@link DataSourceType} enum. Possible values:
     * <ul>
     *   <li>{@code API} – external web API or service.</li>
     *   <li>{@code DATABASE} – relational or NoSQL database.</li>
     *   <li>{@code FILE} – local or remote file storage.</li>
     * </ul>
     */
    @Enumerated(EnumType.STRING)
    private DataSourceType type;

    /**
     * Protocol-specific connection string used to connect to the data source.
     *
     * <p>Must be a valid URI or connection string for the target driver/provider.
     * Avoid embedding credentials directly.
     *
     * @see java.net.URI
     */
    private String connectionUri;
}
