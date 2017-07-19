package com.suryani.manage.booking.service;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.suryani.manage.booking.dao.JobScheduleDao;
import com.suryani.manage.booking.domain.JobSchedule;

@Service
public class JobScheduleService {

    @Inject
    private JobScheduleDao jobScheduleDao;

    public List<JobSchedule> list(int offset, int size) {
        return this.jobScheduleDao.findAll(offset, size);
    }

    public int total() {
        return this.jobScheduleDao.getTotalSize();
    }

    @Transactional
    public void delete(String id) {
        this.jobScheduleDao.delete(id);
    }

    public JobSchedule getById(String id) {
        return this.jobScheduleDao.findOne(id);
    }

    @Transactional
    public void save(JobSchedule jobSchedule) {
        this.jobScheduleDao.save(jobSchedule);
    }

    @Transactional
    public void update(JobSchedule jobSchedule) {
        this.jobScheduleDao.update(jobSchedule);
    }

    public List<JobSchedule> getJobToDo() {
        return jobScheduleDao.getJobToDo();
    }

}