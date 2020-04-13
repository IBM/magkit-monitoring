package com.aperto.magkit.monitoring.endpoint.env;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.aperto.magkit.monitoring.endpoint.AbstractMonitoringEndpoint;
import com.aperto.magkit.monitoring.endpoint.MonitoringEndpointDefinition;

import info.magnolia.rest.DynamicPath;

/**
 * Environment Endpoint class
 *
 */
@Path("")
@DynamicPath
public class EnvironmentEndpoint extends AbstractMonitoringEndpoint<MonitoringEndpointDefinition> {

	@Inject
	protected EnvironmentEndpoint(MonitoringEndpointDefinition endpointDefinition) {
		super(endpointDefinition);
	}

	@GET
	@Path("")
	@Produces(MediaType.APPLICATION_JSON)
	public Environment env() {
		final Environment env = new Environment();

		env.setJvmArgs(EnvironmentPropertiesUtil.getJvmArguments());
		env.setSysProp(EnvironmentPropertiesUtil.getSystemProperties());

		return env;
	}

}
