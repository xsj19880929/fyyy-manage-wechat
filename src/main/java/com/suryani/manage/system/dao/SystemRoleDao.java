package com.suryani.manage.system.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.suryani.manage.db.BaseDao;
import com.suryani.manage.system.domain.SystemRole;

@Repository
public class SystemRoleDao extends BaseDao<SystemRole> {

    @SuppressWarnings("unchecked")
    public List<SystemRole> listAll(String queryParam, int offset, int fetchSize) {
        Query query = em.createQuery("from SystemRole order by createdTime desc");
        query.setFirstResult(offset);
        query.setMaxResults(fetchSize);
        return query.getResultList();
    }

    public int getTotalSize() {
        Query query = em.createQuery("select count(site) from SystemRole site");
        return ((Long) query.getSingleResult()).intValue();
    }

    public void delete(String id) {
        em.createQuery("delete from SystemRole where id=?1").setParameter(1, id).executeUpdate();
    }

}
