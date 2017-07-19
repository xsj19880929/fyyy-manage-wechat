package com.suryani.manage.schedule.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.http.client.ClientProtocolException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.quidsi.core.util.StringUtils;
import com.suryani.manage.booking.domain.Booking;
import com.suryani.manage.booking.domain.DateTime;
import com.suryani.manage.booking.service.BookingService;
import com.suryani.manage.booking.service.DateTimeService;
import com.suryani.manage.util.Constants;
import com.suryani.manage.util.Utils;

@Service
public class BookingDoctorService {
    private final Logger logger = LoggerFactory.getLogger(BookingDoctorService.class);
    @Inject
    private BookingService bookingService;
    @Inject
    private DateTimeService dateTimeService;

    public void startTread() {
        List<Booking> taskList = bookingService.listAfterOneWeek();
        if (taskList != null && !taskList.isEmpty()) {
            ExecutorService executor = Executors.newFixedThreadPool(taskList.size());
            for (Booking task : taskList) {
                final JSONObject jsonTask = Utils.beanToJSON(task);
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        downTime(jsonTask);
                        startWork(jsonTask);
                    }
                });

            }
            logger.info("启动任务数=======" + taskList.size());
            executor.shutdown();
            while (!executor.isTerminated()) {
            }
        }

    }

    // 定义错误信息
    private String defineError(String responseString) {
        if (responseString.contains("网络通讯错误")) {
            logger.info("返回错误信息:网络通讯错误");
            return "error101";

        }
        if (responseString.contains("已经被他人预约")) {
            logger.info("返回错误信息:你选择的时间段已经被他人预约");
            return "error102";
        }
        if (responseString.contains("身份证非18位")) {
            logger.info("返回错误信息:身份证非18位");
            return "error103";
        }
        if (responseString.contains("身份证错误")) {
            logger.info("返回错误信息:身份证错误");
            return "error104";
        }
        if (responseString.contains("您非本院首诊或外地病友")) {
            logger.info("返回错误信息:您非本院首诊或外地病友");
            String icard = getICard(responseString);
            icard = icard.replace("：", "").replace("\"", "");
            return "error105" + icard;
        }
        if (responseString.contains("非医保卡或厦门市健康卡")) {
            logger.info("返回错误信息:非医保卡或厦门市健康卡");
            return "error106";
        }
        if (responseString.contains("男人的卡不能预约")) {
            logger.info("返回错误信息:男人的卡不能预约妇科");
            return "error107";
        }
        if (responseString.contains("该预约医生已无号源")) {
            logger.info("返回错误信息:该预约医生已无号源");
            return "error108";
        }
        if (responseString.contains("超过可预约日期范围")) {
            logger.info("返回错误信息:超过可预约日期范围");
            return "error109";
        }
        if (responseString.contains("已有两次预约")) {
            logger.info("返回错误信息:已有两次预约，本院限定每人每天预约两次");
            return "error110";
        }
        if (responseString.contains("预约失败")) {
            logger.info("返回错误信息:预约失败！请拨打本院的预约电话0592-2662000或拔打96166预约。");
            return "error111";
        }
        if (responseString.contains("非预约时间号")) {
            logger.info("返回错误信息:你选择的是非预约时间号");
            return "error120";
        }

        if (responseString.contains("预约挂号成功")) {
            logger.info("成功了！！！！！！");
            return "ok";
        }
        if (responseString.contains("已有预约")) {
            logger.info("已经预约过了！！！！！！");
            return "successed";
        }

        logger.info("返回错误信息:未知错误信息{" + responseString + "}");
        return null;
    }

    // 开始预约
    private void startWork(JSONObject task) {
        Map<String, String> params = Utils.toMap(task);
        List<DateTime> times = Utils.sortList(dateTimeService.list(task.getString(Constants.DOCTOR_ID), task.getString(Constants.SelectDate)),
                Integer.parseInt(task.getString(Constants.SelectTime).replace(":", "")));
        if (params == null || times == null || times.isEmpty()) {
            return;
        }
        if (!StringUtils.equals(task.getString(Constants.autoTime), "1")) {
            params.put(Constants.SelectTime, times.get(0).getSelectTime());
        }
        boolean flag = true;
        while (flag) {
            try {
                long startRespose = System.currentTimeMillis();
                String responseString = Utils.getHtmlByPost(Constants.POSTURL, params);
                logger.info("提交数据耗时：" + (System.currentTimeMillis() - startRespose) + "ms");
                String errorMsg = defineError(responseString);
                if ("ok".equals(errorMsg)) {
                    flag = false;
                    logger.info(responseString);
                    bookingService.updateStatus(task.getString(Constants.ID), Booking.STATUS_SUCCESS, params.get(Constants.SelectTime));
                } else if ("error102".equals(errorMsg) || "error120".equals(errorMsg)) {
                    List<DateTime> newTimes = Utils.sortList(timeList(task), Integer.parseInt(task.getString(Constants.SelectTime).replace(":", "")));
                    if (newTimes != null && !newTimes.isEmpty()) {
                        params.put("SelectTime", newTimes.get(0).getSelectTime());
                        logger.info("=================================预约时间：" + newTimes.get(0).getSelectTime());
                    } else {
                        flag = false;
                        logger.info("哎呀没抢到~~~");
                        bookingService.updateStatus(task.getString(Constants.ID), Booking.STATUS_FAIL);
                    }
                } else if ("error103".equals(errorMsg) || "error104".equals(errorMsg) || "error106".equals(errorMsg) || "error111".equals(errorMsg)) {
                    flag = false;
                    logger.info("=============这个用户信息错误" + Utils.toJsonString(params));
                    bookingService.updateStatus(task.getString(Constants.ID), Booking.STATUS_FAIL);

                } else if ("error107".equals(errorMsg) || "error108".equals(errorMsg) || "error109".equals(errorMsg) || "error110".equals(errorMsg)) {
                    flag = false;
                    logger.info("=============预约失败" + Utils.toJsonString(params));
                    bookingService.updateStatus(task.getString(Constants.ID), Booking.STATUS_FAIL);

                } else if ("successed".equals(errorMsg)) {
                    flag = false;
                    logger.info("已经预约过了用户信息" + Utils.toJsonString(params));
                    bookingService.updateStatus(task.getString(Constants.ID), Booking.STATUS_SUCCESS);

                } else if (errorMsg != null && errorMsg.startsWith("error105")) {
                    String icardid = errorMsg.replace("error105", "");
                    params.put("icardid", icardid);
                    logger.info("=================================卡号：" + icardid);
                } else {
                    // try {
                    // Thread.sleep(1000);
                    // } catch (InterruptedException e) {
                    // e.printStackTrace();
                    // }
                }

            } catch (ClientProtocolException e) {
                e.printStackTrace();
                logger.error(e.getMessage() + Utils.toJsonString(params));

            } catch (IOException e) {
                e.printStackTrace();
                logger.error(e.getMessage() + Utils.toJsonString(params));

            }
        }

    }

    // 下载预约时间列表
    private synchronized void downTime(JSONObject task) {
        List<DateTime> times = dateTimeService.list(task.getString(Constants.DOCTOR_ID), task.getString(Constants.SelectDate));
        if (times != null && !times.isEmpty()) {
            logger.info(task.getString(Constants.DOCTOR) + "的时间任务存在");
            return;
        }
        String html = null;
        JSONArray array = new JSONArray();
        boolean flag = true;
        while (flag) {
            try {
                html = Utils.getHtmlByRequest(Utils.SpliceUrl(task));
                flag = false;
            } catch (Exception e) {
                flag = true;
                logger.error(e.getMessage());
                e.printStackTrace();
            }
        }

        Document doc = Jsoup.parse(html);
        Elements eles = doc.select("td").select("a");
        for (Element ele : eles) {
            String time = getTime(ele.html());
            if (time != null) {
                logger.info(time);
                array.add(time);
            }
        }
        for (int i = 0; i < array.size(); i++) {
            DateTime dateTime = new DateTime();
            dateTime.setId(UUID.randomUUID().toString());
            dateTime.setSelectDate(task.getString(Constants.SelectDate));
            dateTime.setDoctor(task.getString(Constants.DOCTOR));
            dateTime.setDoctorId(task.getString(Constants.DOCTOR_ID));
            dateTime.setSelectTime(array.getString(i));
            dateTimeService.save(dateTime);
        }

    }

    // 下载预约时间列表
    private List<DateTime> timeList(JSONObject task) {
        logger.info("重新获取预约时间");
        String html = null;
        boolean flag = true;
        while (flag) {
            try {
                html = Utils.getHtmlByRequest(Utils.SpliceUrl(task));
                flag = false;
            } catch (Exception e) {
                flag = true;
                logger.error(e.getMessage());
                e.printStackTrace();
            }
        }
        List<DateTime> times = new ArrayList<DateTime>();
        Document doc = Jsoup.parse(html);
        Elements eles = doc.select("td").select("a");
        for (Element ele : eles) {
            String time = getTime(ele.html());
            if (time != null) {
                // logger.info(time);
                DateTime dateTime = new DateTime();
                dateTime.setSelectTime(time);
                times.add(dateTime);
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
