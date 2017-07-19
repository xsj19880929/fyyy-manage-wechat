package com.suryani.manage.system.web;

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
import com.suryani.manage.annotation.LogOperationRequired;
import com.suryani.manage.system.domain.SystemUser;
import com.suryani.manage.system.service.SystemRoleService;
import com.suryani.manage.system.service.SystemUserService;
import com.suryani.manage.util.DateEditor;
import com.suryani.manage.util.HashUtil;

@Controller
@RequestMapping("system/user")
public class SystemUserController extends SiteController {
    @Inject
    private SystemUserService systemUserService;
    @Inject
    private SystemRoleService systemRoleService;

    @InitBinder
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
        binder.registerCustomEditor(Date.class, new DateEditor());
    }

    @RequestMapping(value = "/home")
    public String home(Map<String, Object> viewData) {
        return "common.system.user-list";
    }

    @LogOperationRequired(value = "编辑用户")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String add(@Valid SystemUser user, Map<String, Object> viewData) {
        String id = user.getId();
        if (StringUtils.hasText(id)) {
            SystemUser storeMember = this.systemUserService.getById(user.getId());
            storeMember.setName(user.getName());
            storeMember.setStatus(user.getStatus());
            storeMember.setUpdateTime(new Date());
            storeMember.setRoleId(user.getRoleId());
            this.systemUserService.update(storeMember);
        } else {
            user.setId(UUID.randomUUID().toString());
            user.setPassword(HashUtil.getMD5Data("123456"));
            user.setCreateTime(new Date());
            user.setUpdateTime(new Date());
            this.systemUserService.save(user);
        }
        return "redirect:home";
    }

    @RequestMapping(value = "/add/view", method = RequestMethod.GET)
    public String addView(@RequestParam("id") String id, Map<String, Object> viewData) {
        if (StringUtils.hasText(id)) {
            SystemUser member = this.systemUserService.getById(id);
            if (member != null) {
                viewData.put("bean", member);
            }
        }
        viewData.put("roles", this.systemRoleService.listRoles("", 0, 1000));
        return "common.system.user-update";
    }

}
