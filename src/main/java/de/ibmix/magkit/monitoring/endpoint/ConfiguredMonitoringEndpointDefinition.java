package de.ibmix.magkit.monitoring.endpoint;

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
 * Concrete configurable implementation of {@link MonitoringEndpointDefinition} representing metadata for a monitoring REST endpoint.
 * Supports enabling/disabling, sensitivity flagging and overrides for name and path resolution.
 * <p><strong>Purpose</strong></p>
 * Encapsulates endpoint descriptive attributes used by Magnolia's REST framework for registration and access control.
 * <p><strong>Main Functionality</strong></p>
 * Acts as a mutable POJO holding endpoint name, path, sensitivity and enabled state; supplies fallback name resolution based on implementation class when explicit name absent.
 * <p><strong>Key Features</strong></p>
 * <ul>
 * <li>Optional explicit endpoint name; falls back to implementation class name.</li>
 * <li>Enable/disable flag controlling exposure.</li>
 * <li>Sensitivity flag indicating whether endpoint requires stronger authorization.</li>
 * <li>Configurable endpoint path segment.</li>
 * </ul>
 * <p><strong>Null and Error Handling</strong></p>
 * Getters may return null for attributes not initialized (e.g. path). Setting a null implementation class will break name fallback logic.
 * <p><strong>Thread-Safety</strong></p>
 * Not thread-safe; mutable fields. Configure once at startup or guard externally for concurrent mutations.
 * <p><strong>Usage Example</strong></p>
 * <pre>{@code
 * ConfiguredMonitoringEndpointDefinition def = new ConfiguredMonitoringEndpointDefinition();
 * def.setImplementationClass(MyEndpoint.class);
 * def.setEndpointPath("health");
 * def.setEnabled(true);
 * }</pre>
 * <p><strong>Important Details</strong></p>
 * The fallback name uses the fully qualified class name; if a shorter name is desired ensure {@link #setName(String)} is invoked.
 * @author Soenke Schmidt (IBM iX)
 * @since 2020-03-29
 */
public class ConfiguredMonitoringEndpointDefinition implements MonitoringEndpointDefinition {

    /** Generated. **/
    private static final long serialVersionUID = -7842531283685607096L;

    private Class<?> _implementationClass;

    private String _name;
    private String _endpointPath;

    private boolean _enabled = true;
    private boolean _sensitive = true;

    /**
     * Returns the concrete endpoint implementation class.
     * @return endpoint implementation class; may be null prior to configuration
     */
    public Class<?> getImplementationClass() {
        return _implementationClass;
    }

    /**
     * Sets the concrete endpoint implementation class used for name fallback.
     * @param implementationClass class object; should not be null
     */
    public void setImplementationClass(Class<?> implementationClass) {
        _implementationClass = implementationClass;
    }

    /**
     * Returns endpoint name; falls back to implementation class name when explicit name is not set.
     * @return endpoint name string; never null if implementation class set
     */
    @Override
    public String getName() {
        return _name == null ? _implementationClass.getName() : _name;
    }

    /**
     * Sets explicit endpoint name overriding fallback behavior.
     * @param name endpoint name string; may be null to restore fallback to implementation class
     */
    public void setName(String name) {
        _name = name;
    }

    /**
     * Returns endpoint path segment.
     * @return path segment string; may be null if unset
     */
    @Override
    public String getEndpointPath() {
        return _endpointPath;
    }

    /**
     * Sets endpoint path segment.
     * @param endpointPath path segment; may be null
     */
    public void setEndpointPath(String endpointPath) {
        _endpointPath = endpointPath;
    }

    /**
     * Indicates whether endpoint is currently enabled/exposed.
     * @return true if enabled
     */
    @Override
    public boolean isEnabled() {
        return _enabled;
    }

    /**
     * Sets enabled flag controlling exposure.
     * @param enabled true to enable endpoint
     */
    public void setEnabled(boolean enabled) {
        _enabled = enabled;
    }

    /**
     * Indicates whether endpoint is considered sensitive.
     * @return true if sensitive
     */
    @Override
    public boolean isSensitive() {
        return _sensitive;
    }

    /**
     * Sets sensitivity flag used for access control decisions.
     * @param sensitive true if sensitive
     */
    public void setSensitive(boolean sensitive) {
        _sensitive = sensitive;
    }

}
