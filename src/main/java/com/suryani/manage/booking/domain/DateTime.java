package com.suryani.manage.booking.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity
public class DateTime {
    @Id
    private String id;
    private String selectTime;
    private String doctor;
    private String doctorId;
    private String selectDate;
    private String timeCode;
    @Transient
    private int abs;
    @Transient
    private int sort;

    public String getTimeCode() {
        return timeCode;
    }

    public void setTimeCode(String timeCode) {
        this.timeCode = timeCode;
    }

    public int getAbs() {
        return abs;
    }

    public void setAbs(int abs) {
        this.abs = abs;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSelectTime() {
        return selectTime;
    }

    public void setSelectTime(String selectTime) {
        this.selectTime = selectTime;
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

}
