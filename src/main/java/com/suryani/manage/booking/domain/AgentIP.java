package com.suryani.manage.booking.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class AgentIP {
    @Id
    private String id;
    private String ip;
    private int port;
    private int timeLong;
    private int status;
    private Date createdTime;
    private int useStatus;
    private Date bookingTime;

    public Date getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(Date bookingTime) {
        this.bookingTime = bookingTime;
    }

    public int getUseStatus() {
        return useStatus;
    }

    public void setUseStatus(int useStatus) {
        this.useStatus = useStatus;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip.trim();
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getTimeLong() {
        return timeLong;
    }

    public void setTimeLong(int timeLong) {
        this.timeLong = timeLong;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
