package com.aperto.magkit.monitoring.endpoint.env;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.aperto.magkit.monitoring.endpoint.AbstractMonitoringEndpoint;
import com.aperto.magkit.monitoring.endpoint.MonitoringEndpointDefinition;

import info.magnolia.init.MagnoliaConfigurationProperties;
import info.magnolia.init.MagnoliaServletContextListener;
import info.magnolia.rest.DynamicPath;

/**
 * Environment Endpoint class
 *
 */
@Path("")
@DynamicPath
public class EnvironmentEndpoint extends AbstractMonitoringEndpoint<MonitoringEndpointDefinition> {

	private final MagnoliaConfigurationProperties _mgnlConfProp;

	@Inject
	protected EnvironmentEndpoint(MonitoringEndpointDefinition endpointDefinition,
			MagnoliaConfigurationProperties mgnlConfProp) {
		
		super(endpointDefinition);
		this._mgnlConfProp = mgnlConfProp;

	}

	@GET
	@Path("")
	@Produces(MediaType.APPLICATION_JSON)
	public Environment env() {
		final Environment env = new Environment();

		env.setJvmArgs(EnvironmentPropertiesUtil.getJvmArguments());
		env.setSysProp(EnvironmentPropertiesUtil.getSystemProperties());
		env.setMagnoliaProperties(getMagnoliaProperties(env.getSysProp()));
		return env;
	}

	private Map<String, String> getMagnoliaProperties(final Map<String, String> systemProperties) {
		final Set<String> magnoliaKeys = _mgnlConfProp.getKeys();
		final Map<String, String> map = new HashMap<>();

		for (final String key : magnoliaKeys) {
			if (!systemProperties.keySet().contains(key)) {
				map.put(key, _mgnlConfProp.getProperty(key));
			}
		}
		return map;
	}

}
