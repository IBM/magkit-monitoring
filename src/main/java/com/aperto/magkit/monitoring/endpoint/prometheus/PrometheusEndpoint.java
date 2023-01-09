package com.aperto.magkit.monitoring.endpoint.prometheus;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.aperto.magkit.monitoring.endpoint.AbstractMonitoringEndpoint;
import com.aperto.magkit.monitoring.endpoint.MonitoringEndpointDefinition;

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
