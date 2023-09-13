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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.quartz.CronExpression;

import info.magnolia.module.scheduler.JobDefinition;
import info.magnolia.module.scheduler.SchedulerModule;

/**
 *
 * This is the service class for Scheduler endpoint.
 *
 * @author MIHAELA PAPARETE (IBM)
 * @since 2020-04-23
 *
 */
public class SchedulerService {

    private SchedulerModule _schedulerModule;

    @Inject
    protected SchedulerService(SchedulerModule module) {
        _schedulerModule = module;
    }

    public List<JobInfo> getEnabledJobs() throws Exception {
        List<JobInfo> scheduledJobs = new ArrayList<JobInfo>();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        for (JobDefinition job : _schedulerModule.getJobs()) {
            if (job.isEnabled()) {
                JobInfo jobInfo = new JobInfo();

                String cronExpression = job.getCron();
                CronExpression cron = new CronExpression(cronExpression);
                Date nextExecution = cron.getNextValidTimeAfter(new Date());

                jobInfo.setName(job.getName());
                jobInfo.setDescription(job.getDescription());
                jobInfo.setCron(cronExpression);
                jobInfo.setNextExecution(formatter.format(nextExecution));
                scheduledJobs.add(jobInfo);
            }
        }

        return scheduledJobs;
    }
}
