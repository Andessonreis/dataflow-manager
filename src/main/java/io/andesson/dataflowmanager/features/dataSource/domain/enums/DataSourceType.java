package io.andesson.dataflowmanager.features.dataSource.domain.enums;

/**
 * Enum representing the type of a data source.
 * <ul>
 *   <li>{@code API} – external web API or service.</li>
 *   <li>{@code DATABASE} – relational or NoSQL database.</li>
 *   <li>{@code FILE} – local or remote file storage.</li>
 * </ul>  
 * 
 * @author Andesson Reis
 */
public enum DataSourceType {
    /** External web API or service. */
    API,
    /** Relational or NoSQL database. */
    DATABASE,
    /** Local or remote file storage. */
    FILE
}