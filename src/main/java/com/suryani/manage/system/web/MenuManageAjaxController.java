package com.suryani.manage.system.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.quidsi.core.platform.web.rest.RESTController;
import com.suryani.manage.system.domain.SystemMenu;
import com.suryani.manage.system.service.SystemMenuService;
import com.suryani.manage.util.Utils;

@Controller
@RequestMapping("system/menu")
public class MenuManageAjaxController extends RESTController {
    private SystemMenuService systemMenuService;

    @RequestMapping(value = "/list")
    @ResponseBody
    public Map<String, Object> getList(@RequestParam(defaultValue = "0") Integer offset, @RequestParam(defaultValue = "10") Integer fetchSize, HttpServletRequest request) {
        List<SystemMenu> menuList = systemMenuService.listMenus(offset, fetchSize);
        List<SystemMenu> menuNewList = new ArrayList<SystemMenu>();
        for (SystemMenu menu : menuList) {
            if ("0".equals(menu.getParent())) {
                menu.setParent("顶级菜单");
            } else {
                menu.setParent(systemMenuService.get(menu.getParent()).getName());
            }
            menuNewList.add(menu);
        }
        return Utils.pagerWarp(menuNewList, systemMenuService.getListMenuCount());
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public String deleteWxCbqProduct(@RequestParam("id") String id) throws Exception {
        systemMenuService.delete(id);
        return "true";
    }

    @Inject
    public void setSystemMenuService(SystemMenuService systemMenuService) {
        this.systemMenuService = systemMenuService;
    }

}
