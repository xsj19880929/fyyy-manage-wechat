package com.suryani.manage.booking.web;

import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.quidsi.core.platform.web.rest.RESTController;
import com.suryani.manage.booking.service.DepartmentService;
import com.suryani.manage.util.Utils;

@Controller
@RequestMapping("department")
public class DepartmentAjaxController extends RESTController {
    @Inject
    private DepartmentService departmentService;

    @RequestMapping(value = "/list")
    @ResponseBody
    public Map<String, Object> listWxCbqShop(@RequestParam(defaultValue = "0") Integer offset, @RequestParam(defaultValue = "10") Integer fetchSize) {
        return Utils.pagerWarp(departmentService.list(offset, fetchSize), departmentService.total());
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public String delete(@RequestParam("id") String id) {
        departmentService.delete(id);
        return "true";
    }

}
