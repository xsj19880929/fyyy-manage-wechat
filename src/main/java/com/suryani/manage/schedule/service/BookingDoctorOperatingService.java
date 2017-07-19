package com.suryani.manage.schedule.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONObject;

import org.apache.http.client.ClientProtocolException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.suryani.manage.util.Constants;
import com.suryani.manage.util.Utils;

@Service
public class BookingDoctorOperatingService {
    private final Logger logger = LoggerFactory.getLogger(BookingDoctorOperatingService.class);

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
    public String simbitBookingDoctor(JSONObject task) {
        Map<String, String> params = Utils.toMap(task);
        String responseString = "";
        try {
            responseString = Utils.getHtmlByPost(Constants.POSTURL, params);
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
}
