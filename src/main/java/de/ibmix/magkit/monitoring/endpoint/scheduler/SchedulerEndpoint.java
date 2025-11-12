package de.ibmix.magkit.monitoring.endpoint.scheduler;

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

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import de.ibmix.magkit.monitoring.endpoint.AbstractMonitoringEndpoint;
import de.ibmix.magkit.monitoring.endpoint.MonitoringEndpointDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import info.magnolia.rest.DynamicPath;

/**
 * Scheduler monitoring REST endpoint exposing information about all enabled recurring Magnolia scheduler jobs.
 * <p><strong>Purpose</strong></p>
 * Provides operational visibility into configured recurring jobs including their next planned execution time.
 * <p><strong>Main Functionality</strong></p>
 * Delegates retrieval of enabled job definitions and cron evaluation to {@link SchedulerService}, returning results
 * as JSON wrapped in an HTTP response object with appropriate status codes.
 * <p><strong>Key Features</strong></p>
 * <ul>
 * <li>Lightweight, read-only job introspection.</li>
 * <li>Computes next execution time on demand from Quartz cron expressions.</li>
 * <li>Graceful error handling with HTTP 400 on failure.</li>
 * </ul>
 * <p><strong>Usage Preconditions</strong></p>
 * The Magnolia scheduler module must be installed and jobs must be configured; DI must supply {@link SchedulerService}.
 * <p><strong>Null and Error Handling</strong></p>
 * Returns HTTP 400 (BAD_REQUEST) with an error message if job retrieval or cron evaluation fails.
 * <p><strong>Thread-Safety</strong></p>
 * Stateless aside from references to injected immutable endpoint definition and service; invocation is safe for concurrent requests.
 * <p><strong>Usage Example</strong></p>
 * <pre>{@code
 * Response r = schedulerEndpoint.getScheduledJobs();
 * }</pre>
 * <p><strong>Side Effects</strong></p>
 * None – only reads scheduler configuration and calculates next execution timestamps.
 * <p><strong>Important Details</strong></p>
 * Cron parsing relies on Quartz; invalid expressions throw exceptions which are converted to HTTP 400 responses.
 * @author MIHAELA PAPARETE (IBM)
 * @since 2020-04-23
 */
@Path("")
@DynamicPath
public class SchedulerEndpoint extends AbstractMonitoringEndpoint<MonitoringEndpointDefinition> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SchedulerEndpoint.class);

    private final SchedulerService _schedulerService;

    /**
     * <p>Creates a scheduler monitoring endpoint with its definition and backing service.</p>
     *
     * @param endpointDefinition endpoint configuration definition injected by Magnolia.
     * @param service scheduler service providing enabled job data.
     */
    @Inject
    protected SchedulerEndpoint(MonitoringEndpointDefinition endpointDefinition, SchedulerService service) {
        super(endpointDefinition);
        _schedulerService = service;
    }

    /**
     * <p>Returns all enabled scheduled jobs including their next planned execution time.</p>
     * <p>On error a HTTP 400 response containing an explanatory message is returned.</p>
     *
     * @return HTTP response with status 200 and a JSON array of enabled jobs or status 400 on failure.
     */
    @GET
    @Path("")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getScheduledJobs() {
        try {
            return Response.status(Status.OK).entity(_schedulerService.getEnabledJobs()).build();
        } catch (Exception e) {
            LOGGER.error("An error occurred getting the scheduled jobs", e);
            return Response.status(Status.BAD_REQUEST).entity("An error occurred: " + e.getMessage()).build();
        }
    }
}
