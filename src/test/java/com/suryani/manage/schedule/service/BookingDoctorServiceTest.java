package com.suryani.manage.schedule.service;

import javax.inject.Inject;

import org.junit.Test;
import org.springframework.test.annotation.Rollback;

import com.suryani.manage.booking.dao.BookingDao;
import com.suryani.manage.booking.service.DateTimeService;
import common.SpringServiceTest;

public class BookingDoctorServiceTest extends SpringServiceTest {
    @Inject
    private BookingDoctorService bookingDoctorService;
    @Inject
    private DateTimeService dateTimeService;
    @Inject
    private BookingDao bookingDao;
    @Inject
    private BookingDoctorOperatingService bookingDoctorOperatingService;

    // private static final int NTHREDS = 10;

    @Test
    @Rollback
    public void test() {
        // System.out.println(bookingDoctorOperatingService.queryBookingDoctor("350521198809291558"));
        // System.out.println(bookingDao.findAfterOneWeek().size());
        // bookingDoctorService.startTread();
        // taskService.getTask();
        // taskExecutorExample.printMessages();
        // SimpleAsyncTaskExecutor taskExecutor = new SimpleAsyncTaskExecutor();
        // TaskExecutorExample tmp = new TaskExecutorExample(taskExecutor);
        // tmp.printMessages();
        // for (int i = 0; i < 20; i++) {
        // Thread t = new Thread(new Runnable() {
        // @Override
        // public void run() {
        // System.out.println(dateTimeService.list("", ""));
        // }
        // });
        // t.start();
        //
        // }
        // ExecutorService executor = Executors.newFixedThreadPool(NTHREDS);
        // for (int i = 0; i < NTHREDS; i++) {
        // executor.execute(new Runnable() {
        // @Override
        // public void run() {
        // System.out.println(dateTimeService.list("", ""));
        // }
        // });
        // }
        //
        // executor.shutdown();
        // while (!executor.isTerminated()) {
        // }
    }
}
