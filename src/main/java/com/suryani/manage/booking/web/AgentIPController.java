package com.suryani.manage.booking.web;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.quidsi.core.platform.web.site.SiteController;
import com.quidsi.core.util.StringUtils;
import com.suryani.manage.booking.domain.AgentIP;
import com.suryani.manage.booking.service.AgentIPService;
import com.suryani.manage.util.DateEditor;

@Controller
@RequestMapping("agentip")
public class AgentIPController extends SiteController {
    @Inject
    private AgentIPService agentIPService;

    @InitBinder
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
        binder.registerCustomEditor(Date.class, new DateEditor());
    }

    @RequestMapping(value = "/home")
    public String home(Map<String, Object> viewData) {
        return "common.agentip.list";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String add(@Valid AgentIP agentIP, Map<String, Object> viewData) {
        String id = agentIP.getId();
        if (StringUtils.hasText(id)) {
            agentIP.setCreatedTime(agentIPService.getById(id).getCreatedTime());
            agentIPService.update(agentIP);
        } else {
            agentIP.setId(UUID.randomUUID().toString());
            agentIP.setCreatedTime(new Date());
            agentIPService.save(agentIP);
        }
        return "redirect:home";
    }

    @RequestMapping(value = "/add/view", method = RequestMethod.GET)
    public String addView(@RequestParam("id") String id, Map<String, Object> viewData) {
        if (StringUtils.hasText(id)) {
            AgentIP agentIP = this.agentIPService.getById(id);
            if (agentIP != null) {
                viewData.put("bean", agentIP);
            }
        }
        return "common.agentip.update";
    }

}
