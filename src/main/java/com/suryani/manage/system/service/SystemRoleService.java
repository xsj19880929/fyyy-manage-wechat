package com.suryani.manage.system.service;

import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.suryani.manage.system.dao.SystemRoleDao;
import com.suryani.manage.system.dao.SystemRoleMenuDao;
import com.suryani.manage.system.domain.SystemRole;
import com.suryani.manage.system.domain.SystemRoleMenu;

@Service
public class SystemRoleService {
    @Inject
    private SystemRoleDao systemRoleDao;
    @Inject
    private SystemRoleMenuDao systemRoleMenuDao;

    @Transactional
    public SystemRole save(SystemRole role, String[] menus) {
        this.systemRoleDao.save(role);
        this.systemRoleMenuDao.deleteByRoleId(role.getId());
        saveMenus(role.getId(), menus);
        return role;
    }

    private void saveMenus(String roleId, String[] menus) {
        if (menus != null) {
            for (String menu : menus) {
                SystemRoleMenu sitemenu = new SystemRoleMenu();
                sitemenu.setId(UUID.randomUUID().toString());
                sitemenu.setMenuId(menu);
                sitemenu.setRoleId(roleId);
                systemRoleMenuDao.save(sitemenu);
            }
        }
    }

    public List<SystemRoleMenu> listMenusByRoleId(String roleId) {
        return this.systemRoleMenuDao.listAllByRoleId(roleId);
    }

    public SystemRole get(String id) {
        return this.systemRoleDao.findOne(id);
    }

    @Transactional
    public SystemRole update(SystemRole role, String[] menus) {
        this.systemRoleDao.update(role);
        this.systemRoleMenuDao.deleteByRoleId(role.getId());
        saveMenus(role.getId(), menus);
        return role;
    }

    @Transactional
    public void delete(String id) {
        systemRoleDao.delete(id);
        systemRoleMenuDao.deleteByRoleId(id);
    }

    public List<SystemRole> listRoles(String queryParam, int offset, int fetchSize) {
        List<SystemRole> list = systemRoleDao.listAll(queryParam, offset, fetchSize);
        return list;
    }

    public int getListRoleCount() {
        return this.systemRoleDao.getTotalSize();
    }
    
}
