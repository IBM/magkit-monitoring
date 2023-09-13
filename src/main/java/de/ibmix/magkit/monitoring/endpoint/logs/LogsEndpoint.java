package de.ibmix.magkit.monitoring.endpoint.logs;

/*-
 * #%L
 * IBM iX Magnolia Monitoring
 * %%
 * Copyright (C) 2023 IBM iX
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

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

import de.ibmix.magkit.monitoring.endpoint.AbstractMonitoringEndpoint;
import de.ibmix.magkit.monitoring.endpoint.MonitoringEndpointDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import info.magnolia.context.MgnlContext;
import info.magnolia.init.MagnoliaConfigurationProperties;
import info.magnolia.rest.DynamicPath;

/**
 * LogsEndpoint.
 *
 * This endpoint provides a JSON list of existing files in the log folder and
 * also provides the contents of the log file specified as a path parameter.
 *
 * @author Dan Olaru (IBM)
 * @author MIHAELA PAPARETE (IBM)
 * @since 2020-04-09
 *
 */

@Path("")
@DynamicPath
public class LogsEndpoint extends AbstractMonitoringEndpoint<MonitoringEndpointDefinition> {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogsEndpoint.class);

    private MagnoliaConfigurationProperties _magnoliaProperties;

    private String _baseLogFilePath = "";

    @Inject
    protected LogsEndpoint(MonitoringEndpointDefinition endpointDefinition,
            MagnoliaConfigurationProperties properties) {
        super(endpointDefinition);
        _magnoliaProperties = properties;

        _baseLogFilePath = _magnoliaProperties.getProperty("magnolia.logs.dir");

    }

    @GET
    @Path("")
    @Produces(MediaType.APPLICATION_JSON)
    public Response availableLogs() {

        List<LogInfo> logFolderContents = new ArrayList<>();

        // it should be "./monitoring/v1/logs/"
        String pathBase;

        try {
            StringBuffer fullUrlPath = MgnlContext.getWebContext().getRequest().getRequestURL();

            pathBase = fullUrlPath.substring(fullUrlPath.indexOf("."));

            // ignores other files that might be in the logs folder that don't have the
            // extension ".log"
            Files.newDirectoryStream(Paths.get(_baseLogFilePath), "*.log").forEach(f -> {
                logFolderContents.add(
                        new LogInfo(f.getFileName().toString(), "/" + pathBase + "/" + f.getFileName().toString()));
            });

        } catch (IOException e) {
            LOGGER.error("An error occurred:", e);
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }

        return Response.status(Status.OK).entity(logFolderContents).build();
    }

    @GET
    @Path("/{logName}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response logs(@PathParam("logName") String logName) {

        String fullLogFilePath = "";

        // preparing the strings
        String uniformLogName = logName.toLowerCase();

        if (uniformLogName.endsWith(".log")) {
            fullLogFilePath = _baseLogFilePath + "/" + uniformLogName;
        } else {
            fullLogFilePath = _baseLogFilePath + "/" + uniformLogName + ".log";
        }

        StringBuilder contentBuilder = new StringBuilder();
        try (Stream<String> stream = Files.lines(Paths.get(fullLogFilePath), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s).append("\r\n"));
        } catch (IOException e) {
            LOGGER.error("An error occurred:", e);
            return Response.status(Status.BAD_REQUEST).entity("The specified log file couldn't be found!").build();
        }

        return Response.status(Status.OK).entity(contentBuilder).build();
    }

}
