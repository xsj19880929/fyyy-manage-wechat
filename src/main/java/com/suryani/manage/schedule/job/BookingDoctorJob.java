package com.suryani.manage.schedule.job;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.quidsi.core.platform.scheduler.Job;
import com.suryani.manage.schedule.service.BookingDoctorNewService;

public class BookingDoctorJob extends Job {
    @Inject
    private BookingDoctorNewService bookingDoctorNewService;
    // @Inject
    // private JobScheduleService jobScheduleService;
    private final Logger logger = LoggerFactory.getLogger(BookingDoctorJob.class);

    @Override
    protected void execute() throws Throwable {
        logger.info("开始执行定时任务");
        bookingDoctorNewService.startTread();
        logger.info("结束执行定时任务");
        // JobSchedule jobSchedule =
        // jobScheduleService.getById("58f8513f-b8a7-41d8-93bc-12dd663012ce");
        // jobSchedule.setJobState(JobSchedule.EXE_JOB_STATE);
        // jobScheduleService.update(jobSchedule);
    }

}
