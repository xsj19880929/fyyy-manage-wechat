package com.suryani.manage.system.service;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.suryani.manage.system.dao.SystemMenuDao;
import com.suryani.manage.system.domain.SystemMenu;

@Service
public class SystemMenuService {
    @Inject
    private SystemMenuDao systemMenuDao;

    @Transactional
    public SystemMenu save(SystemMenu menu) {
        return systemMenuDao.save(menu);
    }

    public SystemMenu get(String id) {
        return systemMenuDao.findOne(id);
    }

    @Transactional
    public SystemMenu update(SystemMenu menu) {
        return systemMenuDao.update(menu);
    }

    @Transactional
    public void delete(String id) {
        systemMenuDao.delete(id);
    }

    public List<SystemMenu> listMenus(int offset, int fetchSize) {
        return systemMenuDao.listAll(offset, fetchSize);
    }

    public List<SystemMenu> listMenusByParent(String parent) {
        return systemMenuDao.listAllByParent(parent);
    }
    
    public List<SystemMenu> listMenusOrderByLevel() {
        return systemMenuDao.listAllOrderByLevel();
    }

    public int getListMenuCount() {
        return this.systemMenuDao.getTotalSize();
    }


}
