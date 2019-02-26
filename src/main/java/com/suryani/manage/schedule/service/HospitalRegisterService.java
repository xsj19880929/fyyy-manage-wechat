package com.suryani.manage.schedule.service;


import com.quidsi.core.json.JSONBinder;
import com.suryani.manage.common.CHttpClient;
import com.suryani.manage.common.ImageCode;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * s
 *
 * @author soldier
 */
@Service
public class HospitalRegisterService {
    private final Logger logger = LoggerFactory.getLogger(HospitalRegisterService.class);
    @Inject
    CHttpClient cHttpClient;

    public static void main(String[] args) {
        String out = "天吧算紧";
        try {
            String s = new String(out.getBytes(), "UTF-8");
            System.out.println(s);
            System.out.println(new String(s.getBytes(), "GBK"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public CookieStore setCookieStore(HttpResponse httpResponse, String host) {
        CookieStore cookieStore = new BasicCookieStore();
        // JSESSIONID
        if (null == httpResponse.getFirstHeader("Set-Cookie")) {
            // 新建一个Cookie
            BasicClientCookie cookie = new BasicClientCookie("JSESSIONID", System.currentTimeMillis() + "");
            cookie.setVersion(0);
            cookie.setDomain(host);
            cookie.setPath("/");
            cookieStore.addCookie(cookie);
        } else {
            String setCookie = httpResponse.getFirstHeader("Set-Cookie").getValue();
            String JSESSIONID = setCookie.substring("JSESSIONID=".length(), setCookie.indexOf(";"));
            // 新建一个Cookie
            BasicClientCookie cookie = new BasicClientCookie("JSESSIONID", JSESSIONID);
            cookie.setVersion(0);
            cookie.setDomain(host);
            cookie.setPath("/");
            cookieStore.addCookie(cookie);
        }
        return cookieStore;
    }

    public void index(RegisterBean registerBean, HttpClientContext clientContext) throws Exception {
        HttpUriRequest request = RequestBuilder.get()
                .setUri("http://wechat.xmsmjk.com/zycapwxsehr/view/appointment/index.jsp?state=124&openid=" + registerBean.getOpenID())
                .setHeader("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 5_1 like Mac OS X) AppleWebKit/534.46 (KHTML, like Gecko) Mobile/9B176 MicroMessenger/4.3.2")
                .build();
        cHttpClient.execute(request, clientContext);
        request.abort();
    }

    public Map<String, Object> login(RegisterBean registerBean, HttpClientContext clientContext) throws Exception {
//        logger.info("登陆网站");
        HttpUriRequest request = RequestBuilder.post()
                .setUri("http://wechat.xmsmjk.com/wechatsehr/v1/bindingController/passwordValidation")
                .setHeader("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 5_1 like Mac OS X) AppleWebKit/534.46 (KHTML, like Gecko) Mobile/9B176 MicroMessenger/4.3.2")
                .setHeader("Oid", registerBean.getOpenID())
                .addParameter("ssid", registerBean.getIdNo())
                .addParameter("password", registerBean.getPassword())
                .addParameter("openid", registerBean.getOpenID())
                .addParameter("wechatType", "1")
                .build();
        HttpResponse httpResponse = cHttpClient.execute(request, clientContext);
        String responseStr = EntityUtils.toString(httpResponse.getEntity());
        logger.info(responseStr);
        request.abort();
        return JSONBinder.binder(Map.class).fromJSON(responseStr);
    }

//    public String resLockNumber(RegisterBean registerBean, HttpClientContext clientContext) throws Exception {
////        logger.info("锁号中");
//        Map params = new HashMap();
//        params.put("orgId", 2);
//        params.put("ssid", "600865500445500594400905502");
//        params.put("idno", "500135400835500094400975500665400875500075400935500365");
//        params.put("numberId", "503");
//        params.put("scheduleId", "400965400905400905800077400984500284500494500186400855500005500148500035");
//        params.put("startTime", "2018-12-12 14:54:00");
//        HttpUriRequest request = RequestBuilder.post()
//                .setUri("http://wechat.xmsmjk.com/wechatsehr/v1/webRegisterController/resLockNumber")
//                .setHeader("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 5_1 like Mac OS X) AppleWebKit/534.46 (KHTML, like Gecko) Mobile/9B176 MicroMessenger/4.3.2")
////                .setHeader("Oid", registerBean.getOpenID())
//                .addParameter("params", JSONBinder.binder(Map.class).toJSON((params)))
//                .addParameter("iswchiss", "false").build();
//        HttpResponse httpResponse = cHttpClient.execute(request, clientContext);
//        String responseStr = EntityUtils.toString(httpResponse.getEntity());
//        logger.info(responseStr);
//        Map<String, Object> resultMap = JSONBinder.binder(Map.class).fromJSON(responseStr);
//        Map<String, Object> dataMap = JSONBinder.binder(Map.class).fromJSON(resultMap.get("result").toString());
//        Map<String, Object> msgMap = JSONBinder.binder(Map.class).fromJSON(JSONBinder.binder(Map.class).toJSON((Map) dataMap.get("RESULT")));
//        request.abort();
//        return msgMap.get("@MESSAGE").toString();
//    }

    public Map<String, Object> getSsid(RegisterBean registerBean, HttpClientContext clientContext) throws Exception {
//        logger.info("登陆网站");
        HttpUriRequest request = RequestBuilder.post()
                .setUri("http://wechat.xmsmjk.com/wechatsehr/v1/userController/getSsid")
                .setHeader("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 5_1 like Mac OS X) AppleWebKit/534.46 (KHTML, like Gecko) Mobile/9B176 MicroMessenger/4.3.2")
                .addParameter("openid", registerBean.getOpenID())
                .build();
        HttpResponse httpResponse = cHttpClient.execute(request, clientContext);
//        Registry<CookieSpecProvider> registry = RegistryBuilder.<CookieSpecProvider>create().register(CookieSpecs.BEST_MATCH, new BestMatchSpecFactory())
//                .register(CookieSpecs.BROWSER_COMPATIBILITY, new BrowserCompatSpecFactory()).build();
//        clientContext.setCookieSpecRegistry(registry);
//        clientContext.setCookieStore(setCookieStore(httpResponse, "wechat.xmsmjk.com"));
        String responseStr = EntityUtils.toString(httpResponse.getEntity());
        logger.info(responseStr);
        request.abort();
        Map<String, Object> resultMap = JSONBinder.binder(Map.class).fromJSON(responseStr);
        Map<String, Object> dataMap = (Map) resultMap.get("result");
        return dataMap;
    }

    public String resLockNumber(RegisterBean registerBean, HttpClientContext clientContext) throws Exception {
//        logger.info("锁号中");
        Map params = new HashMap();
        params.put("orgId", registerBean.getOrgId());
        params.put("ssid", registerBean.getSsid());
        params.put("idno", registerBean.getPatientID());
        params.put("numberId", registerBean.getNumberId());
        params.put("scheduleId", registerBean.getScheduleId());
        params.put("startTime", registerBean.getStartTime());
        HttpUriRequest request = RequestBuilder.post()
                .setUri("http://wechat.xmsmjk.com/wechatsehr/v1/webRegisterController/resLockNumber")
                .setHeader("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 5_1 like Mac OS X) AppleWebKit/534.46 (KHTML, like Gecko) Mobile/9B176 MicroMessenger/4.3.2")
                .setHeader("openid", registerBean.getOpenID())
                .setHeader("Host", "wechat.xmsmjk.com")
                .setHeader("Referer", "http://wechat.xmsmjk.com/wechatsehr/wx/")
                .addParameter("params", JSONBinder.binder(Map.class).toJSON((params)))
                .addParameter("iswchiss", "false").build();
        HttpResponse httpResponse = cHttpClient.execute(request, clientContext);
        String responseStr = EntityUtils.toString(httpResponse.getEntity());
        logger.info(responseStr);
        Map<String, Object> resultMap = JSONBinder.binder(Map.class).fromJSON(responseStr);
        Map<String, Object> dataMap = JSONBinder.binder(Map.class).fromJSON(resultMap.get("result").toString());
        Map<String, Object> msgMap = JSONBinder.binder(Map.class).fromJSON(JSONBinder.binder(Map.class).toJSON((Map) dataMap.get("result")));
        request.abort();
        return msgMap.get("@message").toString();
    }

    public String code(RegisterBean registerBean, HttpClientContext clientContext) throws Exception {
//        logger.info("获取验证码");
        String responseStr = "";
        while (true) {
            try {
                HttpUriRequest request = RequestBuilder.post()
                        .setUri("http://wechat.xmsmjk.com/zycapwxsehr/HospitalNoteController/Code.do")
                        .setHeader("Host", "wechat.xmsmjk.com")
                        .setHeader("Referer", "http://wechat.xmsmjk.com/zycapwxsehr/view/appointment/confirm.jsp")
                        .setHeader("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 5_1 like Mac OS X) AppleWebKit/534.46 (KHTML, like Gecko) Mobile/9B176 MicroMessenger/4.3.2")
                        .setHeader("openid", registerBean.getOpenID())
                        .addParameter("openid", registerBean.getOpenID())
                        .build();
                HttpResponse httpResponse = cHttpClient.execute(request, clientContext);
                responseStr = EntityUtils.toString(httpResponse.getEntity());
                request.abort();
                break;
            } catch (HttpHostConnectException e) {
                logger.error("code获取验证码请求被拒绝");
            } catch (SocketTimeoutException e) {
                logger.error("code获取验证码请求超时");
            } catch (Exception e) {
                throw new RuntimeException();
            }

        }
        return responseStr;
    }

    public String getSelectCode(RegisterBean registerBean, HttpClientContext clientContext) throws Exception {
//        logger.info("获取验证码文字");
        Map<String, Object> resultMap;
        while (true) {
            try {
                HttpUriRequest request = RequestBuilder.post()
                        .setUri("http://wechat.xmsmjk.com/wechatsehr/v1/webRegisterController/getSelectCode")
                        .setHeader("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 5_1 like Mac OS X) AppleWebKit/534.46 (KHTML, like Gecko) Mobile/9B176 MicroMessenger/4.3.2")
                        .setHeader("openid", registerBean.getOpenID())
                        .setHeader("Host", "wechat.xmsmjk.com")
                        .setHeader("Referer", "http://wechat.xmsmjk.com/wechatsehr/wx/")
                        .addParameter("openid", registerBean.getOpenID())
                        .build();
                HttpResponse httpResponse = cHttpClient.execute(request, clientContext);
                String responseStr = EntityUtils.toString(httpResponse.getEntity());
                resultMap = JSONBinder.binder(Map.class).fromJSON(responseStr);
                request.abort();
                break;
            } catch (HttpHostConnectException e) {
                logger.error("获取验证码文字请求被拒绝");
            } catch (SocketTimeoutException e) {
                logger.error("获取验证码文字请求超时");
            } catch (Exception e) {
                throw new RuntimeException();
            }

        }
        return resultMap.get("result").toString();
    }

    public InputStream drawImage(RegisterBean registerBean, HttpClientContext clientContext) throws Exception {
//        logger.info("下载验证码图片");
        InputStream inputStream;
        while (true) {
            try {
                HttpUriRequest request = RequestBuilder.get()
                        .setUri("http://wechat.xmsmjk.com/wechatsehr/v1/DrawImage/getDrawImageCode")
                        .setHeader("Host", "wechat.xmsmjk.com")
                        .setHeader("Referer", "http://wechat.xmsmjk.com/wechatsehr/wx/")
                        .setHeader("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 5_1 like Mac OS X) AppleWebKit/534.46 (KHTML, like Gecko) Mobile/9B176 MicroMessenger/4.3.2")
                        .setHeader("openid", registerBean.getOpenID())
                        .addParameter("openid", registerBean.getOpenID())
                        .addParameter("_", "0.4937472914841695")
                        .build();
                HttpResponse httpResponse = cHttpClient.execute(request, clientContext);
                inputStream = httpResponse.getEntity().getContent();
                break;
//        request.abort();
            } catch (HttpHostConnectException e) {
                logger.error("下载验证码图片请求被拒绝");
            } catch (SocketTimeoutException e) {
                logger.error("下载验证码图片请求超时");
            } catch (Exception e) {
                throw new RuntimeException();
            }

        }
        return inputStream;

    }

    public String getRegister(RegisterBean registerBean, HttpClientContext clientContext) throws Exception {
//        logger.info("预约中");
//        RequestBuilder requestBuilder = RequestBuilder.post()
//                .setUri("http://wechat.xmsmjk.com/wechatsehr/v1/webRegisterController/webRegister")
//                .setHeader("Host", "wechat.xmsmjk.com")
////                .setHeader("Referer", "http://wechat.xmsmjk.com/zycapwxsehr/view/appointment/confirm.jsp")
////                .setHeader("Oid", registerBean.getOpenID())
//                .setHeader("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 5_1 like Mac OS X) AppleWebKit/534.46 (KHTML, like Gecko) Mobile/9B176 MicroMessenger/4.3.2");
        Map params = new HashMap();
//        List<NameValuePair> list = new ArrayList<>();
        params.put("orgCode", registerBean.getOrgCode());
        params.put("deptCode", registerBean.getDeptCode());
        params.put("docCode", registerBean.getDocCode());
        params.put("sectionType", registerBean.getSectionType());
        params.put("startTime", registerBean.getStartTime());
        params.put("ssid", registerBean.getSsid());
        params.put("patientName", registerBean.getPatientName());
        params.put("patientID", registerBean.getPatientID());
        params.put("patientPhone", registerBean.getPatientPhone());
        params.put("patientSex", registerBean.getPatientSex());
        params.put("orgName", registerBean.getOrgName());
        params.put("openid", registerBean.getOpenID());
        params.put("deptName", registerBean.getDeptName());
        params.put("doctorName", registerBean.getDoctorName());
        params.put("telPhone", registerBean.getTelPhone());
        params.put("seq", registerBean.getSeq());
        params.put("state", registerBean.getState());
        params.put("iptCode", registerBean.getIptCode());
        params.put("timeCode", registerBean.getTimeCode());
        System.out.println(registerBean.getIptCode());
        String s = new String(JSONBinder.binder(Map.class).toJSON((params)).getBytes(), "GBK");
        HttpUriRequest request = RequestBuilder.post().setUri("http://wechat.xmsmjk.com/wechatsehr/v1/webRegisterController/webRegister")
                .setHeader("Host", "wechat.xmsmjk.com")
                .setHeader("Referer", "http://wechat.xmsmjk.com/wechatsehr/wx/")
                .setHeader("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 5_1 like Mac OS X) AppleWebKit/534.46 (KHTML, like Gecko) Mobile/9B176 MicroMessenger/4.3.2")
                .setHeader("openid", registerBean.getOpenID())
                .setCharset(Charset.forName("GBK"))
                .addParameter("params", s).build();
        HttpResponse httpResponse = cHttpClient.execute(request, clientContext);
        String responseStr = EntityUtils.toString(httpResponse.getEntity());
        logger.info(responseStr);
        Map<String, Object> resultMap = JSONBinder.binder(Map.class).fromJSON(responseStr);
        request.abort();
        return resultMap.get("result").toString();
    }

    public List<Map<String, Object>> reservationList(RegisterBean registerBean, HttpClientContext clientContext) throws Exception {
        StringBuilder url = new StringBuilder("http://wechat.xmsmjk.com/wechatsehr/v1/doctorController/GetRegDeptDoctorSectionList");
        url.append("?orgCode=").append(registerBean.getOrgCode());
        url.append("&deptCode=").append(registerBean.getDeptCode());
        url.append("&docCode=").append(registerBean.getDocCode());
        url.append("&date=").append(registerBean.getStartDate());
        HttpUriRequest request = RequestBuilder.get()
                .setUri(url.toString())
                .setHeader("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 5_1 like Mac OS X) AppleWebKit/534.46 (KHTML, like Gecko) Mobile/9B176 MicroMessenger/4.3.2")
                .build();
        HttpResponse httpResponse = cHttpClient.execute(request, clientContext);
        String responseStr = EntityUtils.toString(httpResponse.getEntity());
        Map<String, Object> map = JSONBinder.binder(Map.class).fromJSON(responseStr);
        if (!"null".equals(map.get("result").toString())) {
            Map<String, Object> dataMap = JSONBinder.binder(Map.class).fromJSON(map.get("result").toString());
            Map<String, Object> doctorMap = JSONBinder.binder(Map.class).fromJSON(JSONBinder.binder(Map.class).toJSON((Map) dataMap.get("doctor")));
            List<Map<String, Object>> sectionList = null;
            if (doctorMap.get("date") instanceof Map) {
                Map<String, Object> dateList = JSONBinder.binder(Map.class).fromJSON(JSONBinder.binder(Map.class).toJSON((Map) doctorMap.get("date")));
                sectionList = JSONBinder.binder(List.class).fromJSON(JSONBinder.binder(List.class).toJSON((List) dateList.get("section")));
            } else {
                List<Map<String, Object>> dateList = JSONBinder.binder(List.class).fromJSON(JSONBinder.binder(List.class).toJSON((List) doctorMap.get("date")));
                sectionList = JSONBinder.binder(List.class).fromJSON(JSONBinder.binder(List.class).toJSON((List) dateList.get("AM".equals(registerBean.getSectionType()) ? 0 : 1).get("section")));
            }
//            System.out.println(JSONBinder.binder(List.class).toJSON(sectionList));
            return sectionList;
        }
        request.abort();
        return null;
    }

    public Map<String, Object> reservationListAll(RegisterBean registerBean) throws Exception {
        StringBuilder url = new StringBuilder("http://wechat.xmsmjk.com/wechatsehr/v1/doctorController/GetRegDeptDoctorSectionList");
        url.append("?orgCode=").append(registerBean.getOrgCode());
        url.append("&deptCode=").append(registerBean.getDeptCode());
        url.append("&docCode=").append(registerBean.getDocCode());
        url.append("&date=").append(registerBean.getStartDate());
        HttpUriRequest request = RequestBuilder.get()
                .setUri(url.toString())
                .setHeader("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 5_1 like Mac OS X) AppleWebKit/534.46 (KHTML, like Gecko) Mobile/9B176 MicroMessenger/4.3.2")
                .build();
        HttpResponse httpResponse = cHttpClient.execute(request);
        String responseStr = EntityUtils.toString(httpResponse.getEntity());
        responseStr = responseStr.replace("@", "");
        Map<String, Object> map = JSONBinder.binder(Map.class).fromJSON(responseStr);
        request.abort();
        return map;
    }

    //开始预约
    public String doRegister(RegisterBean registerBean, HttpClientContext clientContext) throws Exception {
        String msg = resLockNumber(registerBean, clientContext);
        if (!msg.contains("锁号成功") && !msg.contains("由于您长时间未确认预约") && !"".equals(msg)) {
            return msg;
        }
        do {
            logger.info("获取验证码");
//            code(registerBean, clientContext);
            InputStream inputStream = drawImage(registerBean, clientContext);
            String wordArray = getSelectCode(registerBean, clientContext);
            String iptCode = "取验证码";
            try {
                iptCode = ImageCode.imageToWord(wordArray, registerBean.getIdNo(), inputStream);
            } catch (Exception e) {
                System.out.println("图片切割异常");

            }
            inputStream.close();
            registerBean.setIptCode(iptCode);
            msg = getRegister(registerBean, clientContext);
        } while ("error:请重新输入验证码".equals(msg));
//        if ("验证码错误,请重新输入".equals(msg)) {
//            InputStream inputStream2 = drawImage(registerBean, clientContext);
//            ImageIO.write(ImageIO.read(inputStream2), "png", new File("D:\\code\\" + registerBean.getOpenID() + registerBean.getIptCode() + System.currentTimeMillis() + ".png"));
//            inputStream2.close();
//        }
        return msg;

    }

    public String handDoRegister(RegisterBean registerBean, HttpClientContext clientContext) throws Exception {
        String msg = resLockNumber(registerBean, clientContext);
        if (!msg.contains("锁号成功") && !msg.contains("由于您长时间未确认预约") && !"".equals(msg)) {
            return msg;
        }
        do {
            logger.info("获取验证码");
//            code(registerBean, clientContext);
            InputStream inputStream = drawImage(registerBean, clientContext);
            String wordArray = getSelectCode(registerBean, clientContext);
            String iptCode = "取验证码";
            try {
                iptCode = ImageCode.imageToWord(wordArray, registerBean.getIdNo(), inputStream);
            } catch (Exception e) {
                System.out.println("图片切割异常");

            }
            inputStream.close();
            registerBean.setIptCode(iptCode);
            msg = getRegister(registerBean, clientContext);
        } while ("error:请重新输入验证码".equals(msg));
//        if ("验证码错误,请重新输入".equals(msg)) {
//            InputStream inputStream2 = drawImage(registerBean, clientContext);
//            ImageIO.write(ImageIO.read(inputStream2), "png", new File("D:\\code\\" + registerBean.getOpenID() + registerBean.getIptCode() + System.currentTimeMillis() + ".png"));
//            inputStream2.close();
//        }
        return msg;

    }

    public void testDo() throws Exception {

//        params.put("orgId", 2);
//        params.put("ssid", "600865500445500594400905502");
//        params.put("idno", "500135400835500094400975500665400875500075400935500365");
//        params.put("numberId", "503");
//        params.put("scheduleId", "400965400905400905800077400984500284500494500186400855500005500148500035");
//        params.put("startTime", "2018-12-12 14:54:00");
//        params.put("orgId", registerBean.getOrgId());
//        params.put("ssid", registerBean.getSsid());
//        params.put("idno", registerBean.getPatientID());
//        params.put("numberId", registerBean.getNumberId());
//        params.put("scheduleId", registerBean.getScheduleId());
//        params.put("startTime", registerBean.getStartTime());
        HttpClientContext clientContext = HttpClientContext.create();
        RegisterBean registerBean = new RegisterBean();
        registerBean.setOrgCode("350211G1001");
        registerBean.setDeptCode("1040613");
        registerBean.setDocCode("75532");
        registerBean.setSectionType("PM ");
        registerBean.setStartTime("2018-12-12 14:54:00");
        registerBean.setSsid("600865500445500594400905502");
        registerBean.setPatientName("许少军");
        registerBean.setPatientID("500135400835500094400975500665400875500075400935500365");
        registerBean.setPatientPhone("400935500594400845500484500035505");
        registerBean.setTelPhone("400935500594400845500484500035505");
        registerBean.setPatientSex("女");
        registerBean.setOrgName("厦门大学附属第一医院");
        registerBean.setDeptName("小儿呼吸哮喘门诊");
        registerBean.setDoctorName("张宁");
        registerBean.setSeq("201812052334");
        registerBean.setState("124");
        registerBean.setIptCode("");
        registerBean.setNumberId("503");
        registerBean.setScheduleId("400965400905400905800077400984500284500494500186400855500005500148500035");
        registerBean.setIdNo("D86671124");
        registerBean.setOrgId("2");
        registerBean.setPassword("350521198809291558");
        registerBean.setStartDate("2018-12-12");
        registerBean.setEndDate("2018-12-12");
        registerBean.setTimeCode("wRKSYUdjG/DhFulZfyRYeifOqks1aYLGnx1icyxATWe8BQxw87RMTiUIpPUbhTB8");
        registerBean.setOpenID("WGdtWmhFS0Y5MndGemE2L2tHU2dUMm5nSEZOdVVJbTJoTzN6ZjBjK1U4SVRIaWhqTGtSc0J1am55c1dxNzZZUQ==");
//        index(registerBean, clientContext);
//        Map<String, Object> resultMap = login(registerBean, clientContext);
//        registerBean.setSsid(resultMap.get("patientSsid").toString());
//        registerBean.setPatientID(resultMap.get("patientID").toString());
//        registerBean.setPatientPhone(resultMap.get("patientPhone").toString());
        while (true) {
            String msg = handDoRegister(registerBean, clientContext);
            if (!"error:请重新输入验证码".equals(msg)) {
                break;
            }

        }
//        reservationList(registerBean, clientContext);
//        getRegister(registerBean, clientContext);
    }
}
