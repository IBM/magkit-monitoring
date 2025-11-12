package de.ibmix.magkit.monitoring.endpoint.env;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Data transfer object aggregating runtime environment information: JVM arguments, system properties and Magnolia configuration properties.
 * Serves as the JSON serializable response payload for the environment monitoring endpoint.
 * <p><strong>Purpose</strong></p>
 * Encapsulates heterogeneous environment metadata in a structured form consumable by REST clients.
 * <p><strong>Null and Error Handling</strong></p>
 * All collections are initialized empty and may be replaced; setters permit null which could cause downstream NPE if not guarded.
 * <p><strong>Thread-Safety</strong></p>
 * Not thread-safe; mutable lists/maps exposed directly. Create defensive copies if sharing across threads.
 * <p><strong>Usage Example</strong></p>
 * <pre>{@code
 * Environment env = new Environment();
 * env.setJvmArgs(List.of("-Xmx512m"));
 * env.setSysProp(Map.of("java.version", System.getProperty("java.version")));
 * }</pre>
 * <p><strong>Important Details</strong></p>
 * System and Magnolia properties may contain sensitive values; upstream filtering should exclude secrets before populating this DTO.
 * @author VladNacu
 * @since 2020-04-13
 */
public class Environment {

    private List<String> _jvmArgs = new ArrayList<>();
    private Map<String, String> _sysProp = new HashMap<>();
    private Map<String, String> _magnoliaProperties = new HashMap<>();

    /**
     * default no args constructor.
     */
    public Environment() {

    }

    /**
     * Returns JVM input argument strings.
     * @return list of raw JVM arguments; never null, possibly empty
     */
    public List<String> getJvmArgs() {
        return _jvmArgs;
    }

    /**
     * Replaces JVM input argument strings.
     * @param jvmArgs list of arguments; may be null (not recommended)
     */
    public void setJvmArgs(List<String> jvmArgs) {
        _jvmArgs = jvmArgs;
    }

    /**
     * Returns system properties map.
     * @return map of system properties; never null, possibly empty
     */
    public Map<String, String> getSysProp() {
        return _sysProp;
    }

    /**
     * Replaces system properties map.
     * @param sysProp map of system properties; may be null (not recommended)
     */
    public void setSysProp(Map<String, String> sysProp) {
        _sysProp = sysProp;
    }

    /**
     * Returns Magnolia configuration properties not overlapping with system properties.
     * @return map of Magnolia properties; never null, possibly empty
     */
    public Map<String, String> getMagnoliaProperties() {
        return _magnoliaProperties;
    }

    /**
     * Replaces Magnolia configuration properties map.
     * @param magnoliaProperties map of properties; may be null (not recommended)
     */
    public void setMagnoliaProperties(Map<String, String> magnoliaProperties) {
        _magnoliaProperties = magnoliaProperties;
    }

}
