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

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import de.ibmix.magkit.monitoring.endpoint.AbstractMonitoringEndpoint;
import de.ibmix.magkit.monitoring.endpoint.MonitoringEndpointDefinition;

import info.magnolia.rest.DynamicPath;
import io.micrometer.prometheus.PrometheusMeterRegistry;

/**
 * REST endpoint exposing Prometheus formatted metrics scraped from the application's registry.
 * Delegates to Micrometer {@link io.micrometer.prometheus.PrometheusMeterRegistry} for text exposition format.
 * <p><strong>Purpose</strong></p>
 * Serves as integration point for Prometheus server to collect application metrics.
 * <p><strong>Main Functionality</strong></p>
 * Invokes {@link io.micrometer.prometheus.PrometheusMeterRegistry#scrape()} to generate the current exposition text snapshot for all bound meters.
 * <p><strong>Key Features</strong></p>
 * <ul>
 * <li>Plain text exposition format compliant with Prometheus.</li>
 * <li>Direct scrape of live registry metrics.</li>
 * </ul>
 * <p><strong>Null and Error Handling</strong></p>
 * Always returns non-null text; underlying registry must be initialized or may yield empty output.
 * <p><strong>Thread-Safety</strong></p>
 * Stateless aside from injected registry; scraping is thread-safe per Micrometer design.
 * <p><strong>Usage Example</strong></p>
 * <pre>{@code
 * String metrics = prometheusEndpoint.prometheus();
 * }</pre>
 * <p><strong>Important Details</strong></p>
 * Scrape output may include high-cardinality labels if misconfigured; ensure URI whitelist and other filters minimize cardinality.
 * @author VladNacu
 * @author Sönke Schmidt - IBM iX
 * @since 2023-09-13
 */
@Path("")
@DynamicPath
public class PrometheusEndpoint extends AbstractMonitoringEndpoint<MonitoringEndpointDefinition> {

    private final PrometheusMeterRegistry _registry;

    /**
     * Constructs the prometheus endpoint with its definition and registry.
     * @param endpointDefinition monitoring endpoint definition metadata
     * @param registry prometheus meter registry providing metrics
     */
    @Inject
    protected PrometheusEndpoint(MonitoringEndpointDefinition endpointDefinition, PrometheusMeterRegistry registry) {
        super(endpointDefinition);
        _registry = registry;

    }

    /**
     * Scrapes and returns the current Prometheus exposition text.
     * @return metrics in Prometheus text format
     */
    @GET
    @Path("")
    @Produces(MediaType.TEXT_PLAIN)
    public String prometheus() {
        return _registry.scrape();
    }

}
