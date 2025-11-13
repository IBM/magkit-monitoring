package de.ibmix.magkit.monitoring.endpoint.logs;

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
 * Value object describing a log file by name and path reference used by the logs endpoint.
 * <p><strong>Purpose</strong></p>
 * Provides minimal metadata for listing and linking log resources.
 * <p><strong>Main Functionality</strong></p>
 * Holds two mutable string properties (name, path) for JSON serialization; constructed with both values and exposes standard getters/setters.
 * <p><strong>Key Features</strong></p>
 * <ul>
 * <li>Mutable name and path.</li>
 * <li>Empty string defaults.</li>
 * </ul>
 * <p><strong>Null and Error Handling</strong></p>
 * Setters allow null; avoid setting null to maintain consistent JSON output.
 * <p><strong>Thread-Safety</strong></p>
 * Not thread-safe; designed for per-request instantiation.
 * <p><strong>Usage Example</strong></p>
 * <pre>{@code
 * LogInfo info = new LogInfo("system", "/monitoring/logs/system");
 * }</pre>
 * <p><strong>Important Details</strong></p>
 * Path should reflect a REST-accessible resource, not a filesystem location, ensuring clients do not infer direct file system access.
 * @author Dan Olaru (IBM)
 * @since 2020-04-09
 */

public class LogInfo {

    private String _name = "";

    private String _path = "";

    /**
     * Constructs log file descriptor.
     * @param logName file name
     * @param logPath access path
     */
    public LogInfo(String logName, String logPath) {
        super();
        _name = logName;
        _path = logPath;
    }

    /**
     * Returns log file name.
     * @return name string
     */
    public String getName() {
        return _name;
    }

    /**
     * Sets log file name.
     * @param logName new name
     */
    public void setName(String logName) {
        _name = logName;
    }

    /**
     * Returns log file path.
     * @return path string
     */
    public String getPath() {
        return _path;
    }

    /**
     * Sets log file path.
     * @param logPath new path
     */
    public void setPath(String logPath) {
        _path = logPath;
    }

}
