package com.suryani.manage.booking.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Doctor {
    @Id
    private String id;
    private String doctorId;
    private String doctor;
    private String deptId;
    private int checkClinc;
    private Date createdTime;
    private String doctorSn;

    public String getDoctorSn() {
        return doctorSn;
    }

    public void setDoctorSn(String doctorSn) {
        this.doctorSn = doctorSn;
    }

    public int getCheckClinc() {
        return checkClinc;
    }

    public void setCheckClinc(int checkClinc) {
        this.checkClinc = checkClinc;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
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

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

}
