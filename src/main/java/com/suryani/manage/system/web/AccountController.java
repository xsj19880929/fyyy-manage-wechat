package com.suryani.manage.system.web;

import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.quidsi.core.platform.web.site.SiteController;
import com.suryani.manage.annotation.LogOperationRequired;
import com.suryani.manage.annotation.WhenLog;
import com.suryani.manage.system.domain.SystemUser;
import com.suryani.manage.system.service.SystemUserService;
import com.suryani.manage.util.HashUtil;
import com.suryani.manage.util.SystemUserUtils;

@Controller
@RequestMapping("account")
public class AccountController extends SiteController {

    @Inject
    private SystemUserService systemUserService;

    @RequestMapping(value = "/setting")
    public String myAccountSetting() {
        return "common.system.account-setting";
    }

    @LogOperationRequired(value = "修改密码")
    @RequestMapping(value = "/changepassword", method = RequestMethod.POST)
    public String changePassword(@RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword, Map<String, Object> viewData, HttpServletRequest request) {
        if (StringUtils.isBlank(oldPassword) || StringUtils.isBlank(newPassword))
            throw new IllegalArgumentException("template.commom.illegalargument");
        SystemUser systemUser = SystemUserUtils.getCurrentUser();

        if (!systemUser.getPassword().equals(HashUtil.getMD5Data(oldPassword))) {
            viewData.put("error", "旧密码不正确.");
            return "common.system.account-setting";
        }

        systemUser.setPassword(HashUtil.getMD5Data(newPassword));
        systemUserService.updatePwd(systemUser.getId(), newPassword);
        viewData.put("message", "密码修改成功!");
        return "common.system.account-setting";
    }
}
