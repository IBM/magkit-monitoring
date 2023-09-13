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
import io.micrometer.prometheus.PrometheusMeterRegistry;

/**
 * Prometheus Filter class.
 *
 * @author VladNacu
 * @author Soenke Schmidt - IBM iX
 *
 */
public class PrometheusFilter extends AbstractMgnlFilter {

    private final PrometheusMeterRegistry _registry;
    private final MonitoringModule _monitoringModule;

    @Inject
    public PrometheusFilter(MonitoringModule monitoringModule, PrometheusMeterRegistry registry) {
        _registry = registry;
        _monitoringModule = monitoringModule;

    }

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

    private boolean httpMetricsEnabled() {
        return _monitoringModule.getPrometheusConfig().getMetrics().stream().anyMatch((m) -> m.equalsIgnoreCase("http"));
    }

    private Iterable<Tag> getTags(HttpServletRequest request, HttpServletResponse response) {
        Tag uri = (request != null) ? Tag.of("uri", request.getRequestURI()) : Tag.of("uri", "UNKNOWN");
        Tag method = (request != null) ? Tag.of("method", request.getMethod()) : Tag.of("method", "UNKNOWN");
        Tag status = (response != null) ? Tag.of("status", Integer.toString(response.getStatus())) : Tag.of("method", "UNKNOWN");
        return Tags.of(uri, method, status);
    }

}
