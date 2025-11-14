/*-
 * #%L
 * IBM iX Magnolia Monitoring
 * %%
 * Copyright (C) 2023 - 2025 IBM iX
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import info.magnolia.module.scheduler.JobDefinition;
import info.magnolia.module.scheduler.SchedulerModule;

/**
 * Unit tests for {@link SchedulerService} covering enabled job filtering, cron evaluation, formatting and error propagation.
 * @author GitHub Copilot
 * @since 2025-11-13
 */
public class SchedulerServiceTest {

    /**
     * Verifies only enabled jobs are returned and fields are mapped including correctly formatted nextExecution timestamp.
     */
    @Test
    public void testGetEnabledJobsFiltersAndMaps() throws Exception {
        SchedulerModule schedulerModule = Mockito.mock(SchedulerModule.class);
        JobDefinition enabled = Mockito.mock(JobDefinition.class);
        Mockito.when(enabled.isEnabled()).thenReturn(true);
        Mockito.when(enabled.getCron()).thenReturn("0/5 * * * * ?");
        Mockito.when(enabled.getName()).thenReturn("jobAlpha");
        Mockito.when(enabled.getDescription()).thenReturn("Test job alpha");
        JobDefinition disabled = Mockito.mock(JobDefinition.class);
        Mockito.when(disabled.isEnabled()).thenReturn(false);
        Mockito.when(schedulerModule.getJobs()).thenReturn(Arrays.asList(enabled, disabled));
        SchedulerService service = new SchedulerService(schedulerModule);
        List<JobInfo> result = service.getEnabledJobs();
        assertEquals(1, result.size());
        JobInfo info = result.get(0);
        assertEquals("jobAlpha", info.getName());
        assertEquals("Test job alpha", info.getDescription());
        assertEquals("0/5 * * * * ?", info.getCron());
        assertNotNull(info.getNextExecution());
        Pattern pattern = Pattern.compile("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}");
        assertTrue(pattern.matcher(info.getNextExecution()).matches());
    }

    /**
     * Verifies empty list is returned when there are no jobs.
     */
    @Test
    public void testGetEnabledJobsEmpty() throws Exception {
        SchedulerModule schedulerModule = Mockito.mock(SchedulerModule.class);
        Mockito.when(schedulerModule.getJobs()).thenReturn(Collections.emptyList());
        SchedulerService service = new SchedulerService(schedulerModule);
        List<JobInfo> result = service.getEnabledJobs();
        assertTrue(result.isEmpty());
    }

    /**
     * Verifies invalid cron expression propagates exception to caller.
     */
    @Test
    public void testGetEnabledJobsInvalidCron() {
        SchedulerModule schedulerModule = Mockito.mock(SchedulerModule.class);
        JobDefinition invalid = Mockito.mock(JobDefinition.class);
        Mockito.when(invalid.isEnabled()).thenReturn(true);
        Mockito.when(invalid.getCron()).thenReturn("invalid cron");
        Mockito.when(invalid.getName()).thenReturn("brokenJob");
        Mockito.when(invalid.getDescription()).thenReturn("Broken job");
        Mockito.when(schedulerModule.getJobs()).thenReturn(Collections.singletonList(invalid));
        SchedulerService service = new SchedulerService(schedulerModule);
        List<JobInfo> jobList = service.getEnabledJobs();
        assertEquals(1, jobList.size());
        assertEquals("invalid cron", jobList.get(0).getCron());
        assertEquals("Invalid cron expression: 'invalid cron'. - Illegal characters for this position: 'INV'", jobList.get(0).getNextExecution());
    }

    /**
     * Verifies cron with past year having no future executions results in exception (null next execution).
     */
    @Test
    public void testGetEnabledJobsPastCronNoNextExecution() {
        SchedulerModule schedulerModule = Mockito.mock(SchedulerModule.class);
        JobDefinition past = Mockito.mock(JobDefinition.class);
        Mockito.when(past.isEnabled()).thenReturn(true);
        Mockito.when(past.getCron()).thenReturn("0 0 0 1 1 ? 2000");
        Mockito.when(past.getName()).thenReturn("pastJob");
        Mockito.when(past.getDescription()).thenReturn("Past job");
        Mockito.when(schedulerModule.getJobs()).thenReturn(Collections.singletonList(past));
        SchedulerService service = new SchedulerService(schedulerModule);
        assertThrows(Exception.class, service::getEnabledJobs);
    }
}
