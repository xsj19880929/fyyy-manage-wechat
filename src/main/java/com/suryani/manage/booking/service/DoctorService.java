package com.suryani.manage.booking.service;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.suryani.manage.booking.dao.DepartmentDao;
import com.suryani.manage.booking.dao.DoctorDao;
import com.suryani.manage.booking.domain.Department;
import com.suryani.manage.booking.domain.Doctor;
import com.suryani.manage.db.NativeDao;
import com.suryani.manage.db.RsExtractor4MapList;

@Service
public class DoctorService {

    @Inject
    private DoctorDao doctorDao;
    @Inject
    private DepartmentDao departmentDao;
    @Inject
    private NativeDao nativeDao;

    public List<Doctor> list(int offset, int size) {
        List<Doctor> doctorList = this.doctorDao.findAll(offset, size);
        for (Doctor doctor : doctorList) {
            if (doctor.getDeptId() != null) {
                Department department = departmentDao.findOne(doctor.getDeptId());
                if (department != null) {
                    doctor.setDeptId(department.getDeptName());
                }
            }
        }
        return doctorList;
    }

    public List<Map<String, Object>> listDoctorClinic() {
        String sql = "select t1.doctor_id,t1.doctor,t2.dept_name,t2.triage_no from doctor t1 left join department t2 on t1.dept_id=t2.id";
        return nativeDao.getJdbcTemplate().query(sql, new RsExtractor4MapList());
    }

    public int total() {
        return this.doctorDao.getTotalSize();
    }

    @Transactional
    public void delete(String id) {
        this.doctorDao.delete(id);
    }

    public Doctor getById(String id) {
        return this.doctorDao.findOne(id);
    }

    @Transactional
    public void save(Doctor doctor) {
        this.doctorDao.save(doctor);
    }

    @Transactional
    public void update(Doctor doctor) {
        this.doctorDao.update(doctor);
    }

    public List<Doctor> findByDeptId(String deptId) {
        return doctorDao.findByDeptId(deptId);
    }

    public Doctor getOneByDoctorId(String doctorId) {
        return doctorDao.getOneByDoctorId(doctorId);
    }

}