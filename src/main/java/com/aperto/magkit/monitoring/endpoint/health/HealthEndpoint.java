package com.aperto.magkit.monitoring.endpoint.health;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.aperto.magkit.monitoring.endpoint.AbstractMonitoringEndpoint;
import com.aperto.magkit.monitoring.endpoint.MonitoringEndpointDefinition;

import info.magnolia.rest.DynamicPath;

/**
 * 
 * Health Endpoint.
 * 
 * @author Soenke Schmidt (Aperto - An IBM Company)
 * @since 2020-03-29
 *
 */
@Path("")
@DynamicPath
public class HealthEndpoint extends AbstractMonitoringEndpoint<MonitoringEndpointDefinition> {

    @Inject
    protected HealthEndpoint(MonitoringEndpointDefinition endpointDefinition) {
        super(endpointDefinition);
    }

    @GET
    @Path("")
    @Produces(MediaType.APPLICATION_JSON)
    public Health health() {
        return new Health();
    }

}
