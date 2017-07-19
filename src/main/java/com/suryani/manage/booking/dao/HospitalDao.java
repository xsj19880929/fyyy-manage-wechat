package com.suryani.manage.booking.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.suryani.manage.booking.domain.Hospital;
import com.suryani.manage.db.BaseDao;

@Repository
public class HospitalDao extends BaseDao<Hospital> {

    private String builtHql() {
        StringBuffer hql = new StringBuffer(225);
        hql.append("from Hospital");
        return hql.toString();
    }

    @SuppressWarnings("unchecked")
    public List<Hospital> findAll(int offset, int fetchSize) {
        String hql = builtHql();
        Query query = em.createQuery(hql + " order by createdTime desc");
        query.setFirstResult(offset);
        query.setMaxResults(fetchSize);
        return query.getResultList();
    }

    public Hospital getOneByHospitalId(String orgId) {
        Query query = em.createQuery("from Hospital where orgId=?1").setParameter(1, orgId);
        return (Hospital) query.getSingleResult();
    }

    public int getTotalSize() {
        String hql = builtHql();
        Query query = em.createQuery("select count(id) " + hql);
        return ((Long) query.getSingleResult()).intValue();
    }

    public void delete(String id) {
        em.createQuery("delete from Hospital where id=?1").setParameter(1, id).executeUpdate();
    }

}
