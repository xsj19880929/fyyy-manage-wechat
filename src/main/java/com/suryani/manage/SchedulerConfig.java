package com.suryani.manage;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import com.quidsi.core.platform.DefaultSchedulerConfig;
import com.quidsi.core.platform.JobRegistry;
import com.quidsi.core.platform.scheduler.Job;
import com.suryani.manage.booking.domain.JobSchedule;
import com.suryani.manage.booking.service.JobScheduleService;

@Configuration
public class SchedulerConfig extends DefaultSchedulerConfig {
    private final Logger logger = LoggerFactory.getLogger(SchedulerConfig.class);

    private static final long JOB_SCAN_INTERVAL = 60000000; // 10 mins
    @Inject
    private JobScheduleService jobScheduleService;
    private JobRegistry registry = null;

    @Override
    protected void configure(JobRegistry registry) {
        this.registry = registry;

        registJobFromTable();
    }

    @SuppressWarnings("unchecked")
    @Scheduled(fixedRate = JOB_SCAN_INTERVAL)
    public void registJobFromTable() {
        if (null == registry) {
            return;
        }
        logger.info("regist job from table begin.");
        List<JobSchedule> jobScheduleList = this.jobScheduleService.getJobToDo();
        logger.info("jobScheduleList size={}", jobScheduleList.size());
        for (JobSchedule jobSchedule : jobScheduleList) {
            Class<? extends Job> jobClass = null;
            try {
                logger.info("job:" + jobSchedule.getJobClass());
                jobClass = (Class<? extends Job>) Class.forName(jobSchedule.getJobClass());

            } catch (ClassNotFoundException e) {
                logger.info("The job {} execute exception, message={}", jobSchedule.getJobClass(), e.getMessage());
                // updateJobState(jobSchedule, JobSchedule.FAIL_JOB_STATE);
                continue;
            }
            registry.triggerByCronExpression(UUID.randomUUID().toString(), jobClass, jobSchedule.getCronExpression());
            // updateJobState(jobSchedule, JobSchedule.EXE_JOB_STATE);
        }
    }

    private void updateJobState(JobSchedule jobSchedule, int newState) {
        jobSchedule.setJobState(newState);
        jobSchedule.setUpdatedTime(new Date());
        jobScheduleService.update(jobSchedule);
    }
}