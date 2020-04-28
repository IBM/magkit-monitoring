package com.aperto.magkit.monitoring.endpoint.scheduler;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aperto.magkit.monitoring.endpoint.AbstractMonitoringEndpoint;
import com.aperto.magkit.monitoring.endpoint.MonitoringEndpointDefinition;

import info.magnolia.rest.DynamicPath;

/**
 * 
 * This endpoint provides information about the recurring jobs configured in
 * Magnolia.
 * 
 * It is reachable under the following path: /.rest/monitoring/v1/scheduler
 * 
 * @author MIHAELA PAPARETE (IBM)
 * @since 2020-04-23
 *
 */
@Path("")
@DynamicPath
public class SchedulerEndpoint extends AbstractMonitoringEndpoint<MonitoringEndpointDefinition> {
	private static final Logger LOGGER = LoggerFactory.getLogger(SchedulerEndpoint.class);

	private SchedulerService schedulerService;

	@Inject
	protected SchedulerEndpoint(MonitoringEndpointDefinition endpointDefinition, SchedulerService service) {
		super(endpointDefinition);

		this.schedulerService = service;
	}

	@GET
	@Path("")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getScheduledJobs() {
		try {
			return Response.status(Status.OK).entity(schedulerService.getEnabledJobs()).build();
		} catch (Exception e) {
			LOGGER.error("An error occurred getting the scheduled jobs", e);
			return Response.status(Status.BAD_REQUEST).entity("An error occurred: "+e.getMessage()).build();
		}
	}
}
