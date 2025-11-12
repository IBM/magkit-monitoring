package de.ibmix.magkit.monitoring.config.prometheus;

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

import java.util.Arrays;
import java.util.List;

import javax.inject.Named;

import com.google.inject.Inject;

/**
 * Central configuration bean for enabling and tailoring Prometheus metrics collection within Magnolia.
 * It defines which metric groups are registered and provides access to nested HTTP request metric configuration.
 * <p><strong>Purpose</strong></p>
 * Supplies a list of metric identifiers that should be exposed (e.g. JVM memory, GC, logging) and delegates
 * HTTP specific metric details to {@link HttpRequestMetricsConfig}.
 * <p><strong>Main Functionality</strong></p>
 * Acts as a DI-present configuration aggregate: holds a mutable list of metric names and exposes the injected
 * HTTP request metrics configuration; optionally overrides metrics list from application properties.
 * <p><strong>Default Metrics</strong></p>
 * <ul>
 * <li>Uptime</li>
 * <li>Processor</li>
 * <li>JvmThread</li>
 * <li>JvmGc</li>
 * <li>JvmMemory</li>
 * <li>ClassLoader</li>
 * <li>Log4J2</li>
 * </ul>
 * <p><strong>Configuration Properties</strong></p>
 * <ul>
 * <li><code>magnolia.monitoring.prometheus.metrics</code> - comma separated list of metric names to activate.</li>
 * </ul>
 * <p><strong>Null and Error Handling</strong></p>
 * Metrics list is initialized with sensible defaults. Setters accept null which may propagate null downstream; avoid passing null. Property parsing splits on comma without trimming; empty segments result in empty metric id strings.
 * <p><strong>Thread-Safety</strong></p>
 * Not thread-safe; internal lists are mutable references and can be replaced. Synchronize externally if accessed concurrently.
 * <p><strong>Usage Example</strong></p>
 * <pre>{@code
 * PrometheusConfig prometheusConfig;
 * List<String> metrics = prometheusConfig.getMetrics();
 * HttpRequestMetricsConfig httpCfg = prometheusConfig.getHttpRequestMetricsConfig();
 * }</pre>
 * <p><strong>Important Details</strong></p>
 * Metric identifiers must match {@link de.ibmix.magkit.monitoring.endpoint.prometheus.Metric} enum id values for binder registration; identifiers are case-insensitive during lookup.
 *
 * @author Soenke Schmidt - IBM iX
 * @since 2023-01-02
 */
public class PrometheusConfig {

    private List<String> _metrics = List.of("Uptime", "Processor", "JvmThread", "JvmGc", "JvmMemory", "ClassLoader", "Log4J2");

    @Inject
    private HttpRequestMetricsConfig _httpRequestMetricsConfig;

    /**
     * Returns configured metric identifiers to be exposed.
     * @return list of metric names; never null unless explicitly set to null
     */
    public List<String> getMetrics() {
        return _metrics;
    }

    /**
     * Replaces the list of metric identifiers to be exposed.
     * @param metrics list of metric names; avoid passing null to prevent downstream errors
     */
    public void setMetrics(List<String> metrics) {
        _metrics = metrics;
    }

    /**
     * Returns HTTP request specific metrics configuration.
     * @return config bean; may be null if not injected
     */
    public HttpRequestMetricsConfig getHttpRequestMetricsConfig() {
        return _httpRequestMetricsConfig;
    }

    /**
     * Replaces the HTTP request metrics configuration bean.
     * @param httpRequestMetricsConfig configuration to set; may be null
     */
    public void setHttpRequestMetricsConfig(HttpRequestMetricsConfig httpRequestMetricsConfig) {
        _httpRequestMetricsConfig = httpRequestMetricsConfig;
    }

    /**
     * Injection hook populating metric identifiers from a comma separated property value.
     * @param metricsProp comma separated metric names; must not be null
     */
    @Inject(optional = true)
    protected void provideMetricsFromProps(@Named("magnolia.monitoring.prometheus.metrics") String metricsProp) {
        _metrics = Arrays.asList(metricsProp.split(","));
    }

}
