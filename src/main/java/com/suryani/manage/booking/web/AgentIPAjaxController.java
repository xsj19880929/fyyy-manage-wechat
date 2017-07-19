package com.suryani.manage.booking.web;

import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.quidsi.core.platform.web.rest.RESTController;
import com.suryani.manage.booking.service.AgentIPService;
import com.suryani.manage.util.AjaxHelper;
import com.suryani.manage.util.Utils;

@Controller
@RequestMapping("agentip")
public class AgentIPAjaxController extends RESTController {
    @Inject
    private AgentIPService agentIPService;

    @RequestMapping(value = "/list")
    @ResponseBody
    public Map<String, Object> listWxCbqShop(@RequestParam(defaultValue = "0") Integer offset, @RequestParam(defaultValue = "10") Integer fetchSize) {
        return Utils.pagerWarp(agentIPService.list(offset, fetchSize), agentIPService.total());
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public String delete(@RequestParam("id") String id) {
        agentIPService.delete(id);
        return "true";
    }

    @RequestMapping(value = "/batchcheck")
    @ResponseBody
    public String batchCheck() {
        agentIPService.updateAllTimeLong();
        return "true";
    }

    @RequestMapping(value = "/check")
    @ResponseBody
    public String check(@RequestParam("id") String id) {
        agentIPService.updateTimeLong(agentIPService.getById(id));
        return "true";
    }

    @RequestMapping(value = "/checkip", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> checkPhone(@RequestParam("ip") String ip) {
        int rt = agentIPService.getSizeByIp(ip.trim());
        return AjaxHelper.success(rt == 0 ? "true" : "false");
    }
}
