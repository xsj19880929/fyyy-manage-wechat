package com.suryani.manage.system.web;

import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.quidsi.core.platform.web.rest.RESTController;
import com.suryani.manage.annotation.LogOperationRequired;
import com.suryani.manage.system.service.SystemUserService;
import com.suryani.manage.util.AjaxHelper;
import com.suryani.manage.util.Utils;

@Controller
@RequestMapping("system/user")
public class SystemUserAjaxController extends RESTController {
    @Inject
    private SystemUserService systemUserService;

    @RequestMapping(value = "/list")
    @ResponseBody
    public Map<String, Object> listWxCbqShop(@RequestParam String name, @RequestParam(defaultValue = "0") Integer offset, @RequestParam(defaultValue = "10") Integer fetchSize) {
        return Utils.pagerWarp(systemUserService.list(name, offset, fetchSize), systemUserService.total(name));
    }

    @LogOperationRequired(value = "删除系统用户")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public String delete(@RequestParam("id") String id) {
        systemUserService.delete(id);
        return "true";
    }

    @RequestMapping(value = "/checkName", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> checkName(@RequestParam("name") String name) {
        int rt = systemUserService.countUserByName(name);
        return AjaxHelper.success(rt == 0 ? "true" : "false");
    }
    
    @RequestMapping(value = "/checkPhone", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> checkPhone(@RequestParam("phone") String phone) {
        int rt = systemUserService.countUserByPhone(phone);
        return AjaxHelper.success(rt == 0 ? "true" : "false");
    }


}
