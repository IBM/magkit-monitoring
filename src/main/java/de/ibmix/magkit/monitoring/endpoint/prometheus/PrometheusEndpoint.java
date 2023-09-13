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
 * Prometheus endpoint class.
 *
 * @author VladNacu
 * @author Sönke Schmidt - IBM iX
 *
 */
@Path("")
@DynamicPath
public class PrometheusEndpoint extends AbstractMonitoringEndpoint<MonitoringEndpointDefinition> {

    private final PrometheusMeterRegistry _registry;

    @Inject
    protected PrometheusEndpoint(MonitoringEndpointDefinition endpointDefinition, PrometheusMeterRegistry registry) {
        super(endpointDefinition);
        _registry = registry;

    }

    @GET
    @Path("")
    @Produces(MediaType.TEXT_PLAIN)
    public String prometheus() {
        return _registry.scrape();
    }

}
