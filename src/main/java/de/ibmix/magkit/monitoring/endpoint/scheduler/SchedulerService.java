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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.quartz.CronExpression;

import info.magnolia.module.scheduler.JobDefinition;
import info.magnolia.module.scheduler.SchedulerModule;

import static org.apache.commons.lang3.StringUtils.EMPTY;

/**
 * Service layer component providing enabled scheduled job metadata for the scheduler monitoring endpoint.
 * <p><strong>Purpose</strong></p>
 * Iterates Magnolia scheduler jobs, filters enabled ones, computes next valid execution time using Quartz cron semantics and maps them into {@link JobInfo} instances.
 * <p><strong>Main Functionality</strong></p>
 * For each enabled {@link info.magnolia.module.scheduler.JobDefinition} parses its cron, determines next execution time and assembles a {@link JobInfo} structure for transport.
 * <p><strong>Key Features</strong></p>
 * <ul>
 * <li>Encapsulates cron evaluation.</li>
 * <li>Produces stable ISO-like timestamp formatting.</li>
 * <li>Isolates Magnolia scheduler dependency from REST layer.</li>
 * </ul>
 * <p><strong>Usage Preconditions</strong></p>
 * Magnolia {@link info.magnolia.module.scheduler.SchedulerModule} must be active and injected.
 * <p><strong>Null and Error Handling</strong></p>
 * Throws an {@link Exception} when cron parsing fails; caller maps errors to HTTP responses.
 * <p><strong>Thread-Safety</strong></p>
 * Stateless aside from reference to injected module; safe for concurrent access.
 * <p><strong>Side Effects</strong></p>
 * None – only reads scheduler configuration and performs in-memory transformation.
 * <p><strong>Usage Example</strong></p>
 * <pre>{@code
 * List<JobInfo> jobs = schedulerService.getEnabledJobs();
 * }</pre>
 * <p><strong>Important Details</strong></p>
 * Cron expressions must conform to Quartz syntax; invalid patterns halt processing and surface as exceptions to callers.
 *
 * @author MIHAELA PAPARETE (IBM)
 * @since 2020-04-23
 */
public class SchedulerService {

    private final SchedulerModule _schedulerModule;

    /**
     * <p>Constructs the service with the Magnolia scheduler module dependency.</p>
     *
     * @param module injected Magnolia scheduler module used to access configured jobs.
     */
    @Inject
    protected SchedulerService(SchedulerModule module) {
        _schedulerModule = module;
    }

    /**
     * <p>Returns metadata for all enabled scheduled jobs including computed next execution timestamp.</p>
     * <p>Cron expressions are parsed using Quartz; invalid expressions result in an {@link Exception}.</p>
     *
     * @return list of enabled job info objects; empty list if no jobs are enabled.
     * @throws Exception if a cron expression cannot be parsed or next execution time cannot be determined.
     */
    public List<JobInfo> getEnabledJobs() {
        List<JobInfo> scheduledJobs = new ArrayList<JobInfo>();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        for (JobDefinition job : _schedulerModule.getJobs()) {
            if (job.isEnabled()) {
                JobInfo jobInfo = new JobInfo();

                String cronExpression = job.getCron();
                jobInfo.setName(job.getName());
                jobInfo.setDescription(job.getDescription());
                jobInfo.setCron(cronExpression);
                jobInfo.setNextExecution(getNextExecution(cronExpression, formatter));
                scheduledJobs.add(jobInfo);
            }
        }

        return scheduledJobs;
    }

    String getNextExecution(String cronExpression, SimpleDateFormat formatter) {
        String result = EMPTY;
        try {
            CronExpression cron = new CronExpression(cronExpression);
            Date nextExecution = cron.getNextValidTimeAfter(new Date());
            result =  formatter.format(nextExecution);
        } catch (ParseException e) {
            result = "Invalid cron expression: '" + cronExpression + "'. - " + e.getMessage();
        }
        return result;
    }
}
