package com.suryani.manage.system.web;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.quidsi.core.platform.web.site.SiteController;
import com.suryani.manage.system.domain.SystemMenu;
import com.suryani.manage.system.domain.SystemRole;
import com.suryani.manage.system.service.SystemMenuService;
import com.suryani.manage.system.service.SystemRoleService;

@Controller
@RequestMapping("system/role")
public class RoleManageController extends SiteController {
    @Inject
    private SystemRoleService systemRoleService;
    @Inject
    private SystemMenuService systemMenuService;
    List<SystemMenu> listNewMenu = new ArrayList<SystemMenu>();
    int level = 0;

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String home(Map<String, Object> viewData, HttpServletRequest request) {

        return "common.system.role-list";
    }

    @RequestMapping(value = "/add/view", method = RequestMethod.GET)
    public String addSiteView(Map<String, Object> viewData, HttpServletRequest request) throws UnsupportedEncodingException {
        String id = request.getParameter("id");
        if (id != null) {
            viewData.put("role", this.systemRoleService.get(id));
        }
        viewData.put("parentMenus", getParentMenus("0"));
        viewData.put("roleMenus", systemRoleService.listMenusByRoleId(id));
        return "common.system.role-update";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String addSite(Map<String, Object> viewData, @RequestParam("id") String id, @RequestParam("name") String name, @RequestParam("menus") String[] menus) {
        if (!StringUtils.hasText(id)) {
            SystemRole site = new SystemRole();
            String uuid = UUID.randomUUID().toString();
            id = uuid;
            site.setId(uuid);
            site.setName(name);
            site.setCreatedTime(new Date());
            this.systemRoleService.save(site, menus);
        } else {
            SystemRole site = this.systemRoleService.get(id);
            site.setName(name);
            this.systemRoleService.update(site, menus);
        }
        return "redirect:home";
    }

    private List<SystemMenu> getParentMenus(String id) {
        List<SystemMenu> listMenu = systemMenuService.listMenusByParent(id);
        if (!listMenu.isEmpty()) {
            level = level + 1;
            for (SystemMenu menu : listMenu) {
                listNewMenu.add(menu);
                getParentMenus(menu.getId());
            }
        }
        return listNewMenu;
    }

}
