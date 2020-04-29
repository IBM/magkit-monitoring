package com.aperto.magkit.monitoring.endpoint.overview;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.aperto.magkit.monitoring.endpoint.AbstractMonitoringEndpoint;
import com.aperto.magkit.monitoring.endpoint.ConfiguredMonitoringEndpointDefinition;
import com.aperto.magkit.monitoring.endpoint.MonitoringEndpointDefinition;

import info.magnolia.rest.DynamicPath;
import info.magnolia.rest.EndpointDefinition;
import info.magnolia.rest.RestDispatcherServlet;
import info.magnolia.rest.registry.EndpointDefinitionRegistry;
import info.magnolia.cms.core.AggregationState;
import info.magnolia.config.registry.DefinitionProvider;
import info.magnolia.context.MgnlContext;
import info.magnolia.context.SystemContext;
import info.magnolia.init.MagnoliaConfigurationProperties;

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

	private EndpointDefinitionRegistry registry;

	@Inject
	protected OverviewEndpoint(MonitoringEndpointDefinition endpointDefinition,
			EndpointDefinitionRegistry registry) {
		super(endpointDefinition);

		this.registry = registry;
	}

	@GET
	@Path("")
	@Produces(MediaType.APPLICATION_JSON)
	public Overview overview() {

		Overview endpointsOverview = new Overview(new ArrayList<EndpointsByCategory>());

		registry.getAllProviders().forEach(p -> {
			DefinitionProvider<EndpointDefinition> myDefP = p;
			EndpointDefinition myEndPDef = myDefP.get();

			if (myEndPDef instanceof ConfiguredMonitoringEndpointDefinition) {

				String endpointPath = "/.rest/" + myDefP.getMetadata().getReferenceId(); // "path"

				String[] refIdElements = myDefP.getMetadata().getReferenceId().split("/");

				String version = "";
				String name = "";

				if (refIdElements.length == 3) {  // the endpoint is versioned: "monitoring" + version + moduleName
					version = refIdElements[1];
					name = refIdElements[2];
				} else {							// the endpoint is NOT versioned: "monitoring" + moduleName
					version = "custom";
					name = refIdElements[1];
				}

				EndpointInfo myEndpoint = new EndpointInfo(name, endpointPath);
				
				endpointsOverview.insertByCategory(myEndpoint, version);

			}

		});

		return endpointsOverview;
	}
}
