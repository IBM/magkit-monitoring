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

import de.ibmix.magkit.monitoring.endpoint.AbstractMonitoringEndpoint;
import de.ibmix.magkit.monitoring.endpoint.MonitoringEndpointDefinition;
import info.magnolia.context.MgnlContext;
import info.magnolia.init.MagnoliaConfigurationProperties;
import info.magnolia.rest.DynamicPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * LogsEndpoint.
 * <p>
 * This endpoint provides a JSON list of existing files in the log folder and
 * also provides the contents of the log file specified as a path parameter.
 *
 * @author Dan Olaru (IBM)
 * @author MIHAELA PAPARETE (IBM)
 * @since 2020-04-09
 */
@Path("")
@DynamicPath
public class LogsEndpoint extends AbstractMonitoringEndpoint<MonitoringEndpointDefinition> {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogsEndpoint.class);

    private final String _baseLogFilePath;

    @Inject
    protected LogsEndpoint(MonitoringEndpointDefinition endpointDefinition, MagnoliaConfigurationProperties properties) {
        super(endpointDefinition);
        _baseLogFilePath = properties.getProperty("magnolia.logs.dir");
    }

    @GET
    @Path("")
    @Produces(MediaType.APPLICATION_JSON)
    public Response availableLogs() {
        List<LogInfo> logFolderContents = new ArrayList<>();

        // ignores other files that might be in the logs folder that don't have the extension ".log"
        try (DirectoryStream<java.nio.file.Path> paths = Files.newDirectoryStream(Paths.get(_baseLogFilePath), "*.log")) {
            String uriPath = MgnlContext.getWebContext().getRequest().getRequestURI();

            paths.forEach(f -> logFolderContents.add(new LogInfo(f.getFileName().toString(), uriPath + "/" + f.getFileName())));
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
        // preparing the strings
        String uniformLogName = logName.toLowerCase();
        String fullLogFilePath = _baseLogFilePath + "/" + uniformLogName + ".log";

        if (uniformLogName.endsWith(".log")) {
            fullLogFilePath = _baseLogFilePath + "/" + uniformLogName;
        }

        StringBuilder contentBuilder = new StringBuilder();
        try (Stream<String> stream = Files.lines(Paths.get(fullLogFilePath), UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s).append("\r\n"));
        } catch (IOException e) {
            LOGGER.error("An error occurred:", e);
            return Response.status(Status.BAD_REQUEST).entity("The specified log file couldn't be found!").build();
        }

        return Response.status(Status.OK).entity(contentBuilder).build();
    }

}
