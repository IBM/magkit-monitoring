package de.ibmix.magkit.monitoring.endpoint.overview;

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

import de.ibmix.magkit.monitoring.endpoint.AbstractMonitoringEndpoint;
import de.ibmix.magkit.monitoring.endpoint.ConfiguredMonitoringEndpointDefinition;
import de.ibmix.magkit.monitoring.endpoint.MonitoringEndpointDefinition;
import info.magnolia.context.MgnlContext;
import info.magnolia.rest.registry.EndpointDefinitionRegistry;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.ArrayList;
import java.util.List;

/**
 * REST endpoint aggregating and listing all registered monitoring endpoints grouped by version.
 * It inspects Magnolia's {@link info.magnolia.rest.registry.EndpointDefinitionRegistry} to build a categorized map
 * excluding itself.
 * <p><strong>Purpose</strong></p>
 * Provides a discovery mechanism for clients to enumerate available monitoring endpoints and their paths.
 * <p><strong>Main Functionality</strong></p>
 * Iterates over all endpoint definition providers, filters monitoring definitions, derives version/name segments from their reference id and assembles a version-keyed map excluding the overview endpoint itself.
 * <p><strong>Key Features</strong></p>
 * <ul>
 * <li>Groups endpoints by version (custom or semantic version folder).</li>
 * <li>Excludes the overview endpoint from the list.</li>
 * <li>Builds stable path references for each endpoint.</li>
 * </ul>
 * <p><strong>Usage Preconditions</strong></p>
 * Requires a functional Magnolia context and an initialized endpoint registry. Invocation outside a web request will fail due to path resolution logic.
 * <p><strong>Null and Error Handling</strong></p>
 * Returns 200 with possibly empty map if no endpoints are present. No explicit error handling inside iteration; relies on registry stability.
 * <p><strong>Thread-Safety</strong></p>
 * Stateless aside from injected registry reference; safe for concurrent GET operations.
 * <p><strong>Usage Example</strong></p>
 * <pre>{@code
 * Response r = overviewEndpoint.overview();
 * }</pre>
 * <p><strong>Important Details</strong></p>
 * Reference id parsing assumes structure monitoring[/version]/name; deviations from this pattern may categorize incorrectly.
 * @author Dan Olaru (IBM)
 * @since 2020-04-24
 */
@Path("/monitoring")
public class OverviewEndpoint extends AbstractMonitoringEndpoint<MonitoringEndpointDefinition> {

    private final EndpointDefinitionRegistry _registry;

    /**
     * Constructs the overview endpoint with its definition and registry reference.
     * @param endpointDefinition monitoring endpoint definition metadata
     * @param registry Magnolia endpoint definition registry
     */
    @Inject
    protected OverviewEndpoint(MonitoringEndpointDefinition endpointDefinition, EndpointDefinitionRegistry registry) {
        super(endpointDefinition);
        _registry = registry;
    }

    /**
     * Builds a categorized overview of monitoring endpoints grouped by version.
     * @return HTTP response containing map of version to list of endpoint info
     */
    @GET
    @Path("")
    @Produces(MediaType.APPLICATION_JSON)
    public Response overview() {
        Overview endpointsOverview = new Overview();
        String uriPath = MgnlContext.getWebContext().getRequest().getRequestURI();
        _registry.getAllProviders().forEach(p -> {

            // INFO: the types of system variables we are working with
            // p ----------------------> DefinitionProvider<EndpointDefinition>
            // p.get() ------------------> EndpointDefinition endpointDefinition

            if (p.get() instanceof ConfiguredMonitoringEndpointDefinition) {

                // "path"
                String endpointPath = uriPath.replace("monitoring", p.getMetadata().getReferenceId());

                String[] refIdElements = p.getMetadata().getReferenceId().split("/");

                // the endpoint is NOT versioned: "monitoring" + moduleName
                String version = "custom";
                String name = refIdElements[1];

                if (refIdElements.length == 3) {
                    // the endpoint is versioned: "monitoring" + version + moduleName
                    version = refIdElements[1];
                    name = refIdElements[2];
                }

                // we don't want to list our own Overview endpoint among the found endpoints;
                // every other endpoint that implements or extends
                // ConfiguredMonitoringEndpointDefinition is however added to the list
                boolean isThisOurOverviewEndpoint = (p.getMetadata().getModule().equals("magkit-monitoring")) && ("overview".equals(name));

                if (!isThisOurOverviewEndpoint) {

                    EndpointInfo myEndpoint = new EndpointInfo(name, endpointPath);
                    if (endpointsOverview.getCategorizedEndpoints().containsKey(version)) {
                        endpointsOverview.getCategorizedEndpoints().get(version).add(myEndpoint);
                    } else {
                        endpointsOverview.getCategorizedEndpoints().put(version,
                            new ArrayList<>(List.of(myEndpoint)));
                    }
                }
            }
        });

        return Response.status(Status.OK).entity(endpointsOverview.getCategorizedEndpoints()).build();
    }
}
