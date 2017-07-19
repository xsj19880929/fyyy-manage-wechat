package com.suryani.manage.booking.service;

import com.suryani.manage.booking.dao.AgentIPDao;
import com.suryani.manage.booking.domain.AgentIP;
import com.suryani.manage.util.Constants;
import com.suryani.manage.util.HttpClentUtils;
import org.apache.http.client.CookieStore;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AgentIPService {

    @Inject
    private AgentIPDao agentIPDao;

    public List<AgentIP> list(int offset, int size) {
        List<AgentIP> agentIPList = this.agentIPDao.findAll(offset, size);
        return agentIPList;
    }

    public List<AgentIP> listNoPage() {
        List<AgentIP> agentIPList = this.agentIPDao.findAllNoPage();
        return agentIPList;
    }

    public List<AgentIP> findAllStatus() {
        return agentIPDao.findAllStatus();
    }

    @Transactional
    public void updateAllTimeLong() {
        Map<String, Object> cookies = loginGetCookie();
        List<AgentIP> list = agentIPDao.findAllNoPage();
        for (AgentIP agentIP : list) {
            int status = 0;
            Long timeLong = 10000L;
//            Long start = System.currentTimeMillis();
            try {
                Map<String, Object> result = HttpClentUtils.testDoReservation(getParam(), (CookieStore) cookies.get("cookies"), agentIP.getIp(), agentIP.getPort());
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
//            timeLong = System.currentTimeMillis() - start;
            if (timeLong < 5000 && status == 0 && checkIpIsAble(agentIP.getIp(), agentIP.getPort())) {
                agentIP.setTimeLong(timeLong.intValue());
                agentIP.setStatus(status);
                agentIPDao.update(agentIP);
            } else {
                agentIPDao.delete(agentIP.getId());
            }
        }
    }

    private Boolean checkIpIsAble(String ip, int port) {
        String responseHtml = "";
        try {
            responseHtml = HttpClentUtils.getHtmlByRequestAgentContent("http://1212.ip138.com/ic.asp", ip, port);
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public Map<String, String> getParam() {
        Map<String, String> result = new HashMap<>();
        result.put(Constants.AM_PM, "AM");
        result.put(Constants.DEPT_CODE, "330");
        result.put(Constants.DOCTOR_CODE, "281");
        result.put(Constants.CARD_NO, "D88512805");
        result.put(Constants.ID_CARD, "350521198710181105");
        result.put(Constants.NAME, "%E5%BC%A0%E6%83%A0%E8%90%8D");
        result.put(Constants.ORG_ID, "7");
        result.put(Constants.START_TIME, "2016/9/10 10:00:00");
        result.put(Constants.TELEPHONE, "15060787835");
        result.put(Constants.PASSWORD, "");
        result.put(Constants.NUMBER_ID, "728120160910AM1000");
        result.put(Constants.SCHEDULE_ID, "728120160910AM");
        return result;
    }

    public Map<String, Object> loginGetCookie() {
        Map<String, Object> cookies = new HashMap<String, Object>();
        try {
            cookies = HttpClentUtils.doLogin("D88512805", "350521198710181105");
        } catch (Exception e) {
            e.printStackTrace();

        }
        return cookies;
    }

    @Transactional
    public void updateTimeLong(AgentIP agentIP) {
        int status = 0;
        Long timeLong = 10000L;
        Long start = System.currentTimeMillis();
        try {
            int statueCode = HttpClentUtils.getHtmlByRequestAgent("http://www.xmsmjk.com/UrpOnline/Home/GetIndexList", agentIP.getIp(), agentIP.getPort());
            if (statueCode == 200) {
                status = 0;
            } else {
                status = 1;
            }
        } catch (Exception e) {
            status = 1;
            e.printStackTrace();
        }
        timeLong = System.currentTimeMillis() - start;
        agentIP.setTimeLong(timeLong.intValue());
        agentIP.setStatus(status);
        agentIPDao.update(agentIP);

    }

    public int total() {
        return this.agentIPDao.getTotalSize();
    }

    @Transactional
    public void delete(String id) {
        this.agentIPDao.delete(id);
    }

    public AgentIP getById(String id) {
        return this.agentIPDao.findOne(id);
    }

    @Transactional
    public void save(AgentIP agentIP) {
        this.agentIPDao.save(agentIP);
    }

    @Transactional
    public void update(AgentIP agentIP) {
        this.agentIPDao.update(agentIP);
    }

    public int getSizeByIp(String ip) {
        return agentIPDao.getSizeByIp(ip);
    }

    public List<AgentIP> findByIp(String ip) {
        return agentIPDao.findByIp(ip);
    }
}