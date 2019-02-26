package com.suryani.manage.schedule.service;

import com.suryani.manage.booking.domain.Booking;
import com.suryani.manage.booking.domain.DateTime;
import com.suryani.manage.util.CalendarUtils;
import com.suryani.manage.util.Constants;
import com.suryani.manage.util.Utils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.protocol.HttpClientContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class BookingDoctorOperatingNewService {
    private final Logger logger = LoggerFactory.getLogger(BookingDoctorOperatingNewService.class);
    @Inject
    private BookingDoctorNewService bookingDoctorNewService;
    @Inject
    private HospitalRegisterService hospitalRegisterService;

    // 正则表达式获取相应内容
    private static String getAlert(String html) {
        if (html == null)
            return null;
        Pattern pattern = Pattern.compile("alert[\\s\\S]*\"\\)");
        Matcher matcher = pattern.matcher(html);
        if (matcher.find())
            return matcher.group().replace("alert(\"", "").replace("\")", "");
        return null;
    }

    // 查询预约
    public String queryBookingDoctor(String icardid) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.icardid, icardid);
        String responseString = "";
        try {
            responseString = Utils.getHtmlByPost(Constants.QUERYURL, params);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return getAlert(responseString);
    }

    // 取消预约
    public String cancelBookingDoctor(String icardid, String selectDate, String doctor) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.icardid, icardid);
        params.put(Constants.username, doctor);
        params.put(Constants.weekday, selectDate);
        String responseString = "";
        try {
            responseString = Utils.getHtmlByPost(Constants.CANCELURL, params);
            logger.info(responseString);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return getAlert(responseString);
    }

    // 预约医生
    public String simbitBookingDoctor(Booking booking) {
        String responseString = "";
        try {
            RegisterBean registerBean = RegisterBeanSwitch.switchBean(booking);
            HttpClientContext clientContext = HttpClientContext.create();
            Map<String, Object> resultMap = hospitalRegisterService.getSsid(registerBean, clientContext);
            List<DateTime> newTimesOne = Utils.sortList(timeListNew(registerBean, clientContext), Integer.parseInt(booking.getSelectTime().replace(":", "")));
            if (newTimesOne != null && !newTimesOne.isEmpty()) {
                booking.setSelectTime(newTimesOne.get(0).getSelectTime());
                registerBean = RegisterBeanSwitch.switchBean(booking);
                registerBean.setTimeCode(newTimesOne.get(0).getTimeCode());
                registerBean.setNumberId(newTimesOne.get(0).getNumberId());
                registerBean.setScheduleId(newTimesOne.get(0).getScheduleId());
                logger.info("=================================预约时间：" + newTimesOne.get(0).getSelectTime());
            } else {
                return "还没有放号";
            }
            registerBean.setSsid(resultMap.get("ssid").toString());
            registerBean.setPatientID(resultMap.get("idno").toString());
            registerBean.setPatientPhone(resultMap.get("telphone").toString());
            registerBean.setTelPhone(resultMap.get("telphone").toString());
            registerBean.setPatientSex(resultMap.get("sex").toString());
            responseString = hospitalRegisterService.handDoRegister(registerBean, clientContext);
            logger.info(responseString);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return responseString;

    }

    // 下载预约时间列表
    private List<DateTime> timeListNew(RegisterBean registerBean, HttpClientContext clientContext) {
        logger.info("重新获取预约时间");
        List<DateTime> times = new ArrayList<DateTime>();
        try {
            List<Map<String, Object>> reservationList = hospitalRegisterService.reservationList(registerBean, clientContext);
            if (reservationList != null) {
                for (Map<String, Object> dateMap : reservationList) {
                    if ("0".equals(dateMap.get("@used").toString()) && !"0".equals(dateMap.get("@max").toString())) {
                        DateTime dateTime = new DateTime();
                        dateTime.setSelectTime(CalendarUtils.format(CalendarUtils.parse(dateMap.get("@start_time").toString(), "yyyy/MM/dd HH:mm:ss"), "HH:mm"));
                        dateTime.setTimeCode(dateMap.get("@time_Code").toString());
                        dateTime.setScheduleId(dateMap.get("@scheduleId").toString());
                        dateTime.setNumberId(dateMap.get("@numberId").toString());
                        times.add(dateTime);
                    }

                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return times;
    }

    public String batchBookingDoctor(Booking booking) {
        return bookingDoctorNewService.startWork(booking);
    }
}
