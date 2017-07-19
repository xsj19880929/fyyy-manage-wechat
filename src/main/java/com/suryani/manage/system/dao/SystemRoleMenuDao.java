package com.suryani.manage.system.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.suryani.manage.db.BaseDao;
import com.suryani.manage.system.domain.SystemMenu;
import com.suryani.manage.system.domain.SystemRoleMenu;

@Repository
public class SystemRoleMenuDao extends BaseDao<SystemRoleMenu> {

    @SuppressWarnings("unchecked")
    public List<SystemRoleMenu> listAll(int offset, int fetchSize) {
        Query query = em.createQuery("from SystemRoleMenu");
        query.setFirstResult(offset);
        query.setMaxResults(fetchSize);
        return query.getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<SystemRoleMenu> listAllByRoleId(String roleId) {
        Query query = em.createQuery("from SystemRoleMenu where roleId=:roleId");
        query.setParameter("roleId", roleId);
        return query.getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<SystemRoleMenu> listAllByRoleIdLinkMenu(String roleId) {
        Query query = em.createQuery("from " + SystemRoleMenu.class.getName() + " sm," + SystemMenu.class.getName() + " m where m.id=sm.menuId and sm.roleId=:roleId");
        query.setParameter("roleId", roleId);
        return query.getResultList();
    }

    public int getTotalSize() {
        Query query = em.createQuery("select count(id) from SystemRoleMenu ");
        return ((Long) query.getSingleResult()).intValue();
    }

    public void deleteByRoleId(String roleId) {
        em.createQuery("delete from SystemRoleMenu where roleId=?1").setParameter(1, roleId).executeUpdate();
    }

}
