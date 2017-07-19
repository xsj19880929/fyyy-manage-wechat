package com.suryani.manage.interceptor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.suryani.manage.system.service.MenuGetService;

@Service
@Singleton
public class MenuGetter {
    private final static Log log = LogFactory.getLog(MenuGetter.class);

    @Inject
    private MenuGetService menuGetService;

    @Inject
    public void setMenuService(MenuGetService menuGetService) {
        this.menuGetService = menuGetService;
    }

    private static Map<String, List<Map<String, Object>>> allMenu = new HashMap<String, List<Map<String, Object>>>();

    public List<Map<String, Object>> getMenus(String roleId) {
        List<Map<String, Object>> menus = allMenu.get(roleId);
        if (menus == null) {
            synchronized (MenuGetter.class) {
                if (allMenu.get(roleId) == null) {
                    menus = this.menuGetService.getMenusByRoleId(roleId);
                    allMenu.put(roleId, menus);
                }
            }
        }
        return menus;
    }

    public void clearMenu(String siteId) {
        synchronized (MenuGetter.class) {
            allMenu.put(siteId, null);
        }
    }

    public void clearMenu() {
        synchronized (MenuGetter.class) {
            allMenu = new HashMap<String, List<Map<String, Object>>>();
        }
    }

}
