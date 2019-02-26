package com.suryani.manage.schedule.service;

import com.quidsi.core.json.JSONBinder;
import com.suryani.manage.booking.domain.Booking;
import com.suryani.manage.booking.domain.DateTime;
import com.suryani.manage.booking.service.BookingService;
import com.suryani.manage.util.CalendarUtils;
import com.suryani.manage.util.StringRandom;
import com.suryani.manage.util.Utils;
import org.apache.http.client.protocol.HttpClientContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class BookingDoctorNewService {
    private final Logger logger = LoggerFactory.getLogger(BookingDoctorNewService.class);
    @Inject
    private BookingService bookingService;
    @Inject
    private HospitalRegisterService hospitalRegisterService;

    public void startTread() {
        List<Booking> taskList = bookingService.listAfterOneWeek();
        if (taskList != null && !taskList.isEmpty()) {
            ExecutorService executor = Executors.newFixedThreadPool(taskList.size());
            for (final Booking booking : taskList) {
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        startWork(booking);
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
        if (responseString.contains("该号源不存在或已被占用")) {
            logger.info("返回错误信息:该号源不存在或已被占用");
            return "error101";

        }
        if (responseString.contains("已经没有可以预约的号源")) {
            logger.info("返回错误信息:已经没有可以预约的号源");
            return "error102";
        }
        if (responseString.contains("没有通过身份验证")) {
            logger.info("返回错误信息:没有通过身份验证");
            return "error103";
        }
        if (responseString.contains("请勿频繁操作")) {
            logger.info("返回错误信息:请勿频繁操作");
            return "error104";
        }
        if (responseString.contains("社区20%预约号已满")) {
            logger.info("返回错误信息:社区20%预约号已满");
            return "error105";
        }
        if (responseString.contains("该号源已被其他用户预约中")) {
            logger.info("返回错误信息:该号源已被其他用户预约中，请选择其他号源！");
            return "error106";
        }
        if (responseString.contains("该预约医生已停诊")) {
            logger.info("返回错误信息:该预约医生已停诊！");
            return "noClinic";
        }
        if (responseString.contains("该市民卡所选日期已有预约")) {
            logger.info("该市民卡所选日期已有预约！！！！！！");
            return "successed";
        }
        if (responseString.contains("已经预约了该医生")) {
            logger.info("已经预约了该医生");
            return "successed";
        }
        if (responseString.contains("成功")) {
            logger.info("成功了哈哈！！！！！！");
            return "ok";
        }
        if (responseString.contains("96166")) {
            logger.info("返回错误信息:96166打电话约！");
            return "errorip";
        }
        if (responseString.contains("10分钟内微信号预约次数已达到3次")) {
            logger.info("返回错误信息:您10分钟内微信号预约次数已达到3次,请过段时间再预约");
            return "openIdLimit";
        }
        if (responseString.contains("1分钟内微信号预约次数已达到2次")) {
            logger.info("返回错误信息:您1分钟内微信号预约次数已达到2次,请过段时间再预约");
            return "openIdLimit2";
        }
        if (responseString.contains("请确认是您正在使用的手机号码")) {
            logger.info("返回错误信息:手机号不对");
            return "phoneNumberError";
        }

        logger.info("返回错误信息:未知错误信息{" + responseString + "}");
        return null;
    }

    // 开始预约
    public String startWork(Booking booking) {
        boolean flag = true;
        String result = "";
        RegisterBean registerBean = RegisterBeanSwitch.switchBean(booking);
        HttpClientContext clientContext = null;
        Map<String, Object> resultMap = new HashMap<>();
        int i = 0;
        while (i < 3) {
            try {
                clientContext = HttpClientContext.create();
//                hospitalRegisterService.index(registerBean, clientContext);
                resultMap = hospitalRegisterService.getSsid(registerBean, clientContext);
                registerBean.setSsid(resultMap.get("ssid").toString());
                registerBean.setPatientID(resultMap.get("idno").toString());
                registerBean.setPatientPhone(resultMap.get("telphone").toString());
                registerBean.setPatientSex(resultMap.get("sex").toString());
                logger.info(registerBean.getPatientName() + "登陆成功");
                break;
            } catch (Exception e) {
                if (i++ == 2) {
                    logger.error(e.getMessage(), e);
                    logger.info("登陆失败");
                    flag = false;
                }
            }
        }

        while (flag) {
            try {
                List<DateTime> newTimesOne = Utils.sortList(timeListNew(registerBean, clientContext), Integer.parseInt(booking.getSelectTime().replace(":", "")));
                if (newTimesOne != null && !newTimesOne.isEmpty()) {
                    booking.setSelectTime(newTimesOne.get(0).getSelectTime());
                    registerBean = RegisterBeanSwitch.switchBean(booking);
                    registerBean.setSsid(resultMap.get("patientSsid").toString());
                    registerBean.setPatientID(resultMap.get("patientID").toString());
                    registerBean.setPatientPhone(resultMap.get("patientPhone").toString());
                    registerBean.setPatientSex(resultMap.get("patientSex").toString());
                    registerBean.setTimeCode(newTimesOne.get(0).getTimeCode());
                    logger.info("=================================预约时间：" + newTimesOne.get(0).getSelectTime());
                }
                long startResponse = System.currentTimeMillis();
                String responseString = hospitalRegisterService.doRegister(registerBean, clientContext);
                logger.info("提交数据耗时：" + (System.currentTimeMillis() - startResponse) + "ms");
                String errorMsg = defineError(responseString);
                result = responseString;
                if ("ok".equals(errorMsg)) {
                    flag = false;
                    logger.info(responseString);
                    bookingService.updateStatus(booking.getId(), Booking.STATUS_SUCCESS, booking.getSelectTime());
                } else if ("error102".equals(errorMsg) || "error101".equals(errorMsg) || "error106".equals(errorMsg)) {
                    logger.info("预约信息" + JSONBinder.binder(RegisterBean.class).toJSON(registerBean));
                    List<DateTime> newTimes = Utils.sortList(timeListNew(registerBean, clientContext), Integer.parseInt(booking.getSelectTime().replace(":", "")));
                    if (newTimes != null && !newTimes.isEmpty()) {
                        booking.setSelectTime(newTimes.get(0).getSelectTime());
                        registerBean = RegisterBeanSwitch.switchBean(booking);
                        registerBean.setSsid(resultMap.get("patientSsid").toString());
                        registerBean.setPatientID(resultMap.get("patientID").toString());
                        registerBean.setPatientPhone(resultMap.get("patientPhone").toString());
                        registerBean.setPatientSex(resultMap.get("patientSex").toString());
                        registerBean.setTimeCode(newTimesOne.get(0).getTimeCode());
                        logger.info("=================================预约时间：" + newTimes.get(0).getSelectTime());
                    } else {
                        flag = backupDoctor(registerBean, booking, resultMap);
                        logger.info("哎呀没抢到~~~");
                        bookingService.updateStatus(booking.getId(), Booking.STATUS_FAIL);
                    }
                } else if ("openIdLimit".equals(errorMsg) || "openIdLimit2".equals(errorMsg)) {
                    logger.info("更换openId");
                    registerBean.setOpenID(StringRandom.getOpenId());

                } else if ("error103".equals(errorMsg)) {
                    flag = false;
                    logger.info("没有登陆");
                    bookingService.updateStatus(booking.getId(), Booking.STATUS_FAIL);

                } else if ("successed".equals(errorMsg)) {
                    flag = false;
                    logger.info("已经预约过了用户信息" + booking.getUsername());
                    bookingService.updateStatus(booking.getId(), Booking.STATUS_SUCCESS);

                } else if ("noClinic".equals(errorMsg)) {
                    flag = backupDoctor(registerBean, booking, resultMap);
                    logger.info("医生停诊了");
                    bookingService.updateStatus(booking.getId(), Booking.STATUS_FAIL);

                } else {
                    // try {
                    // Thread.sleep(1000);
                    // } catch (InterruptedException e) {
                    // e.printStackTrace();
                    // }
                }
                if ("error104".equals(errorMsg)) {
                    logger.info("请勿频繁操作");

                }
            } catch (Exception e) {
                logger.error(e.getMessage() + booking.getUsername(), e);
            }
        }
        return result;
    }

    private boolean backupDoctor(RegisterBean registerBean, Booking booking, Map<String, Object> resultMap) {
        if (StringUtils.hasText(booking.getBackupDoctorId()) && !booking.getDoctorId().equals(booking.getBackupDoctorId())) {
            logger.info("换备用医生");
            booking.setDoctor(booking.getBackupDoctor());
            booking.setDoctorId(booking.getBackupDoctorId());
            booking.setDoctorSn(booking.getBackupDoctorSn());
            registerBean = RegisterBeanSwitch.switchBean(booking);
            registerBean.setSsid(resultMap.get("patientSsid").toString());
            registerBean.setPatientID(resultMap.get("patientID").toString());
            registerBean.setPatientPhone(resultMap.get("patientPhone").toString());
            registerBean.setPatientSex(resultMap.get("patientSex").toString());
            return true;
        }
        return false;

    }


    // 下载预约时间列表
    private List<DateTime> timeListNew(RegisterBean registerBean, HttpClientContext clientContext) {
        logger.info("重新获取预约时间");
        boolean wait = true;
        List<DateTime> times = new ArrayList<DateTime>();
        while (wait) {
            try {
                List<Map<String, Object>> reservationList = hospitalRegisterService.reservationList(registerBean, clientContext);
                if (reservationList != null) {
                    for (Map<String, Object> dateMap : reservationList) {
                        if ("0".equals(dateMap.get("@used").toString()) && !"0".equals(dateMap.get("@max").toString())) {
                            DateTime dateTime = new DateTime();
                            dateTime.setSelectTime(CalendarUtils.format(CalendarUtils.parse(dateMap.get("@start_time").toString(), "yyyy/MM/dd HH:mm:ss"), "HH:mm"));
                            dateTime.setTimeCode(dateMap.get("@time_code").toString());
                            times.add(dateTime);
                        }

                    }

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

}
