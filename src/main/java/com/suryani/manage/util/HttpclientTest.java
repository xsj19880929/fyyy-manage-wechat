package com.suryani.manage.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.suryani.manage.booking.domain.DateTime;

public class HttpclientTest {
    private static final Logger logger = LoggerFactory.getLogger(HttpclientTest.class);

    // get方式获取网页数据
    public static String getHtmlByRequest(String url) throws IOException, ClientProtocolException {
        HttpClient client = new DefaultHttpClient();
        client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, Constants.CONNECTION_TIMEOUT);
        client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, Constants.SO_TIMEOUT);
        HttpGet get = new HttpGet(url);
        HttpResponse response = client.execute(get);
        HttpEntity entity = response.getEntity();
        String responseString = new String(EntityUtils.toString(entity));
        // responseString = new String(responseString.getBytes("ISO-8859-1"),
        // "gbk");
        // System.out.println(responseString);
        jiexi2(responseString);
        return responseString;
    }

    public static String jiexi2(String html) {
        Document doc = Jsoup.parse(html);
        Elements eles = doc.select("div.dateInfoDetail div a");
        for (Element ele : eles) {
            Pattern pattern = Pattern.compile("(.*)-\\d{2}[:]\\d{2}");
            Matcher matcher = pattern.matcher(ele.html());
            if (matcher.find())
                System.out.println(matcher.group(1));
        }
        return null;
    }

    public static String jiexi(String html) {
        Document doc = Jsoup.parse(html);
        Element ele = doc.select("div.whliesubscribe ul li").last();
        Element am = ele.select("div").first().select("a").first();
        Element pm = ele.select("div").last().select("a").first();
        if (am != null) {
            String a1h = am.attr("href");
            System.out.println(a1h);
        }
        if (pm != null) {
            String a2h = pm.attr("href");
            System.out.println(a2h);
        }
        // Pattern pattern = Pattern.compile("var securityKey = '(.*)';");
        // Matcher matcher = pattern.matcher(ele.html());
        // if (matcher.find())
        // System.out.println(matcher.group());

        return null;
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
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("result", responseString);
        map.put("cookies", ((AbstractHttpClient) client).getCookieStore());
        return map;
    }

    public static String getHtmlByRequestinfo(String url) throws IOException, ClientProtocolException {
        Map<String, Object> map = new HashMap<String, Object>();
        for (int i = 0; i < 100; i++) {
            map = Utils.getHtmlByRequestLogin("http://www.xmsmjk.com/Home/LoginXman?ssid=D86671124&pwd=350521198809291558&vcode=" + i);
            if (!map.get("result").toString().contains("验证码输入错误")) {
                System.out.println("login success");
                break;
            }
        }
        HttpClient client = new DefaultHttpClient();
        client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, Constants.CONNECTION_TIMEOUT);
        client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, Constants.SO_TIMEOUT);
        ((AbstractHttpClient) client).setCookieStore((CookieStore) map.get("cookies"));
        HttpGet get = new HttpGet(url);
        HttpResponse response = client.execute(get);
        HttpEntity entity = response.getEntity();
        String responseString = new String(EntityUtils.toString(entity));
        // responseString = new String(responseString.getBytes("ISO-8859-1"),
        // "gbk");
        Document doc = Jsoup.parse(responseString);
        Elements eles = doc.select("script");
        String requestVerificationToken = "";
        String requestVerificationkey = "";

        int i = 0;
        for (Element ele : eles) {
            if (i == 5) {
                // System.out.println(ele.html());
                requestVerificationToken = getRequestVerificationToken(ele.html());
                requestVerificationkey = getRequestVerificationKey(ele.html());
                System.out.println(requestVerificationToken);
                System.out.println(requestVerificationkey);
            }
            i++;
        }
        // System.out.println(responseString);

        Map<String, String> params = new HashMap<String, String>();
        params.put("AM_PM", "AM");
        params.put("CARD_NO", "D88512805");
        params.put("DEPT_CODE", "3201");
        params.put("DOCTOR_CODE", "183");
        params.put("ID_CARD", "350521198710181105");
        params.put("NAME", "%E5%BC%A0%E6%83%A0%E8%90%8D");
        params.put("NUMBER_ID", "718320150502AM0828");
        params.put("ORG_ID", "7");
        params.put("SCHEDULE_ID", "718320150502AM");
        params.put("START_TIME", "2015/5/2 8:28:00");
        params.put("TELEPHONE", "15060787835");
        params.put("__RequestVerificationKey", requestVerificationkey);
        params.put("__RequestVerificationToken", requestVerificationToken);
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        for (Map.Entry<String, String> entry : params.entrySet())
            formparams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        HttpPost post = new HttpPost("http://www.xmsmjk.com/UrpOnline/Home/DoReservation");
        HttpEntity postEntity = new UrlEncodedFormEntity(formparams, "utf-8");
        // post.setHeader(new BasicHeader(HttpHeaders.REFERER,
        // "http://www.xmsmjk.com/UrpOnline/Home/Reservation/B9040291C00353133DD349B91B3E2F21DA86FEAA2854EF96D29242A2E1B214F0BDA9DB721AF1F486"));
        // post.setHeader(new BasicHeader(HttpHeaders.ACCEPT,
        // "application/json, text/javascript, */*; q=0.01"));
        // post.setHeader(new BasicHeader(HttpHeaders.CONTENT_TYPE,
        // "application/x-www-form-urlencoded; charset=UTF-8"));
        // post.setHeader(new BasicHeader(HttpHeaders.USER_AGENT,
        // "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:37.0) Gecko/20100101 Firefox/37.0"));
        // post.setHeader(new BasicHeader(HttpHeaders.HOST, "www.xmsmjk.com"));
        // post.setHeader(new BasicHeader(HttpHeaders.ACCEPT_ENCODING,
        // "gzip, deflate"));
        // post.setHeader(new BasicHeader(HttpHeaders.ACCEPT_LANGUAGE,
        // "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3"));
        // post.setHeader(new BasicHeader(HttpHeaders.CONNECTION,
        // "keep-alive"));
        // post.setHeader(new BasicHeader(HttpHeaders.PRAGMA, "no-cache"));
        // post.setHeader("X-Requested-With", "XMLHttpRequest");
        // postEntity.setContentType("application/json");
        // postEntity.setContentEncoding("UTF-8");

        post.setEntity(postEntity);
        HttpResponse responsePost = client.execute(post);
        HttpEntity entityPost = responsePost.getEntity();
        String responseStringPost = new String(EntityUtils.toString(entityPost));
        System.out.println(responseStringPost);

        HttpGet get2 = new HttpGet("http://www.xmsmjk.com/UrpOnline/Home/ConfirmFail/FC6ABB55ACEA51F39218772C7D248F30_");
        HttpResponse response2 = client.execute(get2);
        HttpEntity entity2 = response2.getEntity();
        String responseString2 = new String(EntityUtils.toString(entity2));
        // System.out.println(responseString2);
        Document doc2 = Jsoup.parse(responseString2);
        Element ele2 = doc2.select("div.fail_info div").first();
        System.out.println(ele2.html());
        return responseStringPost;
    }

    public static String getRequestVerificationToken(String content) {
        String token = "";
        // Pattern pattern = Pattern.compile("var securityKey = '(.*)';");
        Pattern pattern = Pattern.compile("name=\"__RequestVerificationToken\" type=\"hidden\" value=\"(.*)\" />");
        Matcher matcher = pattern.matcher(content);
        if (matcher.find())
            token = matcher.group(1);
        // System.out.println(matcher.group(1));
        return token;
    }

    public static String getRequestVerificationKey(String content) {
        String key = "";
        Pattern pattern = Pattern.compile("var securityKey = '(.*)';");
        Matcher matcher = pattern.matcher(content);
        if (matcher.find())
            key = matcher.group(1);
        // System.out.println(matcher.group(1));
        return key;
    }

    /**
     * @param args
     */

    // 下载预约时间列表
    public static List<DateTime> timeListNew(JSONObject task) {
        logger.info("重新获取预约时间");
        String html = null;
        boolean wait = true;
        List<DateTime> times = new ArrayList<DateTime>();
        while (wait) {
            try {
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
                    String dateTimeHtml = "";
                    try {
                        dateTimeHtml = HttpClentUtils.getHtmlByRequest(Constants.TONGYIURL + href);
                    } catch (Exception e) {
                        logger.error(e.getMessage());
                        e.printStackTrace();
                    }
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

    public static void main(String[] args) {
        // try {
        // // getHtmlByRequest("http://www.xmsmjk.com");
        // String ele =
        // "var securityToken = $('<input name=\"__RequestVerificationToken\" type=\"hidden\" value=\"1vB1Rl_Thsm17MGUBoflSBQK2LQsN8F4tl3GmcMZ158e6mORwdoVaVpDLdcJ5sPKnPw-EbBPOsw5tAjIDNhCF9yfJfM1IRrOE8hqUH02qykkv3Ew6KmC0KfKqNH4XxGMukLKULCvdJ6u9w9BAajHDYxVs0kCLeu7kZ0iJNZ2Eqc1\" />').attr('value');";
        // // Pattern pattern = Pattern.compile("var securityKey = '(.*)';");
        // Pattern pattern =
        // Pattern.compile("name=\"__RequestVerificationToken\" type=\"hidden\" value=\"(.*)\" />");
        // Matcher matcher = pattern.matcher(ele);
        // if (matcher.find())
        // System.out.println(matcher.group(1));
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
        try {
            //
            getHtmlByRequestinfo("http://www.xmsmjk.com/UrpOnline/Home/Reservation/B9040291C00353133DD349B91B3E2F21DA86FEAA2854EF96D29242A2E1B214F0BDA9DB721AF1F486");
            // getHtmlByRequest("http://www.xmsmjk.com/UrpOnline/Home/Doctor/A006E1236332E045EC2331E12CF9F6686F558D0C582F3FA4");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // while (true) {
        // JSONObject task = new JSONObject();
        // task.put("doctorSn", "EC6968EF5CA69D720ACB98BDAF512158");
        // task.put(Constants.TIME_DESC, "上午");
        // timeListNew(task);
        // }

    }

}
