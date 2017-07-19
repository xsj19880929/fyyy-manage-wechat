package com.suryani.manage.schedule.service;

import com.suryani.manage.booking.domain.Booking;
import com.suryani.manage.util.CalendarUtils;
import com.suryani.manage.util.StringRandom;
import org.springframework.util.StringUtils;

/**
 * @author soldier
 */
public class RegisterBeanSwitch {
    public static RegisterBean switchBean(Booking booking) {
        RegisterBean registerBean = new RegisterBean();
        registerBean.setOrgCode(booking.getOrgCode());
        registerBean.setDeptCode(booking.getTriageNo());
        registerBean.setDocCode(booking.getDoctorId());
        registerBean.setSectionType("上午".equals(booking.getTimeDesc()) ? "AM" : "PM");
        registerBean.setStartTime(CalendarUtils.format(booking.getSelectDate(), "yyyy-MM-dd") + " " + booking.getSelectTime() + ":00");
        registerBean.setPatientName(booking.getUsername());
        registerBean.setOrgName(booking.getOrgName());
        registerBean.setOpenID(StringUtils.hasText(booking.getOpenId()) ? booking.getOpenId() : StringRandom.getOpenId());
        registerBean.setDeptName(booking.getDeptName());
        registerBean.setDoctorName(booking.getDoctor());
        registerBean.setSeq(CalendarUtils.format(booking.getSelectDate(), "yyyyMMdd") + booking.getSelectTime().replace(":", ""));
        registerBean.setState("124");
        registerBean.setIptCode("");
        registerBean.setNumberId(booking.getOrgId() + booking.getDoctorId() + CalendarUtils.format(booking.getSelectDate(), "yyyyMMdd") + ("上午".equals(booking.getTimeDesc()) ? "AM" : "PM") + booking.getSelectTime().replace(":", ""));
        registerBean.setScheduleId(booking.getOrgId() + booking.getDoctorId() + CalendarUtils.format(booking.getSelectDate(), "yyyyMMdd") + ("上午".equals(booking.getTimeDesc()) ? "AM" : "PM"));
        registerBean.setIdNo(booking.getCardNo());
        registerBean.setOrgId(booking.getOrgId());
        registerBean.setPassword(StringUtils.hasText(booking.getPassword()) ? booking.getPassword() : booking.getIcardid());
        registerBean.setStartDate(CalendarUtils.format(booking.getSelectDate(), "yyyy-MM-dd"));
        registerBean.setEndDate(CalendarUtils.format(booking.getSelectDate(), "yyyy-MM-dd"));
        return registerBean;
    }
}
