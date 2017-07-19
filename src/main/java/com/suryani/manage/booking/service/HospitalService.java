package com.suryani.manage.booking.service;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.suryani.manage.booking.dao.HospitalDao;
import com.suryani.manage.booking.domain.Hospital;

@Service
public class HospitalService {

    @Inject
    private HospitalDao hospitalDao;

    public List<Hospital> list(int offset, int size) {
        List<Hospital> hospitalList = this.hospitalDao.findAll(offset, size);
        return hospitalList;
    }

    public int total() {
        return this.hospitalDao.getTotalSize();
    }

    @Transactional
    public void delete(String id) {
        this.hospitalDao.delete(id);
    }

    public Hospital getById(String id) {
        return this.hospitalDao.findOne(id);
    }

    @Transactional
    public void save(Hospital hospital) {
        this.hospitalDao.save(hospital);
    }

    @Transactional
    public void update(Hospital hospital) {
        this.hospitalDao.update(hospital);
    }

    public Hospital getOneByHospitalId(String hospitalId) {
        return hospitalDao.getOneByHospitalId(hospitalId);
    }

}