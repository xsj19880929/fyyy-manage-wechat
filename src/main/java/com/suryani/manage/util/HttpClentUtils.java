package com.suryani.manage.util;

import com.quidsi.core.util.StringUtils;
import com.suryani.manage.booking.domain.Booking;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpClentUtils {
    // get方式获取网页数据
    public static String getHtmlByRequest(String url) throws Exception {
        HttpClient client = new DefaultHttpClient();
        client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, Constants.CONNECTION_TIMEOUT);
        client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, Constants.SO_TIMEOUT);
        HttpGet get = new HttpGet(url);
        HttpResponse response = client.execute(get);
        HttpEntity entity = response.getEntity();
        String responseString = new String(EntityUtils.toString(entity));
        // System.out.println(responseString);
        return responseString;
    }

    // get方式获取网页数据
    public static String getHtmlByRequest(HttpClient client, String url) throws Exception {
        HttpGet get = new HttpGet(url);
        HttpResponse response = client.execute(get);
        HttpEntity entity = response.getEntity();
        String responseString = new String(EntityUtils.toString(entity));
        get.abort();
        return responseString;
    }

    // get方式获取网页数据
    public static int getHtmlByRequestAgent(String url, String ip, int port) throws Exception {
        HttpClient client = new DefaultHttpClient();
        client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);
        client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 10000);
        HttpHost proxy = new HttpHost(ip, port, "http");
        client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
        HttpGet get = new HttpGet(url);
        HttpResponse response = client.execute(get);
        return response.getStatusLine().getStatusCode();
    }

    // get方式获取网页数据
    public static String getHtmlByRequestAgentContent(String url, String ip, int port) throws Exception {
        HttpClient client = new DefaultHttpClient();
        client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);
        client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 10000);
        HttpHost proxy = new HttpHost(ip, port, "http");
        client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
        HttpGet get = new HttpGet(url);
        HttpResponse response = client.execute(get);
        HttpEntity entity = response.getEntity();
        String responseString = new String(EntityUtils.toString(entity, "GBK"));
        return responseString;
    }

    // 登陆平台
    public static Map<String, Object> login(String url) throws Exception {
        HttpClient client = new DefaultHttpClient();
        client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, Constants.CONNECTION_TIMEOUT);
        client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, Constants.SO_TIMEOUT);
        CookieStore cookieStore = new BasicCookieStore();
        BasicClientCookie cookie = new BasicClientCookie("CheckCode", "9BjiN4MtHjk=");
        cookie.setPath("/");
        cookie.setVersion(0);
        cookie.setDomain("www.xmsmjk.com");
        cookieStore.addCookie(cookie);
        ((AbstractHttpClient) client).setCookieStore(cookieStore);
        HttpGet get = new HttpGet(url);
        HttpResponse response = client.execute(get);
        HttpEntity entity = response.getEntity();
        String responseString = new String(EntityUtils.toString(entity));
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("result", responseString);
        map.put("cookies", ((AbstractHttpClient) client).getCookieStore());
        return map;
    }

    // 多次尝试登陆平台
    public static Map<String, Object> doLogin(String ssid, String pwd) throws Exception {
        String url = "http://www.xmsmjk.com/Home/LoginXman?ssid=" + ssid + "&pwd=" + pwd + "&vcode=";
        Map<String, Object> map = new HashMap<String, Object>();
        for (int i = 0; i < 100; i++) {
            map = login(url + i);
            if (!map.get("result").toString().contains("验证码输入错误")) {
                System.out.println(ssid + " login success");
                break;
            }
        }
        return map;
    }

    public static void firstLogin(CookieStore cookieStore) throws Exception {
        HttpClient client = new DefaultHttpClient();
        client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, Constants.CONNECTION_TIMEOUT);
        client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, Constants.SO_TIMEOUT);
        ((AbstractHttpClient) client).setCookieStore(cookieStore);
        HttpGet get = new HttpGet("http://www.xmsmjk.com/UrpOnline/Home/DoReservation");
        client.execute(get);
        get.abort();
        HttpGet get2 = new HttpGet("http://www.xmsmjk.com/MyInfo?first=1");
        client.execute(get2);
        get2.abort();
        HttpGet get3 = new HttpGet("http://www.xmsmjk.com/MyInfo/XmanPhotoCreat");
        client.execute(get3);
        get3.abort();

    }

    public static JSONObject doReservation(Map<String, String> params, CookieStore cookieStore, String ip, int port) throws Exception {
        // Long start = System.currentTimeMillis();
        HttpClient client = new DefaultHttpClient();
        client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, Constants.CONNECTION_TIMEOUT);
        client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, Constants.SO_TIMEOUT);
        if (StringUtils.hasText(ip)) {
            HttpHost proxy = new HttpHost(ip, port, "http");
            client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
        }
        ((AbstractHttpClient) client).setCookieStore(cookieStore);
        String lockNumberMsg = lockNumber(client, params.get(Constants.NUMBER_ID), params.get(Constants.ORG_ID), params.get(Constants.SCHEDULE_ID));
        if (!"ok".equals(lockNumberMsg)) {
            JSONObject json = new JSONObject();
            json.put("msg", lockNumberMsg);
            return json;
        }
        Map<String, String> tokenMap = getToken(client, "http://www.xmsmjk.com/UrpOnline/Home/Reservation/B9040291C00353133DD349B91B3E2F21DA86FEAA2854EF96D29242A2E1B214F0BDA9DB721AF1F486");
        JSONArray vcodeArray = getValidateCodeFont(client);
        String vcode = getCheckValidateCode(client, tokenMap.get("key"), tokenMap.get("token"), params.get(Constants.TELEPHONE));

        params.put("__RequestVerificationKey", tokenMap.get("key"));
        params.put("__RequestVerificationToken", tokenMap.get("token"));
        params.put("VCODE", vcode);
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            formparams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }

        HttpPost post = new HttpPost("http://www.xmsmjk.com/UrpOnline/Home/DoReservation");
        HttpEntity postEntity = new UrlEncodedFormEntity(formparams, "utf-8");
        post.setEntity(postEntity);
        HttpResponse responsePost = client.execute(post);
        HttpEntity entityPost = responsePost.getEntity();
        String reservationResponse = new String(EntityUtils.toString(entityPost));
        JSONObject json = JSONObject.fromObject(reservationResponse);
        return json;
    }

    public static Map<String, Object> testDoReservation(Map<String, String> params, CookieStore cookieStore, String ip, int port) throws Exception {
        System.out.println("检测ip=" + ip);
        HttpClient client = new DefaultHttpClient();
        client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, Constants.CONNECTION_TIMEOUT);
        client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, Constants.SO_TIMEOUT);
        if (StringUtils.hasText(ip)) {
            HttpHost proxy = new HttpHost(ip, port, "http");
            client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
        }
        ((AbstractHttpClient) client).setCookieStore(cookieStore);
        Map<String, String> tokenMap = getToken(client, "http://www.xmsmjk.com/UrpOnline/Home/Reservation/B9040291C00353133DD349B91B3E2F21DA86FEAA2854EF96D29242A2E1B214F0BDA9DB721AF1F486");
        params.put("__RequestVerificationKey", tokenMap.get("key"));
        params.put("__RequestVerificationToken", tokenMap.get("token"));
        params.put("VCODE", "");
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            formparams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        Long start = System.currentTimeMillis();
        HttpPost post = new HttpPost("http://www.xmsmjk.com/UrpOnline/Home/DoReservation");
        HttpEntity postEntity = new UrlEncodedFormEntity(formparams, "utf-8");
        post.setEntity(postEntity);
        HttpResponse responsePost = client.execute(post);

        Map<String, Object> map = new HashMap<>();
        map.put("statusCode", responsePost.getStatusLine().getStatusCode());
        map.put("timeLong", System.currentTimeMillis() - start);
        HttpEntity entityPost = responsePost.getEntity();
        if (responsePost.getStatusLine().getStatusCode() == 200) {
            System.out.println(ip + "可用");
        }
//        String reservationResponse = new String(EntityUtils.toString(entityPost));
//        JSONObject json = JSONObject.fromObject(reservationResponse);
        return map;
    }

    private static JSONArray getValidateCodeFont(HttpClient client) throws Exception {
        HttpPost post = new HttpPost("http://www.xmsmjk.com/UrpOnline/Home/ValidateCodeFont");
        HttpResponse responsePost = client.execute(post);
        HttpEntity entityPost = responsePost.getEntity();
        String reservationResponse = new String(EntityUtils.toString(entityPost));
        HttpGet get = new HttpGet("http://www.xmsmjk.com/UrpOnline/Home/ValidateCode?random=0.32191495936246006");
        HttpResponse responseGet = client.execute(get);
        HttpEntity entity = responseGet.getEntity();
        InputStream in = entity.getContent();
        saveToLocal(in, "D:\\", "test.png");
        get.abort();
        System.out.println(reservationResponse);
        return JSONArray.fromObject(reservationResponse);

    }

    private static void saveToLocal(InputStream in, String filePath, String filename) throws IOException {
        File file = new File(filePath);
        if (!file.exists())
            file.mkdirs();
        DataOutputStream out = new DataOutputStream(new FileOutputStream(
                new File(filePath + filename)));
        int result;
        while ((result = in.read()) != -1) {
            out.write(result);
        }
        out.flush();
        out.close();
    }

    public static String getCheckValidateCode(HttpClient client, String key, String token, String tel) throws Exception {
        String vcode = "";
        Boolean success = false;
        while (!success) {
            JSONArray vcodeArray = getValidateCodeFont(client);
//            vcode = vcodeArray.getString(7) + vcodeArray.getString(1) + vcodeArray.getString(2) + vcodeArray.getString(3);
//            System.out.println(vcode);
            vcode = readTxtFile();
            while (!StringUtils.hasText(vcode)) {
                vcode = readTxtFile();
                Thread.sleep(3000);
            }
            HttpPost post = new HttpPost("http://www.xmsmjk.com/UrpOnline/Home/CheckValidateCode");
            List<NameValuePair> formparams = new ArrayList<NameValuePair>();
            formparams.add(new BasicNameValuePair("__RequestVerificationKey", key));
            formparams.add(new BasicNameValuePair("__RequestVerificationToken", token));
            formparams.add(new BasicNameValuePair("VCODE", vcode));
            formparams.add(new BasicNameValuePair("TELEPHONE", tel));
            HttpEntity postEntity = new UrlEncodedFormEntity(formparams, "utf-8");
            post.setEntity(postEntity);
            HttpResponse responsePost = client.execute(post);
            HttpEntity entityPost = responsePost.getEntity();
            String reservationResponse = new String(EntityUtils.toString(entityPost));
            JSONObject json = JSONObject.fromObject(reservationResponse);
            System.out.println(json.toString());
            success = json.getBoolean("success");
        }
        return vcode;

    }

    public static String lockNumber(HttpClient client, String numberId, String orgId, String scheduleId) throws Exception {
        HttpPost post = new HttpPost("http://www.xmsmjk.com/UrpOnline/Home/LockNumber");
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("NUMBER_ID", numberId));
        formparams.add(new BasicNameValuePair("ORG_ID", orgId));
        formparams.add(new BasicNameValuePair("SCHEDULE_ID", scheduleId));
        HttpEntity postEntity = new UrlEncodedFormEntity(formparams, "utf-8");
        post.setEntity(postEntity);
        HttpResponse responsePost = client.execute(post);
        HttpEntity entityPost = responsePost.getEntity();
        String reservationResponse = new
                String(EntityUtils.toString(entityPost));
        System.out.println("锁号：" + reservationResponse);
        post.abort();
        return reservationResponse;

    }

    // 获取提交预约需要的key和token
    public static Map<String, String> getToken(HttpClient client, String url) throws Exception {
        Map<String, String> tokenMap = new HashMap<String, String>();
        // 获取预约确认页
        String responseString = getHtmlByRequest(client, url);
        Document doc = Jsoup.parse(responseString);
        Elements eles = doc.select("script");
        Element ele = eles.get(5);
        tokenMap.put("token", getRequestVerificationToken(ele.html()));
        tokenMap.put("key", getRequestVerificationKey(ele.html()));
        return tokenMap;

    }

    public static String getError(String content) {
        Document doc = Jsoup.parse(content);
        Element ele = doc.select("div.fail_info div").first();
        return ele.html();
    }

    public static String getRequestVerificationToken(String content) {
        String token = "";
        Pattern pattern = Pattern.compile("name=\"__RequestVerificationToken\" type=\"hidden\" value=\"(.*)\" />");
        Matcher matcher = pattern.matcher(content);
        if (matcher.find())
            token = matcher.group(1);
        return token;
    }

    public static String getRequestVerificationKey(String content) {
        String key = "";
        Pattern pattern = Pattern.compile("var securityKey = '(.*)';");
        Matcher matcher = pattern.matcher(content);
        if (matcher.find())
            key = matcher.group(1);
        return key;
    }

    public static JSONObject beanToJSON(Booking task) {
        if (task != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");
            JSONObject jsonTask = new JSONObject();
            jsonTask.put(Constants.DEPT_NAME, task.getDeptName());
            jsonTask.put(Constants.TRIAGE_NO, task.getTriageNo());
            jsonTask.put(Constants.DOCTOR, task.getDoctor());
            jsonTask.put(Constants.DOCTOR_ID, task.getDoctorId());
            jsonTask.put(Constants.SelectDate, sdf.format(task.getSelectDate()));
            jsonTask.put(Constants.SelectTime, task.getSelectTime());
            jsonTask.put(Constants.icardid, task.getIcardid());
            jsonTask.put(Constants.username, task.getUsername());
            jsonTask.put(Constants.phone, task.getPhone());
            jsonTask.put(Constants.TIME_DESC, task.getTimeDesc());
            jsonTask.put(Constants.ID, task.getId());
            jsonTask.put(Constants.autoTime, task.getAutoTime());
            jsonTask.put(Constants.CARD_NO, task.getCardNo());
            jsonTask.put(Constants.DOCTORSN, task.getDoctorSn());
            jsonTask.put(Constants.ORG_ID, task.getOrgId());
            jsonTask.put(Constants.PASSWORD, task.getPassword());
            jsonTask.put(Constants.NUMBER_ID, task.getNumberId());
            return jsonTask;
        }
        return null;
    }

    public static Map<String, String> toMap(JSONObject jsonObject) {
        Map<String, String> result = new HashMap<String, String>();
        result.put(Constants.AM_PM, amorpm(jsonObject.getString(Constants.TIME_DESC)));
        result.put(Constants.DEPT_CODE, jsonObject.getString(Constants.TRIAGE_NO));
        result.put(Constants.DOCTOR_CODE, jsonObject.getString(Constants.DOCTOR_ID));
        result.put(Constants.CARD_NO, jsonObject.getString(Constants.CARD_NO));
        result.put(Constants.ID_CARD, jsonObject.getString(Constants.icardid));
        if (jsonObject.getString(Constants.ORG_ID).equals("6")) {
            result.put(Constants.SCHEDULE_ID, getDateNoYear(jsonObject.getString(Constants.SelectDate)) + amorpm(jsonObject.getString(Constants.TIME_DESC)) + jsonObject.getString(Constants.TRIAGE_NO)
                    + "D" + jsonObject.getString(Constants.DOCTOR) + "/" + jsonObject.getString(Constants.DOCTOR_ID));
            result.put(Constants.NUMBER_ID, jsonObject.getString(Constants.NUMBER_ID));
        } else {
            result.put(Constants.SCHEDULE_ID, jsonObject.getString(Constants.ORG_ID) + jsonObject.getString(Constants.DOCTOR_ID) + getDate(jsonObject.getString(Constants.SelectDate))
                    + amorpm(jsonObject.getString(Constants.TIME_DESC)));
            result.put(Constants.NUMBER_ID, jsonObject.getString(Constants.ORG_ID) + jsonObject.getString(Constants.DOCTOR_ID) + getDate(jsonObject.getString(Constants.SelectDate))
                    + amorpm(jsonObject.getString(Constants.TIME_DESC)) + getTime(jsonObject.getString(Constants.SelectTime)));
        }
        result.put(Constants.NAME, encoder(jsonObject.getString(Constants.username)));
        result.put(Constants.ORG_ID, jsonObject.getString(Constants.ORG_ID));
        result.put(Constants.START_TIME, getStartTime(jsonObject.getString(Constants.SelectDate), jsonObject.getString(Constants.SelectTime)));
        result.put(Constants.TELEPHONE, jsonObject.getString(Constants.phone));
        if (jsonObject.get(Constants.PASSWORD) != null) {
            result.put(Constants.PASSWORD, jsonObject.getString(Constants.PASSWORD));
        }
        return result;
    }

    public static String amorpm(String timedesc) {
        if ("上午".equals(timedesc)) {
            return "AM";
        } else {
            return "PM";
        }

    }

    public static String getDate(String dateString) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat simpleDateFormatNoTime = new SimpleDateFormat("yyyyMMdd");
        String dateStr = "";
        try {
            Date date = simpleDateFormat.parse(dateString);
            dateStr = simpleDateFormatNoTime.format(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return dateStr;

    }

    public static String getDateNoYear(String dateString) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat simpleDateFormatNoTime = new SimpleDateFormat("yyMMdd");
        String dateStr = "";
        try {
            Date date = simpleDateFormat.parse(dateString);
            dateStr = simpleDateFormatNoTime.format(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return dateStr;

    }

    public static String getTime(String timeString) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        SimpleDateFormat simpleDateFormatNoDate = new SimpleDateFormat("HHmm");
        String timeStr = "";
        try {
            Date date = simpleDateFormat.parse("2015-01-01 " + timeString);
            timeStr = simpleDateFormatNoDate.format(date);
        } catch (ParseException e) {

            e.printStackTrace();
        }
        return timeStr;

    }

    public static String getStartTime(String dateString, String timeString) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat simpleDateFormatAll = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat simpleDateFormatG = new SimpleDateFormat("yyyy/M/d");
        SimpleDateFormat simpleDateFormatT = new SimpleDateFormat("H:mm:ss");
        String dateStr = "";
        String timeStr = "";
        try {
            Date date = simpleDateFormat.parse(dateString);
            dateStr = simpleDateFormatG.format(date);
            Date date2 = simpleDateFormatAll.parse("2015-01-01 " + timeString + ":00");
            timeStr = simpleDateFormatT.format(date2);
        } catch (ParseException e) {

            e.printStackTrace();
        }
        return dateStr + " " + timeStr;

    }

    // 中文转码成encode
    public static String encoder(String encodeStr) {
        try {
            return URLEncoder.encode(encodeStr, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return encodeStr;
        }
    }

    public static void main(String[] args) {
        System.out.println(getDate("2015-02-03"));
        System.out.println(getTime("08:09"));
        System.out.println(getStartTime("2015-02-03", "08:08"));
        System.out.println(encoder("张惠萍"));
        DecimalFormat df = new DecimalFormat("0.0000000000000000");

        System.out.println(df.format(Math.random()));

    }

    public static String readTxtFile() {
        String txtStr = "";
        try {
            String encoding = "GBK";
            File file = new File("D:\\code.txt");
            if (file.isFile() && file.exists()) { //判断文件是否存在
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file), encoding);//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = "";
                while ((lineTxt = bufferedReader.readLine()) != null) {
                    System.out.println(lineTxt);
                    txtStr = lineTxt;
                }

                read.close();
            } else {
                System.out.println("找不到指定的文件");
            }
        } catch (Exception e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        }
        return txtStr;

    }

}
