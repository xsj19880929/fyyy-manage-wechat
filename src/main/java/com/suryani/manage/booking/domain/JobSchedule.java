package com.suryani.manage.booking.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class JobSchedule {

    public static final int INIT_JOB_STATE = 0; // 未执行的任务，即初始化得任务
    public static final int EXE_JOB_STATE = 1; // 执行中的任务状态
    public static final int END_JOB_STATE = 10; // 任务结束
    public static final int FAIL_JOB_STATE = -1; // 任务失败，可根据错误类型拆分错误码

    @Id
    private String id;
    private String jobName;
    private String jobClass;
    private int jobState;
    private Date createdTime;
    private Date updatedTime;
    private String cronExpression;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobClass() {
        return jobClass;
    }

    public void setJobClass(String jobClass) {
        this.jobClass = jobClass;
    }

    public int getJobState() {
        return jobState;
    }

    public void setJobState(int jobState) {
        this.jobState = jobState;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

}