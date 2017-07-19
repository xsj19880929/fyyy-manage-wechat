package com.suryani.manage.system.service;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.suryani.manage.system.dao.SystemUserDao;
import com.suryani.manage.system.domain.SystemUser;
import com.suryani.manage.util.HashUtil;

@Service
public class SystemUserService {

    @Inject
    private SystemUserDao systemUserDao;

    @Transactional
    public void updateStatus(String id, String status) {
        systemUserDao.updateStatus(id, status);
    }

    @Transactional
    public void updatePwd(String id, String pwd) {
        systemUserDao.updatePwd(id, HashUtil.getMD5Data(pwd));
    }

    public SystemUser getUser(String name, String pwd) {
        return systemUserDao.getByNameAndPwdWithStatus(name, HashUtil.getMD5Data(pwd), SystemUser.ENABLE);
    }

    public SystemUser getUserWithPhone(String phone, String pwd) {
        return systemUserDao.getByPhoneAndPwdWithStatus(phone, HashUtil.getMD5Data(pwd), SystemUser.ENABLE);
    }

    public List<SystemUser> list(String name, int offset, int size) {
        return this.systemUserDao.findAll(name, offset, size);
    }

    public int total(String name) {
        return this.systemUserDao.getTotalSize(name);
    }

    @Transactional
    public void enable(String id) {
        this.systemUserDao.updateStatus(id, SystemUser.ENABLE);
    }

    @Transactional
    public void delete(String id) {
        this.systemUserDao.delete(id);
    }

    public SystemUser getById(String id) {
        return this.systemUserDao.findOne(id);
    }

    @Transactional
    public void save(SystemUser user) {
        this.systemUserDao.save(user);
    }

    @Transactional
    public void update(SystemUser user) {
        this.systemUserDao.update(user);
    }

    public int countUserByName(String name) {
        return this.systemUserDao.countUserByName(name);
    }
    
    public int countUserByPhone(String phone) {
        return this.systemUserDao.countUserByPhone(phone);
    }
}