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
 *
 * <pre>
 * This endpoint provides general information about the JVM runtime.
 * - used / available memory
 * - Garbage Collector information
 *  a) Garbage Collectors Name
 *  b) Garbage Collectors Memory Pools
 *  c) Collection Count for each Garbage Collector
 *  d) Collection Time for each Garbage Collector
 *  e) Total Collection Count
 *  f) Total Collection Time
 * - No. of active threads
 *
 * Example for endpoint call:
 *      http://localhost:8001/author/.rest/monitoring/v1/metrics
 * </pre>
 *
 * @author MIHAELA PAPARETE (IBM)
 * @since 2020-04-08
 *
 */
@Path("")
@DynamicPath
public class MetricsEndpoint extends AbstractMonitoringEndpoint<MonitoringEndpointDefinition> {

    private MetricsService _metricsService;

    @Inject
    protected MetricsEndpoint(MonitoringEndpointDefinition endpointDefinition, MetricsService service) {
        super(endpointDefinition);
        _metricsService = service;
    }

    @GET
    @Path("")
    @Produces(MediaType.APPLICATION_JSON)
    public MetricsInfo getMetrics() {
        return _metricsService.getInfoMetrics();
    }

}
