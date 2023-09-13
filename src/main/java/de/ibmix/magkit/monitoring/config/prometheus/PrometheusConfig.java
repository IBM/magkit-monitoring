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
 *
 * Configuration bean for Prometheus Registry and respective metrics.
 *
 * @author Soenke Schmidt - IBM iX
 * @since 02.01.2023
 *
 */
public class PrometheusConfig {

    private List<String> _metrics = List.of("Uptime", "Processor", "JvmThread", "JvmGc", "JvmMemory", "ClassLoader", "Log4J2");

    @Inject
    private HttpRequestMetricsConfig _httpRequestMetricsConfig;

    public List<String> getMetrics() {
        return _metrics;
    }

    public void setMetrics(List<String> metrics) {
        _metrics = metrics;
    }

    public HttpRequestMetricsConfig getHttpRequestMetricsConfig() {
        return _httpRequestMetricsConfig;
    }

    public void setHttpRequestMetricsConfig(HttpRequestMetricsConfig httpRequestMetricsConfig) {
        _httpRequestMetricsConfig = httpRequestMetricsConfig;
    }

    @Inject(optional = true)
    protected void provideMetricsFromProps(@Named("magnolia.monitoring.prometheus.metrics") String metricsProp) {
        _metrics = Arrays.asList(metricsProp.split(","));
    }

}
