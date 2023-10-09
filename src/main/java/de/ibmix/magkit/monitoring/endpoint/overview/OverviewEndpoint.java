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
 * Overview Endpoint.
 *
 * @author Dan Olaru (IBM)
 * @since 2020-04-24
 */
@Path("/monitoring")
public class OverviewEndpoint extends AbstractMonitoringEndpoint<MonitoringEndpointDefinition> {

    private final EndpointDefinitionRegistry _registry;

    @Inject
    protected OverviewEndpoint(MonitoringEndpointDefinition endpointDefinition, EndpointDefinitionRegistry registry) {
        super(endpointDefinition);

        _registry = registry;
    }

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

                String version;
                String name;
                if (refIdElements.length == 3) {
                    // the endpoint is versioned: "monitoring" + version + moduleName
                    version = refIdElements[1];
                    name = refIdElements[2];
                } else {
                    // the endpoint is NOT versioned: "monitoring" + moduleName
                    version = "custom";
                    name = refIdElements[1];
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
                        endpointsOverview.getCategorizedEndpoints().put(version, new ArrayList<>(List.of(myEndpoint)));
                    }
                }
            }
        });

        return Response.status(Status.OK).entity(endpointsOverview.getCategorizedEndpoints()).build();
    }
}
