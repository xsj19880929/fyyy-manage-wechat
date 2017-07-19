package com.suryani.manage.common;

import com.suryani.manage.booking.domain.AgentIP;
import com.suryani.manage.booking.service.AgentIPService;
import com.suryani.manage.schedule.service.AgentIPCrawler;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author soldier
 */
@Service
public class IPProxyStrategy {
    final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    @Inject
    AgentIPService agentIPService;
    @Inject
    AgentIPCrawler agentIPCrawler;

    @PostConstruct
    void init() {
//        agentIPService.updateAllTimeLong();
        List<AgentIP> agentIPList = agentIPService.listNoPage();
        for (AgentIP agentIP : agentIPList) {
            IPHost ipHost = new IPHost(agentIP.getIp(), agentIP.getPort());
            IPProxyPool.release(ipHost);
        }
//        doCrawler();

    }

    private void doCrawler() {
        executorService.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                if ((double) IPProxyPool.size() / IPProxyPool.CAPACITY < 0.5) {
                    agentIPCrawler.crawler();
                }
            }
        }, 0l, 10l, TimeUnit.SECONDS);
    }
}
