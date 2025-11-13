package de.ibmix.magkit.monitoring.endpoint.metrics;

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

/**
 * REST endpoint exposing a concise snapshot of JVM runtime and garbage collection metrics.
 * <p><strong>Purpose</strong></p>
 * Provides a JSON representation of current memory usage (used, available, total in MB), active thread count and per-garbage-collector statistics (name, associated memory pools, collection counts and times) together with aggregated totals.
 * <p><strong>Main Functionality</strong></p>
 * Delegates data assembly to {@link MetricsService} and serializes the resulting {@link MetricsInfo} DTO for monitoring consumers.
 * <p><strong>Key Features</strong></p>
 * <ul>
 * <li>JAX-RS endpoint (HTTP GET) producing application/json.</li>
 * <li>Aggregates low-level JVM MXBean data into a stable schema.</li>
 * <li>Separates transport concerns (endpoint) from collection logic (service).</li>
 * </ul>
 * <p><strong>Usage Preconditions</strong></p>
 * Magnolia REST module must route requests to the configured monitoring endpoint path; {@link MetricsService} must be injectable.
 * <p><strong>Side Effects</strong></p>
 * None; invocation only reads JVM management interfaces.
 * <p><strong>Null and Error Handling</strong></p>
 * Service level may provide zero / negative sentinel values for unsupported counters; endpoint itself does not throw checked exceptions.
 * <p><strong>Thread-Safety</strong></p>
 * Stateless; safe for concurrent HTTP requests. Underlying service is stateless as well.
 * <p><strong>Usage Example</strong></p>
 * <pre>{@code
 * GET http://localhost:8080/author/.rest/monitoring/v1/metrics
 * -> 200 OK
 * {
 *   "usedMemoryMb": 123.4567,
 *   "availableMemoryMb": 512.0000,
 *   "totalMemoryMb": 635.4567,
 *   "nbActiveThreads": 42,
 *   "gcInfo": [ ... ],
 *   "totalCollectionCount": 1000,
 *   "totalCollectionTime": 2500
 * }
 * }</pre>
 * <p><strong>Important Details</strong></p>
 * Values are point-in-time snapshots; repeat calls reflect current JVM state and can be used for simple polling.
 * <p><strong>Extensibility</strong></p>
 * Additional metrics can be added by extending {@link MetricsInfo} and enriching {@link MetricsService} without changing the endpoint signature.
 *
 * @author MIHAELA PAPARETE (IBM)
 * @since 2020-04-08
 */
@Path("")
@DynamicPath
public class MetricsEndpoint extends AbstractMonitoringEndpoint<MonitoringEndpointDefinition> {

    private final MetricsService _metricsService;

    /**
     * Constructs the metrics endpoint with its definition and backing service.
     * @param endpointDefinition endpoint metadata definition
     * @param service metrics collection service dependency
     */
    @Inject
    protected MetricsEndpoint(MonitoringEndpointDefinition endpointDefinition, MetricsService service) {
        super(endpointDefinition);
        _metricsService = service;
    }

    /**
     * Returns a snapshot of current JVM metrics (memory, threads, GC stats).
     * @return metrics information DTO
     */
    @GET
    @Path("")
    @Produces(MediaType.APPLICATION_JSON)
    public MetricsInfo getMetrics() {
        return _metricsService.getInfoMetrics();
    }

}
