package com.suryani.manage.schedule.service;

import com.suryani.manage.booking.domain.AgentIP;
import com.suryani.manage.booking.service.AgentIPService;
import com.suryani.manage.common.IPHost;
import com.suryani.manage.common.IPProxyPool;
import com.suryani.manage.util.HttpClentUtils;
import org.apache.http.client.CookieStore;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * @author soldier
 */
@Service
public class AgentIPCrawler {
    @Inject
    AgentIPService agentIPService;

    public void crawler() {
        try {
            Map<String, Object> cookies = agentIPService.loginGetCookie();
            String responseHtml = HttpClentUtils.getHtmlByRequest("http://www.66ip.cn/nmtq.php?getnum=100&isp=0&anonymoustype=3&start=&ports=&export=&ipaddress=&area=1&proxytype=2&api=66ip");
            Document doc = Jsoup.parse(responseHtml);
            Elements elements = doc.select("body");
            String body = elements.text();
            String[] ipPortArray = body.split(" ");
            for (String ipPort : ipPortArray) {
                String[] ipHost = ipPort.split(":");
                String ip = ipHost[0].trim();
                int port = Integer.parseInt(ipHost[1].trim());
                AgentIP agentIP = new AgentIP();
                agentIP.setIp(ip);
                agentIP.setPort(port);
                if (agentIPService.getSizeByIp(ip) == 0) {
                    int status;
                    Long timeLong = 10000L;
                    try {
                        Map<String, Object> result = HttpClentUtils.testDoReservation(agentIPService.getParam(), (CookieStore) cookies.get("cookies"), agentIP.getIp(), agentIP.getPort());
                        if (((Integer) result.get("statusCode")).compareTo(200) == 0) {
                            status = 0;
                            timeLong = (Long) result.get("timeLong");
                        } else {
                            status = 1;
                        }
                    } catch (Exception e) {
                        status = 1;
                        e.printStackTrace();
                    }
                    if (timeLong < 5000 && status == 0 && checkIpIsAble(ip, port)) {
                        agentIP.setTimeLong(timeLong.intValue());
                        agentIP.setStatus(status);
                        agentIP.setCreatedTime(new Date());
                        agentIP.setId(UUID.randomUUID().toString());
                        agentIP.setUseStatus(0);
                        agentIPService.save(agentIP);
                        IPHost host = new IPHost(agentIP.getIp(), agentIP.getPort());
                        IPProxyPool.release(host);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public Boolean checkIpIsAble(String ip, int port) throws Exception {
        String responseHtml = HttpClentUtils.getHtmlByRequestAgentContent("http://1212.ip138.com/ic.asp", ip, port);
        Document doc = Jsoup.parse(responseHtml);
        Elements elements = doc.select("center");
        String str = elements.text();
        System.out.println(str + ip);
        if (str.contains(ip)) {
            return true;
        } else {
            return false;
        }

    }
}

