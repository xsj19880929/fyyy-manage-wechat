package com.suryani.manage.system.web;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
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
import com.suryani.manage.system.service.SystemMenuService;

@Controller
@RequestMapping("system/menu")
public class MenuManageController extends SiteController {
    private SystemMenuService systemMenuService;
    List<Map<String, String>> listNewMenu = new ArrayList<Map<String, String>>();
    String subFlag = "";
    int level = 0;

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String home(Map<String, Object> viewData, HttpServletRequest request) {
        return "common.system.menu-list";
    }

    @RequestMapping(value = "/add/view", method = RequestMethod.GET)
    public String addSiteView(Map<String, Object> viewData, @RequestParam(required = false) String id) throws UnsupportedEncodingException {
        viewData.put("parentMenus", getParentMenus("0"));
        if (StringUtils.hasText(id)) {
            viewData.put("menu", systemMenuService.get(id));
        }
        return "common.system.menu-update";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String addSite(Map<String, Object> viewData, SystemMenu menu) {
        if ("0".equals(menu.getParent())) {
            menu.setLevel(0);
        } else {
            menu.setLevel(systemMenuService.get(menu.getParent()).getLevel() + 1);
        }
        if (!StringUtils.hasText(menu.getId())) {
            String uuid = UUID.randomUUID().toString();
            menu.setId(uuid);
            systemMenuService.save(menu);
        } else {
            systemMenuService.update(menu);
        }
        return "redirect:home";
    }

    private List<Map<String, String>> getParentMenus(String id) {
        List<SystemMenu> listMenu = systemMenuService.listMenusByParent(id);
        if (!listMenu.isEmpty()) {
            level = level + 1;
            if (!("0").equals(id)) {
                subFlag = subFlag + "|&nbsp;&nbsp;&nbsp;&nbsp;";
            }
            for (SystemMenu menu : listMenu) {
                if (("0").equals(menu.getParent())) {
                    subFlag = "";
                    level = 1;
                }
                Map<String, String> map = new HashMap<String, String>();
                map.put("id", menu.getId());
                map.put("name", subFlag + "|—" + menu.getName());
                listNewMenu.add(map);
                getParentMenus(menu.getId());
            }
            if (level == 3) {
                level = 2;
                subFlag = "|&nbsp;&nbsp;&nbsp;&nbsp;";
            }

        }
        return listNewMenu;
    }

    @Inject
    public void setSystemMenuService(SystemMenuService systemMenuService) {
        this.systemMenuService = systemMenuService;
    }

}
