package com.suryani.manage.booking.service;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.suryani.manage.booking.dao.DepartmentDao;
import com.suryani.manage.booking.domain.Department;

@Service
public class DepartmentService {

    @Inject
    private DepartmentDao departmentDao;

    public List<Department> list(int offset, int size) {
        return this.departmentDao.findAll(offset, size);
    }

    public int total() {
        return this.departmentDao.getTotalSize();
    }

    @Transactional
    public void delete(String id) {
        this.departmentDao.delete(id);
    }

    public Department getById(String id) {
        return this.departmentDao.findOne(id);
    }

    @Transactional
    public void save(Department department) {
        this.departmentDao.save(department);
    }

    @Transactional
    public void update(Department department) {
        this.departmentDao.update(department);
    }

    public List<Department> findAllNoPaging() {
        return departmentDao.findAllNoPaging();
    }

}