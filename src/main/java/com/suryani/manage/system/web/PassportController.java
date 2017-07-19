package com.suryani.manage.system.web;

import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.quidsi.core.platform.web.site.SiteController;
import com.suryani.manage.annotation.LogOperationRequired;
import com.suryani.manage.annotation.WhenLog;
import com.suryani.manage.system.domain.SystemUser;
import com.suryani.manage.system.service.SystemUserService;
import com.suryani.manage.util.Utils;

@Controller
@RequestMapping("passport")
public class PassportController extends SiteController {
    @Inject
    private SystemUserService systemUserService;

    @RequestMapping(value = "/home")
    public String viewReport(Map<String, Object> model) {
        return "common.system.home";
    }

    @RequestMapping(value = "/login")
    public String login(HttpServletRequest request, Map<String, Object> viewData) {
        if (request.getSession().getAttribute("systemUser") != null) {
            return "redirect:" + Utils.getContextPath(request) + "passport/home";
        }
        viewData.put("preRequestUrl", request.getParameter("preRequestUrl"));
        return "sys.login";
    }

    @LogOperationRequired(value = "登陆")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(@RequestParam("name") String name, @RequestParam("password") String password, HttpServletRequest request, Map<String, Object> viewData) {
        SystemUser systemUser = systemUserService.getUser(name, password);
        if (systemUser == null) {
            viewData.put("error", "用户名或者密码错误.");
            return "sys.login";
        }

        HttpSession session = request.getSession();
        session.setAttribute("systemUser", systemUser);
        viewData.put("systemUser", systemUser);
        String redirectUrl = "".equals(request.getParameter("preRequestUrl")) ? "home" : ".." + request.getParameter("preRequestUrl").toString();
        return "redirect:" + redirectUrl;
    }

    @LogOperationRequired(value = "退出", whenLog = WhenLog.before)
    @RequestMapping(value = "/logout")
    public String logout(HttpServletRequest request, Map<String, Object> viewData) {
        request.getSession().invalidate();
        return "sys.login";
    }

}
