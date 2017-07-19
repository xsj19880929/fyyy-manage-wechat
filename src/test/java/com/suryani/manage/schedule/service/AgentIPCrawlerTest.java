package com.suryani.manage.schedule.service;

import common.SpringServiceTest;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;

import javax.inject.Inject;

public class AgentIPCrawlerTest extends SpringServiceTest {
    @Inject
    private AgentIPCrawler agentIPCrawler;

    @Test
    @Rollback
    public void test() {
//        agentIPCrawler.crawler();

    }
}
