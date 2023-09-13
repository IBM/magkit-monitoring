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
 *
 * This endpoint provides information about the recurring jobs configured in
 * Magnolia.
 *
 * It is reachable under the following path: /.rest/monitoring/v1/scheduler
 *
 * @author MIHAELA PAPARETE (IBM)
 * @since 2020-04-23
 *
 */
@Path("")
@DynamicPath
public class SchedulerEndpoint extends AbstractMonitoringEndpoint<MonitoringEndpointDefinition> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SchedulerEndpoint.class);

    private SchedulerService _schedulerService;

    @Inject
    protected SchedulerEndpoint(MonitoringEndpointDefinition endpointDefinition, SchedulerService service) {
        super(endpointDefinition);

        _schedulerService = service;
    }

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
