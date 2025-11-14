package de.ibmix.magkit.monitoring.endpoint.prometheus;

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

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.ibmix.magkit.monitoring.MonitoringModule;

import info.magnolia.cms.filters.AbstractMgnlFilter;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.Timer;
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry;

/**
 * Servlet filter capturing HTTP request metrics and recording them into a Prometheus meter registry.
 * Wraps request processing in a timer sample when the http metric is enabled in module configuration.
 * <p><strong>Purpose</strong></p>
 * Integrates Micrometer HTTP timing into Magnolia by conditionally timing each request and tagging metrics with URI,
 * method and status allowing Prometheus scraping for latency and throughput analysis.
 * <p><strong>Main Functionality</strong></p>
 * Checks module configuration for presence of metric id "http"; if enabled wraps filter chain execution in a
 * {@link Timer.Sample} and records to timer "http_server_requests" with tags.
 * <p><strong>Key Features</strong></p>
 * <ul>
 * <li>Conditional activation (no overhead when disabled).</li>
 * <li>Captures basic request dimensionality (uri, method, status).</li>
 * <li>Leverages Micrometer registry for downstream Prometheus exposition.</li>
 * </ul>
 * <p><strong>Usage Preconditions</strong></p>
 * Monitoring module must be started and Prometheus registry injected. Filter must be ordered properly in Magnolia's
 * chain to capture desired endpoints.
 * <p><strong>Side Effects</strong></p>
 * Adds small timing overhead for each request when enabled.
 * <p><strong>Null and Error Handling</strong></p>
 * Null request/response objects produce "UNKNOWN" tag values. Exceptions propagate per servlet filter contract and are not swallowed.
 * <p><strong>Thread-Safety</strong></p>
 * Stateless aside from injected dependencies; safe for concurrent request processing.
 * <p><strong>Usage Example</strong></p>
 * <pre>{@code
 * // Configured via Magnolia filter chain, metrics scraped through Prometheus endpoint
 * }</pre>
 * @author VladNacu
 * @author Soenke Schmidt - IBM iX
 * @since 2023-09-13
 */
public class PrometheusFilter extends AbstractMgnlFilter {

    private final PrometheusMeterRegistry _registry;
    private final MonitoringModule _monitoringModule;

    /**
     * Constructs filter with monitoring module and registry dependencies.
     * @param monitoringModule monitoring module providing configuration
     * @param registry prometheus meter registry target
     */
    @Inject
    public PrometheusFilter(MonitoringModule monitoringModule, PrometheusMeterRegistry registry) {
        _registry = registry;
        _monitoringModule = monitoringModule;
    }

    /**
     * Executes filter chain and records timing metrics if http metrics enabled.
     * @param request current HTTP request
     * @param response current HTTP response
     * @param chain remaining filter chain
     * @throws IOException on I/O error during chain execution
     * @throws ServletException on servlet processing error
     */
    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (!httpMetricsEnabled()) {
            chain.doFilter(request, response);
        } else {
            Timer.Sample sample = Timer.start(_registry);
            try {
                chain.doFilter(request, response);
            } finally {
                sample.stop(_registry.timer("http_server_requests", getTags(request, response)));
            }
        }
    }

    /**
     * Indicates if HTTP metrics collection is enabled (presence of "http" metric id).
     * @return true when enabled
     */
    private boolean httpMetricsEnabled() {
        return _monitoringModule.getPrometheusConfig().getMetrics().stream().anyMatch((m) -> m.equalsIgnoreCase("http"));
    }

    /**
     * Builds tag collection for timer including uri, method and status.
     * @param request current HTTP request; may be null
     * @param response current HTTP response; may be null
     * @return iterable tags instance
     */
    private Iterable<Tag> getTags(HttpServletRequest request, HttpServletResponse response) {
        Tag uri = (request != null) ? Tag.of("uri", request.getRequestURI()) : Tag.of("uri", "UNKNOWN");
        Tag method = (request != null) ? Tag.of("method", request.getMethod()) : Tag.of("method", "UNKNOWN");
        Tag status = (response != null) ? Tag.of("status", Integer.toString(response.getStatus())) : Tag.of("method", "UNKNOWN");
        return Tags.of(uri, method, status);
    }

}
