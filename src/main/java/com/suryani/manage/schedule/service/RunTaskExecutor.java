package com.suryani.manage.schedule.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.http.client.ClientProtocolException;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

import com.suryani.manage.booking.domain.Booking;
import com.suryani.manage.booking.domain.DateTime;
import com.suryani.manage.booking.service.BookingService;
import com.suryani.manage.booking.service.DateTimeService;
import com.suryani.manage.util.Constants;
import com.suryani.manage.util.Utils;

@Component
public class RunTaskExecutor {
    private static Logger logger = Logger.getLogger(RunTask.class.getName());
    @Inject
    private BookingService bookingService;
    @Inject
    private DateTimeService dateTimeService;

    private class RunTask implements Runnable {

        private JSONObject task;

        public RunTask(JSONObject task) {
            this.task = task;
        }

        @Override
        public void run() {
            if (task != null && !task.isEmpty()) {
                downTime(task);
                startWork(task);
            }

        }
    }

    private TaskExecutor taskExecutor;

    public RunTaskExecutor(TaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    public void doIt(JSONObject task) {
        taskExecutor.execute(new RunTask(task));

    }

    // 定义错误信息
    private String defineError(String responseString) {
        if (responseString.contains("网络通讯错误")) {
            logger.info("返回错误信息:网络通讯错误");
            return "error101";

        }
        if (responseString.contains("已被别人申请")) {
            logger.info("返回错误信息:已被别人申请");
            return "error102";
        }
        if (responseString.contains("身份证非18位")) {
            logger.info("返回错误信息:身份证非18位");
            return "error103";
        }
        if (responseString.contains("身份证检验错误")) {
            logger.info("返回错误信息:身份证检验错误");
            return "error104";
        }
        if (responseString.contains("您非本院首诊或外地病友")) {
            logger.info("返回错误信息:您非本院首诊或外地病友");
            String icard = getICard(responseString);
            icard = icard.replace("：", "").replace("\"", "");
            return "error105" + icard;
        }
        if (responseString.contains("卡号错误或非医保卡")) {
            logger.info("返回错误信息:卡号错误或非医保卡");
            return "error106";
        }
        if (responseString.contains("恭喜您预约成功")) {
            logger.info("成功了！！！！！！");
            return "ok";
        }
        if (responseString.contains("所选日期您已有预约")) {
            logger.info("已经预约过了！！！！！！");
            return "successed";
        }

        logger.info("返回错误信息:未知错误信息{" + responseString + "}");
        return null;
    }

    // 开始预约
    private void startWork(JSONObject task) {
        Map<String, String> params = Utils.toMap(task);
        List<DateTime> times = dateTimeService.list(task.getString(Constants.DOCTOR_ID), task.getString(Constants.SelectDate));
        if (params == null || times == null) {
            return;
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
                    bookingService.updateStatus(task.getString(Constants.ID), Booking.STATUS_SUCCESS);
                } else if ("error102".equals(errorMsg)) {
                    DateTime time = times.remove(0);
                    params.put("SelectTime", time.getSelectTime());
                    logger.info("=================================预约时间：" + time.getSelectTime());
                    if (times.isEmpty()) {
                        flag = false;
                        logger.info("哎呀没抢到~~~");
                        bookingService.updateStatus(task.getString(Constants.ID), Booking.STATUS_FAIL);
                    }
                } else if ("error103".equals(errorMsg) || "error104".equals(errorMsg) || "error106".equals(errorMsg)) {
                    flag = false;
                    logger.info("=============这个用户信息错误" + Utils.toJsonString(params));
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
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
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
    private void downTime(JSONObject task) {
        List<DateTime> times = dateTimeService.list(task.getString(Constants.DOCTOR_ID), task.getString(Constants.SelectDate));
        if (times != null && times.isEmpty()) {
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
        Elements eles = doc.select("td[height=30]");
        for (Element ele : eles) {
            String time = getTime(ele.html());
            if (time != null) {
                logger.info(time);
                array.add(time);
            }
        }
        List<DateTime> timesNew = dateTimeService.list(task.getString(Constants.DOCTOR_ID), task.getString(Constants.SelectDate));
        if (timesNew == null || timesNew.isEmpty()) {
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
