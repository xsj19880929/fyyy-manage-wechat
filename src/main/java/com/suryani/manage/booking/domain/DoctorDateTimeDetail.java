package com.suryani.manage.booking.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author soldier
 */
@Entity
public class DoctorDateTimeDetail {
    @Id
    private String id;
    private String doctorDateTimeId;
    private String selectTime;
    private Integer ifUser;
    private Integer selectTimeInt;

    public Integer getSelectTimeInt() {
        return selectTimeInt;
    }

    public void setSelectTimeInt(Integer selectTimeInt) {
        this.selectTimeInt = selectTimeInt;
    }

    public Integer getIfUser() {
        return ifUser;
    }

    public void setIfUser(Integer ifUser) {
        this.ifUser = ifUser;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDoctorDateTimeId() {
        return doctorDateTimeId;
    }

    public void setDoctorDateTimeId(String doctorDateTimeId) {
        this.doctorDateTimeId = doctorDateTimeId;
    }

    public String getSelectTime() {
        return selectTime;
    }

    public void setSelectTime(String selectTime) {
        this.selectTime = selectTime;
    }
}
