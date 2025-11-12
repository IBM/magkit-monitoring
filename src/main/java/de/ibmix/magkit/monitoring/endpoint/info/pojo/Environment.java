package de.ibmix.magkit.monitoring.endpoint.info.pojo;

/*-
 * #%L
 * IBM iX Magnolia Monitoring
 * %%
 * Copyright (C) 2023 IBM iX
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

/**
 * Holds runtime environment characteristics for the Magnolia instance (OS, JVM, server, DB, driver, repository).
 * <p><strong>Key Features</strong></p>
 * <ul>
 * <li>Captures operational context for diagnostics.</li>
 * <li>Simple string field mapping for JSON output.</li>
 * </ul>
 * <p><strong>Null and Error Handling</strong></p>
 * Fields may be null until set; avoid relying on defaults.
 * <p><strong>Thread-Safety</strong></p>
 * Not thread-safe; instantiate per request.
 * <p><strong>Usage Example</strong></p>
 * <pre>{@code
 * Environment env = new Environment();
 * env.setOperatingSystem("Linux");
 * }</pre>
 * @author CLAUDIU GONCIULEA (IBM iX)
 * @since 2020-04-09
 */
public class Environment {

    private String _operatingSystem;
    private String _javaVersion;
    private String _applicationServer;
    private String _database;
    private String _dbDriver;
    private String _repository;

    /**
     * Returns operating system name.
     * @return OS name; may be null
     */
    public String getOperatingSystem() {
        return _operatingSystem;
    }

    /**
     * Sets operating system name.
     * @param operatingSystem OS name; may be null
     */
    public void setOperatingSystem(String operatingSystem) {
        _operatingSystem = operatingSystem;
    }

    /**
     * Returns JVM vendor and version.
     * @return JVM info; may be null
     */
    public String getJavaVersion() {
        return _javaVersion;
    }

    /**
     * Sets JVM vendor and version.
     * @param javaVersion JVM info; may be null
     */
    public void setJavaVersion(String javaVersion) {
        _javaVersion = javaVersion;
    }

    /**
     * Returns application server descriptor.
     * @return server; may be null
     */
    public String getApplicationServer() {
        return _applicationServer;
    }

    /**
     * Sets application server descriptor.
     * @param applicationServer server; may be null
     */
    public void setApplicationServer(String applicationServer) {
        _applicationServer = applicationServer;
    }

    /**
     * Returns database name or descriptor.
     * @return database; may be null
     */
    public String getDatabase() {
        return _database;
    }

    /**
     * Sets database name or descriptor.
     * @param database database; may be null
     */
    public void setDatabase(String database) {
        _database = database;
    }

    /**
     * Returns database driver identifier.
     * @return db driver; may be null
     */
    public String getDbDriver() {
        return _dbDriver;
    }

    /**
     * Sets database driver identifier.
     * @param dbDriver driver; may be null
     */
    public void setDbDriver(String dbDriver) {
        _dbDriver = dbDriver;
    }

    /**
     * Returns JCR repository name and version.
     * @return repository descriptor; may be null
     */
    public String getRepository() {
        return _repository;
    }

    /**
     * Sets JCR repository name and version.
     * @param repository repository descriptor; may be null
     */
    public void setRepository(String repository) {
        _repository = repository;
    }
}
