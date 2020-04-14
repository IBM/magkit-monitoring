package com.aperto.magkit.monitoring.endpoint.logs;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
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
import com.aperto.magkit.monitoring.endpoint.MonitoringEndpointDefinition;

import info.magnolia.init.MagnoliaConfigurationProperties;
import info.magnolia.rest.DynamicPath;

/**
 * 
 * This endpoint provides the contents of the specified log file.
 * 
 *
 */
@Path("")
@DynamicPath
public class LogsEndpoint extends AbstractMonitoringEndpoint<MonitoringEndpointDefinition> {
	private MagnoliaConfigurationProperties magnoliaProperties;

	@Inject
	protected LogsEndpoint(MonitoringEndpointDefinition endpointDefinition,
	        MagnoliaConfigurationProperties properties) {
		super(endpointDefinition);
		this.magnoliaProperties = properties;
	}

	@GET
	@Path("/{logName}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response logs(@PathParam("logName") String logName) {
		String baseLogFilePath = magnoliaProperties.getProperty("magnolia.logs.dir");
		String fullLogFilePath = baseLogFilePath + "/" + logName + ".log";

		StringBuilder contentBuilder = new StringBuilder();
		try (Stream<String> stream = Files.lines(Paths.get(fullLogFilePath), StandardCharsets.UTF_8)) {
			stream.forEach(s -> contentBuilder.append(s).append("\r\n"));
		} catch (IOException e) {
			return Response.status(Status.BAD_REQUEST).entity("The specified log file couldn't be found!").build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}

		return Response.status(Status.OK).entity(contentBuilder).build();
	}
}
