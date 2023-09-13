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
 *
 * Default Implementation of {@link MonitoringEndpointDefinition}.
 *
 * @author Soenke Schmidt (IBM iX)
 * @since 2020-03-29
 *
 */
public class ConfiguredMonitoringEndpointDefinition implements MonitoringEndpointDefinition {

    /** Generated. **/
    private static final long serialVersionUID = -7842531283685607096L;

    private Class<?> _implementationClass;

    private String _name;
    private String _endpointPath;

    private boolean _enabled = true;
    private boolean _sensitive = true;

    public Class<?> getImplementationClass() {
        return _implementationClass;
    }

    public void setImplementationClass(Class<?> implementationClass) {
        _implementationClass = implementationClass;
    }

    @Override
    public String getName() {
        return _name == null ? _implementationClass.getName() : _name;
    }

    public void setName(String name) {
        _name = name;
    }

    @Override
    public String getEndpointPath() {
        return _endpointPath;
    }

    public void setEndpointPath(String endpointPath) {
        _endpointPath = endpointPath;
    }

    @Override
    public boolean isEnabled() {
        return _enabled;
    }

    public void setEnabled(boolean enabled) {
        _enabled = enabled;
    }

    @Override
    public boolean isSensitive() {
        return _sensitive;
    }

    public void setSensitive(boolean sensitive) {
        _sensitive = sensitive;
    }

}
