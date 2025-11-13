package de.ibmix.magkit.monitoring;

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

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.ibmix.magkit.monitoring.config.prometheus.PrometheusConfig;

import info.magnolia.module.ModuleLifecycle;
import info.magnolia.module.ModuleLifecycleContext;

/**
 * Core Magnolia monitoring module lifecycle implementation.
 * <p><strong>Purpose</strong></p>
 * Coordinates startup and shutdown hooks for the monitoring functionality and exposes central configuration
 * ({@link PrometheusConfig}) to dependent components (e.g. Prometheus registry provider, servlet filter).
 * <p><strong>Main Functionality</strong></p>
 * Registers itself in Magnolia's module lifecycle, logs lifecycle transitions and acts as DI aggregation point
 * for monitoring-related configuration beans.
 * <p><strong>Key Features</strong></p>
 * <ul>
 * <li>Simplified access to {@link PrometheusConfig}.</li>
 * <li>Lifecycle integration via {@link ModuleLifecycle} start/stop callbacks.</li>
 * <li>Non-intrusive (does not perform heavy initialization logic by itself).</li>
 * </ul>
 * <p><strong>Usage Preconditions</strong></p>
 * Magnolia module system must supply a valid {@link ModuleLifecycleContext} during startup/shutdown. Dependency
 * injection must provide the {@link PrometheusConfig} instance before other components attempt to access it.
 * <p><strong>Side Effects</strong></p>
 * Writes INFO level log statements during start and stop. No persistent state changes or external resource access.
 * <p><strong>Null and Error Handling</strong></p>
 * Getter may return null if configuration has not been injected yet (e.g. misconfigured DI); callers should guard
 * accordingly. Lifecycle methods do not throw checked exceptions.
 * <p><strong>Thread-Safety</strong></p>
 * Effectively stateless; holds a single mutable reference to configuration. After injection the module is safe for
 * concurrent read access. Configuration replacement via setter should be avoided after startup.
 * <p><strong>Usage Example</strong></p>
 * <pre>{@code
 * MonitoringModule monitoringModule;
 * List<String> metrics = monitoringModule.getPrometheusConfig().getMetrics();
 * }</pre>
 * <p><strong>Important Details</strong></p>
 * Module start does not eagerly initialize metrics; binder registration occurs inside the Prometheus registry provider.
 *
 * @author Soenke Schmidt (IBM iX)
 * @since 2020-03-29
 */
public class MonitoringModule implements ModuleLifecycle {

    private static final Logger LOGGER = LoggerFactory.getLogger(MonitoringModule.class);

    @Inject
    private PrometheusConfig _prometheusConfig;

    /**
     * Invoked by Magnolia during module startup.
     * @param moduleLifecycleContext lifecycle context provided by Magnolia
     */
    @Override
    public void start(ModuleLifecycleContext moduleLifecycleContext) {
        LOGGER.info("Starting Monitoring Module");
    }

    /**
     * Invoked by Magnolia during module shutdown.
     * @param moduleLifecycleContext lifecycle context provided by Magnolia
     */
    @Override
    public void stop(ModuleLifecycleContext moduleLifecycleContext) {
        LOGGER.info("Stopping Monitoring Module");
    }

    /**
     * Returns the Prometheus configuration bean.
     * @return configuration instance; may be null if DI failed
     */
    public PrometheusConfig getPrometheusConfig() {
        return _prometheusConfig;
    }

    /**
     * Replaces the Prometheus configuration bean (should normally only be set by DI container).
     * @param prometheusConfig configuration instance; may be null
     */
    public void setPrometheusConfig(PrometheusConfig prometheusConfig) {
        _prometheusConfig = prometheusConfig;
    }

}
