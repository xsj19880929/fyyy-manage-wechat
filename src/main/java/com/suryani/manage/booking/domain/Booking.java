package com.suryani.manage.booking.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.Date;

@Entity
public class Booking {
    public static final String STATUS_NOBOOKING = "0";
    public static final String STATUS_FAIL = "1";
    public static final String STATUS_SUCCESS = "2";
    public static final String STATUS_CANCEL = "3";
    public static final String PAY_NO = "0";
    public static final String PAY_YES = "1";
    @Id
    private String id;
    private String doctorId;
    private String doctor;
    private Date selectDate;
    private String selectTime;
    private String deptName;
    private String triageNo;
    private String icardid;
    private String username;
    private String phone;
    private String timeDesc;
    private String status;
    private String pay;
    private String autoTime;
    private String cardNo;
    private String doctorSn;
    private String orgId;
    private String password;
    private String numberId;
    private String orgCode;
    private String orgName;
    private String backupDoctorId;
    private String backupDoctor;
    private String backupDoctorSn;
    private String remark;
    private String openId;
    @Transient
    private String taskUrl;

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getBackupDoctorId() {
        return backupDoctorId;
    }

    public void setBackupDoctorId(String backupDoctorId) {
        this.backupDoctorId = backupDoctorId;
    }

    public String getBackupDoctor() {
        return backupDoctor;
    }

    public void setBackupDoctor(String backupDoctor) {
        this.backupDoctor = backupDoctor;
    }

    public String getBackupDoctorSn() {
        return backupDoctorSn;
    }

    public void setBackupDoctorSn(String backupDoctorSn) {
        this.backupDoctorSn = backupDoctorSn;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getNumberId() {
        return numberId;
    }

    public void setNumberId(String numberId) {
        this.numberId = numberId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getDoctorSn() {
        return doctorSn;
    }

    public void setDoctorSn(String doctorSn) {
        this.doctorSn = doctorSn;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo.trim();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public Date getSelectDate() {
        return selectDate;
    }

    public void setSelectDate(Date selectDate) {
        this.selectDate = selectDate;
    }

    public String getSelectTime() {
        return selectTime;
    }

    public void setSelectTime(String selectTime) {
        this.selectTime = selectTime.trim();
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getTriageNo() {
        return triageNo;
    }

    public void setTriageNo(String triageNo) {
        this.triageNo = triageNo;
    }

    public String getIcardid() {
        return icardid;
    }

    public void setIcardid(String icardid) {
        this.icardid = icardid.trim();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone.trim();
    }

    public String getTimeDesc() {
        return timeDesc;
    }

    public void setTimeDesc(String timeDesc) {
        this.timeDesc = timeDesc;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPay() {
        return pay;
    }

    public void setPay(String pay) {
        this.pay = pay;
    }

    public String getTaskUrl() {
        return taskUrl;
    }

    public void setTaskUrl(String taskUrl) {
        this.taskUrl = taskUrl;
    }

    public String getAutoTime() {
        return autoTime;
    }

    public void setAutoTime(String autoTime) {
        this.autoTime = autoTime;
    }

}
