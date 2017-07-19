package com.suryani.manage.system.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.suryani.manage.db.BaseDao;
import com.suryani.manage.system.domain.SystemMenu;

@Repository
public class SystemMenuDao extends BaseDao<SystemMenu> {

    @SuppressWarnings("unchecked")
    public List<SystemMenu> listAll(int offset, int fetchSize) {
        Query query = em.createQuery("from SystemMenu");
        query.setFirstResult(offset);
        query.setMaxResults(fetchSize);
        return query.getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<SystemMenu> listAllByParent(String parent) {
        Query query = em.createQuery("from SystemMenu where parent=:parent");
        query.setParameter("parent", parent);
        return query.getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<SystemMenu> listAllOrderByLevel() {
        Query query = em.createQuery("from SystemMenu order by level,sort desc");
        return query.getResultList();
    }

    public int getTotalSize() {
        Query query = em.createQuery("select count(id) from SystemMenu ");
        return ((Long) query.getSingleResult()).intValue();
    }

    public void delete(String id) {
        em.createQuery("delete from SystemMenu where id=?1").setParameter(1, id).executeUpdate();
    }

}
