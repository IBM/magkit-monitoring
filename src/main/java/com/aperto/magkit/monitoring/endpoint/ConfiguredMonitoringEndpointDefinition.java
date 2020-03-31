package com.aperto.magkit.monitoring.endpoint;

/**
 * 
 * Default Implementation of {@link MonitoringEndpointDefinition}.
 * 
 * @author Soenke Schmidt (Aperto - An IBM Company)
 * @since 2020-03-29
 *
 */
public class ConfiguredMonitoringEndpointDefinition implements MonitoringEndpointDefinition {

    /** Generated **/
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
