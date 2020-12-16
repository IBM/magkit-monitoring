package com.aperto.magkit.monitoring.endpoint.overview;

import java.util.ArrayList;
import java.util.Arrays;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.aperto.magkit.monitoring.endpoint.AbstractMonitoringEndpoint;
import com.aperto.magkit.monitoring.endpoint.ConfiguredMonitoringEndpointDefinition;
import com.aperto.magkit.monitoring.endpoint.MonitoringEndpointDefinition;

import info.magnolia.rest.registry.EndpointDefinitionRegistry;

/**
 * 
 * Overview Endpoint.
 * 
 * @author Dan Olaru (IBM)
 * @since 2020-04-24
 *
 */

@Path("/monitoring")
public class OverviewEndpoint extends AbstractMonitoringEndpoint<MonitoringEndpointDefinition> {

    private EndpointDefinitionRegistry _registry;

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

        _registry.getAllProviders().forEach(p -> {

            // INFO: the types of system variables we are working with
            // p ----------------------> DefinitionProvider<EndpointDefinition>
            // p.get() ------------------> EndpointDefinition endpointDefinition

            if (p.get() instanceof ConfiguredMonitoringEndpointDefinition) {

                // "path"
                String endpointPath = "/.rest/" + p.getMetadata().getReferenceId();

                String[] refIdElements = p.getMetadata().getReferenceId().split("/");

                String version = "";
                String name = "";

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
                boolean isThisOurOverviewEndpoint = (p.getMetadata().getModule().equals("monitoring"))
                        && ("overview".equals(name));

                if (!isThisOurOverviewEndpoint) {

                    EndpointInfo myEndpoint = new EndpointInfo(name, endpointPath);
                    if (endpointsOverview.getCategorizedEndpoints().keySet().contains(version)) {
                        endpointsOverview.getCategorizedEndpoints().get(version).add(myEndpoint);
                    } else {
                        endpointsOverview.getCategorizedEndpoints().put(version,
                                new ArrayList<EndpointInfo>(Arrays.asList(myEndpoint)));
                    }
                }

            }
        });

        return Response.status(Status.OK).entity(endpointsOverview.getCategorizedEndpoints()).build();
    }
}
