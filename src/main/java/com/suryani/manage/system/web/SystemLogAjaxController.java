package com.suryani.manage.system.web;

import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.quidsi.core.platform.web.rest.RESTController;
import com.suryani.manage.system.service.SystemLogService;
import com.suryani.manage.util.Utils;

@Controller
@RequestMapping("system/log")
public class SystemLogAjaxController extends RESTController {
    @Inject
    private SystemLogService systemLogService;

    @RequestMapping(value = "/list")
    @ResponseBody
    public Map<String, Object> list(@RequestParam(defaultValue = "0") Integer offset, @RequestParam(defaultValue = "10") Integer fetchSize) {
        return Utils.pagerWarp(systemLogService.listDetail(offset, fetchSize), systemLogService.total());
    }

}
