package com.suryani.manage.system.service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.suryani.manage.system.dao.SystemLogDao;
import com.suryani.manage.system.domain.SystemLog;
import com.suryani.manage.system.dto.SystemLogDto;

@Service
public class SystemLogService {

    public static final Integer RES_SUCC = 1;
    public static final Integer RES_FAILD = 0;

    @Inject
    private SystemLogDao systemLogDao;

    @Transactional
    public SystemLog save(String userId, String operation, Integer result) {
        SystemLog log = new SystemLog();
        log.setId(UUID.randomUUID().toString());
        log.setUserId(userId);
        log.setOperation(operation);
        log.setCreateTime(new Date());
        log.setResult(result);
        return this.systemLogDao.save(log);
    }

    @Transactional
    public SystemLog save(SystemLog log) {
        return this.systemLogDao.save(log);
    }

    public List<SystemLog> list(int offset, int fetchSize) {
        List<SystemLog> list = systemLogDao.list(offset, fetchSize);
        return list;
    }

    public List<SystemLogDto> listDetail(int offset, int fetchSize) {
        List<SystemLogDto> list = systemLogDao.listDetail(offset, fetchSize);
        return list;
    }

    public int total() {
        return this.systemLogDao.getTotalSize();
    }

}
