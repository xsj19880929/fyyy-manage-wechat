package com.suryani.manage.schedule.service;

import javax.inject.Inject;

import org.junit.Test;
import org.springframework.test.annotation.Rollback;

import common.SpringServiceTest;

public class DoctorClinicServiceTest extends SpringServiceTest {
    @Inject
    private DoctorClinicService doctorClinicService;

    @Test
    @Rollback
    public void test() {
        // doctorClinicService.startTread(1);
    }
}
