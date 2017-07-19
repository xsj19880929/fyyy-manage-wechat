package com.suryani.manage.schedule.service;

import javax.inject.Inject;

import org.junit.Test;
import org.springframework.test.annotation.Rollback;

import com.suryani.manage.booking.service.AgentIPService;
import common.SpringServiceTest;

public class AgentIPServiceTest extends SpringServiceTest {
    @Inject
    private AgentIPService agentIPService;

    @Test
    @Rollback
    public void test() {
        // agentIPService.updateTimeLong();

    }
}
