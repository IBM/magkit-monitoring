package de.ibmix.magkit.monitoring.endpoint.overview;

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
 * Value object describing a single monitoring endpoint by name and absolute path.
 * Used for overview listing serialization.
 * <p><strong>Purpose</strong></p>
 * Encapsulates minimal endpoint metadata displayed to clients.
 * <p><strong>Main Functionality</strong></p>
 * Provides a simple two-property container (name, path) for JSON marshalling with basic getters/setters used by the overview aggregation logic.
 * <p><strong>Key Features</strong></p>
 * <ul>
 * <li>Mutable name and path fields.</li>
 * <li>Simple JSON-friendly structure.</li>
 * </ul>
 * <p><strong>Null and Error Handling</strong></p>
 * Fields default to empty strings; setters allow null which may appear as null in JSON.
 * <p><strong>Thread-Safety</strong></p>
 * Not thread-safe; per-instance usage expected.
 * <p><strong>Usage Example</strong></p>
 * <pre>{@code
 * EndpointInfo info = new EndpointInfo("health","/monitoring/v1/health");
 * }</pre>
 * <p><strong>Important Details</strong></p>
 * Path should be pre-normalized (no trailing slashes) before assignment to ensure consistent client display.
 * @author Dan Olaru (IBM)
 * @since 2020-04-24
 */
public class EndpointInfo {

    private String _name = "";
    private String _path = "";

    /**
     * Constructs endpoint info.
     * @param name endpoint name
     * @param path endpoint absolute path
     */
    public EndpointInfo(String name, String path) {
        super();
        _name = name;
        _path = path;
    }

    /**
     * Returns endpoint name.
     * @return name string
     */
    public String getName() {
        return _name;
    }

    /**
     * Sets endpoint name.
     * @param name new name
     */
    public void setName(String name) {
        _name = name;
    }

    /**
     * Returns endpoint path.
     * @return path string
     */
    public String getPath() {
        return _path;
    }

    /**
     * Sets endpoint path.
     * @param path new absolute path
     */
    public void setPath(String path) {
        _path = path;
    }

}
