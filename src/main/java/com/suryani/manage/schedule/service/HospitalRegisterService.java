package com.suryani.manage.schedule.service;


import com.quidsi.core.json.JSONBinder;
import com.suryani.manage.common.CHttpClient;
import com.suryani.manage.common.ImageCode;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
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
                .setUri("http://wechat.xmsmjk.com/zycapwxsehr/ReservationLogController/toConfirm.do")
                .setHeader("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 5_1 like Mac OS X) AppleWebKit/534.46 (KHTML, like Gecko) Mobile/9B176 MicroMessenger/4.3.2")
                .setHeader("Oid", registerBean.getOpenID())
                .addParameter("idno", registerBean.getIdNo())
                .addParameter("password", registerBean.getPassword())
                .build();
        HttpResponse httpResponse = cHttpClient.execute(request, clientContext);
        String responseStr = EntityUtils.toString(httpResponse.getEntity());
        logger.info(responseStr);
        request.abort();
        return JSONBinder.binder(Map.class).fromJSON(responseStr);
    }

    public String resLockNumber(RegisterBean registerBean, HttpClientContext clientContext) throws Exception {
//        logger.info("锁号中");
        HttpUriRequest request = RequestBuilder.post()
                .setUri("http://wechat.xmsmjk.com/zycapwxsehr/HospitalNoteController/ResLockNumber.do")
                .setHeader("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 5_1 like Mac OS X) AppleWebKit/534.46 (KHTML, like Gecko) Mobile/9B176 MicroMessenger/4.3.2")
                .setHeader("Oid", registerBean.getOpenID())
                .addParameter("orgId", registerBean.getOrgId())
                .addParameter("ssid", registerBean.getSsid())
                .addParameter("idCard", registerBean.getPatientID())
                .addParameter("numberId", registerBean.getNumberId())
                .addParameter("scheduleId", registerBean.getScheduleId())
                .addParameter("startTime", registerBean.getStartTime()).build();
        HttpResponse httpResponse = cHttpClient.execute(request, clientContext);
        String responseStr = EntityUtils.toString(httpResponse.getEntity());
        logger.info(responseStr);
        Map<String, Object> resultMap = JSONBinder.binder(Map.class).fromJSON(responseStr);
        Map<String, Object> dataMap = JSONBinder.binder(Map.class).fromJSON(resultMap.get("data").toString());
        Map<String, Object> msgMap = JSONBinder.binder(Map.class).fromJSON(JSONBinder.binder(Map.class).toJSON((Map) dataMap.get("RESULT")));
        request.abort();
        return msgMap.get("@MESSAGE").toString();
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
                        .setHeader("Oid", registerBean.getOpenID())
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
                        .setUri("http://wechat.xmsmjk.com/zycapwxsehr/HospitalNoteController/getSelectCode.do")
                        .setHeader("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 5_1 like Mac OS X) AppleWebKit/534.46 (KHTML, like Gecko) Mobile/9B176 MicroMessenger/4.3.2")
                        .setHeader("Oid", registerBean.getOpenID())
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
        return resultMap.get("data").toString();
    }

    public InputStream drawImage(RegisterBean registerBean, HttpClientContext clientContext) throws Exception {
//        logger.info("下载验证码图片");
        InputStream inputStream;
        while (true) {
            try {
                HttpUriRequest request = RequestBuilder.get()
                        .setUri("http://wechat.xmsmjk.com/zycapwxsehr/DrawImage?openid=" + registerBean.getOpenID())
                        .setHeader("Host", "wechat.xmsmjk.com")
                        .setHeader("Referer", "http://wechat.xmsmjk.com/zycapwxsehr/view/appointment/confirm.jsp")
                        .setHeader("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 5_1 like Mac OS X) AppleWebKit/534.46 (KHTML, like Gecko) Mobile/9B176 MicroMessenger/4.3.2")
                        .setHeader("Oid", registerBean.getOpenID())
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
        RequestBuilder requestBuilder = RequestBuilder.post()
                .setUri("http://wechat.xmsmjk.com/zycapwxsehr/HospitalNoteController/getRegister.do")
                .setHeader("Host", "wechat.xmsmjk.com")
                .setHeader("Referer", "http://wechat.xmsmjk.com/zycapwxsehr/view/appointment/confirm.jsp")
                .setHeader("Oid", registerBean.getOpenID())
                .setHeader("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 5_1 like Mac OS X) AppleWebKit/534.46 (KHTML, like Gecko) Mobile/9B176 MicroMessenger/4.3.2");
        List<NameValuePair> list = new ArrayList<>();
        list.add(new BasicNameValuePair("orgCode", registerBean.getOrgCode()));
        list.add(new BasicNameValuePair("deptCode", registerBean.getDeptCode()));
        list.add(new BasicNameValuePair("docCode", registerBean.getDocCode()));
        list.add(new BasicNameValuePair("sectionType", registerBean.getSectionType()));
        list.add(new BasicNameValuePair("startTime", registerBean.getStartTime()));
        list.add(new BasicNameValuePair("ssid", registerBean.getSsid()));
        list.add(new BasicNameValuePair("patientName", registerBean.getPatientName()));
        list.add(new BasicNameValuePair("patientID", registerBean.getPatientID()));
        list.add(new BasicNameValuePair("patientPhone", registerBean.getPatientPhone()));
        list.add(new BasicNameValuePair("patientSex", registerBean.getPatientSex()));
        list.add(new BasicNameValuePair("orgName", registerBean.getOrgName()));
        list.add(new BasicNameValuePair("openID", registerBean.getOpenID()));
        list.add(new BasicNameValuePair("deptName", registerBean.getDeptName()));
        list.add(new BasicNameValuePair("doctorName", registerBean.getDoctorName()));
        list.add(new BasicNameValuePair("seq", registerBean.getSeq()));
        list.add(new BasicNameValuePair("state", registerBean.getState()));
        list.add(new BasicNameValuePair("iptCode", registerBean.getIptCode()));
        list.add(new BasicNameValuePair("timeCode", registerBean.getTimeCode()));
        UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(list, "UTF-8");
        HttpUriRequest request = requestBuilder.setEntity(uefEntity).build();
        HttpResponse httpResponse = cHttpClient.execute(request, clientContext);
        String responseStr = EntityUtils.toString(httpResponse.getEntity());
//        logger.info(responseStr);
        Map<String, Object> resultMap = JSONBinder.binder(Map.class).fromJSON(responseStr);
        request.abort();
        return resultMap.get("data").toString();
    }

    public List<Map<String, Object>> reservationList(RegisterBean registerBean, HttpClientContext clientContext) throws Exception {
        StringBuilder url = new StringBuilder("http://wechat.xmsmjk.com/zycapwxsehr/ReservationLogController/detailtData.do");
        url.append("?orgid=").append(registerBean.getOrgCode());
        url.append("&deptid=").append(registerBean.getDeptCode());
        url.append("&docid=").append(registerBean.getDocCode());
        url.append("&strStart=").append(registerBean.getStartDate());
        url.append("&strEnd=").append(registerBean.getEndDate());
        HttpUriRequest request = RequestBuilder.get()
                .setUri(url.toString())
                .setHeader("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 5_1 like Mac OS X) AppleWebKit/534.46 (KHTML, like Gecko) Mobile/9B176 MicroMessenger/4.3.2")
                .setHeader("Oid", registerBean.getOpenID())
                .build();
        HttpResponse httpResponse = cHttpClient.execute(request, clientContext);
        String responseStr = EntityUtils.toString(httpResponse.getEntity());
        Map<String, Object> map = JSONBinder.binder(Map.class).fromJSON(responseStr);
        if (!"null".equals(map.get("data").toString())) {
            Map<String, Object> dataMap = JSONBinder.binder(Map.class).fromJSON(map.get("data").toString());
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
        StringBuilder url = new StringBuilder("http://wechat.xmsmjk.com/zycapwxsehr/ReservationLogController/detailtData.do");
        url.append("?orgid=").append(registerBean.getOrgCode());
        url.append("&deptid=").append(registerBean.getDeptCode());
        url.append("&docid=").append(registerBean.getDocCode());
        url.append("&strStart=").append(registerBean.getStartDate());
        url.append("&strEnd=").append(registerBean.getEndDate());
        HttpUriRequest request = RequestBuilder.get()
                .setUri(url.toString())
                .setHeader("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 5_1 like Mac OS X) AppleWebKit/534.46 (KHTML, like Gecko) Mobile/9B176 MicroMessenger/4.3.2")
                .setHeader("Oid", registerBean.getOpenID())
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
//        code(registerBean, clientContext);
            InputStream inputStream = drawImage(registerBean, clientContext);
            String wordArray = getSelectCode(registerBean, clientContext);
            String iptCode = ImageCode.imageToWord(wordArray, registerBean.getIdNo(), inputStream);
            inputStream.close();
            registerBean.setIptCode(iptCode);
            msg = getRegister(registerBean, clientContext);
        } while ("验证码错误,请重新输入".equals(msg));
//        if ("验证码错误,请重新输入".equals(msg)) {
//        InputStream inputStream2 = drawImage(registerBean, clientContext);
//        ImageIO.write(ImageIO.read(inputStream2), "png", new File("D:\\code\\" + registerBean.getOpenID() + registerBean.getIptCode() + System.currentTimeMillis() + ".png"));
//        inputStream2.close();
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
            String iptCode = ImageCode.imageToWord(wordArray, registerBean.getIdNo(), inputStream);
            inputStream.close();
            registerBean.setIptCode(iptCode);
            msg = getRegister(registerBean, clientContext);
        } while ("验证码错误,请重新输入".equals(msg));
//        if ("验证码错误,请重新输入".equals(msg)) {
//            InputStream inputStream2 = drawImage(registerBean, clientContext);
//            ImageIO.write(ImageIO.read(inputStream2), "png", new File("D:\\code\\" + registerBean.getOpenID() + registerBean.getIptCode() + System.currentTimeMillis() + ".png"));
//            inputStream2.close();
//        }
        return msg;

    }


    public void testDo() throws Exception {
        HttpClientContext clientContext = HttpClientContext.create();
        RegisterBean registerBean = new RegisterBean();
        registerBean.setOrgCode("350211G1001");
        registerBean.setDeptCode("320");
        registerBean.setDocCode("040");
        registerBean.setSectionType("PM ");
        registerBean.setStartTime("2016-11-14 14:48:00");
        registerBean.setSsid("600865500445500594400905502");
        registerBean.setPatientName("许少军");
        registerBean.setPatientID("500135400835500094400975500665400875500075400935500365");
        registerBean.setPatientPhone("400935500594400845500484500035505");
        registerBean.setPatientSex("男");
        registerBean.setOrgName("厦门市妇幼保健院");
        registerBean.setOpenID("oYNMFt49BHWpGRDCzqmtJg_vrfXg");
        registerBean.setDeptName("宫颈疾病门诊");
        registerBean.setDoctorName("吴冬梅");
        registerBean.setSeq("201609181436");
        registerBean.setState("124");
        registerBean.setIptCode("");
        registerBean.setNumberId("704020161114PM1448");
        registerBean.setScheduleId("704020161114PM");
        registerBean.setIdNo("D86671124");
        registerBean.setOrgId("7");
        registerBean.setPassword("350521198809291558");
        registerBean.setStartDate("2016-11-14");
        registerBean.setEndDate("2016-11-14");
        registerBean.setTimeCode("Y0jIsj2MxwLlFBpWfuYW7cU+8Oos3GuXLkg5phAMjDrBDgdvH5YLCKnFIWpeShXJ");
        index(registerBean, clientContext);
        Map<String, Object> resultMap = login(registerBean, clientContext);
        registerBean.setSsid(resultMap.get("patientSsid").toString());
        registerBean.setPatientID(resultMap.get("patientID").toString());
        registerBean.setPatientPhone(resultMap.get("patientPhone").toString());
        while (true) {
            String msg = handDoRegister(registerBean, clientContext);
            if (!"验证码错误,请重新输入".equals(msg)) {
                break;
            }

        }
//        reservationList(registerBean, clientContext);
//        getRegister(registerBean, clientContext);
    }
}
