package com.suryani.manage.booking.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Department {
    @Id
    private String id;
    private String deptName;
    private String triageNo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

}
