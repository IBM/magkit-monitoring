package com.aperto.magkit.monitoring.endpoint.metrics;

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
 * <pre>
 * This endpoint provides general information about the JVM runtime.
 * - used / available memory
 * - Garbage Collector information
 * 		a) Garbage Collectors Name
 * 		b) Garbage Collectors Memory Pools
 * 		c) Collection Count for each Garbage Collector
 * 		d) Collection Time for each Garbage Collector
 * 		e) Total Collection Count
 * 		d) Total Collection Time
 * - No. of active threads
 * 
 * Example for endpoint call:
 * 		http://localhost:8001/author/.rest/monitoring/v1/metrics
 * </pre>
 * 
 * @author MIHAELA PAPARETE (IBM)
 * @since 2020-04-08
 *
 */
@Path("")
@DynamicPath
public class MetricsEndpoint extends AbstractMonitoringEndpoint<MonitoringEndpointDefinition> {
	private MetricsService metricsService;

	@Inject
	protected MetricsEndpoint(MonitoringEndpointDefinition endpointDefinition, MetricsService service) {
		super(endpointDefinition);
		this.metricsService = service;
	}

	@GET
	@Path("")
	@Produces(MediaType.APPLICATION_JSON)
	public MetricsInfo getMetrics() {
		return metricsService.getInfoMetrics();
	}

}
