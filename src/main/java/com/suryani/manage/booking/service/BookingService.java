package com.suryani.manage.booking.service;

import com.suryani.manage.booking.dao.BookingDao;
import com.suryani.manage.booking.domain.Booking;
import com.suryani.manage.util.Utils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

@Service
public class BookingService {

    @Inject
    private BookingDao bookingDao;

    @Transactional
    public void updateStatus(String id, String status) {
        bookingDao.updateStatus(id, status, null);
    }

    @Transactional
    public void updateStatus(String id, String status, String selectTime) {
        bookingDao.updateStatus(id, status, selectTime);
    }

    public List<Booking> list(Map<String, Object> params, int offset, int size) {
        List<Booking> list = bookingDao.findAll(params, offset, size);
        for (Booking booking : list) {
            // booking.setTaskUrl(Constants.GETDOCTORURL +
            // booking.getDoctorSn());
            booking.setTaskUrl(Utils.SpliceUrlNew(booking));
        }
        return list;
    }

    public int total(Map<String, Object> params) {
        return this.bookingDao.getTotalSize(params);
    }

    @Transactional
    public void delete(String id) {
        this.bookingDao.delete(id);
    }

    public Booking getById(String id) {
        return this.bookingDao.findOne(id);
    }

    @Transactional
    public void save(Booking booking) {
        this.bookingDao.save(booking);
    }

    @Transactional
    public void update(Booking booking) {
        this.bookingDao.update(booking);
    }

    public List<Booking> listAfterOneWeek() {
        return bookingDao.findAfterOneWeek();
    }

}