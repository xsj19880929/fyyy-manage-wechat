package com.suryani.manage.schedule.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import net.sf.json.JSONObject;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.suryani.manage.booking.domain.DateTime;
import com.suryani.manage.booking.service.DoctorService;
import com.suryani.manage.util.CalendarUtils;
import com.suryani.manage.util.Constants;
import com.suryani.manage.util.MailSenderUtil;
import com.suryani.manage.util.Utils;

@Service
public class DoctorClinicService {
    private final Logger logger = LoggerFactory.getLogger(DoctorClinicService.class);
    @Inject
    private DoctorService doctorService;
    @Inject
    private MailSenderUtil mailSenderUtil;
    List<Map<String, Object>> listClinc = new ArrayList<Map<String, Object>>();

    public void startTread(int threadNum) {
        listClinc = allClinic();
        if (listClinc != null && !listClinc.isEmpty()) {
            ExecutorService executor = Executors.newFixedThreadPool(threadNum);
            for (int i = 0; i < threadNum; i++) {
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        startWork();
                    }
                });

            }
            executor.shutdown();
            while (!executor.isTerminated()) {
            }
        }

    }

    // 开始预约
    private void startWork() {
        String htmlBody = "";
        while (listClinc.size() > 0) {
            Map<String, Object> clinicTask = listClinc.remove(0);
            List<DateTime> timeList = timeList(JSONObject.fromObject(clinicTask));
            if (!timeList.isEmpty()) {
                htmlBody = htmlBody + clinicTask.get(Constants.DOCTOR) + "(" + clinicTask.get(Constants.SelectDate) + "-" + clinicTask.get(Constants.TIME_DESC) + ")剩余(" + timeList.size()
                        + ")个预约号<br/>";
                logger.info(clinicTask.get(Constants.DOCTOR) + "(" + clinicTask.get(Constants.SelectDate) + "-" + clinicTask.get(Constants.TIME_DESC) + ")剩余(" + timeList.size() + ")个预约号\n");
            }
        }
        if (htmlBody != "") {
            mailSenderUtil.sendMail("有预约号提醒", htmlBody);
        }

    }

    // 获取所有医生的所有门诊日期任务
    private List<Map<String, Object>> allClinic() {
        List<Map<String, Object>> list = doctorService.listDoctorClinic();
        List<Map<String, Object>> listClincNew = new ArrayList<Map<String, Object>>();
        String[] timeDescArr = new String[] { "上午", "下午" };
        for (String timeDesc : timeDescArr) {
            for (int i = 7; i <= 7; i++) {
                String selectDate = CalendarUtils.getCurrentAfterStartTime(i);
                for (Map<String, Object> mapDoctor : list) {
                    Map<String, Object> newMap = new HashMap<String, Object>();
                    newMap.put(Constants.DOCTOR, mapDoctor.get("doctor"));
                    newMap.put(Constants.DOCTOR_ID, mapDoctor.get("doctorId"));
                    newMap.put(Constants.DEPT_NAME, mapDoctor.get("deptName"));
                    newMap.put(Constants.TRIAGE_NO, mapDoctor.get("triageNo"));
                    newMap.put(Constants.SelectDate, selectDate);
                    newMap.put(Constants.TIME_DESC, timeDesc);
                    listClincNew.add(newMap);
                }
            }
        }
        return listClincNew;
    }

    // 下载预约时间列表
    private List<DateTime> timeList(JSONObject task) {
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
}
