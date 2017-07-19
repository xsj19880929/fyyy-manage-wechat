package com.suryani.manage.system.dto;

import com.suryani.manage.system.domain.SystemLog;

public class SystemLogDto extends SystemLog {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
