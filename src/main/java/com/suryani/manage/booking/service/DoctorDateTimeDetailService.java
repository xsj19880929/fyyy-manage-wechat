package com.suryani.manage.booking.service;

import com.suryani.manage.booking.dao.DoctorDateTimeDetailDao;
import com.suryani.manage.booking.domain.DoctorDateTimeDetail;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

@Service
public class DoctorDateTimeDetailService {

    @Inject
    private DoctorDateTimeDetailDao doctorDateTimeDetailDao;


    @Transactional
    public void deleteByDoctorDateTimeId(String doctorDateTimeId) {
        this.doctorDateTimeDetailDao.deleteByDoctorDateTimeId(doctorDateTimeId);
    }

    public DoctorDateTimeDetail getById(String id) {
        return this.doctorDateTimeDetailDao.findOne(id);
    }

    @Transactional
    public void save(DoctorDateTimeDetail doctorDateTimeDetail) {
        this.doctorDateTimeDetailDao.save(doctorDateTimeDetail);
    }

    @Transactional
    public void update(DoctorDateTimeDetail doctorDateTimeDetail) {
        this.doctorDateTimeDetailDao.update(doctorDateTimeDetail);
    }

}