package com.suryani.manage.util;

import com.suryani.manage.booking.domain.Booking;
import com.suryani.manage.booking.domain.DateTime;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Utils {
    private static Logger logger = Logger.getLogger(Utils.class.getName());

    public static boolean isAjaxRequest(HttpServletRequest request) {
        return "XMLHttpRequest".equalsIgnoreCase(request.getHeader("X-Requested-With"));
    }

    public static String getContextPath(HttpServletRequest request) {
        String path = request.getContextPath();
        int port = request.getServerPort();
        String basePath = request.getScheme() + "://" + request.getServerName() + (port == 80 ? "" : ":" + port) + path + "/";
        return basePath;
    }

    public static String getRequestUrl(HttpServletRequest request) {
        String requestUrl = request.getRequestURI();
        if (!requestUrl.startsWith("/"))
            requestUrl = "/" + requestUrl;
        requestUrl = StringUtils.replace(requestUrl, request.getContextPath(), "");
        return requestUrl;
    }

    public static String getRequestUrlWithParam(HttpServletRequest request) {
        String queryString = (request.getQueryString() == null) ? "" : "?" + request.getQueryString();
        return getRequestUrl(request) + queryString;
    }

    public static Map<String, Object> getBeanMap(Object bean) {
        Map<String, Object> map = new HashMap<String, Object>();
        Class<?> cls = bean.getClass();
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields) {
            String name = field.getName();
            String strGet = "get" + name.substring(0, 1).toUpperCase() + name.substring(1, name.length());
            try {
                Method methodGet = cls.getDeclaredMethod(strGet);
                Object val = methodGet.invoke(bean);
                map.put(name, val);
            } catch (Exception e) {

            }
        }
        return map;
    }

    public static Map<String, Object> pagerWarp(List<?> datas, int total) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("data", datas);
        map.put("total", total);
        return map;
    }

    public static Map<String, Object> pagerWarp2(Object datas, int total) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("data", datas);
        map.put("total", total);
        return map;
    }

    public static JSONObject pagerWarp4json(JSONArray datas, int total) {
        JSONObject map = new JSONObject();
        map.put("data", datas);
        map.put("total", total);
        return map;
    }

    public static Map<String, Object> getRequestParamMap(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<String, Object>();
        Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            String[] paramValues = request.getParameterValues(paramName);
            if (paramValues.length == 1) {
                String paramValue = paramValues[0];
                if (paramValue.length() != 0) {
                    map.put(paramName, paramValue);
                }
            }
        }
        return map;
    }

    public static MultiValueMap<String, Object> getRequestParamMap2(HttpServletRequest request) {
        MultiValueMap<String, Object> form = new LinkedMultiValueMap<String, Object>();
        Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            String[] paramValues = request.getParameterValues(paramName);
            for (int i = 0; i < paramValues.length; i++) {
                form.add(paramName, paramValues[i]);
            }
        }
        return form;
    }

    public final static String getWebrootPath() {
        String root = Utils.class.getResource("/").getFile();

        try {

            root = new File(root).getParentFile().getParentFile()

                    .getCanonicalPath();

            root += File.separator;

        } catch (IOException e) {

            throw new RuntimeException(e);

        }

        return root;

    }

    public static String getUploadTempDir() {
        String dir = getWebrootPath() + "tmp";
        File f = new File(dir);
        if (!f.exists()) {
            f.mkdir();
        }
        return dir;
    }

    // 中文转码成encode
    public static String encoder(String encodeStr) {
        try {
            return URLEncoder.encode(encodeStr, "GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return encodeStr;
        }
    }

    // decode转码成中文
    public static String decoder(String decodeStr) {
        try {
            return URLDecoder.decode(decodeStr, "GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return decodeStr;
        }
    }

    public static String SpliceUrl(JSONObject task) {
        StringBuffer url = new StringBuffer(Constants.GETURL);
        url.append("?" + Constants.TRIAGE_NO + "=").append(task.get(Constants.TRIAGE_NO));
        url.append("&" + Constants.DOCTOR_ID + "=").append(task.get(Constants.DOCTOR_ID));
        url.append("&" + Constants.SelectDate + "=").append(task.get(Constants.SelectDate));
        url.append("&" + Constants.DOCTOR + "=").append(Utils.encoder(task.getString(Constants.DOCTOR)));
        url.append("&" + Constants.TIME_DESC + "=").append(Utils.encoder(task.getString(Constants.TIME_DESC)));
        url.append("&" + Constants.DEPT_NAME + "=").append(Utils.encoder(task.getString(Constants.DEPT_NAME)));
        logger.info("url:" + url.toString());
        return url.toString();
    }

    public static String SpliceUrlNew(Booking booking) {
        StringBuffer url = new StringBuffer("booking/date");
        url.append("?orgCode=").append(booking.getOrgCode());
        url.append("&deptCode=").append(booking.getTriageNo());
        url.append("&docCode=").append(booking.getDoctorId());
        url.append("&startDate=").append(CalendarUtils.format(CalendarUtils.getDaysDate(booking.getSelectDate(), -7), "yyyy-MM-dd"));
        url.append("&endDate=").append(CalendarUtils.format(booking.getSelectDate(), "yyyy-MM-dd"));
        logger.info("url:" + url.toString());
        return url.toString();
    }

    public static String SpliceUrlNewBg(Booking booking) {
        StringBuffer url = new StringBuffer("http://wechat.xmsmjk.com/zycapwxsehr/ReservationLogController/detailtData.do");
        url.append("?orgid=").append(booking.getOrgCode());
        url.append("&deptid=").append(booking.getTriageNo());
        url.append("&docid=").append(booking.getDoctorId());
        url.append("&strStart=").append(CalendarUtils.format(CalendarUtils.getDaysDate(booking.getSelectDate(), -7), "yyyy-MM-dd"));
        url.append("&strEnd=").append(CalendarUtils.format(booking.getSelectDate(), "yyyy-MM-dd"));
        logger.info("url:" + url.toString());
        return url.toString();
    }

    // get方式获取网页数据
    public static String getHtmlByRequest(String url) throws IOException, ClientProtocolException {
        HttpClient client = new DefaultHttpClient();
        client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, Constants.CONNECTION_TIMEOUT);
        client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, Constants.SO_TIMEOUT);
        HttpGet get = new HttpGet(url);
        HttpResponse response = client.execute(get);
        HttpEntity entity = response.getEntity();
        String responseString = new String(EntityUtils.toString(entity));
        responseString = new String(responseString.getBytes("ISO-8859-1"), "gbk");
        // System.out.println(responseString);
        return responseString;
    }

    // get方式获取网页数据
    public static Map<String, Object> getHtmlByRequestLogin(String url) throws IOException, ClientProtocolException {
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
        // // responseString = new String(responseString.getBytes("ISO-8859-1"),
        // // "gbk");
        // // System.out.println(responseString);
        // // 读取cookie并保存文件
        // List<Cookie> cookies = ((AbstractHttpClient)
        // client).getCookieStore().getCookies();
        // if (cookies.isEmpty()) {
        // System.out.println("None");
        // } else {
        // for (int i = 0; i < cookies.size(); i++) {
        // System.out.println("- " + cookies.get(i).toString());
        //
        // }
        // }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("result", responseString);
        map.put("cookies", ((AbstractHttpClient) client).getCookieStore());
        return map;
    }

    // get方式获取网页数据
    public static String getHtmlByRequestinfo(String url, CookieStore cookieStore) throws IOException, ClientProtocolException {
        HttpClient client = new DefaultHttpClient();
        client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, Constants.CONNECTION_TIMEOUT);
        client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, Constants.SO_TIMEOUT);
        ((AbstractHttpClient) client).setCookieStore(cookieStore);
        HttpGet get = new HttpGet(url);
        HttpResponse response = client.execute(get);
        HttpEntity entity = response.getEntity();
        String responseString = new String(EntityUtils.toString(entity));
        // responseString = new String(responseString.getBytes("ISO-8859-1"),
        // "gbk");
        System.out.println(responseString);
        return responseString;
    }

    // POST方式获取网页数据
    public static String postHtmlByRequestDo(String url, CookieStore cookieStore, Map<String, String> params) throws IOException, ClientProtocolException {
        HttpClient client = new DefaultHttpClient();
        client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, Constants.CONNECTION_TIMEOUT);
        client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, Constants.SO_TIMEOUT);
        ((AbstractHttpClient) client).setCookieStore(cookieStore);
        HttpPost post = new HttpPost(url);
        // List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        // for (Map.Entry<String, String> entry : params.entrySet())
        // formparams.add(new BasicNameValuePair(entry.getKey(),
        // entry.getValue()));
        StringEntity postEntity = new StringEntity(JSONObject.fromObject(params).toString(), "utf-8");
        post.setHeader(new BasicHeader(HttpHeaders.REFERER, "http://www.xmsmjk.com/UrpOnline/Home/Reservation/B9040291C00353133DD349B91B3E2F21DA86FEAA2854EF96D29242A2E1B214F0BDA9DB721AF1F486"));
        post.setHeader(new BasicHeader(HttpHeaders.ACCEPT, "application/json, text/javascript, */*; q=0.01"));
        post.setHeader(new BasicHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded; charset=UTF-8"));
        post.setHeader(new BasicHeader(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:37.0) Gecko/20100101 Firefox/37.0"));
        post.setHeader(new BasicHeader(HttpHeaders.HOST, "www.xmsmjk.com"));
        post.setHeader(new BasicHeader(HttpHeaders.ACCEPT_ENCODING, "gzip, deflate"));
        post.setHeader(new BasicHeader(HttpHeaders.ACCEPT_LANGUAGE, "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3"));
        post.setHeader(new BasicHeader(HttpHeaders.CONNECTION, "keep-alive"));
        post.setHeader(new BasicHeader(HttpHeaders.PRAGMA, "no-cache"));
        // postEntity.setContentType("application/json");
        // postEntity.setContentEncoding("UTF-8");
        post.setEntity(postEntity);
        HttpResponse response = client.execute(post);
        HttpEntity entity = response.getEntity();
        String responseString = new String(EntityUtils.toString(entity));
        // // responseString = new String(responseString.getBytes("ISO-8859-1"),
        // // "gbk");
        // // System.out.println(responseString);
        // // 读取cookie并保存文件
        // List<Cookie> cookies = ((AbstractHttpClient)
        // client).getCookieStore().getCookies();
        // if (cookies.isEmpty()) {
        // System.out.println("None");
        // } else {
        // for (int i = 0; i < cookies.size(); i++) {
        // System.out.println("- " + cookies.get(i).toString());
        //
        // }
        // }
        return responseString;
    }

    // post方式获取网页数据
    public static String getHtmlByPost(String url, Map<String, String> params) throws IOException, ClientProtocolException {
        HttpClient client = new DefaultHttpClient(new ThreadSafeClientConnManager());
        client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, Constants.CONNECTION_TIMEOUT);
        client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, Constants.POST_SO_TIMEOUT);
        HttpGet get = new HttpGet("http://yy.xmfybj.cn/imgchk/validatecode.asp");
        HttpResponse responseGet = client.execute(get);
        HttpEntity entityGet = responseGet.getEntity();
        InputStream inStream = entityGet.getContent();
        BufferedImage iag = ImageIO.read(inStream);
        ImgIdent img = new ImgIdent(iag);
        String validatecode = img.getValidatecode(4);
        get.abort();
        IOUtils.closeQuietly(inStream);
        params.put("validatecode", validatecode);
        HttpPost post = new HttpPost(url);
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        for (Map.Entry<String, String> entry : params.entrySet())
            formparams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        HttpEntity postEntity = new UrlEncodedFormEntity(formparams, "gbk");
        post.setEntity(postEntity);
        HttpResponse response = client.execute(post);
        HttpEntity entity = response.getEntity();
        String responseString = new String(EntityUtils.toString(entity));
        responseString = new String(responseString.getBytes("ISO-8859-1"), "gbk");
        // System.out.println(responseString);
        return responseString;

    }

    // json转成map
    @SuppressWarnings("unchecked")
    public static Map<String, String> toMap(JSONObject jsonObject) {
        Map<String, String> result = new HashMap<String, String>();
        Iterator<String> iterator = jsonObject.keys();
        String key = null;
        String value = null;
        while (iterator.hasNext()) {
            key = iterator.next();
            value = jsonObject.getString(key);
            if (!Constants.ID.equals(key) && !Constants.autoTime.equals(key)) {
                result.put(key, value);
            }
        }
        return result;
    }

    // map转成jsonString
    public static String toJsonString(Map<String, String> params) {
        JSONObject json = JSONObject.fromObject(params);
        if (json != null && !json.isEmpty())
            return json.toString();
        return null;
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
            return jsonTask;
        }
        return null;
    }

    // 写时间任务的数据
    public static void writer(String html, String fileName) {
        try {
            File file = new File(fileName);
            if (!file.isFile() && !file.exists()) {
                file.createNewFile();
            } else {
                file.delete();
                file.createNewFile();
            }
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file.getAbsolutePath(), true), "GBK"));
            bufferedWriter.write(html);
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<DateTime> sortList(List<DateTime> list, int wantTime) {
        Map<Integer, Integer> mapSort = new HashMap<Integer, Integer>();
        List<DateTime> listnew = new ArrayList<DateTime>();
        for (DateTime dt : list) {
            String date = dt.getSelectTime();
            int dateint = Integer.parseInt(date.replace(":", ""));
            dt.setAbs(Math.abs(wantTime - dateint));
        }
        for (DateTime dt : list) {
            int abs = dt.getAbs();
            int sort = 0;
            for (DateTime dt2 : list) {
                int abs2 = dt2.getAbs();
                if (abs > abs2) {
                    sort++;
                }
            }
            if (mapSort.containsKey(sort)) {
                sort++;
            }
            dt.setSort(sort);
            mapSort.put(sort, sort);
            listnew.add(dt);
        }
        for (DateTime dt : list) {
            listnew.set(dt.getSort(), dt);
        }
        // for (DateTime dt : listnew) {
        // System.out.println(JSONObject.fromObject(dt));
        // }
        return listnew;
    }

    public static String ajax() {
        org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject params = new JSONObject();
        params.put("AM_PM", "AM");
        params.put("CARD_NO", "D86671124");
        params.put("DEPT_CODE", "2041");
        params.put("DOCTOR_CODE", "481");
        params.put("ID_CARD", "350521198809291558");
        params.put("NAME", "%E8%AE%B8%E5%B0%91%E5%86%9B");
        params.put("NUMBER_ID", "748120150428AM0827");
        params.put("ORG_ID", "7");
        params.put("SCHEDULE_ID", "748120150428AM");
        params.put("START_TIME", "2015/4/28 8:27:00");
        params.put("TELEPHONE", "15710660257");
        params.put("__RequestVerificationKey", "9b18f642-00f7-4354-bd1b-258ee964db26");
        params.put("__RequestVerificationToken",
                "1vB1Rl_Thsm17MGUBoflSBQK2LQsN8F4tl3GmcMZ158e6mORwdoVaVpDLdcJ5sPKnPw-EbBPOsw5tAjIDNhCF9yfJfM1IRrOE8hqUH02qykkv3Ew6KmC0KfKqNH4XxGMukLKULCvdJ6u9w9BAajHDYxVs0kCLeu7kZ0iJNZ2Eqc1");
        org.springframework.http.HttpEntity request = new org.springframework.http.HttpEntity(params, headers);
        String obj = new RestTemplate().postForObject("http://www.xmsmjk.com/UrpOnline/Home/DoReservation", request, String.class);
        System.out.println(obj);
        return obj;

    }

    public static void main(String[] args) {

        // try {
        // Utils.getHtmlByRequest("http://www.xmsmjk.com/ValidateCode.aspx?s=+Math.random();");
        // } catch (ClientProtocolException e) {
        // e.printStackTrace();
        // } catch (IOException e) {
        // e.printStackTrace();
        // }
        // Map<String, Object> map = new HashMap<String, Object>();
        // try {
        // for (int i = 0; i < 100; i++) {
        // map =
        // Utils.getHtmlByRequestLogin("http://www.xmsmjk.com/MyHealth/LoginForReservation.aspx?ssid=D86671124&pwd=350521198809291558&vcode="
        // + i);
        // if (!map.get("result").toString().contains("验证码输入错误")) {
        // System.out.println("login success");
        // break;
        // }
        // }
        // } catch (Exception e) {
        //
        // e.printStackTrace();
        // }
        // try {
        // getHtmlByRequestinfo("http://www.xmsmjk.com/MyHealth/XmanInfo.aspx",
        // (CookieStore) map.get("cookies"));
        // } catch (Exception e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
        // Map<String, String> params = new HashMap<String, String>();
        // params.put("AM_PM", "AM");
        // params.put("CARD_NO", "D86671124");
        // params.put("DEPT_CODE", "2041");
        // params.put("DOCTOR_CODE", "481");
        // params.put("ID_CARD", "350521198809291558");
        // params.put("NAME", "%E8%AE%B8%E5%B0%91%E5%86%9B");
        // params.put("NUMBER_ID", "748120150428AM0827");
        // params.put("ORG_ID", "7");
        // params.put("SCHEDULE_ID", "748120150428AM");
        // params.put("START_TIME", "2015/4/28 8:27:00");
        // params.put("TELEPHONE", "15710660257");
        // params.put("__RequestVerificationKey",
        // "9b18f642-00f7-4354-bd1b-258ee964db26");
        // params.put("__RequestVerificationToken",
        // "1vB1Rl_Thsm17MGUBoflSBQK2LQsN8F4tl3GmcMZ158e6mORwdoVaVpDLdcJ5sPKnPw-EbBPOsw5tAjIDNhCF9yfJfM1IRrOE8hqUH02qykkv3Ew6KmC0KfKqNH4XxGMukLKULCvdJ6u9w9BAajHDYxVs0kCLeu7kZ0iJNZ2Eqc1");
        // try {
        // String respose =
        // postHtmlByRequestDo("http://www.xmsmjk.com/UrpOnline/Home/DoReservation",
        // (CookieStore) map.get("cookies"), params);
        // System.out.println(respose);
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
        // ajax();

    }
}
