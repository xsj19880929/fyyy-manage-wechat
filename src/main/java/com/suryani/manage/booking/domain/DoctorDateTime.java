package com.suryani.manage.booking.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

/**
 * @author soldier
 */
@Entity
public class DoctorDateTime {
    @Id
    private String id;
    private Date createTime;
    private Date updateTime;
    private String doctor;
    private String doctorId;
    private String selectDate;
    private String timeDesc;
    private String triageNo;
    private String orgCode;
    @Transient
    private List<DoctorDateTimeDetail> doctorDateTimeDetailList;

    public List<DoctorDateTimeDetail> getDoctorDateTimeDetailList() {
        return doctorDateTimeDetailList;
    }

    public void setDoctorDateTimeDetailList(List<DoctorDateTimeDetail> doctorDateTimeDetailList) {
        this.doctorDateTimeDetailList = doctorDateTimeDetailList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getSelectDate() {
        return selectDate;
    }

    public void setSelectDate(String selectDate) {
        this.selectDate = selectDate;
    }

    public String getTimeDesc() {
        return timeDesc;
    }

    public void setTimeDesc(String timeDesc) {
        this.timeDesc = timeDesc;
    }

    public String getTriageNo() {
        return triageNo;
    }

    public void setTriageNo(String triageNo) {
        this.triageNo = triageNo;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }
}
