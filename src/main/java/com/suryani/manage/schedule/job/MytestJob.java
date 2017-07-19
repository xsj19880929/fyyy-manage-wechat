package com.suryani.manage.schedule.job;

import javax.inject.Inject;

import com.quidsi.core.platform.scheduler.Job;
import com.suryani.manage.booking.domain.JobSchedule;
import com.suryani.manage.booking.service.JobScheduleService;

public class MytestJob extends Job {
    // private final Logger logger = LoggerFactory.getLogger(MytestJob.class);
    @Inject
    private JobScheduleService jobScheduleService;

    @Override
    protected void execute() throws Throwable {
        System.out.println("开始testJOB");
        JobSchedule jobSchedule = jobScheduleService.getById("58f8513f-b8a7-41d8-93bc-12dd663012ce");
        jobSchedule.setJobState(JobSchedule.EXE_JOB_STATE);
        jobScheduleService.update(jobSchedule);
    }
}
