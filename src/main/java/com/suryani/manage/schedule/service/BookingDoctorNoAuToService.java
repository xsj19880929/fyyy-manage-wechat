package com.suryani.manage.schedule.service;

import com.quidsi.core.util.StringUtils;
import com.suryani.manage.booking.domain.AgentIP;
import com.suryani.manage.booking.domain.Booking;
import com.suryani.manage.booking.domain.DateTime;
import com.suryani.manage.booking.service.AgentIPService;
import com.suryani.manage.booking.service.BookingService;
import com.suryani.manage.booking.service.DateTimeService;
import com.suryani.manage.common.IPHost;
import com.suryani.manage.common.IPProxyPool;
import com.suryani.manage.util.Constants;
import com.suryani.manage.util.HttpClentUtils;
import com.suryani.manage.util.Utils;
import net.sf.json.JSONObject;
import org.apache.http.client.CookieStore;
import org.apache.http.conn.ConnectTimeoutException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class BookingDoctorNoAuToService {
    private final Logger logger = LoggerFactory.getLogger(BookingDoctorNoAuToService.class);
    @Inject
    private BookingService bookingService;
    @Inject
    private DateTimeService dateTimeService;
    @Inject
    private AgentIPService agentIPService;

    // 定义错误信息
    private String defineError(String responseString) {
        if (responseString.contains("不存在的预约号")) {
            logger.info("返回错误信息:不存在的预约号");
            return "error101";

        }
        if (responseString.contains("已经没有可以预约的号源")) {
            logger.info("返回错误信息:已经没有可以预约的号源");
            return "error102";
        }
        if (responseString.contains("请先登录")) {
            logger.info("返回错误信息:当前用户登录信息超时，请先登录");
            return "error103";
        }
        if (responseString.contains("请勿频繁操作")) {
            logger.info("返回错误信息:请勿频繁操作");
            return "error104";
        }
        if (responseString.contains("社区20%预约号已满")) {
            logger.info("返回错误信息:社区20%预约号已满");
            return "error105";
        }
        if (responseString.contains("该号源已被其他用户预约中")) {
            logger.info("返回错误信息:该号源已被其他用户预约中，请选择其他号源！");
            return "error106";
        }
        if (responseString.contains("该市民卡所选日期已有预约")) {
            logger.info("该市民卡所选日期已有预约！！！！！！");
            return "successed";
        }
        if (responseString.contains("已经预约了该医生")) {
            logger.info("已经预约了该医生");
            return "successed";
        }
        if (responseString.contains("成功")) {
            logger.info("成功了哈哈！！！！！！");
            return "ok";
        }
        if (responseString.contains("请拨打预约电话96166预约")) {
            logger.info("返回错误信息:请拨打预约电话96166预约！");
            return "errorip";
        }

        logger.info("返回错误信息:未知错误信息{" + responseString + "}");
        return null;
    }

    // 开始预约
    public String startWork(JSONObject task) {
        // List<DateTime> times =
        // Utils.sortList(dateTimeService.list(task.getString(Constants.DOCTOR_ID),
        // task.getString(Constants.SelectDate)),
        // Integer.parseInt(task.getString(Constants.SelectTime).replace(":",
        // "")));
        // if (task == null || times == null || times.isEmpty()) {
        // return;
        // }
        // if (!StringUtils.equals(task.getString(Constants.autoTime), "1")) {
        // task.put(Constants.SelectTime, times.get(0).getSelectTime());
        // }
        String result = "";
        Map<String, String> params = HttpClentUtils.toMap(task);
        Map<String, Object> cookies = new HashMap<String, Object>();
        String password = params.get(Constants.ID_CARD);
        if (StringUtils.hasText(params.get(Constants.PASSWORD))) {
            password = params.get(Constants.PASSWORD);
        }
        try {
            cookies = HttpClentUtils.doLogin(params.get(Constants.CARD_NO), password);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage() + Utils.toJsonString(params));

        }
        boolean flag = true;
        IPHost ipHost = getIpHost();
        while (flag) {
            if (ipHost.getFailedCount() > 3) {
                ipHost = getIpHost();
            }
            try {
                logger.info(task.getString("ip") + ":" + task.getInt("port"));
                long startRespose = System.currentTimeMillis();
                JSONObject responseJson = HttpClentUtils.doReservation(params, (CookieStore) cookies.get("cookies"), task.getString("ip"), task.getInt("port"));
                String responseString = responseJson.getString("msg");
                result = responseString;
                logger.info("提交数据耗时：" + (System.currentTimeMillis() - startRespose) + "ms");
                String errorMsg = defineError(responseString);
                if ("ok".equals(errorMsg)) {
                    flag = false;
                    logger.info(responseString);
                    bookingService.updateStatus(task.getString(Constants.ID), Booking.STATUS_SUCCESS, task.getString(Constants.SelectTime));
                    List<AgentIP> list = agentIPService.findByIp(task.getString("ip"));
                    for (AgentIP agentIP : list) {
                        agentIP.setUseStatus(0);
                        agentIP.setBookingTime(new Date());
                        agentIPService.update(agentIP);
                    }
                } else if ("error102".equals(errorMsg) || "error101".equals(errorMsg) || "error106".equals(errorMsg)) {
                    logger.info("预约信息" + Utils.toJsonString(params));
                    List<DateTime> newTimes = Utils.sortList(timeListNew(task), Integer.parseInt(task.getString(Constants.SelectTime).replace(":", "")));
                    if (newTimes != null && !newTimes.isEmpty()) {
                        task.put(Constants.SelectTime, newTimes.get(0).getSelectTime());
                        params = HttpClentUtils.toMap(task);
                        logger.info("=================================预约时间：" + newTimes.get(0).getSelectTime());
                    } else {
                        flag = false;
                        logger.info("哎呀没抢到~~~");
                        bookingService.updateStatus(task.getString(Constants.ID), Booking.STATUS_FAIL);
                    }
                } else if ("error103".equals(errorMsg)) {
                    flag = false;
                    logger.info("没有登陆");
                    bookingService.updateStatus(task.getString(Constants.ID), Booking.STATUS_FAIL);

                } else if ("successed".equals(errorMsg)) {
                    flag = false;
                    logger.info("已经预约过了用户信息" + Utils.toJsonString(params));
                    bookingService.updateStatus(task.getString(Constants.ID), Booking.STATUS_SUCCESS);

                } else {
                    // try {
                    // Thread.sleep(1000);
                    // } catch (InterruptedException e) {
                    // e.printStackTrace();
                    // }
                }

                if ("error104".equals(errorMsg)) {
                    ipHost.incrementAndGet();
                    logger.info("请勿频繁操作");

                } else {
                    //ip异常清0
                    ipHost.clearFailedCount();
                }
            } catch (ConnectTimeoutException e) {
                logger.error(e.getMessage(), e);
                ipHost.incrementAndGet();
            } catch (Exception e) {
                e.printStackTrace();
                logger.error(e.getMessage() + Utils.toJsonString(params));
            }
        }
//        List<AgentIP> list = agentIPService.findByIp(task.getString("ip"));
//        for (AgentIP agentIP : list) {
//            agentIP.setUseStatus(0);
//            agentIPService.update(agentIP);
//        }
        return result;

    }

    private IPHost getIpHost() {
        IPHost ipHost = new IPHost("1.1.1.1", 0);
        try {
            ipHost = IPProxyPool.get();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return ipHost;
    }

    // 下载预约时间列表
    private List<DateTime> timeListNew(JSONObject task) {
        logger.info("重新获取预约时间");
        String html = null;
        boolean wait = true;
        List<DateTime> times = new ArrayList<DateTime>();
        while (wait) {
            try {
                // 获取预约列表链接页面
                html = HttpClentUtils.getHtmlByRequest(Constants.GETDOCTORURL + task.getString(Constants.DOCTORSN));
                Document doc = Jsoup.parse(html);
                Element li = doc.select("div.whliesubscribe ul li").last();
                Element a = null;
                Element span = null;
                if ("上午".equals(task.getString(Constants.TIME_DESC))) {
                    a = li.select("div").first().select("a").first();
                    span = li.select("div").first().select("span").first();
                } else {
                    a = li.select("div").last().select("a").first();
                    span = li.select("div").last().select("span").first();
                }
                if (a != null) {
                    String href = a.attr("href");
                    // 获取预约列表页面
                    String dateTimeHtml = HttpClentUtils.getHtmlByRequest(Constants.TONGYIURL + href);
                    Document dateTimeDoc = Jsoup.parse(dateTimeHtml);
                    Elements dateTimeEles = dateTimeDoc.select("div.dateInfoDetail div a");
                    for (Element dateTimeEle : dateTimeEles) {
                        Pattern pattern = Pattern.compile("(.*)-\\d{2}[:]\\d{2}");
                        Matcher matcher = pattern.matcher(dateTimeEle.html());
                        if (matcher.find()) {
                            String time = matcher.group(1);
                            if (time != null) {
                                logger.info(time);
                                DateTime dateTime = new DateTime();
                                dateTime.setSelectTime(time);
                                times.add(dateTime);
                            }
                        }

                    }
                }
                if (span != null) {
                    wait = false;
                } else {
                    Thread.sleep(500);
                    logger.info("还没放号重新获取时间");
                }
            } catch (Exception e) {
                wait = true;
                logger.error(e.getMessage());
                e.printStackTrace();
            }
        }
        return times;
    }

    // 正则表达式获取时间
    private String getTime(String time) {
        if (time == null)
            return null;
        Pattern pattern = Pattern.compile("\\d{2}[:]\\d{2}");
        Matcher matcher = pattern.matcher(time);
        if (matcher.find())
            return matcher.group();
        return null;
    }

    // 正则表达式获取医保卡号
    private String getICard(String html) {
        if (html == null)
            return null;
        Pattern pattern = Pattern.compile("[您的卡号为：]\\w*[\")]");
        Matcher matcher = pattern.matcher(html);
        if (matcher.find())
            return matcher.group();
        return null;
    }
}
