package com.suryani.manage.booking.service;

import com.suryani.manage.booking.dao.DoctorDateTimeDao;
import com.suryani.manage.booking.dao.DoctorDateTimeDetailDao;
import com.suryani.manage.booking.domain.DoctorDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

@Service
public class DoctorDateTimeService {

    @Inject
    private DoctorDateTimeDao doctorDateTimeDao;
    @Inject
    private DoctorDateTimeDetailDao doctorDateTimeDetailDao;


    @Transactional
    public void delete(String id) {
        this.doctorDateTimeDao.delete(id);
    }

    public DoctorDateTime getById(String id) {
        return this.doctorDateTimeDao.findOne(id);
    }

    @Transactional
    public void save(DoctorDateTime doctorDateTime) {
        this.doctorDateTimeDao.save(doctorDateTime);
    }

    @Transactional
    public void update(DoctorDateTime doctorDateTime) {
        this.doctorDateTimeDao.update(doctorDateTime);
    }

    public DoctorDateTime findDoctorDateTime(DoctorDateTime doctorDateTimeRequest) {
        DoctorDateTime doctorDateTime = doctorDateTimeDao.findDoctorDateTime(doctorDateTimeRequest);
        if (doctorDateTime != null) {
            doctorDateTime.setDoctorDateTimeDetailList(doctorDateTimeDetailDao.findByDoctorDateTimeId(doctorDateTime.getDoctorId()));
        }
        return doctorDateTime;
    }


}