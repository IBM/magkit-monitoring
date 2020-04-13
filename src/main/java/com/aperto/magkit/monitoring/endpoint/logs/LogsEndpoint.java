package com.aperto.magkit.monitoring.endpoint.logs;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.aperto.magkit.monitoring.endpoint.AbstractMonitoringEndpoint;
import com.aperto.magkit.monitoring.endpoint.MonitoringEndpointDefinition;

import info.magnolia.rest.DynamicPath;

@Path("")
@DynamicPath
public class LogsEndpoint extends AbstractMonitoringEndpoint<MonitoringEndpointDefinition> {

	@Inject
	protected LogsEndpoint(MonitoringEndpointDefinition endpointDefinition) {
		super(endpointDefinition);
	}
	
	@GET
	@Path("/{logName}")
	@Produces(MediaType.APPLICATION_JSON)
	public Logs logs(@PathParam("logName") String logName) {

		Logs myLog =  new Logs(logName);
		
		//TODO: find a way to get the contents of the log file
		
		return myLog;
	}
	
	@GET
	@Path("")
	@Produces(MediaType.APPLICATION_JSON)
	public Logs logs() {
		return new Logs();
	}
}
