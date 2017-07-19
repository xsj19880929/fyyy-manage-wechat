package com.suryani.manage.system.web;

import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.quidsi.core.platform.web.rest.RESTController;
import com.suryani.manage.system.service.SystemRoleService;
import com.suryani.manage.util.Utils;

@Controller
@RequestMapping("system/role")
public class RoleManageAjaxController extends RESTController {
    @Inject
    private SystemRoleService systemRoleService;

    @RequestMapping(value = "/list")
    @ResponseBody
    public Map<String, Object> getList(@RequestParam(defaultValue = "0") Integer offset, @RequestParam(defaultValue = "10") Integer fetchSize, HttpServletRequest request) {
        return Utils.pagerWarp(this.systemRoleService.listRoles("", offset, fetchSize), this.systemRoleService.getListRoleCount());
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public String deleteSystemMenu(@RequestParam("id") String id) throws Exception {
        systemRoleService.delete(id);
        return "true";
    }

}
