package com.aperto.magkit.monitoring.endpoint.scheduler;

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
