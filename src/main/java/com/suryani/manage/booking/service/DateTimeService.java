package com.suryani.manage.booking.service;

import java.util.List;

import javax.inject.Inject;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.suryani.manage.booking.dao.DateTimeDao;
import com.suryani.manage.booking.domain.DateTime;

@Service
@Scope(value = "prototype")
public class DateTimeService {

    @Inject
    private DateTimeDao dateTimeDao;

    public List<DateTime> list(String doctorId, String selectDate) {
        return this.dateTimeDao.findAll(doctorId, selectDate);
    }

    @Transactional
    public void save(DateTime dateTime) {
        this.dateTimeDao.save(dateTime);
    }

}