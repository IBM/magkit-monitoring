package com.aperto.magkit.monitoring.endpoint.modules;

import javax.inject.Inject;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.aperto.magkit.monitoring.endpoint.AbstractMonitoringEndpoint;
import com.aperto.magkit.monitoring.endpoint.MonitoringEndpointDefinition;

import info.magnolia.rest.DynamicPath;
import info.magnolia.context.MgnlContext;
import info.magnolia.context.SystemContext;
import info.magnolia.jcr.util.NodeTypes;
import info.magnolia.jcr.util.NodeUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * Modules endpoint
 * 
 * @author Dan Olaru (IBM)
 * @since 2020-04-09
 *
 */

@Path("")
@DynamicPath
public class ModulesEndpoint extends AbstractMonitoringEndpoint<MonitoringEndpointDefinition> {

	private static final Logger LOGGER = LoggerFactory.getLogger(ModulesEndpoint.class);

	@Inject
	protected ModulesEndpoint(MonitoringEndpointDefinition endpointDefinition) {
		super(endpointDefinition);
	}

	@GET
	@Path("")
	@Produces(MediaType.APPLICATION_JSON)
	public Modules modules() {

		Modules registeredModules = new Modules();
		
		try {
			Session session = MgnlContext.getJCRSession("config");
			Node modulesNode = session.getNode("/modules");
			NodeIterator modulesChildrenIterator = modulesNode.getNodes();
			Iterable<Node> childrenOfModules = NodeUtil.asIterable(modulesChildrenIterator);
			childrenOfModules.forEach(c -> registeredModules.addRegisteredModule(c));

		} catch (RepositoryException e) {
			LOGGER.error(e.getMessage());
		}

		return registeredModules;
	}

}
