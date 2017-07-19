package com.suryani.manage.schedule.service;

import javax.inject.Inject;

import com.suryani.manage.booking.service.DateTimeService;

public class MyRunnable implements Runnable {
    @Inject
    private DateTimeService dateTimeService;

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName());
        dateTimeService.list("123", "123");
    }
}
