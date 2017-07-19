package com.suryani.manage.system.web;

import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.quidsi.core.platform.web.site.SiteController;
import com.suryani.manage.system.service.SystemLogService;

@Controller
@RequestMapping("system/log")
public class SystemLogController extends SiteController {
    @Inject
    private SystemLogService systemLogService;

    @RequestMapping(value = "/home")
    public String home(Map<String, Object> viewData) {
        return "common.system.log-list";
    }


}
