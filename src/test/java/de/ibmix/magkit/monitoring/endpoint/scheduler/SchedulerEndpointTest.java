/*-
 * #%L
 * IBM iX Magnolia Monitoring
 * %%
 * Copyright (C) 2025 IBM iX
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
package de.ibmix.magkit.monitoring.endpoint.scheduler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Collections;
import java.util.List;

import javax.ws.rs.core.Response;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import de.ibmix.magkit.monitoring.endpoint.MonitoringEndpointDefinition;

/**
 * Unit tests for {@link SchedulerEndpoint} covering successful retrieval and error mapping to HTTP 400.
 * @author GitHub Copilot
 * @since 2025-11-13
 */
public class SchedulerEndpointTest {

    /**
     * Verifies HTTP 200 response with scheduled jobs list on success.
     */
    @Test
    public void testGetScheduledJobsSuccess() throws Exception {
        MonitoringEndpointDefinition def = Mockito.mock(MonitoringEndpointDefinition.class);
        SchedulerService service = Mockito.mock(SchedulerService.class);
        JobInfo info = new JobInfo();
        info.setName("jobOne");
        info.setDescription("desc");
        info.setCron("0/5 * * * * ?");
        info.setNextExecution("2025-11-13T10:00:00");
        Mockito.when(service.getEnabledJobs()).thenReturn(Collections.singletonList(info));
        SchedulerEndpoint endpoint = new SchedulerEndpoint(def, service);
        Response response = endpoint.getScheduledJobs();
        assertEquals(200, response.getStatus());
        Object entity = response.getEntity();
        assertNotNull(entity);
        @SuppressWarnings("unchecked")
        List<JobInfo> jobs = (List<JobInfo>) entity;
        assertEquals(1, jobs.size());
        assertEquals("jobOne", jobs.get(0).getName());
    }

    /**
     * Verifies HTTP 400 response when service throws exception containing message prefix.
     */
    @Test
    public void testGetScheduledJobsError() throws Exception {
        MonitoringEndpointDefinition def = Mockito.mock(MonitoringEndpointDefinition.class);
        SchedulerService service = Mockito.mock(SchedulerService.class);
        Mockito.when(service.getEnabledJobs()).thenThrow(new RuntimeException("Something wild is going on."));
        SchedulerEndpoint endpoint = new SchedulerEndpoint(def, service);
        Response response = endpoint.getScheduledJobs();
        assertEquals(400, response.getStatus());
        Object entity = response.getEntity();
        assertTrue(entity instanceof String);
        assertEquals("An error occurred: Something wild is going on.", (String) entity);
    }
}

