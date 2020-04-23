package com.aperto.magkit.monitoring.endpoint.logs;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.aperto.magkit.monitoring.endpoint.AbstractMonitoringEndpoint;
import com.aperto.magkit.monitoring.endpoint.ConfiguredMonitoringEndpointDefinition;
import com.aperto.magkit.monitoring.endpoint.MonitoringEndpointDefinition;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import info.magnolia.init.MagnoliaConfigurationProperties;
import info.magnolia.jcr.util.NodeUtil;
import info.magnolia.rest.DynamicPath;

import java.nio.file.*;

import javax.jcr.Session;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;

import info.magnolia.context.MgnlContext;
import info.magnolia.context.WebContext;

/**
 * 
 * This endpoint provides a JSON list of existing files in the log folder and
 * also provides the contents of the log file specified as a path parameter.
 *
 * @authors Dan Olaru (IBM) MIHAELA PAPARETE (IBM)
 * @since 2020-04-09
 *
 */

@Path("")
@DynamicPath
public class LogsEndpoint extends AbstractMonitoringEndpoint<MonitoringEndpointDefinition> {

	private static final Logger LOGGER = LoggerFactory.getLogger(LogsEndpoint.class);

	private MagnoliaConfigurationProperties magnoliaProperties;
	
	private String baseLogFilePath = "";

	@Inject
	protected LogsEndpoint(MonitoringEndpointDefinition endpointDefinition,
			MagnoliaConfigurationProperties properties) {

		super(endpointDefinition);
		this.magnoliaProperties = properties;
		
		baseLogFilePath = magnoliaProperties.getProperty("magnolia.logs.dir");

	}

	@GET
	@Path("")
	@Produces(MediaType.APPLICATION_JSON)
	public Response availableLogs() {

		List<LogInfo> logFolderContents = new ArrayList<>();

		String pathBase; // = "./monitoring/v1/logs/";

		try {
			StringBuffer fullURLPath = MgnlContext.getWebContext().getRequest().getRequestURL();

			pathBase = fullURLPath.substring(fullURLPath.indexOf("."));

			// ignores other files that might be in the logs folder that don't have the extension ".log"
			Files.newDirectoryStream(Paths.get(baseLogFilePath), "*.log") 
					.forEach(f -> {
						logFolderContents.add(
								new LogInfo(f.getFileName().toString(), "/" + pathBase + "/" + f.getFileName().toString()));
					});

		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}

		return Response.status(Status.OK).entity(logFolderContents).build();
	}

	@GET
	@Path("/{logName}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response logs(@PathParam("logName") String logName) {
		
		String fullLogFilePath = "";

		//prepping the strings
		String uniformLogName = logName.toLowerCase();

		if (uniformLogName.endsWith(".log"))
			fullLogFilePath = baseLogFilePath + "/" + uniformLogName;
		else
			fullLogFilePath = baseLogFilePath + "/" + uniformLogName + ".log";

		StringBuilder contentBuilder = new StringBuilder();
		try (Stream<String> stream = Files.lines(Paths.get(fullLogFilePath), StandardCharsets.UTF_8)) {
			stream.forEach(s -> contentBuilder.append(s).append("\r\n"));
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			return Response.status(Status.BAD_REQUEST).entity("The specified log file couldn't be found!").build();
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}

		return Response.status(Status.OK).entity(contentBuilder).build();
	}

}
