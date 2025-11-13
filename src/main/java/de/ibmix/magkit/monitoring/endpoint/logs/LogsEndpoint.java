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
 * REST endpoint providing access to available log files and their contents.
 * Lists all .log files in the configured Magnolia log directory and allows retrieval of an individual file's content.
 * <p><strong>Purpose</strong></p>
 * Facilitates lightweight log inspection without direct filesystem access for operational troubleshooting.
 * <p><strong>Main Functionality</strong></p>
 * Enumerates *.log files in configured directory, constructs access URIs, and streams selected file content with CRLF line endings to aid cross-platform viewing.
 * <p><strong>Key Features</strong></p>
 * <ul>
 * <li>Enumerates log files with dynamic path hints.</li>
 * <li>Returns file contents in plain text with CRLF line endings.</li>
 * <li>Graceful error handling for missing or unreadable files.</li>
 * </ul>
 * <p><strong>Usage Preconditions</strong></p>
 * Magnolia configuration property <code>magnolia.logs.dir</code> must be set. File system permissions must allow read access.
 * <p><strong>Null and Error Handling</strong></p>
 * Returns 500 on directory access issues, 400 if requested file not found. Successful responses always non-null.
 * <p><strong>Thread-Safety</strong></p>
 * Stateless aside from immutable base path; concurrent reads safe. Large files may impact performance under load.
 * <p><strong>Usage Example</strong></p>
 * <pre>{@code
 * Response list = logsEndpoint.availableLogs();
 * }</pre>
 * <p><strong>Important Details</strong></p>
 * Large log files are streamed fully into memory (StringBuilder); consider pagination or tailing for very large files.
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

    /**
     * Returns a JSON list of available .log files with their access URIs.
     * @return HTTP response containing list of {@link LogInfo}
     */
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

    /**
     * Returns the content of the specified log file.
     * @param logName base name or full filename of the log (with or without .log extension)
     * @return HTTP response containing file content or error status
     */
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
