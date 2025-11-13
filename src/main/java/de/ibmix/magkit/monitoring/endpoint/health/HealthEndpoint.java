package de.ibmix.magkit.monitoring.endpoint.health;

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
 * REST endpoint exposing a simple application health indicator for monitoring systems.
 * Currently returns a static {@link Health} instance with default status; intended as extension point for future health checks.
 * <p><strong>Purpose</strong></p>
 * Provides a lightweight liveness signal for orchestrators and dashboards.
 * <p><strong>Main Functionality</strong></p>
 * Instantiates a new {@link Health} DTO per request, currently with default status value, allowing future enhancement to incorporate dynamic checks.
 * <p><strong>Key Features</strong></p>
 * <ul>
 * <li>Stateless GET returning JSON payload.</li>
 * <li>Extensible by adapting {@link Health} population logic.</li>
 * </ul>
 * <p><strong>Usage Preconditions</strong></p>
 * Requires proper wiring of the monitoring endpoint definition by Magnolia's dependency injection.
 * <p><strong>Null and Error Handling</strong></p>
 * Always returns a non-null {@link Health} object; no exceptions thrown in current implementation.
 * <p><strong>Thread-Safety</strong></p>
 * Stateless; safe for concurrent invocation.
 * <p><strong>Usage Example</strong></p>
 * <pre>{@code
 * Health h = healthEndpoint.health();
 * }</pre>
 * <p><strong>Important Details</strong></p>
 * Future health enrichment (database, repository, cache checks) should preserve backward compatibility of existing status field.
 * @author Soenke Schmidt (IBM iX)
 * @since 2020-03-29
 */
@Path("")
@DynamicPath
public class HealthEndpoint extends AbstractMonitoringEndpoint<MonitoringEndpointDefinition> {

    /**
     * Constructs the health endpoint with its monitoring definition.
     * @param endpointDefinition monitoring endpoint meta definition
     */
    @Inject
    protected HealthEndpoint(MonitoringEndpointDefinition endpointDefinition) {
        super(endpointDefinition);
    }

    /**
     * Returns a simple health descriptor with default status value.
     * @return health descriptor; never null
     */
    @GET
    @Path("")
    @Produces(MediaType.APPLICATION_JSON)
    public Health health() {
        return new Health();
    }

}
